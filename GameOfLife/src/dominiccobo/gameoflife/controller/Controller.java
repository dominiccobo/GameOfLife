package dominiccobo.gameoflife.controller;

import dominiccobo.gameoflife.model.Grid;
import dominiccobo.gameoflife.model.GridIterator;
import dominiccobo.gameoflife.model.Save;
import dominiccobo.gameoflife.view.AboutView;
import dominiccobo.gameoflife.view.MainView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

/**
 * <p>
 * Controller class serving as communication between the view and model by
 * implementing the ActionListener, ChangeListener and Observer interfaces.
 * </p>
 * <p>
 * The view is passed to the controller and an instance of the model is created
 * within the controller, although ideally it should be created externally and
 * passed just as the view is.
 * </p>
 * <p>
 * The view simply consists of Swing components and configurations to tie the
 * necessary elements to the action listener, which is implemented by the this
 * class.
 * </p>
 * <p>
 * The model extends the observable class and is observed for any changes by this
 * class, allowing any necessary updates to be made to the GUI.
 * </p>
 * <p>
 *  It could be considered that this is a loose implementation of the extension of the
 *  MVC slash MVP application architecture.
 * </p>
 *  @author Dominic Cobo
 *  @since March 2017
 */
public class Controller implements ActionListener, ChangeListener, Observer {

    /**
     * Instance variable detailing the model MainView object instance to which
     * the Controller will be applying itself to.
     */
    private MainView view;

    /**
     * Instance variable detailing the GridIterator object instance to
     */
    private GridIterator myIterator;

    /**
     * <p>
     *     Constructor for the controller class, initializing the GridIterator and
     *     linking up the passed MainView reference.
     * </p>
     * @param view MainView instance reference to be passed to Controller class for use.
     */
    public Controller(MainView view) {
        this.view = view;

        this.myIterator = new GridIterator();
        this.myIterator.addObserver(this);
    }

    public void init() {
        this.view.getSldrGridSize().setValue(Grid.GRID_MINIMUM_SPAN);
        this.view.getSldrGridSize().setMinimum(Grid.GRID_MINIMUM_SPAN);
        this.view.getSldrGridSize().setMaximum(Grid.GRID_MAXIMUM_SPAN);

        this.view.getSldrIterationSpeed().setValue((int) GridIterator.MAXIMUM_FREQUENCY);
        this.view.getSldrIterationSpeed().setMinimum((int) GridIterator.MINIMUM_FREQUENCY);
        this.view.getSldrIterationSpeed().setMaximum((int) GridIterator.MAXIMUM_FREQUENCY);
    }

    /**
     * <p>
     *     Action handling method triggered by the SaveAs option being selected from
     *     the File Menu. Initiates the save as instruction sequence..
     * </p>
     */
    private void handleSaveAs() {

        JFileChooser saveAsDialog = new JFileChooser();
        saveAsDialog.setDialogTitle("");

        int userSelection = saveAsDialog.showSaveDialog(view.getFrame());
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = saveAsDialog.getSelectedFile();
            Save saver = new Save(this.myIterator);
            saver.setFile(fileToSave.getAbsolutePath());
            saver.write();
        }
    }

    /**
     *<p>
     *     Action handling method triggered by the Clear All option being selected
     *     from the Edit Menu. Resets all grid cells to dead and clears the saved
     *     content.
     *</p>
     */
    private void handleClearAll() {

        int value = this.myIterator.getCurrentGrid().getGridDimensions();
        this.view.updateGridSize(value);
        int[][] clearArray = new int[value][value];
        for(int i = 0; i < clearArray.length; i++) {
            Arrays.fill(clearArray[i], (byte) Grid.STATE_DEAD);
        }
        this.myIterator.getCurrentGrid().setGridState(clearArray);
        this.myIterator.getPreviousIterations().clear();
    }

    /**
     * <p>
     *     Action handling method triggered by the About option being selected
     *     from the Help Menu. Shows the same information as showed in the
     *     splash screen.
     * </p>
     */
    private void handleAbout() {
        new AboutView();
    }

    /**
     * <p>
     *     Action handling method triggered by clicking a cell to enable
     *     disable it. Clicking reverses current state.
     * </p>
     * @param e Action Event to be passed to cell handler for use.
     */
    private void handleCell(ActionEvent e) {

        for(int i = 0; i < this.view.getGrids().length; i++) {
            for(int j = 0; j < this.view.getGrids().length; j++) {

                if(this.view.getGrids()[i][j] == e.getSource()) {

                    this.myIterator.getCurrentGrid().toggleState(i, j);

                    if(this.myIterator.getCurrentGrid().isCellAlive(i, j)) {
                        this.view.getGrids()[i][j].setBackground(this.myIterator.getCurrentGrid().getCellColor(i, j, this.view.getColors("color_cell_alive")));
                    }
                    else {
                        view.getGrids()[i][j].setBackground(this.myIterator.getCurrentGrid().getCellColor(i, j, this.view.getColors("color_cell_dead")));
                    }
                    break;
                }
            }
        }
    }

    /**
     * <p>
     *     Action handling method triggered by clicking the start
     *     button. Calls the run instruction from the instantiated iterator
     *     and disables needed fields temporarily to avoid program errors.
     * </p>
     */
    private void handleStartBtn() {
        this.myIterator.run();
        this.view.getBtnStart().setEnabled(false);
        this.view.getBtnStop().setEnabled(true);
        this.view.getChkCellShading().setEnabled(false);
        this.view.getChkIterationSaving().setEnabled(false);
        this.view.getSldrGridSize().setEnabled(false);
    }

    /**
     * <p>
     *     Action handling method triggered by clicking the Stop
     *     button. Calls the the stop instruction from the instantiated
     *     iterator and re-enables disabled fields for usage again.
     * </p>
     */
    private void handleStopBtn() {

        this.view.getBtnStart().setEnabled(true);
        this.view.getBtnStop().setEnabled(false);
        this.view.getChkCellShading().setEnabled(true);
        this.view.getChkIterationSaving().setEnabled(true);
        this.view.getSldrGridSize().setEnabled(true);
        this.myIterator.stop();
    }

    /**
     * <p>
     *     Action handling method triggered by selection/deselection of the
     *     iteration saving option. Calls method to update the respective data
     *     in the instantiated iterator.
     * </p>
     */
    private void handleIterationSave() {
        if(this.view.getChkIterationSaving().isSelected()) {
            this.myIterator.enableSaving(true);

        }
        else {
            this.myIterator.enableSaving(false);
        }
    }

    /**
     * <p>
     *     Action handling method triggered by selection/deselection of the
     *     cell shading option. Calls method to update the respective data
     *     in the instantiated iterator, and resets all selection data from
     *     grid.
     * </p>
     */
    private void handleCellShading() {
        if(this.view.getChkCellShading().isSelected()) {
            this.myIterator.getCurrentGrid().setType(Grid.GRID_TYPE_SHADED);
        }
        else {
            this.myIterator.getCurrentGrid().setType(Grid.GRID_TYPE_UNSHADED);
        }
    }

    /**
     * <p>
     *     Action handling method triggered by moving the grid size slider
     *     option. Calls method to update the instantiated iterator instance
     *     along with the instantiated MainView, resizing the grid to the new
     *     inputted value.
     * </p>
     */
    private void handleGridSize() {
        int value = this.view.getSldrGridSize().getValue();
        this.view.updateGridSize(value);
        this.view.getPbarLevelOfLife().setMinimum(0);
        this.view.getPbarLevelOfLife().setMaximum(value*value);
        int[][] clearArray = new int[value][value];
        for(int i = 0; i < clearArray.length; i++) {
            Arrays.fill(clearArray[i], Grid.STATE_DEAD);
        }
        this.myIterator.getCurrentGrid().setGridState(clearArray);
        this.myIterator.getPreviousIterations().clear();
    }

    /**
     * <p>
     *      Action handling method triggered by moving the iteration speed slider
     *      option. Calls method to update the instantiated iterator instance with
     *      the new selected iterator speed.
     * </p>
     */
    private void handleIterationSpeed() {
        int value = this.view.getSldrIterationSpeed().getValue();
        this.myIterator.setIteratorFrequency(value);
    }

    /**
     * <p>
     *     Action handling method triggered by the Observer pattern noticing an update
     *     to data held within the GridIterator instance, then appropriately notifies the
     *     view of said change, updating all view contents.
     * </p>
     */
    private void updateView() {

        int aliveCount = 0;
        int deadCount = 0;

        for(int i = 0; i < this.view.getGrids().length; i++) {
            for(int j = 0; j < this.view.getGrids().length; j++) {

                if(this.myIterator.getCurrentGrid().isCellAlive(i, j)) {
                    aliveCount++;
                    this.view.getGrids()[i][j].setBackground(this.myIterator.getCurrentGrid().getCellColor(i, j, this.view.getColors("color_cell_alive")));
                }
                else {
                    deadCount++;
                    this.view.getGrids()[i][j].setBackground(this.myIterator.getCurrentGrid().getCellColor(i, j, this.view.getColors("color_cell_dead")));
                }
            }
        }
        this.view.getLblIterationCount().setText(Long.toString(myIterator.getIteratorCount()));
        this.view.getLblCellsAliveCount().setText(Integer.toString(aliveCount));
        this.view.getLblCellsDeadCount().setText(Integer.toString(deadCount));
        this.view.getPbarLevelOfLife().setValue(aliveCount);

        switch(this.myIterator.getState()) {
            case GridIterator.PROGRAM_STOPPED: {
                this.view.getLblProgramStatusState().setText(this.view.getLanguageString("status_stopped"));
                break;
            }
            case GridIterator.PROGRAM_RUNNING: {
                this.view.getLblProgramStatusState().setText(this.view.getLanguageString("status_running"));
                break;
            }
        }
    }

    /**
     * <p>
     *     Method from implemented Observer model. Allows listening to any class that
     *     extends the Observable class.
     * </p>
     * @param o The element to be observed.
     * @param arg Object which has been changed, not used.
     */
    @Override
    public void update(Observable o, Object arg) {
        updateView(); // on noticed changed update all view data.
    }

    /**
     * <p>
     *     Method from implemented ChangeListener model. Allows listening for a change
     *     for any object that implements a changeListener.
     * </p>
     * @param e The event triggered by the changed state.
     */
    @Override
    public void stateChanged(ChangeEvent e) {

        // grid size slider
        if(e.getSource() == this.view.getSldrGridSize()) {
            this.handleGridSize();
        }

        // iterations speed slider
        if(e.getSource() == this.view.getSldrIterationSpeed()) {
            this.handleIterationSpeed();
        }
    }

    /**
     * <p>
     *     Method from implemented ActionListener model. Allows listening for a change
     *     for any object that implements an ActionListener.
     * </p>
     * @param e The event triggered by the action performed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        switch(e.getActionCommand()) {

            // menu options
            case "menuOptExit": {
                System.exit(0);
                break;
            }

            case "menuOptSaveAs": {
                this.handleSaveAs();
                break;
            }

            case "menuOptClear": {
                this.handleClearAll();
                break;
            }
            case "menuOptAbout": {
                this.handleAbout();
                break;
            }

            case "cell": {
                this.handleCell(e);
                break;
            }

            case "btnStart": {
                this.handleStartBtn();
                break;
            }
            case "btnStop": {
                this.handleStopBtn();
                break;
            }

            case "chkIterationSaving": {
                this.handleIterationSave();
                break;
            }

            case "chkCellShading": {
                this.handleCellShading();
                break;
            }

            default: {
                System.err.println("Undefined listener action");
                break;
            }
        }
    }
}
