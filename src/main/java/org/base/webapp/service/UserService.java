package org.base.webapp.service;

import org.base.webapp.domain.Role;
import org.base.webapp.domain.User;
import org.base.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Value("${app.address}")
    private String applicationAddress;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user){
        User currentUser = userRepository.findByUsername(user.getUsername());

        if(currentUser != null){
            return false;
        }

        user.setActive(true);
        user.setUserRoles(Collections.singleton(Role.USER));
        user.setActivationEmailCode(UUID.randomUUID().toString());

        userRepository.save(user);

        if(!StringUtils.isEmpty(user.getUserEmail())){
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to test baseApp!" +
                            "Please, click to the next link to activate your account: %s/activate/%s",
                    user.getUsername(), applicationAddress, user.getActivationEmailCode()
            );

            mailSender.sendEmailMessage(user.getUserEmail(), "Activate your account", message);
        }

        return true;
    }

    public boolean activateUserAccount(String code) {
        User user = userRepository.findByActivationEmailCode(code);

        if(user == null){
            return false;
        }

        user.setActivationEmailCode(null);

        userRepository.save(user);

        return true;
    }
}
