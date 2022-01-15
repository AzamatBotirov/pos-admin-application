/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import FileStorageUpdateComponent from '@/entities/file-storage/file-storage-update.vue';
import FileStorageClass from '@/entities/file-storage/file-storage-update.component';
import FileStorageService from '@/entities/file-storage/file-storage.service';

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
  describe('FileStorage Management Update Component', () => {
    let wrapper: Wrapper<FileStorageClass>;
    let comp: FileStorageClass;
    let fileStorageServiceStub: SinonStubbedInstance<FileStorageService>;

    beforeEach(() => {
      fileStorageServiceStub = sinon.createStubInstance<FileStorageService>(FileStorageService);

      wrapper = shallowMount<FileStorageClass>(FileStorageUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          fileStorageService: () => fileStorageServiceStub,
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
        comp.fileStorage = entity;
        fileStorageServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(fileStorageServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.fileStorage = entity;
        fileStorageServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(fileStorageServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFileStorage = { id: 123 };
        fileStorageServiceStub.find.resolves(foundFileStorage);
        fileStorageServiceStub.retrieve.resolves([foundFileStorage]);

        // WHEN
        comp.beforeRouteEnter({ params: { fileStorageId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.fileStorage).toBe(foundFileStorage);
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
