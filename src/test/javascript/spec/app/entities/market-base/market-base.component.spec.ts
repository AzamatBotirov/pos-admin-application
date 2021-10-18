/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import MarketBaseComponent from '@/entities/market-base/market-base.vue';
import MarketBaseClass from '@/entities/market-base/market-base.component';
import MarketBaseService from '@/entities/market-base/market-base.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('MarketBase Management Component', () => {
    let wrapper: Wrapper<MarketBaseClass>;
    let comp: MarketBaseClass;
    let marketBaseServiceStub: SinonStubbedInstance<MarketBaseService>;

    beforeEach(() => {
      marketBaseServiceStub = sinon.createStubInstance<MarketBaseService>(MarketBaseService);
      marketBaseServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<MarketBaseClass>(MarketBaseComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          marketBaseService: () => marketBaseServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      marketBaseServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllMarketBases();
      await comp.$nextTick();

      // THEN
      expect(marketBaseServiceStub.retrieve.called).toBeTruthy();
      expect(comp.marketBases[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      marketBaseServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeMarketBase();
      await comp.$nextTick();

      // THEN
      expect(marketBaseServiceStub.delete.called).toBeTruthy();
      expect(marketBaseServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
