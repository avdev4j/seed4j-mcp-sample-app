<script lang="ts">
  import axios from 'axios';
  import { defineComponent } from 'vue';
  import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
  import { AxiosBreedRepository } from '@/breed/infrastructure/secondary/AxiosBreedRepository';
  import type { Breed } from '@/breed/domain/Breed';

  const repository = new AxiosBreedRepository(new AxiosHttp(axios.create()));

  export default defineComponent({
    name: 'BreedsListVue',
    data() {
      return {
        breeds: [] as Breed[],
        newBreedName: '',
        editingId: null as string | null,
        editingName: '',
        error: '',
      };
    },
    async mounted() {
      await this.refresh();
    },
    methods: {
      async refresh() {
        this.error = '';
        try {
          this.breeds = await repository.list();
        } catch (e) {
          this.error = `Failed to load breeds: ${(e as Error).message}`;
        }
      },
      async create() {
        if (!this.newBreedName.trim()) return;
        this.error = '';
        try {
          await repository.create({ name: this.newBreedName.trim() });
          this.newBreedName = '';
          await this.refresh();
        } catch (e) {
          this.error = `Create failed: ${(e as Error).message}`;
        }
      },
      startEdit(breed: Breed) {
        this.editingId = breed.id;
        this.editingName = breed.name;
      },
      cancelEdit() {
        this.editingId = null;
        this.editingName = '';
      },
      async saveEdit(id: string) {
        this.error = '';
        try {
          await repository.update(id, { name: this.editingName.trim() });
          this.cancelEdit();
          await this.refresh();
        } catch (e) {
          this.error = `Update failed: ${(e as Error).message}`;
        }
      },
      async remove(id: string) {
        if (!confirm('Delete this breed?')) return;
        this.error = '';
        try {
          await repository.delete(id);
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
    <h2>Breeds</h2>
    <p v-if="error" class="error">{{ error }}</p>

    <form @submit.prevent="create">
      <input v-model="newBreedName" placeholder="New breed name" />
      <button type="submit">Add</button>
    </form>

    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="breed in breeds" :key="breed.id">
          <td>
            <template v-if="editingId === breed.id">
              <input v-model="editingName" />
            </template>
            <template v-else>{{ breed.name }}</template>
          </td>
          <td>
            <template v-if="editingId === breed.id">
              <button @click="saveEdit(breed.id)">Save</button>
              <button @click="cancelEdit">Cancel</button>
            </template>
            <template v-else>
              <button @click="startEdit(breed)">Edit</button>
              <button @click="remove(breed.id)">Delete</button>
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
