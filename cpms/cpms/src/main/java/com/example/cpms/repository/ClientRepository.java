package com.example.cpms.repository;

import com.example.cpms.entity.ClientEntity;
import com.example.cpms.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    List<ClientEntity> findByUser(UserEntity user);
}