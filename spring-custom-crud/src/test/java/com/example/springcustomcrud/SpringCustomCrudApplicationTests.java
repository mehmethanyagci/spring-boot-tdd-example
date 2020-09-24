package com.example.springcustomcrud;

import com.example.springcustomcrud.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void contextLoads() {
    }

    @Test
    public void getPersons_returnsEmptyList() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<Person> expected = Arrays.asList(
                new Person(1, "John", "Doe", "john.doe@example.com", 21),
                new Person(2, "Mary", "Smith", "mary.smith@gmail.com", 35)
        );
        List<Person> list = mapper.readValue(
                response.getContentAsString(),
                new TypeReference<List<Person>>() {
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
    }

}

