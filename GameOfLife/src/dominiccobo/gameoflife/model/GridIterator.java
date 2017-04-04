package dominiccobo.gameoflife.model;

import java.util.*;

/**
 *  <p>
 *      Class creates an iterator that creates a timer allowing an instance of the
 *      Grid model to be refreshed at a certain frequency, whilst also providing
 *      options such as saving each different state of the grid at each iteration
 *      and tracking the number of iterations performed.
 *  </p>
 *
 *  @author Dominic Cobo
 *  @since March 2017
 */
public class GridIterator extends Observable {

    /** <p>
     *     Constant detailing the integer state corresponding to the iteration being in
     *     the stopped state.
     * </p>
     * <p>
     *     In the stopped state the program when resumed will start afresh from a new grid
     *     and from zero iterations.
     * </p>
     */
    public static final int         PROGRAM_STOPPED             = 0;

    /**
     * Constant detailing the integer state corresponding to the iteration being in the
     * running / execution state.
     */
    public static final int         PROGRAM_RUNNING             = 1;

    /**
     * Constant detailing the minimum delay possible between iterations in milliseconds.
     */
    public static final long        MINIMUM_FREQUENCY           = 150;

    /**
     * Constant detailing the maximum delay possible between iterations in milliseconds.
     */
    public static final long        MAXIMUM_FREQUENCY           = 1500;

    /**
     * Instance variable detailing the integer state corresponding to the current program
     * state that is whether the program is stopped, running or paused.
     */
    private int                     iteratorState               = PROGRAM_STOPPED;

    /**
     * Instance variable detailing the long integer count of iterations elapsed during the
     * runtime of the iterator.
     */
    private long                    iteratorCount               = 0;

    /**
     * Instance variable detailing the long integer frequency in milliseconds between
     * iterations.
     */
    private long                    iteratorFrequency           = 1000;

    /**
     * Instance variable detailing the boolean toggle state of iteration saving. When
     * enabled each instance mutation of the Grid will be saved to an array list so
     * it can be retrieved and saved as either an external file.
     */
    private boolean                 iteratorSave                = false;

    /**
     * Instance variable ArrayList containing previous iterations of the game of life, that
     * is the different mutations of the grid at different stages. Items will only be saved
     * to here if saving is enabled.
     */
    private ArrayList<String>         previousIterations          = new ArrayList<>();

    /**
     * Instance variable referencing a Timer. Used to queue timer tasks which will run the
     * algorithm required for updating the grid at a specified interval.
     */
    private Timer                   timer;

    /**
     * Instance variable
     */
    private TimerTask               timerTask;

    /**
     * Instance variable detailing the currentGrid being operated on by the GridIterator,
     * that is the current grid being displayed in the view.
     */
    private Grid                    currentGrid                 = new Grid(Grid.GRID_MINIMUM_SPAN, Grid.GRID_TYPE_UNSHADED);

    /**
     * Default empty constructor for the GridIterator model.
     */
    public GridIterator() {}

    /**
     * <p>
     * Initialization code for the timer.
     * </p>
     */
    public void run() {

        // set the program state
        this.iteratorState = PROGRAM_RUNNING;

        // if the timer is created, then cancel it.
        if(this.timer != null) {
            this.timer.purge();
            this.timer.cancel();
        }
        this.timer = new Timer();

        // if the task is created, then cancel it.
        if(this.timerTask != null) {
            this.timerTask.cancel();
        }

        this.timerTask = new TimerTask() {
            @Override
            public void run() {
                iterate();
            }
        };
        // schedule the created task at the specified rate.
        this.timer.scheduleAtFixedRate(timerTask, 0, iteratorFrequency);

    }

    /**
     * Method terminating the running of the iterator.
     */
    public void stop() {

        // prevent the program from being attempted to stop if it's not running.
        if(iteratorState == PROGRAM_RUNNING) {
            this.iteratorState = PROGRAM_STOPPED;
            this.timer.cancel();
            this.timer.purge();
            this.setChanged();
            this.notifyObservers();
        }
    }

    /**
     * Algorithm to apply each time an iteration is performed. That is applying
     * the logic from the Grid model and performing the necessary mutation.
     */
    private void iterate() {

        if(iteratorSave) {
            previousIterations.add(Arrays.deepToString(currentGrid.getCurrentShaded()));
        }
        currentGrid.getMutation();

        this.iteratorCount++; // increase iteration count.

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Getter method for the Iterator count.
     * @return A reference to the variable containing the iterations elapsed count.
     */
    public long getIteratorCount() {
        return iteratorCount;
    }

    /**
     * Getter method for the current iterator state, stating the program state.
     * @return A reference to the iteratorState object.
     */
    public int getState() {
        return this.iteratorState;
    }

    /**
     * Getter method for currentGrid.
     * @return A reference to the current Grid object.
     */
    public Grid getCurrentGrid() {
        return this.currentGrid;
    }

    /**
     * Setter method for iteratorFrequency.
     * @param frequency The frequency separation in milliseconds.
     */
    public void setIteratorFrequency(int frequency) {
        this.iteratorFrequency = frequency;
        // if running, update the timer with the new frequency.
        if(this.iteratorState == PROGRAM_RUNNING) {
            this.run();
        }
    }

    /**
     * Getter method for previousIterations ArrayList containing previous iterations.
     * @return Returns a reference to the previousIterations object.
     */
    public ArrayList<String> getPreviousIterations() {
        return this.previousIterations;
    }


    /**
     * <p>
     * Setter method for iteratorSave. Toggles saving to previous iterations array list.
     * </p>
     * @param savingState Boolean flag for enabling or disabling.
     */
    public void enableSaving(boolean savingState) {
        this.iteratorSave = savingState;
    }
}
