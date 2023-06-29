<?php

namespace Database\Seeders;

use App\Models\Order;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;
use Ramsey\Uuid\Uuid;

class OrderSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $division = ['DURBAN', 'CAPETOWN'];
        for ($count = 0; $count < 1000; $count++) {
            $insert = [];
            for ($i = 0; $i < 1000; $i++){
                array_push($insert, [
                    'order_code' => Str::random(20),
                    'division' => $division[rand(0, 1)],
                ]);
            }
            DB::table('orders')->insert($insert);
        }
    }
}
