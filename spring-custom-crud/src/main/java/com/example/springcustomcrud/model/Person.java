package com.example.springcustomcrud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
// You need this for your JSON Mapping on your endpoint
@NoArgsConstructor
public class Person {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
}
