<?php

namespace App\Models;

use Carbon\CarbonImmutable;
use Illuminate\Database\Eloquent\Model;

/**
 * @property int $id
 * @property string $division
 * @property string $order_code
 * @property CarbonImmutable $created_at
 * @property CarbonImmutable $updated_at
 */
class Order extends Model
{
    public static $snakeAttributes = false;

    protected $hidden = ['created_at', 'updated_at'];

}
