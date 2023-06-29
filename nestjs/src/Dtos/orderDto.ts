export class OrderDto {

    id: number;
    orderCode: string;
    division: string;

    constructor(id: number, orderCode: string, division: string) {
        this.id = id;
        this.orderCode = orderCode;
        this.division = division;
      }
}