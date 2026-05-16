import type { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import type { Breed, BreedToSave } from '@/breed/domain/Breed';
import type { BreedRepository } from '@/breed/domain/BreedRepository';

export class AxiosBreedRepository implements BreedRepository {
  constructor(private readonly http: AxiosHttp) {}

  async list(): Promise<Breed[]> {
    const response = await this.http.get<Breed[]>('/api/breeds');
    return response.data;
  }

  async create(breed: BreedToSave): Promise<Breed> {
    const response = await this.http.post<Breed, BreedToSave>('/api/breeds', breed);
    return response.data;
  }

  async update(id: string, breed: BreedToSave): Promise<Breed> {
    const response = await this.http.put<Breed, BreedToSave>(`/api/breeds/${id}`, breed);
    return response.data;
  }

  async delete(id: string): Promise<void> {
    await this.http.delete<void>(`/api/breeds/${id}`);
  }
}
