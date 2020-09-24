package com.example.springcustomcrud.controller;

import com.example.springcustomcrud.service.PersonCrudLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Testing
 *
 * Notice we're not using Spring.  We're just using the controller class called by
 * a constructor.
 */
class PersonControllerTest {

    PersonController personController;
    PersonCrudLogic personService;

    @BeforeEach
    public void setup(){
        personService = Mockito.mock(PersonCrudLogic.class);
        personController = new PersonController(personService);
    }

    @Test
    public void getAll_callsLogic(){
        personController.getAllPersons();
        Mockito.verify(personService).getAll();
    }


}