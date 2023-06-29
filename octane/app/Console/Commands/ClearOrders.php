<?php

namespace App\Console\Commands;

use App\Models\Order;
use Illuminate\Console\Command;

class ClearOrders extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
    protected $signature = 'app:clear-orders';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Command description';

    /**
     * Execute the console command.
     */
    public function handle()
    {
        Order::where('id', '>', 1000000)->delete();
    }
}
