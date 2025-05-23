package br.ufrn.EchoTyper.meeting.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.ufrn.EchoTyper.meeting.model.Meeting;


@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>{
   public Optional<Meeting> findById(Long id);
}
