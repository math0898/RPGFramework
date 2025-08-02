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
     * The size of the grid.
     */
    private static final int GRID_SIZE = 80;

    /**
     * The random instance used throughout generation.
     */
    private static final Random rand = new Random(8);

    /**
     * The boss room.
     */
    private static final Room BOSS_ROOM = new Room("Boss Room", new Pos(8, 4), new Pos(0, -4), Arrays.asList());

    /**
     * The starting room.
     */
    private static final Room STARTING_ROOM = new Room("Starting Room", new Pos(4, 4), new Pos(0, 0), Arrays.asList(new Pos(5, 2)));

    /**
     * Most positions, except the very first origin point, are offsets from doorways.
     */
    private record Pos (int dx, int dz) {}

    /**
     * Each room has a starting location, a list of doors, and dimensions which block the generation of other rooms.
     */
    private record Room (String name, Pos corner1, Pos corner2, List<Pos> doors){

        /**
         * Places this Room on the given matrix with the given door origin point.
         *
         * @param doorway The starting point for this room.
         */
        public void placeRoom (Pos doorway, int[][] array) {
            for (int i = doorway.dx() - corner1.dx(); i <= doorway.dx() - corner2().dx(); i++)
                for (int j = doorway.dz() - corner1.dz(); j <= doorway.dz() - corner2().dz(); j++)
                    array[i][j] += 1;
            for (Pos p : doors)
                if (array[doorway.dx() - p.dx()][doorway.dz() - p.dz()] == 0)
                    array[doorway.dx() - p.dx()][doorway.dz() - p.dz()] = -1;
        }

        /**
         * Prints this room to the console to aid in debugging.
         */
        public void print() {
            System.out.println(name + " => (" + corner1.dx() + ", " + corner1.dz() + ") (" + corner2.dx() + ", " + corner2.dz() + ")");
        }
    }

    /**
     * A list of rooms that can be generated.
     */
    private static final List<Room> rooms = new ArrayList<>();

    /**
     * We utilize a matrix to quickly check whether a generation candidate works.
     */
    private static final int[][] FLOOR = new int[GRID_SIZE][GRID_SIZE]; // todo: Significant performance increase if these each correspond to 3 minecraft blocks.

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
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[i].length; j++)
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
     * Replaces all instances of the first value in the matrix with the second value.
     */
    private static void matrixReplace (int[][] matrix, int from, int to) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++)
                if (matrix[i][j] == from) matrix[i][j] = to;
        }
    }

    /**
     * Attempts to place a room in the given map.
     */
    private static boolean attemptPlacement (int[][] copy, Room room) {
        boolean picked = false;
        try {
            for (int i = 0; i < copy.length && !picked; i++)
                for (int j = 0; j < copy[i].length && !picked; j++) {
                    if (copy[i][j] == -1) {
                        room.placeRoom(new Pos(i, j), copy);
                        if (validateArray(copy)) {
                            picked = true;
                        } else copyArray(copy, FLOOR);
                    }
                }
        } catch (ArrayIndexOutOfBoundsException ignored) { } // Room placement failed. Picked is still false.
        if (picked) room.print();
        return picked;
    }

    /**
     * This is the filling algorithm portion that involves placing rooms at the first available doorway.
     */
    private static void fillAlgorithm (int roomsSoFar, int maxRooms) {
        int numRooms = roomsSoFar;
        int attempts = 0;
        while (numRooms < maxRooms && attempts < 10) {
            Room room = rooms.get(rand.nextInt(rooms.size()));
            int[][] copy = new int[GRID_SIZE][GRID_SIZE];
            copyArray(copy, FLOOR);
            boolean result = attemptPlacement(copy, room);
            if (result) { // A room was picked.
                copyArray(FLOOR, copy);
                attempts = 0;
                numRooms++;
            } else attempts++;
        }
    }

    /**
     * The important algorithm portion of the generation.
     */
    private static void algorithm (int lengthToEnd, double deadEndProb, int maxRooms) {
        STARTING_ROOM.placeRoom(new Pos(GRID_SIZE / 2, GRID_SIZE / 2), FLOOR); // Starting seed.
        int attempts = 0;
        int[][] copy = new int[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < lengthToEnd && attempts < 1000; i++) {
            boolean result = false;
            while (!result && attempts < 1000) {
                Room room = rooms.get(rand.nextInt(rooms.size()));
                copyArray(copy, FLOOR);
                result = attemptPlacement(copy, room);
                if (result) copyArray(FLOOR, copy);
                else attempts++;
            }
        }
        copyArray(copy, FLOOR);
        boolean result = attemptPlacement(copy, BOSS_ROOM);
        if (result) copyArray(FLOOR, copy);
        else {
            System.out.println(" ==== Generation Failed to Place Boss Room ==== ");
            throw new IllegalStateException("Exit");
        }
        fillAlgorithm(lengthToEnd + 2 /* Starting room and Boss Room */, maxRooms);
    }
    // Note Lower X is up. Lower Z is left.
    public static void main (String[] args) { // todo: Pick a chain of n non-terminating rooms to place, followed by the boss room.
        rooms.add(new Room("Up Hallway", new Pos(4, 1), new Pos(0, -1), Arrays.asList(new Pos(5, 0)))); // "North" Hallway
        rooms.add(new Room("Down Hallway", new Pos(0, 1), new Pos(-4, -1), Arrays.asList(new Pos(-5, 0)))); // "South" Hallway
//        rooms.add(new Room("Right Hallway", new Pos(0, -1), new Pos(4, 1), Arrays.asList(new Pos(5, 0)))); // "East" Hallway
//        rooms.add(new Room("Left Hallway", new Pos(-4, 1), new Pos(0, -1), Arrays.asList(new Pos(-5, 0)))); // "West" Hallway

        // todo: There is a serious consideration to be made here.
        //  Right now each room kinda has an origin which is the entry point.
        //  But that means I need 4 roughly identical data entries to represent a large 4 way room.
//        rooms.add(new Room(new Pos(-2, 0), new Pos(2, 5), Arrays.asList(new Pos(0, 6), new Pos(2, 3), new Pos(-2, 3)))); // Small Room
//        rooms.add(new Room(new Pos(2, 0), new Pos(2, -5), Arrays.asList(new Pos(0, -6), new Pos(2, -3), new Pos(-2, -3)))); // Small Room
//        rooms.add(new Room(new Pos(-2, 0), new Pos(2, 0), Arrays.asList(new Pos(6, 0), new Pos(-3, 2), new Pos(-2, -3))));
//        rooms.add(new Room(new Pos(-2, 0), new Pos(2, 0), Arrays.asList(new Pos(6, 0), new Pos(-3, 2), new Pos(-2, -3))));

//        rooms.add(new Room(new Pos(1, 10), new Pos(-1, 0), Arrays.asList(new Pos(0, 10)))); // Large Room

        initArray(FLOOR);
        algorithm(2, 0.2, 0);
        matrixReplace(FLOOR, -1, 5);
        printMatrix();
    }
}
