package com.example.springdatarestcrud;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Person {
    @Id
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
}
