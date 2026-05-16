import type { Breed, BreedToSave } from './Breed';

export interface BreedRepository {
  list(): Promise<Breed[]>;
  create(breed: BreedToSave): Promise<Breed>;
  update(id: string, breed: BreedToSave): Promise<Breed>;
  delete(id: string): Promise<void>;
}
