package com.application.splitwise.Repository;

import com.application.splitwise.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {
    public Optional<Member> findById(long id);

}
