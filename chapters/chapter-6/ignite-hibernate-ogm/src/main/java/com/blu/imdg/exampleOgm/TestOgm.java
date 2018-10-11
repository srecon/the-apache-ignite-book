package com.blu.imdg.exampleOgm;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform;
import org.hibernate.jpa.HibernateEntityManagerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.transaction.*;
import java.util.List;

/**
 * Created by mikl on 19.11.16.
 */
public class TestOgm {
    public static void main(String[] args) throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ogm-jpa-tutorial");

        EntityManager em =  emf.createEntityManager();
        em.getTransaction().begin();

        Breed collie = new Breed();


        collie.setName("breed-collie");
        em.persist(collie);

        Dog dina = new Dog();
        dina.setName("dina");
        dina.setBreed(collie);
        //persis dina
        em.persist(dina);
        em.getTransaction().commit();
        //em.flush();
        //get ID dina
        Long dinaId = dina.getId();
        // query
        Dog ourDina =  em.find(Dog.class, dinaId);
        System.out.println("Dina:" + ourDina);

        em.close();

    }

    private static TransactionManager extractJBossTransactionManager(EntityManagerFactory factory) {
        SessionFactoryImplementor sessionFactory = (SessionFactoryImplementor) ( (HibernateEntityManagerFactory) factory ).getSessionFactory();
        return sessionFactory.getServiceRegistry().getService( JtaPlatform.class ).retrieveTransactionManager();
    }
}
