package io.belac.benchmarks.quarkus3;

import io.smallrye.mutiny.Uni;

import java.util.List;

public class Orders {

    public Uni<List<Order>> orders;

    public Orders(Uni<List<Order>> orders){
        this.orders = orders;
    }
    
}
