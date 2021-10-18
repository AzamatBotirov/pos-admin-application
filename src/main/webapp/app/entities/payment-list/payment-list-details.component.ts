import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPaymentList } from '@/shared/model/payment-list.model';
import PaymentListService from './payment-list.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PaymentListDetails extends Vue {
  @Inject('paymentListService') private paymentListService: () => PaymentListService;
  @Inject('alertService') private alertService: () => AlertService;

  public paymentList: IPaymentList = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.paymentListId) {
        vm.retrievePaymentList(to.params.paymentListId);
      }
    });
  }

  public retrievePaymentList(paymentListId) {
    this.paymentListService()
      .find(paymentListId)
      .then(res => {
        this.paymentList = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
