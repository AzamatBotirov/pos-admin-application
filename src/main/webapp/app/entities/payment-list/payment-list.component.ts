import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IPaymentList } from '@/shared/model/payment-list.model';

import PaymentListService from './payment-list.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class PaymentList extends Vue {
  @Inject('paymentListService') private paymentListService: () => PaymentListService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public paymentLists: IPaymentList[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllPaymentLists();
  }

  public clear(): void {
    this.retrieveAllPaymentLists();
  }

  public retrieveAllPaymentLists(): void {
    this.isFetching = true;
    this.paymentListService()
      .retrieve()
      .then(
        res => {
          this.paymentLists = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IPaymentList): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removePaymentList(): void {
    this.paymentListService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('posAdnimApp.paymentList.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllPaymentLists();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
