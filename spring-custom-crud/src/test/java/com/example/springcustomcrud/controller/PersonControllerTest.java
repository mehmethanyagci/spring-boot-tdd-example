package com.example.springcustomcrud.controller;

import com.example.springcustomcrud.exception.PersonNotFoundException;
import com.example.springcustomcrud.model.Person;
import com.example.springcustomcrud.service.PersonCrudLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit Testing
 * <p>
 * Notice we're not using Spring.  We're just using the controller class called by
 * a constructor.
 */
class PersonControllerTest {

    PersonController personController;
    PersonCrudLogic personService;

    @BeforeEach
    public void setup() {
        personService = Mockito.mock(PersonCrudLogic.class);
        personController = new PersonController(personService);
    }

    @Test
    public void getAll_callsLogic() {
        //arrange
        List<Person> expected = Arrays.asList(
                new Person(3L, "John", "Doe", "john.doe@example.com", 21),
                new Person(4L, "Mary", "Smith", "mary.smith@gmail.com", 35)
        );
        Mockito.when(personService.getAll()).thenReturn(expected);

        //act
        List<Person> actual = personController.getAllPersons().getBody();

        //assert
        Mockito.verify(personService).getAll();
        // you can also write:
        // Mockito.verify(personService, times(1)).getAll();
        assertEquals(expected, actual);
    }

    @Test
    public void getPersonById_CallsLogicAndReturns_OKResult() {
        //arrange
        Person expected = new Person(3L, "John", "Doe", "john.doe@example.com", 21);
        Mockito.when(personService.getPersonById(3L)).thenReturn(expected);

        //act
        ResponseEntity<Person> response = personController.getPersonById(3L);

        //assert
        Mockito.verify(personService).getPersonById(3L);
        assertEquals(expected, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getPersonById_ShouldReturnError404_WhenPersonNotFoundException() {
        //arrange
        Mockito.when(personService.getPersonById(3L)).thenThrow(new PersonNotFoundException());

        //act
        ResponseEntity<Person> response = personController.getPersonById(3L);

        //assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void patchPerson_ShouldReturnUpdatedRecordAndOK() {
        //arrange
        Person input = new Person(null, "John", "Doe", null, 21);
        Person expected = new Person(3L, "John", "Doe", "john.doe@example.com", 21);
        Mockito.when(personService.patchPerson(3L, input)).thenReturn(expected);

        //act
        ResponseEntity<Person> response = personController.patchPerson(3L, input);

        //assert
        Mockito.verify(personService).patchPerson(3L, input);
        assertEquals(expected, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void patchPerson_ShouldReturnError404_WhenPersonNotFoundException() {
        //arrange
        Person input = new Person();
        Mockito.when(personService.patchPerson(3L, input)).thenThrow(new PersonNotFoundException());

        //act
        ResponseEntity<Person> response = personController.patchPerson(3L, input);

        //assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void putPerson_ShouldReturnSavedRecordAndCreated(){
        //arrange
        Person expected = new Person(3L, "John", "Doe", "john.doe@example.com", 21);
        Mockito.when(personService.putPerson(3L, expected)).thenReturn(expected);

        //act
        ResponseEntity<Person> response = personController.putPerson(3L, expected);

        //assert
        Mockito.verify(personService).putPerson(3L, expected);
        assertEquals(expected, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void deletePerson_ShouldReturnUpdatedRecordAnd_NO_CONTENT() {
        //arrange

        //act
        ResponseEntity<Person> response = personController.deletePerson(3L);

        //assert
        Mockito.verify(personService).deletePerson(3L);
        assertEquals(null, response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


    @Test
    public void deletePerson_ShouldReturnError404_WhenPersonNotFoundException() {
        //arrange
        // Need to do this because of void return type
        // https://www.journaldev.com/21834/mockito-mock-void-method#mockito-mock-void-method-with-exception
        Mockito.doThrow(PersonNotFoundException.class).when(personService).deletePerson(3L);

        //act
        ResponseEntity<Person> response = personController.deletePerson(3L);

        //assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}