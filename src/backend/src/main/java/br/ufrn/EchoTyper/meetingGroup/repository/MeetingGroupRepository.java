package br.ufrn.EchoTyper.meetingGroup.repository;

import org.springframework.stereotype.Repository;

import br.ufrn.EchoTyper.meetingGroup.model.MeetingGroup;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MeetingGroupRepository extends JpaRepository<MeetingGroup,Long> {
}
