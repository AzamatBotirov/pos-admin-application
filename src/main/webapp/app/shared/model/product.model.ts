import { ProductType } from '@/shared/model/enumerations/product-type.model';
import { ProductDirective } from '@/shared/model/enumerations/product-directive.model';
import { Status } from '@/shared/model/enumerations/status.model';
export interface IProduct {
  id?: number;
  name?: string | null;
  code?: string | null;
  type?: ProductType | null;
  directive?: ProductDirective | null;
  status?: Status | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public type?: ProductType | null,
    public directive?: ProductDirective | null,
    public status?: Status | null
  ) {}
}
