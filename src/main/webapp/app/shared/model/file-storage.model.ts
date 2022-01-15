import { IProduct } from '@/shared/model/product.model';

import { FileStorageStatus } from '@/shared/model/enumerations/file-storage-status.model';
export interface IFileStorage {
  id?: number;
  fileName?: string | null;
  extension?: string | null;
  fileSize?: number | null;
  hashId?: string | null;
  contentType?: string | null;
  uploadPath?: string | null;
  fileStorageStatus?: FileStorageStatus | null;
  name?: IProduct | null;
}

export class FileStorage implements IFileStorage {
  constructor(
    public id?: number,
    public fileName?: string | null,
    public extension?: string | null,
    public fileSize?: number | null,
    public hashId?: string | null,
    public contentType?: string | null,
    public uploadPath?: string | null,
    public fileStorageStatus?: FileStorageStatus | null,
    public name?: IProduct | null
  ) {}
}
