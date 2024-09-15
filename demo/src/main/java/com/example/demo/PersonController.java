package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("api/person/{id}")
    public Person getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id).get();
    }

    @GetMapping("api/person")
    public List<Person> getPersonById() {
        return personService.getAllPeople();
    }
}
