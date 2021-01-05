package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;

import java.util.*;

public class ConsciousPlayer implements Player {
    Labyrinth l;
    Stack<Direction> theGoodPath;
    Coordinate start;

    public static void main(String[] args) throws CellException, InvalidMoveException {
        LabyrinthImpl lab = new LabyrinthImpl();
        lab.loadLabyrinthFile("labyrinth1.txt");
        ConsciousPlayer cp = new ConsciousPlayer();
        cp.nextMove(lab);
    }

    @Override
    public Direction nextMove(Labyrinth l) throws CellException, InvalidMoveException {
        this.l = l;
        if (l.getCellType(l.getPlayerPosition()) == CellType.START) {
            theGoodPath = new Stack<>();
            int[][] miniMap = new int[l.getHeight()][l.getHeight()];
            start = l.getPlayerPosition();
            findTheWayOutBFS(miniMap, start, 1, null);
        }
        return theGoodPath.pop();
    }

    Stack<Direction> getThePath(int[][] miniMap, Coordinate currentPosition) throws CellException {
        int currentNumber = miniMap[currentPosition.getRow()][currentPosition.getCol()];
        int targetNumber = currentNumber - 1;
        if (targetNumber > 0) {
            if (currentPosition.getRow() - 1 >= 0 && miniMap[currentPosition.getRow() - 1][currentPosition.getCol()] == targetNumber) {
                theGoodPath.push(Direction.SOUTH);
                Coordinate nextPosition = new Coordinate(currentPosition.getCol(), currentPosition.getRow() - 1);
                return getThePath(miniMap, nextPosition);
            }
            if (currentPosition.getRow() < miniMap.length && miniMap[currentPosition.getRow() + 1][currentPosition.getCol()] == targetNumber) {
                theGoodPath.push(Direction.NORTH);
                Coordinate nextPosition = new Coordinate(currentPosition.getCol(), currentPosition.getRow() + 1);
                return getThePath(miniMap, nextPosition);
            }
            if (currentPosition.getCol() - 1 >= 0 && miniMap[currentPosition.getRow()][currentPosition.getCol() - 1] == targetNumber) {
                theGoodPath.push(Direction.EAST);
                Coordinate nextPosition = new Coordinate(currentPosition.getCol() - 1, currentPosition.getRow());
                return getThePath(miniMap, nextPosition);
            }
            if (currentPosition.getRow() < miniMap[0].length && miniMap[currentPosition.getRow()][currentPosition.getCol() + 1] == targetNumber) {
                theGoodPath.push(Direction.WEST);
                Coordinate nextPosition = new Coordinate(currentPosition.getCol() + 1, currentPosition.getRow());
                return getThePath(miniMap, nextPosition);
            }
        }
        return null;
    }

    public void findTheWayOutBFS(int[][] miniMap, Coordinate currentPosition, int numberOfSteps, Direction directionOfLastStep) throws CellException, InvalidMoveException {
        miniMap[currentPosition.getRow()][currentPosition.getCol()] = numberOfSteps;
        if (hasVirtualPlayerFinished(currentPosition)) {
            theGoodPath = new Stack<>();
            getThePath(miniMap, currentPosition);
        }
        List<Direction> possibleMovesWithoutBacktracking = possibleVirtualMoves(currentPosition);
        if (l.getCellType(currentPosition) != CellType.START) {
            Direction oppositeOfPrevious = getOppositeDirectionOfLastStep(directionOfLastStep);
            possibleMovesWithoutBacktracking.remove(oppositeOfPrevious);
        }
        int size = possibleMovesWithoutBacktracking.size();
        if (size > 0) {
            Coordinate nextPosition = moveVirtualPlayer(possibleMovesWithoutBacktracking.get(0), currentPosition);
            findTheWayOutBFS(miniMap, nextPosition, numberOfSteps + 1, possibleMovesWithoutBacktracking.get(0));
        }
        if (size > 1) {
            Coordinate nextPosition = moveVirtualPlayer(possibleMovesWithoutBacktracking.get(1), currentPosition);
            findTheWayOutBFS(miniMap, nextPosition, numberOfSteps + 1, possibleMovesWithoutBacktracking.get(1));
        }
        if (size > 2) {
            Coordinate nextPosition = moveVirtualPlayer(possibleMovesWithoutBacktracking.get(2), currentPosition);
            findTheWayOutBFS(miniMap, nextPosition, numberOfSteps + 1, possibleMovesWithoutBacktracking.get(2));
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

    Direction getOppositeDirectionOfLastStep(Direction directionOfStep) {
        switch (directionOfStep) {
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
