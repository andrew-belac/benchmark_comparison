import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { ConfigModule } from '@nestjs/config';
import { Order } from './Entities/order.entity';

@Module({
  imports: [
    ConfigModule.forRoot({
      ignoreEnvFile: true,
      isGlobal: true,
    }),
    TypeOrmModule.forRoot({
      type: "postgres",
      host: process.env.DATABASE_HOST,
      port: 5432,
      username: "postgres",
      password: process.env.DATABASE_PASSWORD,
      database: "octane_test",
      logging: false,
      entities: [Order],
      synchronize: false,
      poolSize: parseInt(process.env.DATABASE_POOL_SIZE),
    }),
    TypeOrmModule.forFeature([Order]),
  ],
  exports: [TypeOrmModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
