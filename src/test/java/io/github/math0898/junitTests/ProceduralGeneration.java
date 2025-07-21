package io.github.math0898.junitTests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A test sandbox for the Procedural Algorithm used by the RPGFramework plugin to generate dungeons.
 *
 * @author Sugaku
 */
public class ProceduralGeneration {

    /**
     * Most positions, except the very first origin point, are offsets from doorways.
     */
    private record Pos (int dx, int dz) {}

    /**
     * Each room has a starting location, a list of doors, and dimensions which block the generation of other rooms.
     */
    private record Room (Pos corner1, Pos corner2, List<Pos> doors){

        /**
         * Places this Room on the given matrix with the given door origin point.
         *
         * @param doorway The starting point for this room.
         */
        public void placeRoom (Pos doorway, int[][] array) {
            for (int i = doorway.dx() - corner1.dx(); i <= doorway.dx() - corner2().dx(); i++)
                for (int j = doorway.dz() - corner1.dz(); j <= doorway.dz() - corner2().dz(); j++)
                    array[i][j] = 1;
            for (Pos p : doors)
                array[doorway.dx() - p.dx()][doorway.dz() - p.dz()] = -1;
        }
    }

    /**
     * A list of rooms that can be generated.
     */
    private static final List<Room> rooms = new ArrayList<>();

    /**
     * We utilize a matrix to quickly check whether a generation candidate works.
     */
    private static final int[][] FLOOR = new int[50][50]; // todo: Significant performance increase if these each correspond to 3 minecraft blocks.

    /**
     * Init the floor matrix.
     */
    private static void init_FLOOR () {
        for (int[] ints : FLOOR) Arrays.fill(ints, 0);
    }

    /**
     * Prints the matrix to the console.
     */
    private static void printMatrix () {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < FLOOR.length; i++) {
            for (int j = 0; j < FLOOR[i].length; j++)
                line.append(FLOOR[i][j]);
            System.out.println(line);
            line = new StringBuilder();
        }
    }

    public static void main (String[] args) {
        rooms.add(new Room(new Pos(1, 10), new Pos(-1, 0), Arrays.asList(new Pos(0, 10)))); // Hallway
        init_FLOOR();
        rooms.get(0).placeRoom(new Pos(25, 25), FLOOR);
        printMatrix();
    }
}
