package dominiccobo.gameoflife.model;

import java.awt.*;
import java.util.Arrays;

/**
 *  <p>
 *  Models a single instance of a grid of defined dimensions n by n with defined
 *  states according to John Conway's rules for his Game of Life.
 *  </p>
 *  <p>
 *  Model contains methods to obtain the next instance of life and update itself
 *  based on John Conway's rules. This is known as an iteration.
 *  </p>
 *
 *  @author Dominic Cobo
 *  @since March 2017
 */
public class Grid {

    /**
     * Constant int value association for a dead cell, for a grid with type definition
     * GRID_TYPE_SHADED.
     */
    public static final int STATE_DEAD = -1;

    /**
     * Constant int value association for an alive cell, for a grid with type definition
     * GRID_TYPE_SHADED.
     */
    public static final int STATE_ALIVE = 1;

    /**
     * Constant detailing the needed number of adjacent alive cells surrounding a defined cell needed for
     * said cell to stay in the alive state, or if dead, turn to the defined alive state.
     */
    public static final int NEEDED_FOR_BIRTH = 3;

    /**
     * Constant detailing the number of adjacent alive cells surrounding a defined cell needed for said
     * cell to remain in the alive state.
     */
    public static final int NEEDED_TO_SURVIVE = 2;

    /**
     * Constant used to define a value for reference as to the grid being created needing to consist
     * of a single color shade for each cell state. That is the cell is either displayed as dead or
     * alive.
     */
    public static final int GRID_TYPE_UNSHADED = 0;

    /**
     * Constant used to define a value for reference as to the grid being created needing to consist
     * of a degree of color shades for each cell state. That is the cell color shading will change
     * depending on the time said cell has spent in the state.
     */
    public static final int GRID_TYPE_SHADED = 1;

    /**
     * Constant used to define the minimum acceptable numeric integer value for the n by n span of
     * any modelled Grid.
     */
    public static final int GRID_MINIMUM_SPAN = 20;


    /**
     * Constant used to define the maximum acceptable numeric integer value for the n by n span of
     * any modelled Grid.
     */
    public static final int GRID_MAXIMUM_SPAN = 100;

    /**
     * Instance variable defining a null grid which contains the state of each cell on a time ALIVE / DEAD
     * numeric basis which can then be used to establish degrees of the age of life.
     */
    private int[][] currentShaded;


    /**
     * Instance variable detailing the current type of grid cell type being used. That is whether the
     * the grid is abiding by the GRID_TYPE_UNSHADED configuration or GRID_TYPE_SHADED configuration.
     */
    private int gridCellType;

    /**
     * Instance variable detailing the n by n dimensions of a square grid.
     */
    private int gridDimensions;

    /**
     * <p>
     * Constructor to create a fresh unpopulated grid model instance specifying
     * the span, that is the n by n dimension and the type of cells in the grid,
     * that is  whether there are simply two colours, or color shades vary
     * according to life time of said cell in said state.
     * </p>
     *
     * @param gridSpan     The n dimension of the square Grid to be modelled.
     * @param gridCellType <p>Cell shading option. See GRID_TYPE_SHADED and
     *                     GRID_TYPE_UNSHADED.</p>
     */
    public Grid(int gridSpan, int gridCellType) {

        this.gridDimensions = gridSpan;
        this.gridCellType = gridCellType;

        this.currentShaded = new int[gridSpan][gridSpan];
        for(int i = 0; i < gridSpan; i++) {
            Arrays.fill(this.currentShaded[i], (byte) Grid.STATE_DEAD);
        }
    }

    /**
     * <p>
     * Update the status of the cells in a Grid instance conforming with the GRID_TYPE_SHADED
     * definition, that is cells are either dead or alive with a varying degree of 'aliveness'
     * or 'deadness' according to the time spent in either state.
     * </p>
     *
     * @param stateArray <p>The array of boolean states to instantiate the currentUnshaded
     *                   array with. For cleaner understanding the static constants STATE_DEAD_UNSHADED
     *                   and STATE_ALIVE_UNSHADED are available for use.</p>
     */
    public void setGridState(int[][] stateArray) {

        this.gridDimensions = stateArray.length;
        this.currentShaded = Arrays.copyOf(stateArray, this.gridDimensions);
    }


    /**
     * <p>
     * Updates the currentGrid state to a new mutation based on John Conway's rules for the game of life.
     * </p>
     * <p>
     * Works by cycling through a current Grid's state, and determining the number of alive cells surrounding
     * each cell. It then applies the following logic:
     * </p>
     * <ul>
     *     <li>
     *         If a cell alive count abides by the constraints of not being able to stay alive, its state is set
     *         to dead.
     *     </li>
     *     <li>
     *         If a cell alive count abides by the constraints of being able to be born and it is dead, then its state
     *         is set to alive.
     *     </li>
     *     <li>
     *         Otherwise by process of logical elimination a cell is able to stay alive, so it's previous state is
     *         simply copied to the new mutation.
     *     </li>
     * </ul>
     */
    public void getMutation() {

        int aliveCount = 0;

        int[][] next = new int[gridDimensions][gridDimensions];

        /*
            For the span of the defined grid, we check each cell's adjacent alive cells and later
            apply John Conway's rules for life state changing
         */
        for (int rowIdx = 0; rowIdx < this.gridDimensions; rowIdx++) {
            for (int colIdx = 0; colIdx < this.gridDimensions; colIdx++) {

                // obtain and store the adjacent alive cell count to the current cell.
                aliveCount = getAdjacentAlive(rowIdx, colIdx);

                // if life is not sustainable based on the surrounding alive cells.
                if (!isLifeSustainable(aliveCount) && isCellAlive(rowIdx, colIdx)) {
                    next[rowIdx][colIdx] = STATE_DEAD;
                }
                // if birth  is possible based on the number of surrounding alive cells
                else if(isBirthPossible(aliveCount) && !isCellAlive(rowIdx, colIdx)) {
                    next[rowIdx][colIdx] = STATE_ALIVE;
                }
                // otherwise, if life is sustainable, we'll copy the old value of the cell
                else {
                    next[rowIdx][colIdx] = this.currentShaded[rowIdx][colIdx];
                    if(next[rowIdx][colIdx] > 0) next[rowIdx][colIdx]++;
                    else if(next[rowIdx][colIdx] < 0) next[rowIdx][colIdx]--;
                }
            }
        }
        // copy the mutation from the temporary next array to the instance variable.
        this.currentShaded = Arrays.copyOf(next, gridDimensions);
    }

    /**
     * <p>
     * Applying John Conway's logical principle for checking whether a cell in the dead state
     * can change its state to alive based on the number of alive immediate cells surrounding it.
     * </p>
     *
     * @param aliveCount The surrounding adjacent alive cells of the cell being checked.
     * @return A boolean evaluation of the possibility of the cell matching the criteria to 'revive'.
     */
    private static boolean isBirthPossible(int aliveCount) {
        return aliveCount == NEEDED_FOR_BIRTH;
    }

    /**
     * <p>
     * Applying John Conway's logical principles for checking whether a cell's adjacent surrounding
     * alive count permits a cell to stay alive.
     * </p>
     *
     * @param aliveCount The surrounding adjacent alive cells of the cell being checked.
     * @return A boolean evaluation of the possibility of the cell matching the criteria to remain the alive state.
     */
    private static boolean isLifeSustainable(int aliveCount) {
        return (aliveCount == NEEDED_FOR_BIRTH || aliveCount == NEEDED_TO_SURVIVE);
    }


    /**
     * <p>
     * Updates the count of adjacent alive cells for a specified centralized cell.
     * </p>
     * <p>
     * To remedy the issue of the grid not having infinite dimensions the index
     * coordinates are wrapped in order to produce a simulted infinitely large
     * </p>
     *
     * @param row <p>The row index, that is the first dimension index, of the cell to apply
     *            the algorithm to.</p>
     * @param col <p>The column index, that is the second dimension index, of the cell to apply
     *            the algorithm to.</p>
     * @return The count of adjacent alive cells obtained for the inputted coordinate.
     */
    private int getAdjacentAlive(int row, int col) {

        int aliveCount = 0; // stores the count of adjacent alive cells around a the inputted cell indexes.

        /*
            Using the mathematical principles of translation to determine the offset of the surrounding cells using
            relative coordinates.

            We use a series of if statements as this is minimally faster than performing a for-loop for the same
            purpose,  which would require a further unnecessary check. In this program speed is preferable to
            space, in particular when large grids are being updated.
        */

        // Cells in the first row above the central cell being checked.
        if (isCellAlive(normalizeCoordinate(row - 1), normalizeCoordinate(col - 1)))
            aliveCount++;
        if (isCellAlive(normalizeCoordinate(row - 1), normalizeCoordinate(col)))
            aliveCount++;
        if (isCellAlive(normalizeCoordinate(row - 1), normalizeCoordinate(col +1)))
            aliveCount++;

        // Cells in the same row as the central cell being checked.
        if (isCellAlive(normalizeCoordinate(row), normalizeCoordinate(col - 1)))
            aliveCount++;
        if (isCellAlive(normalizeCoordinate(row), normalizeCoordinate(col + 1)))
            aliveCount++;

        // Cells in the row after the central cell being checked.
        if (isCellAlive(normalizeCoordinate(row + 1), normalizeCoordinate(col - 1)))
            aliveCount++;
        if (isCellAlive(normalizeCoordinate(row + 1), normalizeCoordinate(col)))
            aliveCount++;
        if (isCellAlive(normalizeCoordinate(row + 1), normalizeCoordinate(col + 1)))
            aliveCount++;

        // return the generated count.
        return aliveCount;
    }

    /**
     * <p>
     * Summarized; method ensures that indexes inputted are converted into acceptable
     * indexes that adhere to the bounds of the defined array.
     * </p>
     * <p>
     * Although mathematically coordinates can be negative, it is necessary to consider
     * we are not plotting the coordinates relative to a standard x,y axis, but rather
     * plotting index "coordinates" for an a standard array.
     * </p>
     * <p>
     * This defines a permissible value range of 0 to the array (dimension - 1).
     * Therefore to remedy the issue of out of bound values, we "wrap" our values by
     * using complementary maths with the dimension of the array.
     * </p>
     * <p>
     * This means that for example having an index coordinate of [-1, 3] in an array of
     * dimensions 4 x 4, the wrapped value would be [(4 + -1), 3] resulting in a the
     * "wrapped" coordinate [3, 3].
     * </p>
     * <p>
     * However if the index "coordinate" is within the acceptable bounds, it is simply
     * left alone and returned by the method.
     * </p>
     *
     * @param inputVal The numeric coordinate value to normalize by wrapping.
     * @return If inputVal is out of bounds, its normalized corresponding value, else inputVal.
     */
    private int normalizeCoordinate(int inputVal) {
        if ((inputVal < 0)) return (this.gridDimensions + inputVal);
        else if (inputVal >= this.gridDimensions) return (this.gridDimensions - inputVal);
        else return (inputVal);
    }

    /**
     * <p>
     * Toggles the state of a cell depending on the grid cell type in use
     * by the instance.
     * </p>
     *
     * @param rowIdx The row (1d) index of the cell to apply the toggle to.
     * @param colIdx The column (2d) index of the cell to apply the toggle to.
     */
    public void toggleState(int rowIdx, int colIdx) {

        if(this.isCellAlive(rowIdx, colIdx)) {
            this.currentShaded[rowIdx][colIdx] = STATE_DEAD;
        }
        else this.currentShaded[rowIdx][colIdx] = STATE_ALIVE;
    }

    /**
     * <p>
     * Returns the boolean alive / dead cell status of a specific cell based
     * on the grid cell type in use by the instance.
     * </p>
     *
     * @param rowIdx The row (1d) index of the cell to retrieve the status for.
     * @param colIdx The column (2d) index of the cell to retrieve the status for.
     * @return Boolean evaluation of whether a cell is alive or dead.
     */

    public boolean isCellAlive(int rowIdx, int colIdx) {
        return this.currentShaded[rowIdx][colIdx] > 0;
    }

    /**
     * <p>
     * Returns the integer n by n grid dimensions for the current grid.
     * </p>
     * @return The integer value of the grid dimension.
     */
    public int getGridDimensions() {
        return this.gridDimensions;
    }

    /**
     * <p>
     * Allows the specification of the grid cell type that will determine whether
     * shade of the cells vary on alive time or not.
     * </p>
     * <ul>
     *     <li>Specify <b>GRID_TYPE_UNSHADED</b> for no shade variance depending on life time.</li>
     *     <li>Specify <b>GRID_TYPE_SHADED</b> for a shade variance depending on life time.</li>
     * </ul>
     * @param type The grid type value to set the grid to.
     */
    public void setType(int type) {
        this.gridCellType = type;
    }

    public int[][] getCurrentShaded() {
        return this.currentShaded;
    }

    /**
     * <p>
     * Method generating the cell color based on the alive / death time a cell
     * has been in.
     * </p>
     * @param rowIdx Array row index of the cell to generate the color for.
     * @param colIdx Array column index of the cell to generate the color for.
     * @param color Base color to base the generation on.
     * @return Generated color from the parameters.
     */
    public Color getCellColor(int rowIdx, int colIdx, Color color) {

        int modifyBy = Math.abs(this.currentShaded[rowIdx][colIdx]);

        if(modifyBy < 80) modifyBy = modifyBy+80;
        if(modifyBy > 250)  modifyBy = 250;

        Color myColor = color;

        if(this.gridCellType == GRID_TYPE_SHADED) {

            if(this.currentShaded[rowIdx][colIdx] > 0) {
                myColor = new Color(0, modifyBy,  0);
            }
            if(this.currentShaded[rowIdx][colIdx] < 0) {
                myColor = new Color(modifyBy, 0, 0);
            }
        }
        else if(this.gridCellType == GRID_TYPE_UNSHADED){
            myColor = color;
        }

        return myColor;
    }
}


