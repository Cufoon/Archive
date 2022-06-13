package com.manager.dao;

import com.manager.entity.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeadlineDAO extends JpaRepository<Deadline, String> {

    Deadline findByStudentId(String studentId);
}
