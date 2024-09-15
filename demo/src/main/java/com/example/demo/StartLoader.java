package com.example.demo;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartLoader implements CommandLineRunner {
    private final PersonService personService;

    private final Faker faker = new Faker();

    @Autowired
    public StartLoader(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 1000; i++) {
            Person person = Person.builder()
                    .name(faker.funnyName().name())
                    .build();

            personService.savePerson(person);
        }

        Person person = personService.getPersonById(1L).get();
        Person partner = personService.getPersonById(2L).get();
        person.setPartner(partner);
        personService.savePerson(person);
    }
}
