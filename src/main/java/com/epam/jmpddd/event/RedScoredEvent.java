package com.epam.jmpddd.event;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class RedScoredEvent extends Event {
    public RedScoredEvent() {
    }

    public RedScoredEvent(UUID id) {
        super(id);
    }
}
