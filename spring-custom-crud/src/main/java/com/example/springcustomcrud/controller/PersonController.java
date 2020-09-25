package com.example.springcustomcrud.controller;


import com.example.springcustomcrud.exception.PersonNotFoundException;
import com.example.springcustomcrud.model.Person;
import com.example.springcustomcrud.service.PersonCrudLogic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonCrudLogic personCrudLogic;

    public PersonController(PersonCrudLogic personService) {
        this.personCrudLogic = personService;
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return new ResponseEntity<>(personCrudLogic.getAll(), OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(personCrudLogic.getPersonById(id), OK);
        } catch (PersonNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Person> patchPerson(@PathVariable("id") long id, @RequestBody Person person) {
        try {
            return new ResponseEntity<>(personCrudLogic.patchPerson(id, person), OK);
        } catch (PersonNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> putPerson(@PathVariable("id") long id, @RequestBody Person person) {
        return new ResponseEntity<>(personCrudLogic.putPerson(id, person), CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> deletePerson(@PathVariable("id") long id) {
        try {
            personCrudLogic.deletePerson(id);
            return new ResponseEntity<>(null, NO_CONTENT);
        } catch (PersonNotFoundException e) {
            return new ResponseEntity<>(null, NOT_FOUND);
        }
    }
}
