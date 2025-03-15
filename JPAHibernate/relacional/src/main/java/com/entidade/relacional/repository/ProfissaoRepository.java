package com.entidade.relacional.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entidade.relacional.model.Profissao;

public interface ProfissaoRepository extends JpaRepository<Profissao, Long>{

}
