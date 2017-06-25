package com.epam.jmpddd.event;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class BlueScoredEvent extends Event {
    public BlueScoredEvent() {
    }

    public BlueScoredEvent(UUID id) {
        super(id);
    }
}
