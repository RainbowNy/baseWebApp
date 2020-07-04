package org.base.webapp.service;

import org.base.webapp.domain.Role;
import org.base.webapp.domain.User;
import org.base.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Value("${app.address}")
    private String applicationAddress;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean addUser(User user){
        User currentUser = userRepository.findByUsername(user.getUsername());

        if(currentUser != null){
            return false;
        }

        user.setActive(true);
        user.setUserRoles(Collections.singleton(Role.USER));
        user.setActivationEmailCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        sendActivationMessage(user);

        return true;
    }

    private void sendActivationMessage(User user) {
        if(!StringUtils.isEmpty(user.getUserEmail())){
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to test baseApp!" +
                            "Please, click to the next link to activate your account: <a href=%s/activate/%s>Click</a>",
                    user.getUsername(), applicationAddress, user.getActivationEmailCode()
            );

            mailSender.sendEmailMessage(user.getUserEmail(), "Confirm your email at baseApp", message);
        }
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

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays
                .stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getUserRoles().clear();

        for(String key : form.keySet()){
            if(roles.contains(key)){
                user.getUserRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getUserEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) || (userEmail != null && !userEmail.equals(email));

        if(isEmailChanged) {
            user.setUserEmail(email);

            if (!StringUtils.isEmpty(email)) {
                user.setActivationEmailCode(UUID.randomUUID().toString());
                sendActivationMessage(user);
            }
        }

        if(!StringUtils.isEmpty(password)){
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);
    }
}
