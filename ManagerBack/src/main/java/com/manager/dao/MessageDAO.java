package com.manager.dao;

import com.manager.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MessageDAO extends JpaRepository<Message, Integer> {

    List<Message> findAllByRecipientId(String rid);

    List<Message> findAllByRecipientIdIsNullAndTypeIsIn(Collection<String> type);
}
