package br.ufrn.EchoTyper.register.repository;

import org.springframework.stereotype.Repository;

import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.register.model.RegisterGroup;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RegisterGroupRepository<RegisterGroupImpl extends RegisterGroup<RegisterImpl>, RegisterImpl extends Register>
        extends JpaRepository<RegisterGroupImpl, Long> {
}
