<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="posAdnimApp.marketBase.home.createOrEditLabel"
          data-cy="MarketBaseCreateUpdateHeading"
          v-text="$t('posAdnimApp.marketBase.home.createOrEditLabel')"
        >
          Create or edit a MarketBase
        </h2>
        <div>
          <div class="form-group" v-if="marketBase.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="marketBase.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.marketBase.quantity')" for="market-base-quantity">Quantity</label>
            <input
              type="text"
              class="form-control"
              name="quantity"
              id="market-base-quantity"
              data-cy="quantity"
              :class="{ valid: !$v.marketBase.quantity.$invalid, invalid: $v.marketBase.quantity.$invalid }"
              v-model="$v.marketBase.quantity.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.marketBase.price')" for="market-base-price">Price</label>
            <input
              type="number"
              class="form-control"
              name="price"
              id="market-base-price"
              data-cy="price"
              :class="{ valid: !$v.marketBase.price.$invalid, invalid: $v.marketBase.price.$invalid }"
              v-model.number="$v.marketBase.price.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.marketBase.currentPrice')" for="market-base-currentPrice"
              >Current Price</label
            >
            <input
              type="number"
              class="form-control"
              name="currentPrice"
              id="market-base-currentPrice"
              data-cy="currentPrice"
              :class="{ valid: !$v.marketBase.currentPrice.$invalid, invalid: $v.marketBase.currentPrice.$invalid }"
              v-model.number="$v.marketBase.currentPrice.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.marketBase.createDate')" for="market-base-createDate"
              >Create Date</label
            >
            <div class="d-flex">
              <input
                id="market-base-createDate"
                data-cy="createDate"
                type="datetime-local"
                class="form-control"
                name="createDate"
                :class="{ valid: !$v.marketBase.createDate.$invalid, invalid: $v.marketBase.createDate.$invalid }"
                :value="convertDateTimeFromServer($v.marketBase.createDate.$model)"
                @change="updateZonedDateTimeField('createDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.marketBase.date')" for="market-base-date">Date</label>
            <div class="d-flex">
              <input
                id="market-base-date"
                data-cy="date"
                type="datetime-local"
                class="form-control"
                name="date"
                :class="{ valid: !$v.marketBase.date.$invalid, invalid: $v.marketBase.date.$invalid }"
                :value="convertDateTimeFromServer($v.marketBase.date.$model)"
                @change="updateZonedDateTimeField('date', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.marketBase.name')" for="market-base-name">Name</label>
            <select class="form-control" id="market-base-name" data-cy="name" name="name" v-model="marketBase.name">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="marketBase.name && productOption.id === marketBase.name.id ? marketBase.name : productOption"
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
            :disabled="$v.marketBase.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./market-base-update.component.ts"></script>
