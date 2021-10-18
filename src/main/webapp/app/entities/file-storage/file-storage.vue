<template>
  <div>
    <h2 id="page-heading" data-cy="FileStorageHeading">
      <span v-text="$t('posAdnimApp.fileStorage.home.title')" id="file-storage-heading">File Storages</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('posAdnimApp.fileStorage.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'FileStorageCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-file-storage"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('posAdnimApp.fileStorage.home.createLabel')"> Create a new File Storage </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && fileStorages && fileStorages.length === 0">
      <span v-text="$t('posAdnimApp.fileStorage.home.notFound')">No fileStorages found</span>
    </div>
    <div class="table-responsive" v-if="fileStorages && fileStorages.length > 0">
      <table class="table table-striped" aria-describedby="fileStorages">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.fileStorage.fileName')">File Name</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.fileStorage.extension')">Extension</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.fileStorage.fileSize')">File Size</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.fileStorage.hashId')">Hash Id</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.fileStorage.contentType')">Content Type</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.fileStorage.uploadPath')">Upload Path</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.fileStorage.fileStorageStatus')">File Storage Status</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.fileStorage.name')">Name</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="fileStorage in fileStorages" :key="fileStorage.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'FileStorageView', params: { fileStorageId: fileStorage.id } }">{{ fileStorage.id }}</router-link>
            </td>
            <td>{{ fileStorage.fileName }}</td>
            <td>{{ fileStorage.extension }}</td>
            <td>{{ fileStorage.fileSize }}</td>
            <td>{{ fileStorage.hashId }}</td>
            <td>{{ fileStorage.contentType }}</td>
            <td>{{ fileStorage.uploadPath }}</td>
            <td v-text="$t('posAdnimApp.FileStorageStatus.' + fileStorage.fileStorageStatus)">{{ fileStorage.fileStorageStatus }}</td>
            <td>
              <div v-if="fileStorage.name">
                <router-link :to="{ name: 'ProductView', params: { productId: fileStorage.name.id } }">{{
                  fileStorage.name.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'FileStorageView', params: { fileStorageId: fileStorage.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'FileStorageEdit', params: { fileStorageId: fileStorage.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(fileStorage)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="posAdnimApp.fileStorage.delete.question" data-cy="fileStorageDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-fileStorage-heading" v-text="$t('posAdnimApp.fileStorage.delete.question', { id: removeId })">
          Are you sure you want to delete this File Storage?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-fileStorage"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeFileStorage()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./file-storage.component.ts"></script>
