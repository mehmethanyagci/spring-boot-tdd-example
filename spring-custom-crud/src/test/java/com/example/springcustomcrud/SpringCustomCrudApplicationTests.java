package com.example.springcustomcrud;

import com.example.springcustomcrud.model.Person;
import com.example.springcustomcrud.repository.PersonRepository;
import com.example.springcustomcrud.service.PersonCrudLogic;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        personRepository.save(new Person(1, "John", "Doe", "john.doe@example.com", 21));
        personRepository.save(new Person(2, "Mary", "Smith", "mary.smith@gmail.com", 35));

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
        Person expected = new Person(1, "John", "Doe", "john.doe@example.com", 21);
        Person actual = mapper.readValue(response.getContentAsString(), Person.class);
        assertEquals(expected, actual);

        response = mockMvc.perform(get("/persons/2"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        expected = new Person(2, "Mary", "Smith", "mary.smith@gmail.com", 35);
        actual = mapper.readValue(response.getContentAsString(), Person.class);
        assertEquals(expected, actual);
    }

}

