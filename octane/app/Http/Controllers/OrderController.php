<?php

namespace App\Http\Controllers;

use App\Dtos\OrderDto;
use App\Models\Order;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Str;
use OpenApi\Attributes as QA;

#[QA\Info(version: "1.0", title: "Random Api")]
class OrderController extends \Illuminate\Routing\Controller
{
    /** @var string[]  */
    private array $divisions;
    public function __construct()
    {
        $this->divisions = ['DURBAN', 'CAPETOWN'];
    }

    #[QA\Post(path: '/api/hello')]
    #[QA\Response(response: '200',)]
    public function hello(Request $request) : Response
    {
        return response('Hello');
    }
    #[QA\Get(path: "/api/get-random")]
    #[QA\Response(response: '200', description: 'Random number')]
    public function getRandom(Request $request): JsonResponse
    {
        $division = $this->divisions[rand(0,1)];
        /** @var Collection $orders */
        $orders = Order::where('division', $division)
            ->offset(
                rand(0, Order::where('division', $division)->count() - 10)
            )
            ->limit(rand(1,10))->get();
        return new JsonResponse($orders->map(function(Order $order){
            return $this->map($order);
        })->toArray());
    }

    public function create() : JsonResponse
    {
        return response()->json((array)$this->map($this->createOne()));
    }

    public function createAndGet(): JsonResponse
    {
        $this->createOne();
        return new JsonResponse(
            (array) $this->map(
                $this->get()
            )
        );
    }
    private function createOne() : Order
    {
        $order = new Order();
        $order->order_code = Str::random(20);
        $order->division = $this->divisions[rand(0, 1)];
        $order->saveOrFail();
        return $order;
    }



    private function get() : Order
    {
        return Order::findOrFail(rand(1,1000000));
    }

    private function map(Order $order) : OrderDto
    {
        return new OrderDto(id: $order->id, orderCode: $order->order_code, division: $order->division);
    }
}
