import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IMarketBase } from '@/shared/model/market-base.model';

import MarketBaseService from './market-base.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class MarketBase extends Vue {
  @Inject('marketBaseService') private marketBaseService: () => MarketBaseService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public marketBases: IMarketBase[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllMarketBases();
  }

  public clear(): void {
    this.retrieveAllMarketBases();
  }

  public retrieveAllMarketBases(): void {
    this.isFetching = true;
    this.marketBaseService()
      .retrieve()
      .then(
        res => {
          this.marketBases = res.data;
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

  public prepareRemove(instance: IMarketBase): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeMarketBase(): void {
    this.marketBaseService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('posAdnimApp.marketBase.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllMarketBases();
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
