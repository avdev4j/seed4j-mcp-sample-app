<script lang="ts">
  import axios from 'axios';
  import { defineComponent } from 'vue';
  import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
  import { AxiosPetRepository } from '@/pet/infrastructure/secondary/AxiosPetRepository';
  import { AxiosBreedRepository } from '@/breed/infrastructure/secondary/AxiosBreedRepository';
  import type { Pet } from '@/pet/domain/Pet';
  import type { Breed } from '@/breed/domain/Breed';

  const http = new AxiosHttp(axios.create());
  const petRepository = new AxiosPetRepository(http);
  const breedRepository = new AxiosBreedRepository(http);

  interface PetDraft {
    name: string;
    breedId: string;
  }

  export default defineComponent({
    name: 'PetsListVue',
    data() {
      return {
        pets: [] as Pet[],
        breeds: [] as Breed[],
        draft: { name: '', breedId: '' } as PetDraft,
        editingId: null as string | null,
        editingDraft: { name: '', breedId: '' } as PetDraft,
        error: '',
      };
    },
    computed: {
      breedNameById(): Record<string, string> {
        return Object.fromEntries(this.breeds.map(b => [b.id, b.name]));
      },
    },
    async mounted() {
      await this.refresh();
    },
    methods: {
      async refresh() {
        this.error = '';
        try {
          const [pets, breeds] = await Promise.all([petRepository.list(), breedRepository.list()]);
          this.pets = pets;
          this.breeds = breeds;
          if (!this.draft.breedId && breeds.length > 0) {
            this.draft.breedId = breeds[0].id;
          }
        } catch (e) {
          this.error = `Failed to load: ${(e as Error).message}`;
        }
      },
      async create() {
        if (!this.draft.name.trim() || !this.draft.breedId) return;
        this.error = '';
        try {
          await petRepository.create({ name: this.draft.name.trim(), breedId: this.draft.breedId });
          this.draft.name = '';
          await this.refresh();
        } catch (e) {
          this.error = `Create failed: ${(e as Error).message}`;
        }
      },
      startEdit(pet: Pet) {
        this.editingId = pet.id;
        this.editingDraft = { name: pet.name, breedId: pet.breedId };
      },
      cancelEdit() {
        this.editingId = null;
        this.editingDraft = { name: '', breedId: '' };
      },
      async saveEdit(id: string) {
        this.error = '';
        try {
          await petRepository.update(id, {
            name: this.editingDraft.name.trim(),
            breedId: this.editingDraft.breedId,
          });
          this.cancelEdit();
          await this.refresh();
        } catch (e) {
          this.error = `Update failed: ${(e as Error).message}`;
        }
      },
      async remove(id: string) {
        if (!confirm('Delete this pet?')) return;
        this.error = '';
        try {
          await petRepository.delete(id);
          await this.refresh();
        } catch (e) {
          this.error = `Delete failed: ${(e as Error).message}`;
        }
      },
    },
  });
</script>

<template>
  <section class="crud-page">
    <h2>Pets</h2>
    <p v-if="error" class="error">{{ error }}</p>

    <form @submit.prevent="create">
      <input v-model="draft.name" placeholder="Pet name" />
      <select v-model="draft.breedId">
        <option v-for="breed in breeds" :key="breed.id" :value="breed.id">
          {{ breed.name }}
        </option>
      </select>
      <button type="submit" :disabled="breeds.length === 0">Add</button>
    </form>

    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Breed</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="pet in pets" :key="pet.id">
          <td>
            <template v-if="editingId === pet.id">
              <input v-model="editingDraft.name" />
            </template>
            <template v-else>{{ pet.name }}</template>
          </td>
          <td>
            <template v-if="editingId === pet.id">
              <select v-model="editingDraft.breedId">
                <option v-for="breed in breeds" :key="breed.id" :value="breed.id">
                  {{ breed.name }}
                </option>
              </select>
            </template>
            <template v-else>{{ breedNameById[pet.breedId] ?? pet.breedId }}</template>
          </td>
          <td>
            <template v-if="editingId === pet.id">
              <button @click="saveEdit(pet.id)">Save</button>
              <button @click="cancelEdit">Cancel</button>
            </template>
            <template v-else>
              <button @click="startEdit(pet)">Edit</button>
              <button @click="remove(pet.id)">Delete</button>
            </template>
          </td>
        </tr>
      </tbody>
    </table>

    <router-link to="/">← back home</router-link>
  </section>
</template>

<style scoped>
  .crud-page {
    max-width: 720px;
    margin: 2rem auto;
    font-family: Avenir, Helvetica, Arial, sans-serif;
  }
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 1rem;
  }
  th,
  td {
    border-bottom: 1px solid #ddd;
    padding: 0.5rem;
    text-align: left;
  }
  button {
    margin-right: 0.25rem;
  }
  .error {
    color: #c0392b;
  }
</style>
