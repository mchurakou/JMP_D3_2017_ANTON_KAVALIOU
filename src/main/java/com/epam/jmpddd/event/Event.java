package com.epam.jmpddd.event;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public class Event {

    public Event(UUID id) {
        this.id = id;
    }

    public Event() {
    }

    private UUID id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long key;

    public UUID getId() {
        return id;
    }
}
