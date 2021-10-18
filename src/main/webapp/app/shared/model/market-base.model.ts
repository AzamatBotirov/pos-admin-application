import { IProduct } from '@/shared/model/product.model';

export interface IMarketBase {
  id?: number;
  quantity?: string | null;
  price?: number | null;
  currentPrice?: number | null;
  createDate?: Date | null;
  date?: Date | null;
  name?: IProduct | null;
}

export class MarketBase implements IMarketBase {
  constructor(
    public id?: number,
    public quantity?: string | null,
    public price?: number | null,
    public currentPrice?: number | null,
    public createDate?: Date | null,
    public date?: Date | null,
    public name?: IProduct | null
  ) {}
}
