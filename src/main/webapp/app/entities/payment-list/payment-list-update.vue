<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="posAdnimApp.paymentList.home.createOrEditLabel"
          data-cy="PaymentListCreateUpdateHeading"
          v-text="$t('posAdnimApp.paymentList.home.createOrEditLabel')"
        >
          Create or edit a PaymentList
        </h2>
        <div>
          <div class="form-group" v-if="paymentList.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="paymentList.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.paymentList.quantity')" for="payment-list-quantity">Quantity</label>
            <input
              type="number"
              class="form-control"
              name="quantity"
              id="payment-list-quantity"
              data-cy="quantity"
              :class="{ valid: !$v.paymentList.quantity.$invalid, invalid: $v.paymentList.quantity.$invalid }"
              v-model.number="$v.paymentList.quantity.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.paymentList.summa')" for="payment-list-summa">Summa</label>
            <input
              type="number"
              class="form-control"
              name="summa"
              id="payment-list-summa"
              data-cy="summa"
              :class="{ valid: !$v.paymentList.summa.$invalid, invalid: $v.paymentList.summa.$invalid }"
              v-model.number="$v.paymentList.summa.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('posAdnimApp.paymentList.name')" for="payment-list-name">Name</label>
            <select class="form-control" id="payment-list-name" data-cy="name" name="name" v-model="paymentList.name">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="paymentList.name && productOption.id === paymentList.name.id ? paymentList.name : productOption"
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
            :disabled="$v.paymentList.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./payment-list-update.component.ts"></script>
