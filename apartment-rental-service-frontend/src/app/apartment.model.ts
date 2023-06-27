export class Apartment {
    id?: number;
    name: string;
    address: string;
    numberOfRooms: number;
    price: number;
  
    constructor(id: number, name: string, address: string, numberOfRooms: number, price: number) {
      this.id = id;
      this.name = name;
      this.address = address;
      this.numberOfRooms = numberOfRooms;
      this.price = price;
    }
  }
  