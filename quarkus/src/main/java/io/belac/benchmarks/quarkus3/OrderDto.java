package io.belac.benchmarks.quarkus3;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderDto {

    public Long id;
    public String orderCode;
    public String division;

    public OrderDto(Long id, String orderCode, String division) {
        this.id = id;
        this.orderCode = orderCode;
        this.division = division;
    }
}
