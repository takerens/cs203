package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    public void savePerson(Person person) {
        personRepository.save(person);
    }

    public List<Person> getAllPeople() {
        return personRepository.findAll();
    }
}
