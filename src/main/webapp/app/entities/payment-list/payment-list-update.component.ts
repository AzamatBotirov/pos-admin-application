import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import ProductService from '@/entities/product/product.service';
import { IProduct } from '@/shared/model/product.model';

import { IPaymentList, PaymentList } from '@/shared/model/payment-list.model';
import PaymentListService from './payment-list.service';

const validations: any = {
  paymentList: {
    quantity: {},
    summa: {},
  },
};

@Component({
  validations,
})
export default class PaymentListUpdate extends Vue {
  @Inject('paymentListService') private paymentListService: () => PaymentListService;
  @Inject('alertService') private alertService: () => AlertService;

  public paymentList: IPaymentList = new PaymentList();

  @Inject('productService') private productService: () => ProductService;

  public products: IProduct[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.paymentListId) {
        vm.retrievePaymentList(to.params.paymentListId);
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
    if (this.paymentList.id) {
      this.paymentListService()
        .update(this.paymentList)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('posAdnimApp.paymentList.updated', { param: param.id });
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
      this.paymentListService()
        .create(this.paymentList)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('posAdnimApp.paymentList.created', { param: param.id });
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

  public retrievePaymentList(paymentListId): void {
    this.paymentListService()
      .find(paymentListId)
      .then(res => {
        this.paymentList = res;
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
