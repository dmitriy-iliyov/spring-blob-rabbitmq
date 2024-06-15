package com.example.rabbitmq_api.repositorys;

import com.example.rabbitmq_api.models.MessageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<MessageEntity, Long> {
}
