package com.application.splitwise.Repository;

import com.application.splitwise.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Long> {

}
