package com.manager.service.count;

import com.manager.dao.ProvinceDAO;
import com.manager.entity.Province;
import com.manager.vo.count.CountVO;
import com.manager.vo.count.ProvinceVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountPeopleServiceImpl implements CountPeopleService {

    private final ProvinceDAO provinceDAO;

    public CountPeopleServiceImpl(ProvinceDAO provinceDAO) {

        this.provinceDAO = provinceDAO;
    }

    @Override
    public CountVO countPeople() {

        List<Province> provinceList = provinceDAO.findAll();
//        List<City> cityList = cityDAO.findAll();
        List<ProvinceVO> provinceVOList = new ArrayList<>();
//        List<CityVO> cityVOList = new ArrayList<>();

        for (Province province : provinceList) {
//            if (province.getPeople() == 0) {
//                continue;
//            }
            ProvinceVO provinceVO = new ProvinceVO(
                    province.getProvinceId(),
                    province.getProvince(),
                    province.getPeople()
            );
            provinceVOList.add(provinceVO);
        }

//        for (City city : cityList) {
//            if (city.getPeople() == 0) {
//                continue;
//            }
//            CityVO cityVO = new CityVO(
//                    city.getCityId(),
//                    city.getCity(),
//                    city.getProvinceId(),
//                    city.getPeople()
//            );
//            cityVOList.add(cityVO);
//        }
//        CountVO countVO = new CountVO(
//                provinceVOList,
//                cityVOList
//        );
        return new CountVO(provinceVOList);
    }
}
