package org.base.webapp.controller;

import org.base.webapp.domain.User;
import org.base.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("registration")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model){
        if(user.getPassword() != null && !user.getPassword().equals(user.getAdditionalPassword())){
            model.addAttribute("passwordError", "Passwords are not equal");

            return "registration";
        }

        if(bindingResult.hasErrors()){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);

            return "registration";
        }

        if(!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activateAccount(Model model, @PathVariable String code){
        boolean isActivated = userService.activateUserAccount(code);

        if(isActivated){
            model.addAttribute("message", "Your account has been activated");
        } else {
            model.addAttribute("message", "Activation error. Please, make help request to support");
        }

        return "redirect:/main";
    }
}
