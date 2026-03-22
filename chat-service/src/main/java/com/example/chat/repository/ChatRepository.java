package com.example.chat.repository;

import com.example.chat.entity.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Message, Long> {

    Message save(Message message);

    Optional<Message> findById(Long id);

    List<Message> findByUserId(String userId);
}
