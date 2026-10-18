package com.blu.imdg;

import com.blu.imdg.model.Breed;
import com.blu.imdg.model.Dog;
import com.blu.imdg.repositories.BreedRepository;
import com.blu.imdg.repositories.DogRepository;
import com.blu.imdg.repositories.SpringAppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        log.info("Spring Data Example!");

        var ctx = new AnnotationConfigApplicationContext(SpringAppConfig.class);
        ctx.refresh();

        var breedRepository = ctx.getBean(BreedRepository.class);
        var dogRepository = ctx.getBean(DogRepository.class);

        // Fill the repository with data and save
        var collie = new Breed(1L, "collie");
        breedRepository.save(1L, collie);

        log.info("Add one breed in the repository!");

        // Query the breed
        var getAllBreeds = breedRepository.getAllBreedsByName("collie");

        for (var breed : getAllBreeds) {
            log.info("Breed: {}", breed);
        }

        // Add some dogs
        var dina = new Dog(1L, "dina", 1L, LocalDate.now());
        dogRepository.save(2L, dina);
        log.info("Dog dina save into the cache!");

        // Query the Dog Dina
        var dogs = dogRepository.getDogByName("dina");
        for (var dog : dogs) {
            log.info("Dog: {}", dog);
        }

        log.info("Enter ctrl+C to exit");
    }
}
