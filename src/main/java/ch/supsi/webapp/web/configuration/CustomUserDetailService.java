package ch.supsi.webapp.web.configuration;

import ch.supsi.webapp.web.model.User;
import ch.supsi.webapp.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> auth = AuthorityUtils.createAuthorityList(user.getRole().getValue().toString());
        return new org.springframework.security.core.userdetails
                .User(username, user.getPassword(), true, true,
                true, true, auth);

    }
}
