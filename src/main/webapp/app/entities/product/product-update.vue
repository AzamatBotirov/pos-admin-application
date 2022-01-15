<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="posAdnimApp.product.home.createOrEditLabel"
          data-cy="ProductCreateUpdateHeading"
          v-text="$t('posAdnimApp.product.home.createOrEditLabel')"
        >
          Create or edit a Product
        </h2>
        <div>
          <div class="form-group" v-if="product.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="product.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.product.name')" for="product-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="product-name"
              data-cy="name"
              :class="{ valid: !$v.product.name.$invalid, invalid: $v.product.name.$invalid }"
              v-model="$v.product.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.product.code')" for="product-code">Code</label>
            <input
              type="text"
              class="form-control"
              name="code"
              id="product-code"
              data-cy="code"
              :class="{ valid: !$v.product.code.$invalid, invalid: $v.product.code.$invalid }"
              v-model="$v.product.code.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.product.type')" for="product-type">Type</label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !$v.product.type.$invalid, invalid: $v.product.type.$invalid }"
              v-model="$v.product.type.$model"
              id="product-type"
              data-cy="type"
            >
              <option value="KG" v-bind:label="$t('posAdnimApp.ProductType.KG')">KG</option>
              <option value="L" v-bind:label="$t('posAdnimApp.ProductType.L')">L</option>
              <option value="DONA" v-bind:label="$t('posAdnimApp.ProductType.DONA')">DONA</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.product.directive')" for="product-directive">Directive</label>
            <select
              class="form-control"
              name="directive"
              :class="{ valid: !$v.product.directive.$invalid, invalid: $v.product.directive.$invalid }"
              v-model="$v.product.directive.$model"
              id="product-directive"
              data-cy="directive"
            >
              <option value="MEVA" v-bind:label="$t('posAdnimApp.ProductDirective.MEVA')">MEVA</option>
              <option value="SABZAVOT" v-bind:label="$t('posAdnimApp.ProductDirective.SABZAVOT')">SABZAVOT</option>
              <option value="SHIRINLIK" v-bind:label="$t('posAdnimApp.ProductDirective.SHIRINLIK')">SHIRINLIK</option>
              <option value="GOSHT" v-bind:label="$t('posAdnimApp.ProductDirective.GOSHT')">GOSHT</option>
              <option value="NON" v-bind:label="$t('posAdnimApp.ProductDirective.NON')">NON</option>
              <option value="BOSHQA" v-bind:label="$t('posAdnimApp.ProductDirective.BOSHQA')">BOSHQA</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.product.status')" for="product-status">Status</label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !$v.product.status.$invalid, invalid: $v.product.status.$invalid }"
              v-model="$v.product.status.$model"
              id="product-status"
              data-cy="status"
            >
              <option value="ACTIVE" v-bind:label="$t('posAdnimApp.Status.ACTIVE')">ACTIVE</option>
              <option value="DRAFT" v-bind:label="$t('posAdnimApp.Status.DRAFT')">DRAFT</option>
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
            :disabled="$v.product.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./product-update.component.ts"></script>
