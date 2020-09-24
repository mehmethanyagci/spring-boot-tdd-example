package com.example.springcustomcrud.repository;

import com.example.springcustomcrud.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
