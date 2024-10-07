package ch.supsi.webapp.web.runner;

import ch.supsi.webapp.web.model.Role;
import ch.supsi.webapp.web.model.User;
import ch.supsi.webapp.web.repository.RoleRepository;
import ch.supsi.webapp.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class UserInitRunner implements CommandLineRunner, Ordered {

    @Autowired private UserService userService;
    @Autowired private RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {
        if(userService.getAllUsers().isEmpty()) {
            Optional<Role> roleOpt = roleRepository.findById(Role.ADMIN_ID);

            Role role;
            if(roleOpt.isEmpty()) {
                role = new Role(Role.USER_ID, Role.Value.ROLE_ADMIN);
            }
            else {
                role = roleOpt.get();
            }

            User user = new User("admin", "admin", "Mario", "Rossi",
                    role);

            userService.addUser(user);
        }
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
