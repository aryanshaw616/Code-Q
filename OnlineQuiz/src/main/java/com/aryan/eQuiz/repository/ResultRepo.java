package com.aryan.eQuiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aryan.eQuiz.model.Result;

@Repository
public interface ResultRepo extends JpaRepository<Result, Integer> {

}