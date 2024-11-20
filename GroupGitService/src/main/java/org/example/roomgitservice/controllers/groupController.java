package org.example.roomgitservice.controllers;

import lombok.AllArgsConstructor;
import org.example.roomgitservice.models.CreatingGroup;
import org.example.roomgitservice.models.Developer;
import org.example.roomgitservice.models.EnteringGroup;
import org.example.roomgitservice.models.Group;
import org.example.roomgitservice.repositories.DeveloperRepository;
import org.example.roomgitservice.repositories.GroupRepository;
import org.example.roomgitservice.services.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@AllArgsConstructor
public class groupController {

    @Autowired
    private final GroupService roomService;


    @GetMapping("/")
    public List<Group> groupList(){
        return roomService.getGroups();
    }


    @PostMapping("/addDeveloper")
    public String addDeveloper(@RequestBody EnteringGroup enteringGroup){
        return  roomService.addDeveloper(enteringGroup);
    }

    @PostMapping("/createGroup")
    public String createGroup(@RequestBody CreatingGroup creatingGroup){
        return roomService.createGroup(creatingGroup);
    }


}
