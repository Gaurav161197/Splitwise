package com.application.splitwise.Controllers;


import com.application.splitwise.Repository.GroupRepository;
import com.application.splitwise.Repository.UserRepository;
import com.application.splitwise.models.Member;
import com.application.splitwise.models.SplitwiseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class UserController {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private GroupRepository groupRepository;

    public void registerMember(List<Member> memberList){
        userRepository.saveAll(memberList);
    }

    public void createGroup(SplitwiseGroup group){
            groupRepository.save(group);
    }
}
