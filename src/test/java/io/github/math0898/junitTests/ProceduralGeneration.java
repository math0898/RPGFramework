package io.github.math0898.junitTests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    private static void initArray (int[][] array) {
        for (int[] ints : array) Arrays.fill(ints, 0);
    }

    /**
     * Copies the given matrix into the next matrix.
     */
    private static void copyArray (int[][] target, int[][] source) {
        for (int i = 0; i < target.length; i++)
            System.arraycopy(source[i], 0, target[i], 0, target[i].length);
    }

    /**
     * Validates whether the given matrix is a valid room placement or not.
     */
    private static boolean validateArray (int[][] array) {
        for (int i = 0; i < 50; i++)
            for (int j = 0; j < 50; j++)
                if (array[i][j] > 1) return false;
        return true;
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

    /**
     * Attempts to place a room in the given map.
     */
    private static boolean attemptPlacement (int[][] copy, Room room) {
        boolean picked = false;
        try {
            for (int i = 0; i < 50 && !picked; i++)
                for (int j = 0; j < 50 && !picked; j++) {
                    if (copy[i][j] == -1) {
                        room.placeRoom(new Pos(i, j), copy);
                        if (validateArray(copy)) {
                            picked = true;
                        } else copyArray(copy, FLOOR);
                    }
                }
        } catch (ArrayIndexOutOfBoundsException ignored) { } // Room placement failed. Picked is still false.
        return picked;
    }

    /**
     * The important algorithm portion of the generation.
     */
    private static void algorithm (int lengthToEnd, double deadEndProb, int maxRooms) {
        int numRooms = 1;
        int attempts = 0;
        Random rand = new Random(3);
        rooms.get(1).placeRoom(new Pos(25, 25), FLOOR);
        while (numRooms < maxRooms && attempts < 10) {
            Room room = rooms.get(rand.nextInt(rooms.size()));
            int[][] copy = new int[50][50];
            copyArray(copy, FLOOR);
            boolean result = attemptPlacement(copy, room);
            if (result) { // A room was picked.
                copyArray(FLOOR, copy);
                attempts = 0;
                numRooms++;
            } else attempts++;
        }
    }

    public static void main (String[] args) {
        rooms.add(new Room(new Pos(1, 10), new Pos(-1, 0), Arrays.asList(new Pos(0, 10)))); // Hallway
        rooms.add(new Room(new Pos(4, 4), new Pos(0, 0), Arrays.asList(new Pos(5, 2)))); // Starting Room
        initArray(FLOOR);
        algorithm(5, 0.2, 5);
        printMatrix();
    }
}
