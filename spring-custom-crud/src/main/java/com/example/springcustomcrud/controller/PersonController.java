package com.example.springcustomcrud.controller;


import com.example.springcustomcrud.model.Person;
import com.example.springcustomcrud.service.PersonCrudLogic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonCrudLogic personCrudLogic;

    public PersonController(PersonCrudLogic personService) {
        this.personCrudLogic = personService;
    }

    @GetMapping
    public List<Person> getAllPersons() {
        return personCrudLogic.getAll();
    }

    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable("id") Long id) {
        List<Person> expected = Arrays.asList(
                new Person(1, "John", "Doe", "john.doe@example.com", 21),
                new Person(2, "Mary", "Smith", "mary.smith@gmail.com", 35)
        );
        return expected.get(Math.toIntExact(id) - 1);
    }


}
