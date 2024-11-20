package org.example.roomgitservice.services;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.roomgitservice.models.CreatingGroup;
import org.example.roomgitservice.models.Developer;
import org.example.roomgitservice.models.EnteringGroup;
import org.example.roomgitservice.models.Group;
import org.example.roomgitservice.repositories.DeveloperRepository;
import org.example.roomgitservice.repositories.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
    @Autowired
    private final GroupRepository groupRepository;
    private final DeveloperRepository developerRepository;

    public List<Group> getGroups(){
        return groupRepository.findAll();
    }
    @Transactional
    public String addDeveloper(EnteringGroup enteringGroup){
        logger.info("1");
        if(!groupRepository.existsById(enteringGroup.getId())){
            return "group with this id doesnt exist";
        }
        logger.info("12");
        Group group = groupRepository.findById(enteringGroup.getId()).orElseThrow(null);
        if(groupRepository.findAll().contains(group)){
            return "you are already registered";
        }else{
            group.getDevelopers().add(enteringGroup.getUser());

            logger.info("14");

            groupRepository.save(group);

            logger.info("15");

            Developer developer = enteringGroup.getUser();
            logger.info("16");

            developer.setRegisteredGroup(group);
            logger.info("17");

            developerRepository.save(developer);
            return "success saved";

        }




    }
    @Transactional
    public String createGroup(CreatingGroup creatingGroup) {
        logger.info("after");
        System.out.println(creatingGroup.getUser());


        developerRepository.save(creatingGroup.getUser());

        logger.info("before");



        Group group = groupRepository.save(Group.builder()
                .nameOfGroup(creatingGroup.getGroupName())
                .owner(creatingGroup.getUser())
                .build());


        group.getDevelopers().add(group.getOwner());

        groupRepository.save(group);

        return "successfully created";
    }



}
