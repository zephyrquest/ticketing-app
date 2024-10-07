package ch.supsi.webapp.web.runner;

import ch.supsi.webapp.web.model.Role;
import ch.supsi.webapp.web.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleInitRunner implements CommandLineRunner, Ordered {

    @Autowired private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.count() == 0) {
            Role admin = new Role(Role.ADMIN_ID, Role.Value.ROLE_ADMIN);
            Role user = new Role(Role.USER_ID, Role.Value.ROLE_USER);

            roleRepository.saveAll(List.of(admin, user));
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
