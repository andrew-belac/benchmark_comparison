export default class OrderDto {
  public id: number

  public orderCode: string

  public division: string

  constructor(id: number, orderCode: string, division: string) {
    this.id = id
    this.division = division
    this.orderCode = orderCode
  }
}
