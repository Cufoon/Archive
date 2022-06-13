package com.manager.dao;

import com.manager.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityDAO extends JpaRepository<City, Integer> {

    List<City> findAll();
}
