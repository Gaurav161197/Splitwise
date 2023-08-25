package com.application.splitwise.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class SplitwiseGroup extends BaseModel {

    private String groupName;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Member> groupMembers;
    @ManyToOne(fetch = FetchType.EAGER)
    private Member adminMember;
    @OneToMany(mappedBy = "splitWiseGroup", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Expense> expensesList;

    private SplitwiseGroup(String groupName, List<Member> groupMembers, Member admin) {
        this.groupName = groupName;
        this.groupMembers = groupMembers;
        this.adminMember = admin;
        this.expensesList = new ArrayList<>();
    }

    public SplitwiseGroup() {

    }

    public static GroupBuilder getGroupBuilder() {
        return new GroupBuilder();
    }


    public static class GroupBuilder {
        private String groupName;
        private List<Member> groupMembers;
        private Member admin;

        public GroupBuilder() {
            groupMembers = new ArrayList<>();
        }

        public GroupBuilder setGroupName(String name) {
            this.groupName = name;
            return this;
        }

        public GroupBuilder setGroupMembers(List<Member> memberList) {
            this.groupMembers = memberList;
            return this;
        }

        public GroupBuilder setGroupAdmin(Member admin) {
            this.admin = admin;
            return this;
        }

        public SplitwiseGroup build() {
            return new SplitwiseGroup(groupName, groupMembers, admin);
        }


    }

}
