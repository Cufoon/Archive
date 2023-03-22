package com.manager.dao;

import com.manager.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProvinceDAO extends JpaRepository<Province, String> {

    Province findByProvinceId(Integer provinceId);

    List<Province> findAll();
}
