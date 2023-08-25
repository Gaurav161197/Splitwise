package com.application.splitwise.Repository;

import com.application.splitwise.models.SplitwiseGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GroupRepository extends JpaRepository<SplitwiseGroup, Long> {
       public Optional<SplitwiseGroup> findById(long id);
}
