package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is where all your sql methods comes from
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
