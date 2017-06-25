package com.epam.jmpddd.event;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class MatchCreatedEvent extends Event {

    private String redTeam;
    private String blueTeam;

    public MatchCreatedEvent() {
    }

    public MatchCreatedEvent(UUID id, String redTeam, String blueTeam) {
        super(id);
        this.redTeam = redTeam;
        this.blueTeam = blueTeam;
    }

    public String getRedTeam() {
        return redTeam;
    }

    public String getBlueTeam() {
        return blueTeam;
    }

    @Override
    public String toString() {
        return "MatchCreatedEvent{" + "redTeam='" + redTeam + '\'' + ", blueTeam='" + blueTeam + '\'' + '}';
    }
}
