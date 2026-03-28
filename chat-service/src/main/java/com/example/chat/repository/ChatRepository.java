package com.example.chat.repository;

import com.example.chat.entity.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Message, Long> {

    Message save(Message message);

    Optional<Message> findById(Long id);

        @Query("""
    SELECT m FROM Message m 
    WHERE (m.senderId = :currentUserId AND m.receiverId = :userId)
    OR (m.senderId = :userId AND m.receiverId = :currentUserId) ORDER BY m.timestamp""")
        List<Message> findConversation(Long currentUserId, Long userId);

        @Modifying
        @Query("""
    UPDATE Message m SET m.isRead = true WHERE m.receiverId = :currentUserId 
    AND m.senderId = :userId AND m.isRead = false""")
        void markMessagesAsRead(Long currentUserId, Long userId);
}
