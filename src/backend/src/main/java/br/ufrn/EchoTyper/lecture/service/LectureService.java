package br.ufrn.EchoTyper.lecture.service;

import org.springframework.stereotype.Service;

import br.ufrn.EchoTyper.lecture.model.Lecture;
import br.ufrn.EchoTyper.lecture.model.Syllabus;
import br.ufrn.EchoTyper.register.dto.RegisterGroupMapper;
import br.ufrn.EchoTyper.register.dto.RegisterMapper;
import br.ufrn.EchoTyper.register.repository.RegisterGroupRepository;
import br.ufrn.EchoTyper.register.repository.RegisterRepository;
import br.ufrn.EchoTyper.register.service.RegisterService;

@Service
public class LectureService extends RegisterService<Lecture, Syllabus> {

    public LectureService(RegisterRepository<Lecture> registerRepository,
            RegisterGroupRepository<Syllabus, Lecture> registerGroupRepository, RegisterMapper<Lecture> registerMapper,
            RegisterGroupMapper<Syllabus, Lecture> registerGroupMapper) {
        super(registerRepository, registerGroupRepository, registerMapper, registerGroupMapper);
    }

    @Override
    protected void addRegisterToGroupHook(Syllabus group, Lecture register) {
        group.registerAttendance();
        Double grade = register.getGrade();
        if (grade != null) {
            group.addGrade(grade);
        }
    }

    @Override
    protected void removeRegisterFromGroupHook(Syllabus group, Lecture register) {
        group.removeAttendance();
    }

    
}
