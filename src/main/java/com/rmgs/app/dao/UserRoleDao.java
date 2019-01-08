package com.rmgs.app.dao;


import com.rmgs.app.model.UserRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public interface UserRoleDao extends PagingAndSortingRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {


}
