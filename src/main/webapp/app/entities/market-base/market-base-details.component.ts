import { Component, Vue, Inject } from 'vue-property-decorator';

import { IMarketBase } from '@/shared/model/market-base.model';
import MarketBaseService from './market-base.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class MarketBaseDetails extends Vue {
  @Inject('marketBaseService') private marketBaseService: () => MarketBaseService;
  @Inject('alertService') private alertService: () => AlertService;

  public marketBase: IMarketBase = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.marketBaseId) {
        vm.retrieveMarketBase(to.params.marketBaseId);
      }
    });
  }

  public retrieveMarketBase(marketBaseId) {
    this.marketBaseService()
      .find(marketBaseId)
      .then(res => {
        this.marketBase = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
