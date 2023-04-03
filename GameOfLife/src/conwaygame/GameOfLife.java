package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(file);
        int numberRows = StdIn.readInt();
        int numberCols = StdIn.readInt();
        grid = new boolean[numberRows][numberCols];

        for (int r = 0; r < numberRows; r++) {
            for (int c = 0; c < numberCols; c++) {
                boolean value = StdIn.readBoolean();
                grid[r][c] = value;

                if (value == true) {
                    totalAliveCells += 1;
                }
            }
        }
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

        // WRITE YOUR CODE HERE
        return grid[row][col]; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        // WRITE YOUR CODE HERE
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] == true) {
                    return true;
                }
            }
        }

        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {

        // WRITE YOUR CODE HERE
        int numAliveNeighbors = 0;

        for (int r = row - 1; r <= row + 1; r++) {
            int tempR = r; // tempR helps circle to the other side of the grid when looking for neighbors
            if (r == -1) {
                tempR = grid.length - 1;
            } else if (r == grid.length) {
                tempR = grid.length - grid.length;
            }

            for (int c = col - 1; c <= col + 1; c++) {
                int tempC = c; // tempC helps circle to the other side of the grid when looking for neighbors
                if (c == -1) {
                    tempC = grid[0].length - 1;
                } else if (c == grid[0].length) {
                    tempC = grid[0].length - grid[0].length;
                }

                // We do not want to increment numAliveNeighbors if we are looking at the cell for which we
                // we are determining the number of alive neighbors, or if the cell state of a neighnbor 
                // is not true
                if ( (r != row || c != col) && (grid[tempR][tempC] == true) ) {
                    numAliveNeighbors += 1;
                }
            }
        }

        return numAliveNeighbors; // update this line, provided so that code compiles
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {

        // WRITE YOUR CODE HERE
        boolean[][] newGrid = new boolean[grid.length][grid[0].length];

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                int numAliveNeighbors = numOfAliveNeighbors(r, c);
                boolean cellState = getCellState(r, c);

                if (cellState == true && numAliveNeighbors <= 1) { // Rule 1
                    newGrid[r][c] = false;
                } else if (cellState == false && numAliveNeighbors == 3) { // Rule 2
                    newGrid[r][c] = true;
                } else if (cellState == true && numAliveNeighbors <= 3) { // Rule 3
                    newGrid[r][c] = true;
                } else if (cellState == true && numAliveNeighbors >= 4) { // Rule 4
                    newGrid[r][c] = false;
                }
            }
        }

        return newGrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {

        // WRITE YOUR CODE HERE
        grid = computeNewGrid();
        totalAliveCells = numAliveCells();

    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        // WRITE YOUR CODE HERE
        for (int i = 0; i < n; i++) {
            grid = computeNewGrid();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {

        // WRITE YOUR CODE HERE
        WeightedQuickUnionUF communities = new WeightedQuickUnionUF(grid.length, grid[0].length);

        // In this quadruple nested for loop, we are looping through the grid and creating a union between
        // alive cells and their alive neighbors. This is done by checking to see if a cell at grid[row][col]
        // is alive, and if it is, then we use some of the code from numAliveNeighbors() method to find the alive neighbors
        // of the given cell, which we then union with the given cell.
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                // We only want to look for alive neighbors of alive cells to union
                if (grid[row][col] == true) {
                    for (int r = row - 1; r <= row + 1; r++) {
                        int tempR = r; // tempR helps circle to the other side of the grid when looking for neighbors
                        if (r == -1) {
                            tempR = grid.length - 1;
                        } else if (r == grid.length) {
                            tempR = grid.length - grid.length;
                        }
        
                        for (int c = col - 1; c <= col + 1; c++) {
                            int tempC = c; // tempC helps circle to the other side of the grid when looking for neighbors
                            if (c == -1) {
                                tempC = grid[0].length - 1;
                            } else if (c == grid[0].length) {
                                tempC = grid[0].length - grid[0].length;
                            }
        
                            // We only want to union an alive cell with its alive neighbors. Therefore, we only perform union
                            // if we are not looking at the cell for which we are currently examining possible alive neighbors
                            // and if the cell state of a neighbor is true.
                            if ( (r != row || c != col) && (grid[tempR][tempC] == true) ) {
                                communities.union(row, col, tempR, tempC);
                            }
                        }
                    }
                }
            }
        }

        // In this nested for loop, we are getting the roots of alive cells and placing them in an
        // arrayOfRoots
        int[] arrayOfRoots = new int[numAliveCells()];
        int arrayIndex = -1;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == true) {
                    arrayIndex++;
                    arrayOfRoots[arrayIndex] = communities.find(row, col);
                }
            }
        }

        // In this nested for loop, we are looping through our arrayOfRoots and eliminating any
        // duplicates of the root at arrayOfRoots[i]
        for (int i = 0; i < arrayOfRoots.length; i++) {
            if (arrayOfRoots[i] == -1) {
                continue;
            }
            for (int j = i + 1; j < arrayOfRoots.length; j++) {
                if (arrayOfRoots[i] == arrayOfRoots[j]) {
                    arrayOfRoots[j] = -1;
                }
            }
        }

        // In this for loop, now that the duplicates are gone, we are counting the number of roots
        // in our array, which should only be distinct roots where each distinct root
        // represents a different community on the grid
        int numberOfCommunities = 0;
        for (int i = 0; i < arrayOfRoots.length; i++) {
            if (arrayOfRoots[i] != -1) {
                numberOfCommunities += 1;
            }
        }

        return numberOfCommunities; // update this line, provided so that code compiles
    }

    // Private method to find the number of alive cells on the grid
    private int numAliveCells() {
        int numberAliveCells = 0;

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] == true) {
                    numberAliveCells += 1;
                }
            }
        }

        return numberAliveCells;
    }
}
