package com.example.cpms.repository;

import com.example.cpms.entity.ProjectEntity;
import com.example.cpms.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByClient_Id(Long clientId);
    List<ProjectEntity> findByUser(UserEntity user);

   //This method prevent duplicate projects
    boolean existsByClient_IdAndTitle(Long clientId, String title);
}

