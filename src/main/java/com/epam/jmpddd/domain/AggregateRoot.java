package com.epam.jmpddd.domain;

import com.epam.jmpddd.event.Event;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.function.Consumer;

public abstract class AggregateRoot {
    private static final int EVENT_FREQ_MS = 10000;

    private final EntityManager em;

    public AggregateRoot() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        em = factory.createEntityManager();
        new Thread(() -> {
            boolean exit = false;
            while(!exit) {
                exit = checkEvents();
                try {
                    Thread.sleep(EVENT_FREQ_MS);
                } catch(InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    protected abstract boolean checkEvents();

    protected synchronized void apply(Event event) {
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive()) {
            tx.begin();
        }
        em.persist(event);
        if (tx.isActive()) {
            tx.commit();
        }
    }

    protected synchronized <T extends Event> int processEvents(Class<T> type, Consumer<T> processEvent) {
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive()) {
            tx.begin();
        }
        List<T> events = em.createQuery("from " + type.getSimpleName(), type).getResultList();
        events.forEach(processEvent);
        events.forEach(em::remove);
        if (tx.isActive()) {
            tx.commit();
        }
        return events.size();
    }
}
