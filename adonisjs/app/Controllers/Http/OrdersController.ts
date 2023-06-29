import OrderDto from 'App/Dtos/OrderDto'
import Order from 'App/Models/Order'
import { string } from '@ioc:Adonis/Core/Helpers'
import Database from '@ioc:Adonis/Lucid/Database'

export default class OrdersController {
  private divisions = ['DURBAN', 'CAPETOWN']

  public async create(): Promise<OrderDto> {
    return this.map(await this.createOne())
  }

  public async createAndGet(): Promise<OrderDto> {
    await this.createOne()
    return this.map(await Order.findOrFail(this.getRandomInt(999999) + 1))
  }

  public async getRandom(): Promise<OrderDto[]> {
    let division = this.divisions[this.getRandomInt(1)]
    const orders = await Order.query()
      .where('division', division)
      .offset(this.getRandomInt(await this.getDivisionCount(division)))
      .limit(this.getRandomInt(10) + 1)
    return orders.map((order) => this.map(order))
  }

  private async getDivisionCount(division: string): Promise<number> {
    return await Database.from('orders')
      .count('id', 'count')
      .where('division', division)
      .first()
      .then((countResult) => countResult.count)
  }

  private getRandomInt(max: number): number {
    return Math.floor(Math.random() * max)
  }

  private async createOne(): Promise<Order> {
    const order = new Order()
    order.division = this.divisions[this.getRandomInt(1)]
    order.orderCode = string.generateRandom(20)
    return order.save()
  }

  private map(order: Order): OrderDto {
    return new OrderDto(order.id, order.orderCode, order.division)
  }
}
