import { IProduct } from '@/shared/model/product.model';

export interface IPaymentList {
  id?: number;
  quantity?: number | null;
  summa?: number | null;
  name?: IProduct | null;
}

export class PaymentList implements IPaymentList {
  constructor(public id?: number, public quantity?: number | null, public summa?: number | null, public name?: IProduct | null) {}
}
