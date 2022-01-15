import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import ProductService from '@/entities/product/product.service';
import { IProduct } from '@/shared/model/product.model';

import { IFileStorage, FileStorage } from '@/shared/model/file-storage.model';
import FileStorageService from './file-storage.service';

const validations: any = {
  fileStorage: {
    fileName: {},
    extension: {},
    fileSize: {},
    hashId: {},
    contentType: {},
    uploadPath: {},
    fileStorageStatus: {},
  },
};

@Component({
  validations,
})
export default class FileStorageUpdate extends Vue {
  @Inject('fileStorageService') private fileStorageService: () => FileStorageService;
  @Inject('alertService') private alertService: () => AlertService;

  public fileStorage: IFileStorage = new FileStorage();

  @Inject('productService') private productService: () => ProductService;

  public products: IProduct[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.fileStorageId) {
        vm.retrieveFileStorage(to.params.fileStorageId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.fileStorage.id) {
      this.fileStorageService()
        .update(this.fileStorage)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('posAdnimApp.fileStorage.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.fileStorageService()
        .create(this.fileStorage)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('posAdnimApp.fileStorage.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveFileStorage(fileStorageId): void {
    this.fileStorageService()
      .find(fileStorageId)
      .then(res => {
        this.fileStorage = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.productService()
      .retrieve()
      .then(res => {
        this.products = res.data;
      });
  }
}
