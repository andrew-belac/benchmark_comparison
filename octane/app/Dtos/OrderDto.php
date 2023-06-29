<?php

namespace App\Dtos;

class OrderDto
{
    public function __construct(
        public int    $id,
        public string $orderCode,
        public string $division
    )
    {}
}
