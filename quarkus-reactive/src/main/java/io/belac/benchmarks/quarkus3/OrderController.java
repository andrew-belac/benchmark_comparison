package io.belac.benchmarks.quarkus3;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ApplicationScoped
@Path("/api")
public class OrderController {

    Random random = new Random();

    private final List<String> divisions = new ArrayList<>();

    Logger logger = Logger.getLogger(OrderController.class);


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hello")
    public Uni<String> hello() {
        return Uni.createFrom().item("Hello");
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-random")
    public Uni<List<OrderDto>> getRandomOrders() {
        return Panache.withTransaction(() -> {
            var random = new Random();
            var d = random.ints(0, 2).findFirst().getAsInt();
            var numRecords = Order.count("division", getDivisions().get(d));
            return numRecords.flatMap((num) -> {
                var start = random.ints(11,  num.intValue() + 1)
                        .findFirst().getAsInt();
                var end = start + random.ints(1, 11)
                        .findFirst().getAsInt();
                return Order.findByDivision(getDivisions().get(d), start, end);
            });
        });

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public Uni<OrderDto> create() {
       return Panache.withTransaction(this::createOrder);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create-and-get")
    public Uni<OrderDto> createAndGet() {
        Long recNum = (long) random.ints(1, 1000000)
                .findFirst()
                .getAsInt();
        return Panache.withTransaction(()-> createOrder()
                .onItem()
                .call(() -> Order.findById(recNum)
                .onItem()
                .castTo(Order.class)
                .onItem()
                .transform(this::map)
                )
        );
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


    public Uni<OrderDto> createOrder() {
        var d = random.ints(0, 1).findFirst().getAsInt();
        Order order = new Order();
        var cdt = OffsetDateTime.now();
        order.setCreatedAt(cdt);
        order.setUpdatedAt(cdt);
        order.setOrderCode(randomString(20));
        order.setDivision(getDivisions().get(d));
        order.setDivision("Mine");
        return order.persist().onItem().castTo(Order.class).onItem().transform(this::map);
    }

    private OrderDto map(Order order) {
        return new OrderDto(order.id, order.orderCode, order.division);
    }
}