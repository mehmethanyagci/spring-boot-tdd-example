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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
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
                new Person(1L, "John", "Doe", "john.doe@example.com", 21),
                new Person(2L, "Mary", "Smith", "mary.smith@gmail.com", 35)
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
    public void getPersonById_ThrowsPersonNotFoundExceptionWhenPersonNotFound() {
        Assertions.assertThrows(PersonNotFoundException.class, () -> {
            personCrudLogic.getPersonById(20l);
        });
    }

    @Test
    public void getPersonById_ReturnsPerson() {
        //arrange
        Person expected = new Person(1L, "John", "Doe", "john.doe@example.com", 21);
        when(personRepository.findById(1L)).thenReturn(Optional.of(expected));

        //act
        Person actual = personCrudLogic.getPersonById(1L);

        //assert
        assertEquals(expected, actual);
        verify(personRepository).findById(1L);
    }

    /**
     * Although you would think it would be okay to pass the Person from the controller directly to the service
     * directly to the repository there is a caveat.
     * <p>
     * controller.putPerson(person) -> service.putPerson(person) -> repository.save(person)
     * Easy!
     * <p>
     * But what if you call PUT /persons/5 with:
     * {
     * "id": 6,
     * "firstName": "John"
     * }
     * <p>
     * Should you use the 5 or the 6 for the id?  And if you want to maintain the id as 5, what happens when you
     * pass the body directly to the service which passes it to the repo?
     * <p>
     * In what layer should you replace the 6 with the 5?  The controller, service, or repository?
     * <p>
     * The tendency is to put it in the controller because it's easiest to set the Id with the PathVariable
     * <p>
     * But our business logic should live in the service layer.
     */
    @Test
    public void putPerson_SavesToRepositoryWithProvidedId() {
        //arrange
        Person input = new Person(null, "John", "Doe", "john.doe@example.com", 21);
        Person expected = new Person(1L, "John", "Doe", "john.doe@example.com", 21);

        //act
        personCrudLogic.putPerson(1L, input);

        //assert
        verify(personRepository).save(expected);
    }

    @Test
    public void putPerson_ReturnsModifiedRecord() {
        //arrange
        Person input = new Person(null, "John", "Doe", "john.doe@example.com", 21);
        Person expected = new Person(1L, "John", "Doe", "john.doe@example.com", 21);
        when(personRepository.save(expected)).thenReturn(expected);

        //act
        Person actual = personCrudLogic.putPerson(1L, input);

        //assert
        assertEquals(expected, actual);
    }

    /**
     * PATCH involves multiple steps:
     * 1.) Get record for given ID from repo
     * 2.) Update record for specified fields
     * 3.) Save the updated record to repo
     * <p>
     * If record is not found, a PersonNotFoundException should be thrown.
     * <p>
     * The ID provided as a parameter should override any ID provided by the body.
     */


    @Test
    public void patchPerson_updatesFirstNameForExistingRecord() {
        //arrange
        Person input = new Person(null, "Robert", null, null, null);
        Person fromRepo = new Person(1L, "John", "Doe", "john.doe@example.com", 21);
        Person expected = new Person(1L, "Robert", "Doe", "john.doe@example.com", 21);
        when(personRepository.findById(1L)).thenReturn(Optional.of(fromRepo));
        when(personRepository.save(expected)).thenReturn(expected);

        //act
        Person actual = personCrudLogic.patchPerson(1L, input);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    public void patchPerson_updatesLastNameForExistingRecord() {
        //arrange
        Person input = new Person(null, null, "Smith", null, null);
        Person fromRepo = new Person(1L, "John", "Doe", "john.doe@example.com", 21);
        Person expected = new Person(1L, "John", "Smith", "john.doe@example.com", 21);
        when(personRepository.findById(1L)).thenReturn(Optional.of(fromRepo));
        when(personRepository.save(expected)).thenReturn(expected);

        //act
        Person actual = personCrudLogic.patchPerson(1L, input);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    public void patchPerson_updatesEmailForExistingRecord() {
        //arrange
        Person input = new Person(null, null, null, "John.Doe@hotmail.com", null);
        Person fromRepo = new Person(1L, "John", "Doe", "john.doe@example.com", 21);
        Person expected = new Person(1L, "John", "Doe", "John.Doe@hotmail.com", 21);
        when(personRepository.findById(1L)).thenReturn(Optional.of(fromRepo));
        when(personRepository.save(expected)).thenReturn(expected);

        //act
        Person actual = personCrudLogic.patchPerson(1L, input);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    public void patchPerson_updatesAgeForExistingRecord() {
        //arrange
        Person input = new Person(null, null, null, null, 50);
        Person fromRepo = new Person(1L, "John", "Doe", "john.doe@example.com", 21);
        Person expected = new Person(1L, "John", "Doe", "john.doe@example.com", 50);
        when(personRepository.findById(1L)).thenReturn(Optional.of(fromRepo));
        when(personRepository.save(expected)).thenReturn(expected);

        //act
        Person actual = personCrudLogic.patchPerson(1L, input);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    public void patchPerson_ShouldThrowErrorIfPersonDoesNotExist() {
        //arrange
        when(personRepository.findById(1L)).thenReturn(Optional.empty());
        Person input = new Person(null, null, null, null, 50);

        //assert
        Assertions.assertThrows(PersonNotFoundException.class, () -> {
            //act
            personCrudLogic.patchPerson(1L, input);
        });
    }

    @Test
    public void patchPerson_IgnoresIdPassedWithPersonParameter() {
        //arrange
        Person input = new Person(2L, null, null, "John.Doe@hotmail.com", null);
        Person fromRepo = new Person(1L, "John", "Doe", "john.doe@example.com", 21);
        Person expected = new Person(1L, "John", "Doe", "John.Doe@hotmail.com", 21);
        when(personRepository.findById(1L)).thenReturn(Optional.of(fromRepo));
        when(personRepository.save(expected)).thenReturn(expected);

        //act
        Person actual = personCrudLogic.patchPerson(1L, input);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    public void patchPerson_UpdatesAllFields() {
        //arrange
        Person input = new Person(null, "Mary", "Smith", "mary.smith@gmail.com", 35);
        Person fromRepo = new Person(1L, "John", "Doe", "john.doe@example.com", 21);
        Person expected = new Person(1L, "Mary", "Smith", "mary.smith@gmail.com", 35);
        when(personRepository.findById(1L)).thenReturn(Optional.of(fromRepo));
        when(personRepository.save(expected)).thenReturn(expected);

        //act
        Person actual = personCrudLogic.patchPerson(1L, input);

        //assert
        assertEquals(expected, actual);
    }

    /**
     * DELETE is considered idempotent because the state of the system does not change
     * after multiple calls.
     * <p>
     * However, this doesn't mean that the system can't throw back a different response.
     * <p>
     * If a record doesn't exist, a PersonNotFoundException should be thrown.
     */
    @Test
    public void deletePerson_ThrowsErrorIfRecordNotFound() {
        //arrange
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        //assert
        Assertions.assertThrows(PersonNotFoundException.class, () -> {
            //act
            personCrudLogic.deletePerson(1L);
        });
    }

    @Test
    public void deletePerson_CallsRepoToDeleteRecord() {
        //arrange
        Person fromRepo = new Person(1L, "John", "Doe", "john.doe@example.com", 21);
        when(personRepository.findById(1L)).thenReturn(Optional.of(fromRepo));

        //act
        personCrudLogic.deletePerson(1L);

        //assert
        verify(personRepository).delete(fromRepo);
    }

}