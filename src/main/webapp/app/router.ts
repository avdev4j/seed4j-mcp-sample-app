import { homeRoutes } from '@/home/application/HomeRouter';
import { breedRoutes } from '@/breed/application/BreedRouter';
import { petRoutes } from '@/pet/application/PetRouter';
import { createRouter, createWebHistory } from 'vue-router';

export const routes = [...homeRoutes(), ...breedRoutes(), ...petRoutes()];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
