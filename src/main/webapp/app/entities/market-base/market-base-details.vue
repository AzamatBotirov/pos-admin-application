<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="marketBase">
        <h2 class="jh-entity-heading" data-cy="marketBaseDetailsHeading">
          <span v-text="$t('posAdnimApp.marketBase.detail.title')">MarketBase</span> {{ marketBase.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('posAdnimApp.marketBase.quantity')">Quantity</span>
          </dt>
          <dd>
            <span>{{ marketBase.quantity }}</span>
          </dd>
          <dt>
            <span v-text="$t('posAdnimApp.marketBase.price')">Price</span>
          </dt>
          <dd>
            <span>{{ marketBase.price }}</span>
          </dd>
          <dt>
            <span v-text="$t('posAdnimApp.marketBase.currentPrice')">Current Price</span>
          </dt>
          <dd>
            <span>{{ marketBase.currentPrice }}</span>
          </dd>
          <dt>
            <span v-text="$t('posAdnimApp.marketBase.createDate')">Create Date</span>
          </dt>
          <dd>
            <span v-if="marketBase.createDate">{{ $d(Date.parse(marketBase.createDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('posAdnimApp.marketBase.date')">Date</span>
          </dt>
          <dd>
            <span v-if="marketBase.date">{{ $d(Date.parse(marketBase.date), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('posAdnimApp.marketBase.name')">Name</span>
          </dt>
          <dd>
            <div v-if="marketBase.name">
              <router-link :to="{ name: 'ProductView', params: { productId: marketBase.name.id } }">{{ marketBase.name.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="marketBase.id"
          :to="{ name: 'MarketBaseEdit', params: { marketBaseId: marketBase.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./market-base-details.component.ts"></script>
