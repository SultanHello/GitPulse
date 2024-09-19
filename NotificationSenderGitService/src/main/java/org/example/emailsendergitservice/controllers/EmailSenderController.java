package org.example.emailsendergitservice.controllers;


import lombok.AllArgsConstructor;

import org.example.emailsendergitservice.model.Starter;
import org.example.emailsendergitservice.services.EmailSenderService;
import org.example.emailsendergitservice.services.SlackSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor
public class EmailSenderController {

    @Autowired
    private final SlackSenderService slackSenderService;

    @Autowired
    private final EmailSenderService emailSenderService;

    @PostMapping("/sendSlack")
    public void sendSlack(@RequestParam String message, @RequestBody Starter starter) {
        slackSenderService.sendNotification(message,starter);
    }

    @PostMapping("/sendEmail")
    public void sendEmail(@RequestParam String message,@RequestBody Starter starter){
        emailSenderService.sendEmail(starter,message);
    }



}
