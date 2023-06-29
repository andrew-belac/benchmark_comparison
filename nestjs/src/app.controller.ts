import { Controller, Get } from '@nestjs/common';
import { AppService } from './app.service';
import { OrderDto } from './Dtos/orderDto';

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get("/api/hello")
  getHello(): string {
    return this.appService.getHello();
  }

  @Get("/api/create")
  getCreate(): Promise<OrderDto> {
    return this.appService.create();
  }

  @Get("/api/create-and-get")
  getCreateAndGet(): Promise<OrderDto> {
    return this.appService.createAndGet();
  }

  @Get("/api/get-random")
  getRandom(): Promise<Array<OrderDto>> {
    return this.appService.getRandom();
  }
}
