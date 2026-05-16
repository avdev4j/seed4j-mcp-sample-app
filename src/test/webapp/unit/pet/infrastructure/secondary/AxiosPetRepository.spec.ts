import type { Pet, PetToSave } from '@/pet/domain/Pet';
import { AxiosPetRepository } from '@/pet/infrastructure/secondary/AxiosPetRepository';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import { describe, expect, it } from 'vitest';
import { dataAxiosResponse, stubAxiosInstance } from '../../../shared/http/infrastructure/secondary/AxiosStub';

const umee = (): Pet => ({
  id: '22222222-2222-2222-2222-222222222222',
  name: 'umee',
  breedId: '11111111-1111-1111-1111-111111111111',
});

const newPetPayload = (): PetToSave => ({ name: 'yuki', breedId: '11111111-1111-1111-1111-111111111111' });

describe('AxiosPetRepository', () => {
  it('should list pets', async () => {
    const axiosInstance = stubAxiosInstance();
    axiosInstance.get.mockResolvedValue(dataAxiosResponse([umee()]));
    const repository = new AxiosPetRepository(new AxiosHttp(axiosInstance));

    const pets = await repository.list();

    expect(axiosInstance.get).toHaveBeenCalledWith('/api/pets', {});
    expect(pets).toEqual([umee()]);
  });

  it('should create a pet', async () => {
    const axiosInstance = stubAxiosInstance();
    axiosInstance.post.mockResolvedValue(dataAxiosResponse(umee()));
    const repository = new AxiosPetRepository(new AxiosHttp(axiosInstance));

    const created = await repository.create(newPetPayload());

    expect(axiosInstance.post).toHaveBeenCalledWith('/api/pets', newPetPayload(), undefined);
    expect(created).toEqual(umee());
  });

  it('should update a pet', async () => {
    const axiosInstance = stubAxiosInstance();
    axiosInstance.put.mockResolvedValue(dataAxiosResponse(umee()));
    const repository = new AxiosPetRepository(new AxiosHttp(axiosInstance));

    const updated = await repository.update(umee().id, newPetPayload());

    expect(axiosInstance.put).toHaveBeenCalledWith(`/api/pets/${umee().id}`, newPetPayload());
    expect(updated).toEqual(umee());
  });

  it('should delete a pet', async () => {
    const axiosInstance = stubAxiosInstance();
    axiosInstance.delete.mockResolvedValue(dataAxiosResponse(undefined));
    const repository = new AxiosPetRepository(new AxiosHttp(axiosInstance));

    await repository.delete(umee().id);

    expect(axiosInstance.delete).toHaveBeenCalledWith(`/api/pets/${umee().id}`);
  });
});
