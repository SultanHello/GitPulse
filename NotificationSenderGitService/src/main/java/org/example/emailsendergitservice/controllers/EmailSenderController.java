package org.example.emailsendergitservice.controllers;


import lombok.AllArgsConstructor;

import org.example.emailsendergitservice.model.Starter;
import org.example.emailsendergitservice.services.EmailSenderService;
import org.example.emailsendergitservice.services.SlackSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor
public class EmailSenderController {
    private static final Logger logger = LoggerFactory.getLogger(EmailSenderController.class);

    @Autowired
    private final SlackSenderService slackSenderService;

    @Autowired
    private final EmailSenderService emailSenderService;

    @PostMapping("/sendSlack")
    public void sendSlack(@RequestParam String message, @RequestBody Starter starter) {
        logger.info("starting connect with slack sender class");
        slackSenderService.sendNotification(message,starter);
    }

    @PostMapping("/sendEmail")
    public void sendEmail(@RequestParam String message,@RequestBody Starter starter){
        logger.info("starting connect with slack email sender class");
        emailSenderService.sendEmail(starter,message);
    }



}
