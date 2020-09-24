package com.example.springcustomcrud.service;

import com.example.springcustomcrud.exception.PersonNotFoundException;
import com.example.springcustomcrud.model.Person;
import com.example.springcustomcrud.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PersonCrudLogic {

    private final PersonRepository personRepository;

    public PersonCrudLogic(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public void getPersonById(long l) {
        throw new PersonNotFoundException();
    }
}
