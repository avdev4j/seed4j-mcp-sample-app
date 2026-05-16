import BreedsListVue from '@/breed/infrastructure/primary/BreedsListVue.vue';
import type { RouteRecordRaw } from 'vue-router';

export const breedRoutes = (): RouteRecordRaw[] => [
  {
    path: '/breeds',
    name: 'Breeds',
    component: BreedsListVue,
  },
];
