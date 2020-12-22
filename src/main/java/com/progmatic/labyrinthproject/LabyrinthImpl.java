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
    CellType[][] labyrinth;
    Coordinate playerPosition;

    public LabyrinthImpl() {
    }

    public static void main(String[] args) throws CellException {
        LabyrinthImpl l = new LabyrinthImpl();
        l.loadLabyrinthFile("labyrinth1.txt");
        System.out.println("a játékos poz. oszlopszáma: " + l.getPlayerPosition().getCol());
        System.out.println("a játékos poz. sorszáma: " + l.getPlayerPosition().getRow());
        System.out.println("l szélessége: " + l.getWidth());
        System.out.println("l magassága: " + l.getHeight());
        Coordinate c1 = new Coordinate(0, 1);
        System.out.println(l.getCellType(c1));
        Coordinate c2 = new Coordinate(2, 3);
        System.out.println(l.getCellType(c2));
        Coordinate c3 = new Coordinate(3, 3);
        l.setCellType(c3, CellType.WALL);
        System.out.println(l.getCellType(c3));
        l.playerPosition = c2;
        System.out.println(l.hasPlayerFinished());
        System.out.println(l.possibleMoves());
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
                    switch (line.charAt(ww)) {
                        case 'W':
                            labyrinth[hh][ww] = CellType.WALL;
                            break;
                        case 'E':
                            labyrinth[hh][ww] = CellType.END;
                            break;
                        case 'S':
                            labyrinth[hh][ww] = CellType.START;
                            playerPosition = new Coordinate(ww, hh);
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
            if (type == CellType.START) {
                playerPosition = c;
            }
            labyrinth[c.getRow()][c.getCol()] = type;
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
        if (labyrinth[getPlayerPosition().getRow()][getPlayerPosition().getCol()] == CellType.END) {
            return true;
        }
        return false;
    }

    @Override
    public List<Direction> possibleMoves() {
        List<Direction> list = new ArrayList<>();
        Coordinate pp = new Coordinate(getPlayerPosition().getCol(), getPlayerPosition().getRow());
        if (hasPlayerFinished()) {
            return list;
        } else {
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
                playerPosition = new Coordinate(playerPosition.getCol(), playerPosition.getRow() - 1);
            }
            if (direction == Direction.SOUTH) {
                playerPosition = new Coordinate(playerPosition.getCol(), playerPosition.getRow() + 1);
            }
//            System.out.println(playerPosition.getCol() + " - " + playerPosition.getRow());
        }
    }
}
