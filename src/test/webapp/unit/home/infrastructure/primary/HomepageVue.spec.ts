import type { Breed } from '@/breed/domain/Breed';
import HomepageVue from '@/home/infrastructure/primary/HomepageVue.vue';
import type { Pet } from '@/pet/domain/Pet';
import { flushPromises, mount, type VueWrapper } from '@vue/test-utils';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { createRouter, createWebHistory, type Router } from 'vue-router';

const { petListMock, breedListMock } = vi.hoisted(() => ({
  petListMock: vi.fn(),
  breedListMock: vi.fn(),
}));

vi.mock('@/pet/infrastructure/secondary/AxiosPetRepository', () => ({
  AxiosPetRepository: class {
    list = petListMock;
  },
}));

vi.mock('@/breed/infrastructure/secondary/AxiosBreedRepository', () => ({
  AxiosBreedRepository: class {
    list = breedListMock;
  },
}));

const caneCorso = (): Breed => ({ id: 'b-1', name: 'cane corso' });
const umee = (): Pet => ({ id: 'p-1', name: 'umee', breedId: 'b-1' });
const orphanPet = (): Pet => ({ id: 'p-2', name: 'noname', breedId: 'b-missing' });

let router: Router;

const wrap = (): VueWrapper => {
  return mount(HomepageVue, {
    global: { plugins: [router] },
  });
};

beforeEach(() => {
  router = createRouter({ history: createWebHistory(), routes: [{ path: '/:pathMatch(.*)*', component: HomepageVue }] });
  petListMock.mockReset();
  breedListMock.mockReset();
});

afterEach(() => {
  vi.restoreAllMocks();
});

describe('HomepageVue', () => {
  it('should show the hero copy', async () => {
    petListMock.mockResolvedValue([]);
    breedListMock.mockResolvedValue([]);

    const wrapper = wrap();
    await flushPromises();

    expect(wrapper.text()).toContain('Pet Manager');
    expect(wrapper.text()).toContain('Every good boy and girl');
  });

  it('should render the loading state before promises resolve', () => {
    petListMock.mockReturnValue(new Promise(() => {}));
    breedListMock.mockReturnValue(new Promise(() => {}));

    const wrapper = wrap();

    expect(wrapper.find('.state-loading').exists()).toBe(true);
  });

  it('should render the empty state when no pets are returned', async () => {
    petListMock.mockResolvedValue([]);
    breedListMock.mockResolvedValue([]);

    const wrapper = wrap();
    await flushPromises();

    expect(wrapper.find('.state-empty').exists()).toBe(true);
  });

  it('should render the pets table with breed names when pets are returned', async () => {
    petListMock.mockResolvedValue([umee()]);
    breedListMock.mockResolvedValue([caneCorso()]);

    const wrapper = wrap();
    await flushPromises();

    expect(wrapper.find('.pets-table').exists()).toBe(true);
    expect(wrapper.text()).toContain('umee');
    expect(wrapper.text()).toContain('cane corso');
    expect(wrapper.findAll('.stat-value').map(node => node.text())).toEqual(['1', '1']);
  });

  it('should fall back to a dash when the breed of a pet is unknown', async () => {
    petListMock.mockResolvedValue([orphanPet()]);
    breedListMock.mockResolvedValue([]);

    const wrapper = wrap();
    await flushPromises();

    expect(wrapper.find('.breed-pill').text()).toBe('—');
  });

  it('should render the error state when the backend call fails', async () => {
    petListMock.mockRejectedValue(new Error('offline'));
    breedListMock.mockResolvedValue([]);

    const wrapper = wrap();
    await flushPromises();

    expect(wrapper.find('.state-error').exists()).toBe(true);
  });
});
