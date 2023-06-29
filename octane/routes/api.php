<?php

use App\Models\Order;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::get('/hello', function (Request $request) {
    return 'hello';
});
Route::get("/get-random", [\App\Http\Controllers\OrderController::class, 'getRandom']);
Route::get('/create-and-get', [\App\Http\Controllers\OrderController::class, "createAndGet"]);
Route::get('/create', [\App\Http\Controllers\OrderController::class, "create"]);
