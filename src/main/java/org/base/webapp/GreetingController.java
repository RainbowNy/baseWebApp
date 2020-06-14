package org.base.webapp;

import org.aspectj.bridge.IMessage;
import org.base.webapp.domain.Message;
import org.base.webapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GreetingController {
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping
    public String basePage(Model model){
        Iterable<Message> messages = messageRepository.findAll();

        model.addAttribute("messages",messages);
        return "basePage";
    }

    @PostMapping
    public String addMessage(@RequestParam String textToSent, @RequestParam String tag, Model model){
        Message message = new Message(textToSent,tag);

        messageRepository.save(message);

       Iterable<Message> messages = messageRepository.findAll();

       model.addAttribute("messages",messages);

        return "basePage";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filterName, Model model){
        Iterable<Message> messageList;

        if(filterName == null || filterName.isEmpty()){
            messageList = messageRepository.findAll();
        }else {
            messageList = messageRepository.findByTag(filterName);
        }

        model.addAttribute("messages", messageList);

        return "basePage";
    }
}
