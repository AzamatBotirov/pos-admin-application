/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import PaymentListUpdateComponent from '@/entities/payment-list/payment-list-update.vue';
import PaymentListClass from '@/entities/payment-list/payment-list-update.component';
import PaymentListService from '@/entities/payment-list/payment-list.service';

import ProductService from '@/entities/product/product.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('PaymentList Management Update Component', () => {
    let wrapper: Wrapper<PaymentListClass>;
    let comp: PaymentListClass;
    let paymentListServiceStub: SinonStubbedInstance<PaymentListService>;

    beforeEach(() => {
      paymentListServiceStub = sinon.createStubInstance<PaymentListService>(PaymentListService);

      wrapper = shallowMount<PaymentListClass>(PaymentListUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          paymentListService: () => paymentListServiceStub,
          alertService: () => new AlertService(),

          productService: () => new ProductService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.paymentList = entity;
        paymentListServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentListServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.paymentList = entity;
        paymentListServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentListServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPaymentList = { id: 123 };
        paymentListServiceStub.find.resolves(foundPaymentList);
        paymentListServiceStub.retrieve.resolves([foundPaymentList]);

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
