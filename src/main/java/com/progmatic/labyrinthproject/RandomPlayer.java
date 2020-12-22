package com.progmatic.labyrinthproject;


import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;

import java.util.Random;

public class RandomPlayer implements Player {

    @Override
    public Direction nextMove(Labyrinth l) {
        if (l.hasPlayerFinished()) {
            return null;
        }
        Random r = new Random();
        int nrOfPossibleMoves = l.possibleMoves().size();
        switch (nrOfPossibleMoves) {
            case 4:
                return l.possibleMoves().get(r.nextInt(4));
            case 3:
                return l.possibleMoves().get(r.nextInt(3));
            case 2:
                return l.possibleMoves().get(r.nextInt(2));
            default:
                return l.possibleMoves().get(0);
        }
    }
}
