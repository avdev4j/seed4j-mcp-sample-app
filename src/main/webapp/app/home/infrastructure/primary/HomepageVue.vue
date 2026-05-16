<script lang="ts">
import type { Breed } from '@/breed/domain/Breed';
import { AxiosBreedRepository } from '@/breed/infrastructure/secondary/AxiosBreedRepository';
import type { Pet } from '@/pet/domain/Pet';
import { AxiosPetRepository } from '@/pet/infrastructure/secondary/AxiosPetRepository';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import axios from 'axios';
import { defineComponent } from 'vue';

const http = new AxiosHttp(axios.create());
const petRepository = new AxiosPetRepository(http);
const breedRepository = new AxiosBreedRepository(http);

type Status = 'loading' | 'ready' | 'error';

export default defineComponent({
  name: 'HomepageVue',
  data() {
    return {
      pets: [] as Pet[],
      breeds: [] as Breed[],
      status: 'loading' as Status,
    };
  },
  computed: {
    breedNameById(): Record<string, string> {
      return Object.fromEntries(this.breeds.map(b => [b.id, b.name]));
    },
    totalPets(): number {
      return this.pets.length;
    },
    totalBreeds(): number {
      return this.breeds.length;
    },
    hasPets(): boolean {
      return this.pets.length > 0;
    },
  },
  async mounted() {
    try {
      const [pets, breeds] = await Promise.all([petRepository.list(), breedRepository.list()]);
      this.pets = pets;
      this.breeds = breeds;
      this.status = 'ready';
    } catch {
      this.status = 'error';
    }
  },
});
</script>

<template>
  <main class="homepage">
    <header class="hero">
      <p class="hero-eyebrow">Pet Manager</p>
      <h1 class="hero-title">Every good boy and girl, in one place.</h1>
      <p class="hero-subtitle">
        Browse, edit, and care for the entire crew — pets and their breeds, all wired through a hexagonal Spring + Vue stack.
      </p>
      <nav class="hero-actions">
        <router-link to="/pets" class="btn btn-primary">Manage pets</router-link>
        <router-link to="/breeds" class="btn btn-ghost">Manage breeds</router-link>
      </nav>
    </header>

    <section class="stats">
      <article class="stat-card stat-card--pets">
        <span class="stat-label">Pets</span>
        <span class="stat-value">{{ totalPets }}</span>
      </article>
      <article class="stat-card stat-card--breeds">
        <span class="stat-label">Breeds</span>
        <span class="stat-value">{{ totalBreeds }}</span>
      </article>
    </section>

    <section class="panel">
      <header class="panel-header">
        <h2>Pets in residence</h2>
        <router-link to="/pets" class="panel-link">Open CRUD →</router-link>
      </header>

      <p v-if="status === 'loading'" class="state state-loading">Fetching the pack…</p>
      <p v-else-if="status === 'error'" class="state state-error">
        Could not reach the backend. Make sure the Spring app is running on port 8080.
      </p>
      <p v-else-if="!hasPets" class="state state-empty">
        No pets yet. Head over to <router-link to="/pets">/pets</router-link> to add your first companion.
      </p>
      <table v-else class="pets-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Breed</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="pet in pets" :key="pet.id">
            <td class="pet-name">{{ pet.name }}</td>
            <td>
              <span class="breed-pill">{{ breedNameById[pet.breedId] ?? '—' }}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </section>
  </main>
</template>

<style scoped>
.homepage {
  min-height: 100vh;
  padding: 4rem 1.5rem 6rem;
  background: radial-gradient(circle at top left, #6366f1 0%, #0f172a 55%, #020617 100%);
  color: #e2e8f0;
  font-family: 'Inter', Avenir, Helvetica, Arial, sans-serif;
  text-align: left;
}

.hero {
  max-width: 880px;
  margin: 0 auto 3rem;
  text-align: center;
}

.hero-eyebrow {
  text-transform: uppercase;
  letter-spacing: 0.4em;
  font-size: 0.75rem;
  color: #a5b4fc;
  margin-bottom: 1rem;
}

.hero-title {
  font-size: clamp(2rem, 4vw, 3.5rem);
  font-weight: 800;
  line-height: 1.1;
  background: linear-gradient(135deg, #f8fafc 0%, #c7d2fe 50%, #fbcfe8 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  margin: 0 0 1rem;
}

.hero-subtitle {
  color: #cbd5f5;
  max-width: 640px;
  margin: 0 auto 2rem;
  font-size: 1.05rem;
  line-height: 1.6;
}

.hero-actions {
  display: inline-flex;
  gap: 0.75rem;
  flex-wrap: wrap;
  justify-content: center;
}

.btn {
  display: inline-block;
  padding: 0.75rem 1.5rem;
  border-radius: 999px;
  text-decoration: none;
  font-weight: 600;
  transition:
    transform 0.15s ease,
    box-shadow 0.15s ease,
    background 0.15s ease;
}

.btn-primary {
  background: linear-gradient(135deg, #6366f1, #ec4899);
  color: #fff;
  box-shadow: 0 12px 30px -10px rgba(99, 102, 241, 0.7);
}
.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 18px 36px -10px rgba(236, 72, 153, 0.6);
}

.btn-ghost {
  background: rgba(255, 255, 255, 0.08);
  color: #e2e8f0;
  border: 1px solid rgba(255, 255, 255, 0.15);
}
.btn-ghost:hover {
  background: rgba(255, 255, 255, 0.16);
}

.stats {
  max-width: 880px;
  margin: 0 auto 3rem;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 1.25rem;
}

.stat-card {
  padding: 1.75rem 1.5rem;
  border-radius: 1.25rem;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(12px);
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  position: relative;
  overflow: hidden;
}

.stat-card::after {
  content: '';
  position: absolute;
  inset: 0;
  background: radial-gradient(circle at top right, rgba(255, 255, 255, 0.15), transparent 60%);
  pointer-events: none;
}

.stat-card--pets {
  box-shadow: 0 18px 40px -22px rgba(99, 102, 241, 0.8);
}
.stat-card--breeds {
  box-shadow: 0 18px 40px -22px rgba(236, 72, 153, 0.7);
}

.stat-label {
  text-transform: uppercase;
  letter-spacing: 0.15em;
  font-size: 0.75rem;
  color: #c7d2fe;
}

.stat-value {
  font-size: 2.5rem;
  font-weight: 800;
  color: #fff;
}

.panel {
  max-width: 880px;
  margin: 0 auto;
  background: rgba(15, 23, 42, 0.65);
  border-radius: 1.5rem;
  border: 1px solid rgba(255, 255, 255, 0.08);
  padding: 2rem;
  backdrop-filter: blur(14px);
  box-shadow: 0 30px 60px -30px rgba(2, 6, 23, 0.9);
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1.25rem;
}

.panel-header h2 {
  margin: 0;
  font-size: 1.35rem;
  color: #f8fafc;
}

.panel-link {
  color: #a5b4fc;
  text-decoration: none;
  font-weight: 600;
  font-size: 0.95rem;
}
.panel-link:hover {
  color: #fbcfe8;
}

.state {
  margin: 0;
  padding: 1.5rem 0;
  text-align: center;
  color: #cbd5f5;
}

.state-error {
  color: #fda4af;
}

.state-empty a {
  color: #a5b4fc;
}

.pets-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
}

.pets-table th,
.pets-table td {
  padding: 0.85rem 1rem;
  text-align: left;
}

.pets-table thead th {
  text-transform: uppercase;
  letter-spacing: 0.12em;
  font-size: 0.7rem;
  color: #94a3b8;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.pets-table tbody tr {
  transition: background 0.15s ease;
}
.pets-table tbody tr:hover {
  background: rgba(255, 255, 255, 0.04);
}

.pets-table tbody td {
  border-bottom: 1px solid rgba(255, 255, 255, 0.04);
}

.pet-name {
  font-weight: 600;
  color: #f8fafc;
}

.breed-pill {
  display: inline-block;
  padding: 0.25rem 0.75rem;
  border-radius: 999px;
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.25), rgba(236, 72, 153, 0.25));
  color: #e0e7ff;
  font-size: 0.85rem;
  border: 1px solid rgba(165, 180, 252, 0.25);
}
</style>
