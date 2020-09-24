package com.example.springcustomcrud.controller;

import com.example.springcustomcrud.model.Person;
import com.example.springcustomcrud.service.PersonCrudLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

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
        //arrange
        List<Person> expected = Arrays.asList(
                new Person(3, "John", "Doe", "john.doe@example.com", 21),
                new Person(4, "Mary", "Smith", "mary.smith@gmail.com", 35)
        );
        Mockito.when(personService.getAll()).thenReturn(expected);

        //act
        List<Person> actual = personController.getAllPersons();

        //assert
        Mockito.verify(personService).getAll();
        // you can also write:
        // Mockito.verify(personService, times(1)).getAll();
        assertEquals(expected, actual);
    }
}