<?php

namespace App\Models;

class HelloName
{
    public function __construct(
        public readonly string $name,
        /** @var OtherName[] $aliases */
        public readonly array $aliases
    )
    {}
}
