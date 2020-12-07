package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author pappgergely
 */
public class LabyrinthImpl implements Labyrinth {
    private CellType[][] labyrinth;
    private Coordinate playerPosition;

    public LabyrinthImpl() {
    }

    @Override
    public void loadLabyrinthFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            int width = Integer.parseInt(sc.nextLine());
            int height = Integer.parseInt(sc.nextLine());
            setSize(width, height);
            for (int hh = 0; hh < height; hh++) {
                String line = sc.nextLine();
                for (int ww = 0; ww < width; ww++) {
                    Coordinate c = new Coordinate(height, width);
                    switch (line.charAt(ww)) {
                        case 'W':
                            labyrinth[hh][ww] = CellType.WALL;
                            break;
                        case 'E':
                            labyrinth[hh][ww] = CellType.END;
                            break;
                        case 'S':
                            labyrinth[hh][ww] = CellType.START;
                            playerPosition = c;
                            break;
                        default:
                            labyrinth[hh][ww] = CellType.EMPTY;
                    }
                }
            }
        } catch (NumberFormatException | FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public int getWidth() {
        try {
            return labyrinth[0].length;
        } catch (NullPointerException e) {
            return -1;
        }
    }

    @Override
    public int getHeight() {
        try {
            return labyrinth.length;
        } catch (NullPointerException e) {
            return -1;
        }
    }

    @Override
    public CellType getCellType(Coordinate c) throws CellException {
        try {
            return this.labyrinth[c.getRow()][c.getCol()];
        } catch (IndexOutOfBoundsException e) {
            throw new CellException(c.getRow(), c.getCol(), "Coordinate is out of the labyrinth's range.");
        }
    }

    @Override
    public void setSize(int width, int height) {
        this.labyrinth = new CellType[height][width];
    }

    @Override
    public void setCellType(Coordinate c, CellType type) throws CellException {
        try {
            labyrinth[c.getRow()][c.getCol()] = type;
//            if (type == CellType.START) {
//                this.playerPosition = c;
//            }
        } catch (Exception e) {
            throw new CellException(c.getRow(), c.getCol(), "Coordinate is out of the labyrinth's range.");
        }
    }

    @Override
    public Coordinate getPlayerPosition() {
        return playerPosition;
    }

    @Override
    public boolean hasPlayerFinished() {
        if (labyrinth[playerPosition.getRow()][playerPosition.getCol()] == CellType.END) {
            return true;
        }
        return false;
    }

    @Override
    public List<Direction> possibleMoves() {
        List<Direction> list = new ArrayList<>();
        Coordinate pp = new Coordinate(playerPosition.getRow(), playerPosition.getCol());
        if (pp.getRow() > 0 && labyrinth[pp.getRow() - 1][pp.getCol()] != CellType.WALL) {
            list.add(Direction.NORTH);
        }
        if (pp.getRow() < labyrinth.length - 1 && labyrinth[pp.getRow() + 1][pp.getCol()] != CellType.WALL) {
            list.add(Direction.SOUTH);
        }
        if (pp.getCol() > 0 && labyrinth[pp.getRow()][pp.getCol() - 1] != CellType.WALL) {
            list.add(Direction.WEST);
        }
        if (pp.getRow() < labyrinth[0].length - 1 && labyrinth[pp.getRow()][pp.getCol() + 1] != CellType.WALL) {
            list.add(Direction.EAST);
        }
        return list;
    }

    @Override
    public void movePlayer(Direction direction) throws InvalidMoveException {

        if (!possibleMoves().contains(direction)) {
            throw new InvalidMoveException();
        } else {
            if (direction == Direction.EAST) {
                playerPosition = new Coordinate(playerPosition.getCol() + 1, playerPosition.getRow());
            }
            if (direction == Direction.WEST) {
                playerPosition = new Coordinate(playerPosition.getCol() - 1, playerPosition.getRow());
            }
            if (direction == Direction.NORTH) {
                playerPosition = new Coordinate(playerPosition.getCol(), playerPosition.getRow() + 1);
            }
            if (direction == Direction.SOUTH) {
                playerPosition = new Coordinate(playerPosition.getCol(), playerPosition.getRow() - 1);
            }

        }

    }
}
