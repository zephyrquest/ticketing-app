package ch.supsi.webapp.web.runner;

import ch.supsi.webapp.web.model.Type;
import ch.supsi.webapp.web.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TypeInitRunner implements CommandLineRunner, Ordered {

    @Autowired private TypeRepository typeRepository;


    @Override
    public void run(String... args) throws Exception {
        if(typeRepository.count() == 0) {
            Type task = new Type(Type.TASK_ID, Type.Value.TASK);
            Type story = new Type(Type.STORY_ID, Type.Value.STORY);
            Type issue = new Type(Type.ISSUE_ID, Type.Value.ISSUE);
            Type bug = new Type(Type.BUG_ID, Type.Value.BUG);
            Type investigation = new Type(Type.INVESTIGATION_ID, Type.Value.INVESTIGATION);

            typeRepository.saveAll(List.of(task, story, issue, bug, investigation));
        }
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
