package ch.supsi.webapp.web.controller;

import ch.supsi.webapp.web.model.Role;
import ch.supsi.webapp.web.model.User;
import ch.supsi.webapp.web.repository.RoleRepository;
import ch.supsi.webapp.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static ch.supsi.webapp.web.controller.TicketController.*;

@Controller
public class UserController {
    public static final String CTRL_LOGIN = "/login";
    public static final String CTRL_LOGOUT = "/logout";
    public static final String CTRL_REGISTER = "/register";
    private static final String CTRL_USER = "/user";
    private static final String CTRL_SEARCH = "/search";

    @Autowired private UserService userService;
    @Autowired private RoleRepository roleRepository;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("homeUrl", CTRL_HOME);
        model.addAttribute("createTicketUrl", CTRL_TICKET + CTRL_NEW);
        model.addAttribute("loginUrl", UserController.CTRL_LOGIN);
        model.addAttribute("registerUrl", UserController.CTRL_REGISTER);
    }

    @GetMapping(CTRL_LOGIN)
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        model.addAttribute("error", error);

        return "login";
    }

    @GetMapping(CTRL_LOGOUT)
    public String logout() {

        return "redirect:" + CTRL_LOGIN;
    }

    @GetMapping(CTRL_REGISTER)
    public String registrationPage(@RequestParam(value = "error", required = false) String error,
                                   Model model) {
        User user = new User();
        model.addAttribute("user", user);

        model.addAttribute("error", error);

        return "registration";
    }

    @PostMapping(CTRL_REGISTER)
    public String registration(User user) {

        if(userService.getUser(user.getUsername()) == null) {
            user.setRole(roleRepository.findById(Role.USER_ID).get());
            userService.addUser(user);
            return "redirect:" + CTRL_LOGIN;
        }

        return "redirect:" + CTRL_REGISTER + "?error";
    }

    @GetMapping(CTRL_USER + CTRL_SEARCH)
    public ResponseEntity<User> searchUser(@RequestParam("username") String username) {
        User user = userService.getUser(username);

        if(user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
