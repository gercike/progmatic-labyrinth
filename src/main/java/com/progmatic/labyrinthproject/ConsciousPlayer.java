package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class ConsciousPlayer implements Player {
    Labyrinth l;
    ArrayDeque<Direction> theGoodpath;

    public static void main(String[] args) throws CellException, InvalidMoveException {
        LabyrinthImpl lab = new LabyrinthImpl();
        lab.loadLabyrinthFile("labyrinth2.txt");
//        System.out.println(lab.getHeight());
        ConsciousPlayer cp = new ConsciousPlayer();
        cp.nextMove(lab);
    }

    @Override
    public Direction nextMove(Labyrinth l) throws CellException, InvalidMoveException {
        this.l = l;
        if (l.getCellType(l.getPlayerPosition()) == CellType.START) {
            ArrayDeque<Direction> path = new ArrayDeque<>();
            Coordinate miniPlayer = l.getPlayerPosition();
            path.add(possibleVirtualMoves(miniPlayer).get(0));
            findTheWayOut(path, miniPlayer);
        }
//        System.out.println(path.size());
//        System.out.println(theGoodpath);
        return theGoodpath.remove();
    }

    public void findTheWayOut(ArrayDeque<Direction> thePath, Coordinate miniPlayer) throws InvalidMoveException, CellException {
        ArrayDeque<Direction> path0 = new ArrayDeque<>();
        for (Direction direction : thePath) {
            path0.add(direction);
        }
        Coordinate miniPlayer0 = moveVirtualPlayer(path0.getLast(), miniPlayer);
        if (hasVirtualPlayerFinished(miniPlayer0)) {
            theGoodpath = path0;
        }
        Direction oppositeOfPrevious = getOppositeDirectionOfLastStep(path0);
        List<Direction> possibleMovesWithoutBacktracking = possibleVirtualMoves(miniPlayer0);
        possibleMovesWithoutBacktracking.remove(oppositeOfPrevious);
//        System.out.println(possibleMovesWithoutBacktracking);
        int size = possibleMovesWithoutBacktracking.size();
        if (size == 1) {
            ArrayDeque<Direction> path1 = new ArrayDeque<>();
            for (Direction direction : path0) {
                path1.add(direction);
            }
            path1.add(possibleMovesWithoutBacktracking.get(0));
            findTheWayOut(path1, miniPlayer0);
        }
        if (size == 2) {
            ArrayDeque<Direction> path1 = new ArrayDeque<>();
            for (Direction direction : path0) {
                path1.add(direction);
            }
            Coordinate miniPlayer1 = miniPlayer0;
            path1.add(possibleMovesWithoutBacktracking.get(0));
            findTheWayOut(path1, miniPlayer1);
            ArrayDeque<Direction> path2 = new ArrayDeque<>();
            for (Direction direction : path0) {
                path2.add(direction);
            }
            Coordinate miniPlayer2 = miniPlayer0;
            path2.add(possibleMovesWithoutBacktracking.get(1));
            findTheWayOut(path2, miniPlayer2);
        }
        if (size == 3) {
            ArrayDeque<Direction> path1 = new ArrayDeque<>();
            for (Direction direction : path0) {
                path1.add(direction);
            }
            Coordinate miniPlayer1 = miniPlayer0;
            path1.add(possibleMovesWithoutBacktracking.get(0));
            findTheWayOut(path1, miniPlayer1);
            ArrayDeque<Direction> path2 = new ArrayDeque<>();
            for (Direction direction : path0) {
                path2.add(direction);
            }
            Coordinate miniPlayer2 = miniPlayer0;
            path2.add(possibleMovesWithoutBacktracking.get(1));
            findTheWayOut(path2, miniPlayer2);
            ArrayDeque<Direction> path3 = new ArrayDeque<>();
            for (Direction direction : path0) {
                path3.add(direction);
            }
            Coordinate miniPlayer3 = miniPlayer0;
            path3.add(possibleMovesWithoutBacktracking.get(2));
            findTheWayOut(path3, miniPlayer3);
        }
    }

    public boolean hasVirtualPlayerFinished(Coordinate player) throws CellException {
        if (l.getCellType(player) == CellType.END) {
            return true;
        }
        return false;
    }

    public Coordinate moveVirtualPlayer(Direction direction, Coordinate player) throws InvalidMoveException, CellException {
        if (!possibleVirtualMoves(player).contains(direction)) {
            throw new InvalidMoveException();
        } else {
            if (direction == Direction.EAST) {
                player = new Coordinate(player.getCol() + 1, player.getRow());
            }
            if (direction == Direction.WEST) {
                player = new Coordinate(player.getCol() - 1, player.getRow());
            }
            if (direction == Direction.NORTH) {
                player = new Coordinate(player.getCol(), player.getRow() - 1);
            }
            if (direction == Direction.SOUTH) {
                player = new Coordinate(player.getCol(), player.getRow() + 1);
            }
        }
        return player;
    }

    public List<Direction> possibleVirtualMoves(Coordinate pp) throws CellException {
        List<Direction> moveList = new ArrayList<>();
        if (hasVirtualPlayerFinished(pp)) {
            return moveList;
        } else {
            if (pp.getRow() > 0 && l.getCellType(new Coordinate(pp.getCol(), pp.getRow() - 1)) != CellType.WALL) {
                moveList.add(Direction.NORTH);
            }
            if (pp.getRow() < l.getHeight() - 1 && l.getCellType(new Coordinate(pp.getCol(), pp.getRow() + 1)) != CellType.WALL) {
                moveList.add(Direction.SOUTH);
            }
            if (pp.getCol() > 0 && l.getCellType(new Coordinate(pp.getCol() - 1, pp.getRow())) != CellType.WALL) {
                moveList.add(Direction.WEST);
            }
            if (pp.getRow() < l.getWidth() - 1 && l.getCellType(new Coordinate(pp.getCol() + 1, pp.getRow())) != CellType.WALL) {
                moveList.add(Direction.EAST);
            }
        }
        return moveList;
    }

    Direction getOppositeDirectionOfLastStep(ArrayDeque<Direction> path) {
        Direction directionOfLastStep = path.getLast();
        switch (directionOfLastStep) {
            case SOUTH:
                return Direction.NORTH;
            case WEST:
                return Direction.EAST;
            case NORTH:
                return Direction.SOUTH;
            case EAST:
                return Direction.WEST;
        }
        return null;
    }
}
