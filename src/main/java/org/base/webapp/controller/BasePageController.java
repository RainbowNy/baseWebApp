package org.base.webapp.controller;

import org.base.webapp.domain.Message;
import org.base.webapp.domain.User;
import org.base.webapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class BasePageController extends ControllerUtils {
    @Autowired
    private MessageRepository messageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String basePage(@RequestParam(required = false) String filterName,Model model){
        Iterable<Message> messages = messageRepository.findAll();

        if(filterName == null || filterName.isEmpty()){
            messages = messageRepository.findAll();
        }else {
            messages = messageRepository.findByTag(filterName);
        }

        model.addAttribute("messages",messages);
        model.addAttribute("filterName", filterName);

        return "basePage";
    }

    @PostMapping("/main")
    public String addMessage(@AuthenticationPrincipal User user, @Valid Message message, BindingResult bindingResult, Model model, @RequestParam("file")MultipartFile file) throws IOException {
        message.setAuthor(user);

        if(bindingResult.hasErrors()){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            if (!file.isEmpty()) {
                File uploadDir = new File(uploadPath);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String uuidFile = UUID.randomUUID().toString();
                String fileName = uuidFile + '.' + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + fileName));

                message.setFilename(fileName);
            }

            model.addAttribute("message",null);

            messageRepository.save(message);
        }

        Iterable<Message> messages = messageRepository.findAll();

        model.addAttribute("messages",messages);

        return "basePage";
    }
}
