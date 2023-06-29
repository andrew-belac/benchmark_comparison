package io.belac.benchmarks.quarkus3;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


@ApplicationScoped
@Path("/api")
public class OrderController {

    Random random = new Random();

    Semaphore getRandomSemaphore = new Semaphore(16);

    private final List<String> divisions = new ArrayList<>();

    Logger logger = Logger.getLogger(OrderController.class);


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hello")
    public String hello() {
        return "Hello";
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-random-rate-limited")
    public Response getRandomOrders() {
        try {
            if (this.getRandomSemaphore.tryAcquire(5, TimeUnit.SECONDS)){
                return Response.ok(getOrderDtos()).build();
            } else {
                return Response.status(429).entity("Too many queries").build();
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            return Response.status(500).entity("Interrupted").build();
        }
        finally {
            this.getRandomSemaphore.release(1);
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-random")
    public List<OrderDto> getRandomOrdersNoLimiting() {
        return getOrderDtos();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    @Transactional
    public OrderDto create() {
        return this.createOrder();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create-and-get")
    @Transactional
    public OrderDto createAndGet() {
        Long recNum = Long.valueOf(
                random.ints(0, 1000000)
                        .findFirst()
                        .getAsInt()
        );
        this.createOrder();

        return this.map(Order.findById(recNum));

    }

    private List<OrderDto> getOrderDtos() {
        var random = new Random();
        var d = random.ints(0, 2).findFirst().getAsInt();
        var numRecords = Order.count("division", getDivisions().get(d));
        var start = random.ints(11,  (int) numRecords + 1)
                .findFirst().getAsInt();
        var end = start + random.ints(1, 11)
                .findFirst().getAsInt();
        return Order.findByDivision(getDivisions().get(d), start, end);
    }

    private List<String> getDivisions() {
        if (divisions.size() == 0) {
            divisions.add("CAPETOWN");
            divisions.add("DURBAN");
        }
        return divisions;
    }


    private String randomString(Integer length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


    public OrderDto createOrder() {
        var d = random.ints(0, 1).findFirst().getAsInt();
        Order order = new Order();
        var cdt = OffsetDateTime.now();
        order.setCreatedAt(cdt);
        order.setUpdatedAt(cdt);
        order.setOrderCode(randomString(20));
        order.setDivision(getDivisions().get(d));
        order.setDivision("Mine");
        order.persist();
        return this.map(order);
    }

    private OrderDto map(Order order) {
        return new OrderDto(order.id, order.orderCode, order.division);
    }
}