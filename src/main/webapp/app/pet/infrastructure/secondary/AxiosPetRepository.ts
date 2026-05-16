import type { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import type { Pet, PetToSave } from '@/pet/domain/Pet';
import type { PetRepository } from '@/pet/domain/PetRepository';

export class AxiosPetRepository implements PetRepository {
  constructor(private readonly http: AxiosHttp) {}

  async list(): Promise<Pet[]> {
    const response = await this.http.get<Pet[]>('/api/pets');
    return response.data;
  }

  async create(pet: PetToSave): Promise<Pet> {
    const response = await this.http.post<Pet, PetToSave>('/api/pets', pet);
    return response.data;
  }

  async update(id: string, pet: PetToSave): Promise<Pet> {
    const response = await this.http.put<Pet, PetToSave>(`/api/pets/${id}`, pet);
    return response.data;
  }

  async delete(id: string): Promise<void> {
    await this.http.delete<void>(`/api/pets/${id}`);
  }
}
