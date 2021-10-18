/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PaymentListDetailComponent from '@/entities/payment-list/payment-list-details.vue';
import PaymentListClass from '@/entities/payment-list/payment-list-details.component';
import PaymentListService from '@/entities/payment-list/payment-list.service';
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
  describe('PaymentList Management Detail Component', () => {
    let wrapper: Wrapper<PaymentListClass>;
    let comp: PaymentListClass;
    let paymentListServiceStub: SinonStubbedInstance<PaymentListService>;

    beforeEach(() => {
      paymentListServiceStub = sinon.createStubInstance<PaymentListService>(PaymentListService);

      wrapper = shallowMount<PaymentListClass>(PaymentListDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { paymentListService: () => paymentListServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPaymentList = { id: 123 };
        paymentListServiceStub.find.resolves(foundPaymentList);

        // WHEN
        comp.retrievePaymentList(123);
        await comp.$nextTick();

        // THEN
        expect(comp.paymentList).toBe(foundPaymentList);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPaymentList = { id: 123 };
        paymentListServiceStub.find.resolves(foundPaymentList);

        // WHEN
        comp.beforeRouteEnter({ params: { paymentListId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.paymentList).toBe(foundPaymentList);
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
