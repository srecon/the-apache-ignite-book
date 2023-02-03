package com.blu.imdg;

import com.blu.imdg.model.Breed;
import com.blu.imdg.model.Dog;
import com.blu.imdg.repositories.BreedRepository;
import com.blu.imdg.repositories.DogRepository;
import com.blu.imdg.repositories.SpringAppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Date;
import java.util.List;


public class App 
{
    private static AnnotationConfigApplicationContext ctx;
    private static BreedRepository breedRepository;
    private static DogRepository dogRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {
        LOGGER.info( "Spring Data Example!" );
        ctx = new AnnotationConfigApplicationContext();
        ctx.register(SpringAppConfig.class);
        ctx.refresh();

        breedRepository = ctx.getBean(BreedRepository.class);
        dogRepository = ctx.getBean(DogRepository.class);

        //fill the repository with data and Save
        Breed collie = new Breed();
        collie.setId(1L);
        collie.setName("collie");
        //save Breed with name collie
        breedRepository.save(1L, collie);

        LOGGER.info("Add one breed in the repository!");
        // Query the breed
        List<Breed> getAllBreeds = breedRepository.getAllBreedsByName("collie");

        for(Breed breed : getAllBreeds){
            LOGGER.info("Breed:" + breed);
        }
        //Add some dogs
        Dog dina = new Dog();
        dina.setName("dina");
        dina.setId(1L);
        dina.setBreedid(1L);
        //Save Dina
        dogRepository.save(2L,dina);
        LOGGER.info("Dog dina save into the cache!");
        //Query the Dog Dina
        List<Dog> dogs = dogRepository.getDogByName("dina");
        for(Dog dog : dogs){
            LOGGER.info("Dog:"+ dog);
        }
        LOGGER.info("Enter ctrl+C to exit");
    }
}
