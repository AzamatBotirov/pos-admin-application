<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="posAdnimApp.fileStorage.home.createOrEditLabel"
          data-cy="FileStorageCreateUpdateHeading"
          v-text="$t('posAdnimApp.fileStorage.home.createOrEditLabel')"
        >
          Create or edit a FileStorage
        </h2>
        <div>
          <div class="form-group" v-if="fileStorage.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="fileStorage.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.fileStorage.fileName')" for="file-storage-fileName">File Name</label>
            <input
              type="text"
              class="form-control"
              name="fileName"
              id="file-storage-fileName"
              data-cy="fileName"
              :class="{ valid: !$v.fileStorage.fileName.$invalid, invalid: $v.fileStorage.fileName.$invalid }"
              v-model="$v.fileStorage.fileName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.fileStorage.extension')" for="file-storage-extension"
              >Extension</label
            >
            <input
              type="text"
              class="form-control"
              name="extension"
              id="file-storage-extension"
              data-cy="extension"
              :class="{ valid: !$v.fileStorage.extension.$invalid, invalid: $v.fileStorage.extension.$invalid }"
              v-model="$v.fileStorage.extension.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.fileStorage.fileSize')" for="file-storage-fileSize">File Size</label>
            <input
              type="number"
              class="form-control"
              name="fileSize"
              id="file-storage-fileSize"
              data-cy="fileSize"
              :class="{ valid: !$v.fileStorage.fileSize.$invalid, invalid: $v.fileStorage.fileSize.$invalid }"
              v-model.number="$v.fileStorage.fileSize.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.fileStorage.hashId')" for="file-storage-hashId">Hash Id</label>
            <input
              type="text"
              class="form-control"
              name="hashId"
              id="file-storage-hashId"
              data-cy="hashId"
              :class="{ valid: !$v.fileStorage.hashId.$invalid, invalid: $v.fileStorage.hashId.$invalid }"
              v-model="$v.fileStorage.hashId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.fileStorage.contentType')" for="file-storage-contentType"
              >Content Type</label
            >
            <input
              type="text"
              class="form-control"
              name="contentType"
              id="file-storage-contentType"
              data-cy="contentType"
              :class="{ valid: !$v.fileStorage.contentType.$invalid, invalid: $v.fileStorage.contentType.$invalid }"
              v-model="$v.fileStorage.contentType.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.fileStorage.uploadPath')" for="file-storage-uploadPath"
              >Upload Path</label
            >
            <input
              type="text"
              class="form-control"
              name="uploadPath"
              id="file-storage-uploadPath"
              data-cy="uploadPath"
              :class="{ valid: !$v.fileStorage.uploadPath.$invalid, invalid: $v.fileStorage.uploadPath.$invalid }"
              v-model="$v.fileStorage.uploadPath.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.fileStorage.fileStorageStatus')" for="file-storage-fileStorageStatus"
              >File Storage Status</label
            >
            <select
              class="form-control"
              name="fileStorageStatus"
              :class="{ valid: !$v.fileStorage.fileStorageStatus.$invalid, invalid: $v.fileStorage.fileStorageStatus.$invalid }"
              v-model="$v.fileStorage.fileStorageStatus.$model"
              id="file-storage-fileStorageStatus"
              data-cy="fileStorageStatus"
            >
              <option value="ACTIVE" v-bind:label="$t('posAdnimApp.FileStorageStatus.ACTIVE')">ACTIVE</option>
              <option value="DRAFT" v-bind:label="$t('posAdnimApp.FileStorageStatus.DRAFT')">DRAFT</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.fileStorage.name')" for="file-storage-name">Name</label>
            <select class="form-control" id="file-storage-name" data-cy="name" name="name" v-model="fileStorage.name">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="fileStorage.name && productOption.id === fileStorage.name.id ? fileStorage.name : productOption"
                v-for="productOption in products"
                :key="productOption.id"
              >
                {{ productOption.name }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.fileStorage.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./file-storage-update.component.ts"></script>
