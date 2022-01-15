<template>
  <div>
    <h2 id="page-heading" data-cy="MarketBaseHeading">
      <span v-text="$t('posAdnimApp.marketBase.home.title')" id="market-base-heading">Market Bases</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('posAdnimApp.marketBase.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'MarketBaseCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-market-base"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('posAdnimApp.marketBase.home.createLabel')"> Create a new Market Base </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && marketBases && marketBases.length === 0">
      <span v-text="$t('posAdnimApp.marketBase.home.notFound')">No marketBases found</span>
    </div>
    <div class="table-responsive" v-if="marketBases && marketBases.length > 0">
      <table class="table table-striped" aria-describedby="marketBases">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.marketBase.quantity')">Quantity</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.marketBase.price')">Price</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.marketBase.currentPrice')">Current Price</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.marketBase.createDate')">Create Date</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.marketBase.date')">Date</span></th>
            <th scope="row"><span v-text="$t('posAdnimApp.marketBase.name')">Name</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="marketBase in marketBases" :key="marketBase.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MarketBaseView', params: { marketBaseId: marketBase.id } }">{{ marketBase.id }}</router-link>
            </td>
            <td>{{ marketBase.quantity }}</td>
            <td>{{ marketBase.price }}</td>
            <td>{{ marketBase.currentPrice }}</td>
            <td>{{ marketBase.createDate ? $d(Date.parse(marketBase.createDate), 'short') : '' }}</td>
            <td>{{ marketBase.date ? $d(Date.parse(marketBase.date), 'short') : '' }}</td>
            <td>
              <div v-if="marketBase.name">
                <router-link :to="{ name: 'ProductView', params: { productId: marketBase.name.id } }">{{
                  marketBase.name.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MarketBaseView', params: { marketBaseId: marketBase.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MarketBaseEdit', params: { marketBaseId: marketBase.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(marketBase)"
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
        ><span id="posAdnimApp.marketBase.delete.question" data-cy="marketBaseDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-marketBase-heading" v-text="$t('posAdnimApp.marketBase.delete.question', { id: removeId })">
          Are you sure you want to delete this Market Base?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-marketBase"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeMarketBase()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./market-base.component.ts"></script>
