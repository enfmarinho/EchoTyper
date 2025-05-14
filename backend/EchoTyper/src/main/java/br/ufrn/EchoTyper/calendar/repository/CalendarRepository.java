package br.ufrn.EchoTyper.calendar.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.EchoTyper.calendar.model.Calendar;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long>{
   public Optional<Calendar> findById(Long id);
   public Optional<Calendar> findByTitle(String title);
   public Optional<Calendar> findByDate(LocalDate date);
}
