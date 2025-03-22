package com.entidade.relacional.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entidade.relacional.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long>{

    
}
