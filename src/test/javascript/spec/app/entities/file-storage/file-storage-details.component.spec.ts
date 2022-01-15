/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import FileStorageDetailComponent from '@/entities/file-storage/file-storage-details.vue';
import FileStorageClass from '@/entities/file-storage/file-storage-details.component';
import FileStorageService from '@/entities/file-storage/file-storage.service';
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
  describe('FileStorage Management Detail Component', () => {
    let wrapper: Wrapper<FileStorageClass>;
    let comp: FileStorageClass;
    let fileStorageServiceStub: SinonStubbedInstance<FileStorageService>;

    beforeEach(() => {
      fileStorageServiceStub = sinon.createStubInstance<FileStorageService>(FileStorageService);

      wrapper = shallowMount<FileStorageClass>(FileStorageDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { fileStorageService: () => fileStorageServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundFileStorage = { id: 123 };
        fileStorageServiceStub.find.resolves(foundFileStorage);

        // WHEN
        comp.retrieveFileStorage(123);
        await comp.$nextTick();

        // THEN
        expect(comp.fileStorage).toBe(foundFileStorage);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFileStorage = { id: 123 };
        fileStorageServiceStub.find.resolves(foundFileStorage);

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
