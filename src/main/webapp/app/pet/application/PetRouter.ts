import PetsListVue from '@/pet/infrastructure/primary/PetsListVue.vue';
import type { RouteRecordRaw } from 'vue-router';

export const petRoutes = (): RouteRecordRaw[] => [
  {
    path: '/pets',
    name: 'Pets',
    component: PetsListVue,
  },
];
