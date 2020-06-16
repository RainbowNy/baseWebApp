package org.base.webapp.controller;

import org.base.webapp.domain.Role;
import org.base.webapp.domain.User;
import org.base.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userRepository.findAll());

        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("user",user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PostMapping
    @Transactional
    public String saveUseEdit(@RequestParam("username") String username,@RequestParam Map<String,String> form, @RequestParam("userId") User user){
        user.setUsername(username);

       Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

       user.getUserRoles().clear();

        for (String key : form.keySet()) {
            if(roles.contains(key)){
                user.getUserRoles().add(Role.valueOf(key));
            }
        }

        return "redirect:/user";
    }
}
