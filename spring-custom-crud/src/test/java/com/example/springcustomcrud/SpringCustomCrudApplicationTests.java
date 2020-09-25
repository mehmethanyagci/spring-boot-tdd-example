package com.example.springcustomcrud;

import com.example.springcustomcrud.model.Person;
import com.example.springcustomcrud.repository.PersonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class SpringCustomCrudApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    PersonRepository personRepository;

    Person person1;
    Person person2;

    @BeforeEach
    public void setup() {
        person1 = new Person(1L, "John", "Doe", "john.doe@example.com", 21);
        person2 = new Person(2L, "Mary", "Smith", "mary.smith@gmail.com", 35);
        personRepository.save(person1);
        personRepository.save(person2);
    }

    @AfterEach
    public void cleanup() {
        personRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }


    /**
     * This test shows you how to open a .json file located under src/main/resources.
     * <p>
     * This is a good starter test to begin with.  The customer gives you .json representing
     * the desired output.  You can then use that file in your test to create your data
     * structures and to configure your endpoint.  For the first test, your controller should
     * return hard-coded values.
     */
    @Test
    public void getPersons_returnsListOfPersons() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        InputStream sampleJson = getClass().getClassLoader().getResourceAsStream("examples/getPersons_sample.json");
        List<Person> expected = mapper.readValue(
                sampleJson, new TypeReference<List<Person>>() {
                });

        List<Person> list = mapper.readValue(
                response.getContentAsString(), new TypeReference<List<Person>>() {
                });

        assertEquals(expected, list);
    }

    @Test
    public void getPersons_ShouldReturnPersonWithGivenId() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/persons/1"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        Person expected = person1;
        Person actual = mapper.readValue(response.getContentAsString(), Person.class);
        assertEquals(expected, actual);

        response = mockMvc.perform(get("/persons/2"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        expected = person2;
        actual = mapper.readValue(response.getContentAsString(), Person.class);
        assertEquals(expected, actual);
    }

    @Test
    public void PatchPerson_ShouldUpdatePersonWithGivenId() throws Exception {
        Person input = new Person(null, "Steve", "Smith", "steve@example.com", null);
        Person expected = new Person(1L, "Steve", "Smith", "steve@example.com", 21);

        MockHttpServletResponse response = mockMvc.perform(patch("/persons/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(mapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        Person actual = mapper.readValue(response.getContentAsString(), Person.class);

        assertEquals(expected, actual);
    }

    @Test
    public void PutPerson_ShouldCreatePersonWithGivenId() throws Exception {
        Person input = new Person(null, "Steve", "Smith", "steve@example.com", null);
        Person expected = new Person(3L, "Steve", "Smith", "steve@example.com", null);

        MockHttpServletResponse response = mockMvc.perform(put("/persons/3")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(mapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        Person actual = mapper.readValue(response.getContentAsString(), Person.class);

        assertEquals(expected, actual);
    }

    @Test
    public void DeletePerson_ShouldDeletePersonWithGivenId() throws Exception {
        assertTrue(personRepository.existsById(1L));
        mockMvc.perform(delete("/persons/1"))
                .andExpect(status().isNoContent())
                .andReturn();
        assertFalse(personRepository.existsById(1L));
    }

}

