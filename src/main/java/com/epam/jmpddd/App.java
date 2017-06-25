package com.epam.jmpddd;

import com.epam.jmpddd.domain.Match;
import com.epam.jmpddd.exception.MatchAlreadyFinishedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class App {

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Please input team names using args");
        } else {
            final Match match = new Match(UUID.randomUUID(), args[0], args[1]);
            try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                while(!match.isFinished()) {
                    System.out.println("Enter goal[red/blue]: ");
                    try {
                        switch(br.readLine()) {
                            case "red" : match.scoreRed(); break;
                            case "blue" : match.scoreBlue(); break;
                            default : System.out.println("Unknown");
                        }
                    } catch(MatchAlreadyFinishedException e) {
                        System.out.println("Match already finished: " + e);
                    } catch(Exception e) {
                        System.out.println("Error: " + e);
                    }
                }
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
