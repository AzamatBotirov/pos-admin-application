/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import PaymentListComponent from '@/entities/payment-list/payment-list.vue';
import PaymentListClass from '@/entities/payment-list/payment-list.component';
import PaymentListService from '@/entities/payment-list/payment-list.service';
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
  describe('PaymentList Management Component', () => {
    let wrapper: Wrapper<PaymentListClass>;
    let comp: PaymentListClass;
    let paymentListServiceStub: SinonStubbedInstance<PaymentListService>;

    beforeEach(() => {
      paymentListServiceStub = sinon.createStubInstance<PaymentListService>(PaymentListService);
      paymentListServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<PaymentListClass>(PaymentListComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          paymentListService: () => paymentListServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      paymentListServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllPaymentLists();
      await comp.$nextTick();

      // THEN
      expect(paymentListServiceStub.retrieve.called).toBeTruthy();
      expect(comp.paymentLists[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      paymentListServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removePaymentList();
      await comp.$nextTick();

      // THEN
      expect(paymentListServiceStub.delete.called).toBeTruthy();
      expect(paymentListServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
