package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;

public class LeftyWallFollowerPlayer implements Player {
    Direction facing;
    Direction leftHandPointing;
    CellType leftHandPointingAt;
    Direction[] turnOrder = {Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST, Direction.SOUTH};

    @Override
    public Direction nextMove(Labyrinth l) throws CellException {
        if (facing == null) {
            facing = l.possibleMoves().get(0);
        }
        if (l.hasPlayerFinished()) {
            return null;
        }
        setLeftHandPointingAt(facing, l);
        while (!(leftHandPointingAt == CellType.EMPTY || leftHandPointingAt == CellType.END)) {
            for (int i = 0; i < turnOrder.length; i++) {
                if (turnOrder[i] == facing) {
                    facing = turnOrder[i + 1];
                    setLeftHandPointingAt(facing, l);
                    break;
                }
            }
        }
        facing = leftHandPointing;
        return facing;
    }

    void setLeftHandPointingAt(Direction facing, Labyrinth l) throws CellException {
        switch (facing) {
            case SOUTH:
                leftHandPointing = Direction.EAST;
                leftHandPointingAt = l.getCellType(new Coordinate(l.getPlayerPosition().getCol() + 1, l.getPlayerPosition().getRow()));
                break;
            case EAST:
                leftHandPointing = Direction.NORTH;
                leftHandPointingAt = l.getCellType(new Coordinate(l.getPlayerPosition().getCol(), l.getPlayerPosition().getRow() - 1));
                break;
            case NORTH:
                leftHandPointing = Direction.WEST;
                leftHandPointingAt = l.getCellType(new Coordinate(l.getPlayerPosition().getCol() - 1, l.getPlayerPosition().getRow()));
                break;
            case WEST:
                leftHandPointing = Direction.SOUTH;
                leftHandPointingAt = l.getCellType(new Coordinate(l.getPlayerPosition().getCol(), l.getPlayerPosition().getRow() + 1));
                break;
        }
    }
}
