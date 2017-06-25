package com.epam.jmpddd.event;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class MatchFinishedEvent extends Event {
    private String winner;

    public MatchFinishedEvent() {
    }

    public MatchFinishedEvent(UUID id, String winner) {
        super(id);
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
}
