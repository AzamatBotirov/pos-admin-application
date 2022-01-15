<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="product">
        <h2 class="jh-entity-heading" data-cy="productDetailsHeading">
          <span v-text="$t('posAdnimApp.product.detail.title')">Product</span> {{ product.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('posAdnimApp.product.name')">Name</span>
          </dt>
          <dd>
            <span>{{ product.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('posAdnimApp.product.code')">Code</span>
          </dt>
          <dd>
            <span>{{ product.code }}</span>
          </dd>
          <dt>
            <span v-text="$t('posAdnimApp.product.type')">Type</span>
          </dt>
          <dd>
            <span v-text="$t('posAdnimApp.ProductType.' + product.type)">{{ product.type }}</span>
          </dd>
          <dt>
            <span v-text="$t('posAdnimApp.product.directive')">Directive</span>
          </dt>
          <dd>
            <span v-text="$t('posAdnimApp.ProductDirective.' + product.directive)">{{ product.directive }}</span>
          </dd>
          <dt>
            <span v-text="$t('posAdnimApp.product.status')">Status</span>
          </dt>
          <dd>
            <span v-text="$t('posAdnimApp.Status.' + product.status)">{{ product.status }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="product.id" :to="{ name: 'ProductEdit', params: { productId: product.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./product-details.component.ts"></script>
