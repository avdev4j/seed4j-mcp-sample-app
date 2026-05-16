import type { Breed, BreedToSave } from '@/breed/domain/Breed';
import { AxiosBreedRepository } from '@/breed/infrastructure/secondary/AxiosBreedRepository';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import { describe, expect, it } from 'vitest';
import { dataAxiosResponse, stubAxiosInstance } from '../../../shared/http/infrastructure/secondary/AxiosStub';

const caneCorso = (): Breed => ({ id: '11111111-1111-1111-1111-111111111111', name: 'cane corso' });

const newBreedPayload = (): BreedToSave => ({ name: 'labrador' });

describe('AxiosBreedRepository', () => {
  it('should list breeds', async () => {
    const axiosInstance = stubAxiosInstance();
    axiosInstance.get.mockResolvedValue(dataAxiosResponse([caneCorso()]));
    const repository = new AxiosBreedRepository(new AxiosHttp(axiosInstance));

    const breeds = await repository.list();

    expect(axiosInstance.get).toHaveBeenCalledWith('/api/breeds', {});
    expect(breeds).toEqual([caneCorso()]);
  });

  it('should create a breed', async () => {
    const axiosInstance = stubAxiosInstance();
    axiosInstance.post.mockResolvedValue(dataAxiosResponse(caneCorso()));
    const repository = new AxiosBreedRepository(new AxiosHttp(axiosInstance));

    const created = await repository.create(newBreedPayload());

    expect(axiosInstance.post).toHaveBeenCalledWith('/api/breeds', newBreedPayload(), undefined);
    expect(created).toEqual(caneCorso());
  });

  it('should update a breed', async () => {
    const axiosInstance = stubAxiosInstance();
    axiosInstance.put.mockResolvedValue(dataAxiosResponse(caneCorso()));
    const repository = new AxiosBreedRepository(new AxiosHttp(axiosInstance));

    const updated = await repository.update(caneCorso().id, newBreedPayload());

    expect(axiosInstance.put).toHaveBeenCalledWith(`/api/breeds/${caneCorso().id}`, newBreedPayload());
    expect(updated).toEqual(caneCorso());
  });

  it('should delete a breed', async () => {
    const axiosInstance = stubAxiosInstance();
    axiosInstance.delete.mockResolvedValue(dataAxiosResponse(undefined));
    const repository = new AxiosBreedRepository(new AxiosHttp(axiosInstance));

    await repository.delete(caneCorso().id);

    expect(axiosInstance.delete).toHaveBeenCalledWith(`/api/breeds/${caneCorso().id}`);
  });
});
