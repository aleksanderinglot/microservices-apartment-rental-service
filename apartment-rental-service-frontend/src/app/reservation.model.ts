export interface Reservation {
    id?: number;
    apartmentId: number;
    guestName: string;
    guestEmail: string;
    startDate: Date;
    endDate: Date;
  }  