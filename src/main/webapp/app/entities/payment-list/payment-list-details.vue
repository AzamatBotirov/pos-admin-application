<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="paymentList">
        <h2 class="jh-entity-heading" data-cy="paymentListDetailsHeading">
          <span v-text="$t('posAdnimApp.paymentList.detail.title')">PaymentList</span> {{ paymentList.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('posAdnimApp.paymentList.quantity')">Quantity</span>
          </dt>
          <dd>
            <span>{{ paymentList.quantity }}</span>
          </dd>
          <dt>
            <span v-text="$t('posAdnimApp.paymentList.summa')">Summa</span>
          </dt>
          <dd>
            <span>{{ paymentList.summa }}</span>
          </dd>
          <dt>
            <span v-text="$t('posAdnimApp.paymentList.name')">Name</span>
          </dt>
          <dd>
            <div v-if="paymentList.name">
              <router-link :to="{ name: 'ProductView', params: { productId: paymentList.name.id } }">{{ paymentList.name.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="paymentList.id"
          :to="{ name: 'PaymentListEdit', params: { paymentListId: paymentList.id } }"
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

<script lang="ts" src="./payment-list-details.component.ts"></script>
