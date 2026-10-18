package com.blu.imdg.repositories;

import com.blu.imdg.model.Breed;
import com.blu.imdg.model.Dog;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableIgniteRepositories
public class SpringAppConfig {

    @Bean
    public Ignite igniteInstance() {
        var cfg = new IgniteConfiguration();

        cfg.setIgniteInstanceName("springDataNode");
        cfg.setPeerClassLoadingEnabled(true);

        var ccfgDog = new CacheConfiguration<Long, Dog>("DogCache");
        var ccfgBreed = new CacheConfiguration<Long, Breed>("BreedCache");

        ccfgBreed.setIndexedTypes(Long.class, Breed.class);
        ccfgDog.setIndexedTypes(Long.class, Dog.class);

        cfg.setCacheConfiguration(new CacheConfiguration[]{ccfgDog, ccfgBreed});

        return Ignition.start(cfg);
    }
}
