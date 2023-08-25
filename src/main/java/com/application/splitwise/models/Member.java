package com.application.splitwise.models;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Entity
@Getter
public class Member extends BaseModel {

    private String name;
    @Email(message = "please provide valid email")
    private String email;


    private Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Member() {

    }

    public static MemberBuilder getMemberBuilder() {
        return new MemberBuilder();
    }


    public static class MemberBuilder {
        private String name;
        private String email;

        public MemberBuilder() {
        }


        public MemberBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public MemberBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Member build() {
            return new Member(name, email);
        }


    }
}

