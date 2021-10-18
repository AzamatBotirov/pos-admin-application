import { Component, Vue, Inject } from 'vue-property-decorator';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import ProductService from '@/entities/product/product.service';
import { IProduct } from '@/shared/model/product.model';

import { IMarketBase, MarketBase } from '@/shared/model/market-base.model';
import MarketBaseService from './market-base.service';

const validations: any = {
  marketBase: {
    quantity: {},
    price: {},
    currentPrice: {},
    createDate: {},
    date: {},
  },
};

@Component({
  validations,
})
export default class MarketBaseUpdate extends Vue {
  @Inject('marketBaseService') private marketBaseService: () => MarketBaseService;
  @Inject('alertService') private alertService: () => AlertService;

  public marketBase: IMarketBase = new MarketBase();

  @Inject('productService') private productService: () => ProductService;

  public products: IProduct[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.marketBaseId) {
        vm.retrieveMarketBase(to.params.marketBaseId);
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
    if (this.marketBase.id) {
      this.marketBaseService()
        .update(this.marketBase)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('posAdnimApp.marketBase.updated', { param: param.id });
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
      this.marketBaseService()
        .create(this.marketBase)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('posAdnimApp.marketBase.created', { param: param.id });
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.marketBase[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.marketBase[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.marketBase[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.marketBase[field] = null;
    }
  }

  public retrieveMarketBase(marketBaseId): void {
    this.marketBaseService()
      .find(marketBaseId)
      .then(res => {
        res.createDate = new Date(res.createDate);
        res.date = new Date(res.date);
        this.marketBase = res;
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
