package org.base.webapp.controller;

import org.base.webapp.domain.User;
import org.base.webapp.domain.dto.CaptchaResponseDto;
import org.base.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String secretCapCode;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("registration")
    public String addUser(
            @RequestParam("additionalPassword") String passwordConfirmation,
            @RequestParam("g-recaptcha-response") String recaptchaResponse,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        String captchaURL = String.format(CAPTCHA_URL, secretCapCode, recaptchaResponse);
        CaptchaResponseDto captchaResponse = restTemplate.postForObject(captchaURL, Collections.emptyList(), CaptchaResponseDto.class);

        boolean confirmationPasswordCheck = StringUtils.isEmpty(passwordConfirmation);

        if(confirmationPasswordCheck){
            model.addAttribute("passwordConfirmationError", "Password confirmation cannot be empty");
        }

        if(user.getPassword() != null && !user.getPassword().equals(passwordConfirmation)){
            model.addAttribute("passwordError", "Passwords are not equal");

            return "registration";
        }

        if(confirmationPasswordCheck || bindingResult.hasErrors() || !captchaResponse.isSuccess()){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);

            if(!captchaResponse.isSuccess()){
                model.addAttribute("captchaError", "Please, fill captcha");
            }

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
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Your account has been activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation error. Please, make help request to support");
        }

        return "login";
    }
}
