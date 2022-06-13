package com.manager.dao;

import com.manager.entity.Intern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternDAO extends JpaRepository<Intern, String> {

    List<Intern> getByOrderByPeriodIdDesc();
}
