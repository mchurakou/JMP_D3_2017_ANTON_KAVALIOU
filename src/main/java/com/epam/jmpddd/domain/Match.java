package com.epam.jmpddd.domain;

import com.epam.jmpddd.event.BlueScoredEvent;
import com.epam.jmpddd.event.MatchCreatedEvent;
import com.epam.jmpddd.event.MatchFinishedEvent;
import com.epam.jmpddd.event.RedScoredEvent;
import com.epam.jmpddd.exception.MatchAlreadyFinishedException;
import com.epam.jmpddd.value.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class Match extends AggregateRoot {
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static final int MAX_GOALS = 10;

    private UUID id;
    private Team redTeam;
    private Team blueTeam;
    private int redGoals = 0;
    private int blueGoals = 0;

    public Match(UUID id, String redTeam, String blueTeam) {
        apply(new MatchCreatedEvent(id, redTeam, blueTeam));
    }

    private void handle(MatchCreatedEvent event) {
        this.id = event.getId();
        this.redTeam = new Team(event.getRedTeam());
        this.blueTeam = new Team(event.getBlueTeam());
    }

    public void scoreRed() throws MatchAlreadyFinishedException {
        if (isFinished()) {
            throw new MatchAlreadyFinishedException();
        }
        apply(new RedScoredEvent(id));
    }

    private void handle(RedScoredEvent event) {
        redGoals++;
        calculateWinner(redTeam);
    }

    public void scoreBlue() throws MatchAlreadyFinishedException {
        if (isFinished()) {
            throw new MatchAlreadyFinishedException();
        }
        apply(new BlueScoredEvent(id));
    }

    private void handle(BlueScoredEvent event) {
        blueGoals++;
        calculateWinner(blueTeam);
    }

    private void calculateWinner(Team lastScore) {
        if (isFinished()) {
            Team winner;
            if (redGoals == blueGoals) {
                winner = lastScore;
            } else {
                winner = redGoals < blueGoals ? blueTeam : redTeam;
            }
            apply(new MatchFinishedEvent(id, winner.getName()));
        }
    }

    private void handle(MatchFinishedEvent event) {
        LOGGER.info("Game " + event.getId() + " finished. Congratulations to the winner: " + event.getWinner());
    }

    public boolean isFinished() {
        return redGoals + blueGoals == MAX_GOALS;
    }

    @Override
    protected boolean checkEvents() {
        boolean result = false;
        processEvents(MatchCreatedEvent.class, e -> {
            LOGGER.info("Match created: " + e);
            handle(e);
        });

        processEvents(RedScoredEvent.class, e -> {
            LOGGER.info(redTeam.getName() + " scored!");
            handle(e);
            printScore();
        });

        processEvents(BlueScoredEvent.class, e -> {
            LOGGER.info(blueTeam.getName() + " scored!");
            handle(e);
            printScore();
        });

        int count = processEvents(MatchFinishedEvent.class, this::handle);
        if (count > 0) {
            result = true;
        }

        return result;
    }

    private void printScore() {
        LOGGER.info(redTeam.getName() + "   " + redGoals + " - " + blueGoals + "   " + blueTeam.getName());
    }
}
