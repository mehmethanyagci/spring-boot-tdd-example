package com.example.springcustomcrud.service;

import com.example.springcustomcrud.exception.PersonNotFoundException;
import com.example.springcustomcrud.model.Person;
import com.example.springcustomcrud.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonCrudLogic {

    private final PersonRepository personRepository;

    public PersonCrudLogic(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public Person getPersonById(long id) {
        Optional<Person> optional = personRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new PersonNotFoundException();
    }

    public Person putPerson(long id, Person person) {
        person.setId(id);
        return personRepository.save(person);
    }

    public Person patchPerson(long id, Person person) {
        Optional<Person> optional = personRepository.findById(id);
        if (optional.isEmpty()) {
            throw new PersonNotFoundException();
        }

        Person personToUpdate = optional.get();

        if (person.getFirstName() != null) {
            personToUpdate.setFirstName(person.getFirstName());
        }

        if (person.getLastName() != null) {
            personToUpdate.setLastName(person.getLastName());
        }

        if (person.getEmail() != null) {
            personToUpdate.setEmail(person.getEmail());
        }

        if (person.getAge() != null) {
            personToUpdate.setAge(person.getAge());
        }

        return personRepository.save(personToUpdate);
    }

    public void deletePerson(long id) {
        Optional<Person> optional = personRepository.findById(id);
        if (optional.isEmpty()) {
            throw new PersonNotFoundException();
        }
        personRepository.delete(optional.get());
    }
}
