/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import MarketBaseDetailComponent from '@/entities/market-base/market-base-details.vue';
import MarketBaseClass from '@/entities/market-base/market-base-details.component';
import MarketBaseService from '@/entities/market-base/market-base.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('MarketBase Management Detail Component', () => {
    let wrapper: Wrapper<MarketBaseClass>;
    let comp: MarketBaseClass;
    let marketBaseServiceStub: SinonStubbedInstance<MarketBaseService>;

    beforeEach(() => {
      marketBaseServiceStub = sinon.createStubInstance<MarketBaseService>(MarketBaseService);

      wrapper = shallowMount<MarketBaseClass>(MarketBaseDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { marketBaseService: () => marketBaseServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundMarketBase = { id: 123 };
        marketBaseServiceStub.find.resolves(foundMarketBase);

        // WHEN
        comp.retrieveMarketBase(123);
        await comp.$nextTick();

        // THEN
        expect(comp.marketBase).toBe(foundMarketBase);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundMarketBase = { id: 123 };
        marketBaseServiceStub.find.resolves(foundMarketBase);

        // WHEN
        comp.beforeRouteEnter({ params: { marketBaseId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.marketBase).toBe(foundMarketBase);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
