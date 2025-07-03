package br.ufrn.EchoTyper.registerGroup.repository;

import org.springframework.stereotype.Repository;

import br.ufrn.EchoTyper.registerGroup.model.RegisterGroup;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RegisterGroupRepository extends JpaRepository<RegisterGroup,Long> {
}
