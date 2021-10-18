/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import FileStorageComponent from '@/entities/file-storage/file-storage.vue';
import FileStorageClass from '@/entities/file-storage/file-storage.component';
import FileStorageService from '@/entities/file-storage/file-storage.service';
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
  describe('FileStorage Management Component', () => {
    let wrapper: Wrapper<FileStorageClass>;
    let comp: FileStorageClass;
    let fileStorageServiceStub: SinonStubbedInstance<FileStorageService>;

    beforeEach(() => {
      fileStorageServiceStub = sinon.createStubInstance<FileStorageService>(FileStorageService);
      fileStorageServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<FileStorageClass>(FileStorageComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          fileStorageService: () => fileStorageServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      fileStorageServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllFileStorages();
      await comp.$nextTick();

      // THEN
      expect(fileStorageServiceStub.retrieve.called).toBeTruthy();
      expect(comp.fileStorages[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      fileStorageServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeFileStorage();
      await comp.$nextTick();

      // THEN
      expect(fileStorageServiceStub.delete.called).toBeTruthy();
      expect(fileStorageServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
