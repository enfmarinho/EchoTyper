package br.ufrn.EchoTyper.register.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.EchoTyper.register.model.Register;

public interface RegisterRepository<RegisterImpl extends Register> extends JpaRepository<RegisterImpl, Long>{
   public Optional<RegisterImpl> findById(Long id);
}
