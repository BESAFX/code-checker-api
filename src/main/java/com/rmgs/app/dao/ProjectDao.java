package com.rmgs.app.dao;

import com.rmgs.app.model.Project;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public interface ProjectDao extends PagingAndSortingRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    Optional<Project> findByName(String name);
    Optional<Project> findByProjKey(String key);
}
