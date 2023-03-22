import { cityListOption } from '@/utils/config';

export const findCityCodeData = (name: string): [number, number] => {
  let cityCode = 0;
  let provinceCode = 0;
  try {
    cityListOption.map((item) => {
      item.children.map((city) => {
        if (city.label === name) {
          cityCode = city.value;
          provinceCode = item.value;
          throw new Error('ok');
        }
      });
    });
  } catch (e: any) {
    console.log(e.message);
  }
  return [provinceCode, cityCode];
};
