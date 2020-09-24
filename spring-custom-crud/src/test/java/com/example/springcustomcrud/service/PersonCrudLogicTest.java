package com.example.springcustomcrud.service;

import com.example.springcustomcrud.exception.PersonNotFoundException;
import com.example.springcustomcrud.model.Person;
import com.example.springcustomcrud.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PersonCrudLogicTest {

    PersonCrudLogic personCrudLogic;
    PersonRepository personRepository;

    @BeforeEach
    public void setup() {
        personRepository = Mockito.mock(PersonRepository.class);
        personCrudLogic = new PersonCrudLogic(personRepository);
    }

    @Test
    public void getAll_ShouldCallRepoFindAll() {
        //arrange
        List<Person> expected = Arrays.asList(
                new Person(1, "John", "Doe", "john.doe@example.com", 21),
                new Person(2, "Mary", "Smith", "mary.smith@gmail.com", 35)
        );
        when(personRepository.findAll()).thenReturn(expected);

        //act
        personCrudLogic.getAll();

        //assert
        Mockito.verify(personRepository).findAll();
    }


    //throw exception when person doesn't exist
    //return person if exist
    @Test
    public void getPersonByIdThrowsPersonNotFoundExceptionWhenPersonNotFound(){
        Assertions.assertThrows(PersonNotFoundException.class,()->{personCrudLogic.getPersonById(1l);});
    }
}