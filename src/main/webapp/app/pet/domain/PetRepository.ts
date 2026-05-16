import type { Pet, PetToSave } from './Pet';

export interface PetRepository {
  list(): Promise<Pet[]>;
  create(pet: PetToSave): Promise<Pet>;
  update(id: string, pet: PetToSave): Promise<Pet>;
  delete(id: string): Promise<void>;
}
