import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Order } from './Entities/order.entity';
import { OrderDto } from './Dtos/orderDto';

@Injectable()
export class AppService {
  constructor(
    @InjectRepository(Order)
    private ordersRepository: Repository<Order>,
  ) {}

  private divisions: Array<string> = ['DURBAN', 'CAPETOWN'];

  getHello(): string {
    return 'Hello';
  }

  async create(): Promise<OrderDto> {
    const order = new Order();
    order.division = this.getRandomDivision();
    order.orderCode = this.genRandomString(20);
    const orderPromise = this.ordersRepository.save(order);
    return orderPromise.then((order) => {
      return this.map(order);
    });
  }

  async createAndGet(): Promise<OrderDto> {
    const savedOrderPromise = this.create();
    return savedOrderPromise.then((savedOrder) => {
      const fetchId = this.getRandomInt(999999) + 1;
      const foundPromise = this.ordersRepository.findOneBy({ id: fetchId });
      return foundPromise.then((found) => {
        return this.map(found);
      });
    });
  }

  async getRandom(): Promise<Array<OrderDto>> {
    const division = this.getRandomDivision();

    //Get the total orders in a random division
    const count = await this.ordersRepository
      .createQueryBuilder('orders')
      .where('orders.division = :division', { division })
      .getCount();

    const start = this.getRandomInt(count);
    const limit = this.getRandomInt(10) + 1;

    const query = this.ordersRepository
      .createQueryBuilder('orders')
      .where('orders.division = :division', { division })
      .offset(start)
      .limit(limit);
    return query.getMany().then((orders) => {
      return orders.map((order) => {
        return this.map(order);
      });
    });
  }

  private map(order: Order): OrderDto {
    return new OrderDto(order.id, order.orderCode, order.division);
  }

  private getRandomInt(max: number): number {
    return Math.floor(Math.random() * max);
  }

  private getRandomDivision(): string {
    return this.divisions[Math.floor(Math.random() * 2)];
  }

  private genRandomString(length) {
    const chars =
      'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()';
    const charLength = chars.length;
    let result = '';
    for (let i = 0; i < length; i++) {
      result += chars.charAt(Math.floor(Math.random() * charLength));
    }
    return result;
  }
}
