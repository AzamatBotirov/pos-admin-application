import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const FileStorage = () => import('@/entities/file-storage/file-storage.vue');
// prettier-ignore
const FileStorageUpdate = () => import('@/entities/file-storage/file-storage-update.vue');
// prettier-ignore
const FileStorageDetails = () => import('@/entities/file-storage/file-storage-details.vue');
// prettier-ignore
const MarketBase = () => import('@/entities/market-base/market-base.vue');
// prettier-ignore
const MarketBaseUpdate = () => import('@/entities/market-base/market-base-update.vue');
// prettier-ignore
const MarketBaseDetails = () => import('@/entities/market-base/market-base-details.vue');
// prettier-ignore
const PaymentList = () => import('@/entities/payment-list/payment-list.vue');
// prettier-ignore
const PaymentListUpdate = () => import('@/entities/payment-list/payment-list-update.vue');
// prettier-ignore
const PaymentListDetails = () => import('@/entities/payment-list/payment-list-details.vue');
// prettier-ignore
const Product = () => import('@/entities/product/product.vue');
// prettier-ignore
const ProductUpdate = () => import('@/entities/product/product-update.vue');
// prettier-ignore
const ProductDetails = () => import('@/entities/product/product-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/file-storage',
    name: 'FileStorage',
    component: FileStorage,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/file-storage/new',
    name: 'FileStorageCreate',
    component: FileStorageUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/file-storage/:fileStorageId/edit',
    name: 'FileStorageEdit',
    component: FileStorageUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/file-storage/:fileStorageId/view',
    name: 'FileStorageView',
    component: FileStorageDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/market-base',
    name: 'MarketBase',
    component: MarketBase,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/market-base/new',
    name: 'MarketBaseCreate',
    component: MarketBaseUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/market-base/:marketBaseId/edit',
    name: 'MarketBaseEdit',
    component: MarketBaseUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/market-base/:marketBaseId/view',
    name: 'MarketBaseView',
    component: MarketBaseDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/payment-list',
    name: 'PaymentList',
    component: PaymentList,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/payment-list/new',
    name: 'PaymentListCreate',
    component: PaymentListUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/payment-list/:paymentListId/edit',
    name: 'PaymentListEdit',
    component: PaymentListUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/payment-list/:paymentListId/view',
    name: 'PaymentListView',
    component: PaymentListDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/product',
    name: 'Product',
    component: Product,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/product/new',
    name: 'ProductCreate',
    component: ProductUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/product/:productId/edit',
    name: 'ProductEdit',
    component: ProductUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/product/:productId/view',
    name: 'ProductView',
    component: ProductDetails,
    meta: { authorities: [Authority.USER] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
