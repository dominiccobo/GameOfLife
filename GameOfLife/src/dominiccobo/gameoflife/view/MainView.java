package dominiccobo.gameoflife.view;

import dominiccobo.gameoflife.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *  <p>
 *  Model representing the construction of the main Graphical User
 *  interface for the application.
 *  </p>
 *  <p>
 *  Displays a BorderLayout formed frame, consisting of the custom dynamically
 *  initialized grid, along with status labels, menu bars and option buttons.
 *  </p>
 *  @author Dominic Cobo
 *  @since March 2017
 */
public class MainView {

    // main holder items.
    private JFrame mainFrame;

    // menuBar Items.
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenu menuEdit;
    private JMenu menuHelp;
    private JMenuItem menuOptExit;
    private JMenuItem menuOptSaveAs;
    private JMenuItem menuOptClear;
    private JMenuItem menuOptAbout;

    // Options Panel Items.
    private JPanel optionsPanel;
    private JPanel optionsPanelTitleHolder;
    private JLabel lbloptionsTitle;

    private JButton btnStart;
    private JButton btnStop;
    private JLabel lblGridSize;
    private JLabel lblIterationSpeed;
    private JLabel lblIterationSaving;
    private JLabel lblCellShading;
    private JSlider sldrGridSize;
    private JSlider sldrIterationSpeed;
    private JCheckBox chkIterationSaving;
    private JCheckBox chkCellShading;

    // Status Panel Items
    private JPanel statusPanel;
    private JLabel lblIterations;
    private JLabel lblIterationCount;
    private JLabel lblCellsAlive;
    private JLabel lblCellsAliveCount;
    private JLabel lblCellsDead;
    private JLabel lblCellsDeadCount;
    private JLabel lblProgramStatus;
    private JLabel lblProgramStatusState;
    private JLabel lblLevelOfLife;
    private JProgressBar pbarLevelOfLife;

    // Grid Panel items.
    private JPanel gridPanel;
    private JPanel grid;
    private JButton[][] grids;

    // other resources
    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 600;
    //private Font FONT_HEADER = null;
    //private Font FONT_LABEL = null;

    private ResourceBundle language;
    private ResourceBundle clr_palette;

    private final Controller controller = new Controller(this);

    // GridBagConstraint object -> used for assigning items to a GridBag style layout.
    private GridBagConstraints gbc = new GridBagConstraints();

    public MainView() {
        initGui();
    }

    /**
     * Invoke creation of GUI elements and running in a different thread.
     */
    private void initGui() {
        // invoke runtime for GUI in different program thread
        SwingUtilities.invokeLater(() -> {
            // Compose the GUI.
            this.loadResources();
            this.createMainFrame();
            this.createMenuBar();
            this.createStatusPane();
            this.createGridPane();
            this.createOptionPane();
            this.execResources();
            this.showAll();
            // call the Controller initialization GUI which sets up some items as per the defined models
            controller.init();
        });
    }

    /**
     * Instantiate Main Frame and establish config.
     */
    private void createMainFrame() {

        // Standardize the layout to use Java's generic L&F -> avoids issues with formatting with other OS
        try {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException $e) {
            System.err.println($e.getMessage());
        }

        // create the main frame.
        this.mainFrame = new JFrame();
        this.mainFrame.setLayout(new BorderLayout());
        this.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // when closed, termiante program.
        this.mainFrame.setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // prevent the screen from resizing too small
        this.mainFrame.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.mainFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.mainFrame.setLocationRelativeTo(null); // center the frame
        this.mainFrame.setResizable(false);
    }

    /**
     * Instantiate the menu bar, and assign it to the main frame.
     */
    private void createMenuBar() {
        this.menuBar = new JMenuBar();

        this.menuFile = new JMenu();
        this.menuEdit = new JMenu();
        this.menuHelp = new JMenu();

        this.menuOptExit = new JMenuItem();
        this.menuOptExit.setActionCommand("menuOptExit");
        this.menuOptExit.addActionListener(this.controller);

        this.menuOptSaveAs = new JMenuItem();
        this.menuOptSaveAs.setActionCommand("menuOptSaveAs");
        this.menuOptSaveAs.addActionListener(this.controller);

        this.menuOptClear = new JMenuItem();
        this.menuOptClear.setActionCommand("menuOptClear");
        this.menuOptClear.addActionListener(this.controller);

        this.menuOptAbout = new JMenuItem();
        this.menuOptAbout.setActionCommand("menuOptAbout");
        this.menuOptAbout.addActionListener(this.controller);

        /*
            MenuBar

            File
              >Save As
              >Exit
            Edit
              >Clear All
            Help
               >About
         */

        this.menuBar.add(this.menuFile); // add the File menu to the menu bar
        this.menuBar.add(this.menuEdit); // add the Edit menu to the menu bar
        this.menuBar.add(this.menuHelp); // add the Help menu to the menu bar

        this.menuFile.add(this.menuOptSaveAs, 0); // add the Save As menu option to the File menu
        this.menuFile.add(this.menuOptExit, 1);
        this.menuEdit.add(this.menuOptClear, 0);
        this.menuHelp.add(this.menuOptAbout, 0);

        /// assign the menuBar to the mainFrame
        this.mainFrame.setJMenuBar(this.menuBar);

    }

    /**
     * Instantiate grid pane elements and frame, populate the frame and add to main frame.
     */
    private void createGridPane() {
        this.gridPanel = new JPanel(new GridLayout(1, 1));
        this.gridPanel.setSize(600, 600);
        this.mainFrame.add(this.gridPanel, BorderLayout.CENTER);
    }

    /**
     * Instantiate the actual grid itself by using an array of buttons and a grid layout.
     * @param dimensions dimensions of the square grid to create.
     */
    public void updateGridSize(int dimensions) {
        if(this.grids != null) {
            for(int i = 0; i < this.grids.length; i++) {
                for(int j = 0; j < this.grids.length; j++) {
                    this.grid.remove(this.grids[i][j]);
                    this.grids[i][j] = null;
                }
            }
        }
        if(this.grid != null) this.gridPanel.remove(this.grid);

        this.grids = new JButton[dimensions][dimensions];

        this.grid = new JPanel();
        this.grid.setLayout(new GridLayout(dimensions, dimensions));

        for(int i = 0; i < this.grids.length; i++) {
            for (int j = 0; j < this.grids.length; j++) {
                this.grids[i][j] = new JButton();
                this.grids[i][j].setBackground(getColors("color_cell_dead"));
                this.grids[i][j].setSize(600/20, 600/20);
                this.grids[i][j].setPreferredSize(new Dimension(600/20, 600/20));
                this.grids[i][j].setActionCommand("cell");
                this.grids[i][j].addActionListener(this.controller);
                this.grid.add(grids[i][j]);
            }
        }
        this.grid.updateUI();
        this.gridPanel.updateUI();
        this.gridPanel.add(grid);
    }

    /**
     * Instantiate option pane elements and frame, populate the frame and add to main frame.
     */
    private void createOptionPane() {

        /*
            Instantiate elements.
         */
        // optionsPanel -> container for all elements in the options panel
        this.optionsPanel = new JPanel();
        this.optionsPanel.setLayout(new GridBagLayout());
        this.optionsPanel.setBounds(10, 10, SCREEN_WIDTH/3, SCREEN_HEIGHT);

        // optionsPanelTitleHolder -> container that holds the option pane title label.
        this.optionsPanelTitleHolder = new JPanel();
        this.optionsPanelTitleHolder.setVisible(true);

        // lblOptionsTitle -> label containing the title for the options panel screen.
        this.lbloptionsTitle = new JLabel();
        this.lbloptionsTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));

        // btnStart -> button to start
        this.btnStart = new JButton();
        this.btnStart.setActionCommand("btnStart");
        this.btnStart.addActionListener(this.controller);

        // btnStop -> button to pause / stop the running of the program
        this.btnStop = new JButton();
        this.btnStop.setActionCommand("btnStop");
        this.btnStop.addActionListener(this.controller);

        // lblGridSize -> label describing the slider that sets the grid size.
        this.lblGridSize = new JLabel();
        this.lblGridSize.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        // sldrGridSize -> slider to set the grid size.
        this.sldrGridSize = new JSlider();
        this.sldrGridSize.addChangeListener(this.controller);

        // lblIterationSpeed -> label describing the slider that sets the iteration speed
        this.lblIterationSpeed = new JLabel();
        this.lblIterationSpeed.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        // sldrIterationSpeed -> slider to set the iteration rate for the running of the app
        this.sldrIterationSpeed = new JSlider();
        //sldrIterationSpeed.setInverted(true);
        this.sldrIterationSpeed.addChangeListener(this.controller);

        // lblIterationSaving -> label describing checkbox that disables/enables iteration saving
        this.lblIterationSaving = new JLabel();
        this.lblIterationSaving.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        // chkIterationSaving -> checkbox toggling the saving of iterations...
        this.chkIterationSaving = new JCheckBox();
        this.chkIterationSaving.setActionCommand("chkIterationSaving");
        this.chkIterationSaving.addActionListener(this.controller);

        // lblCellShading -> label describing checkbox toggles different cell shading depending on cell life/death period.
        this.lblCellShading = new JLabel();
        this.lblCellShading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        // chkCellShading -> checkbox toggling different cell shading depending on the cell life/death period.
        this.chkCellShading = new JCheckBox();
        this.chkCellShading.setActionCommand("chkCellShading");
        this.chkCellShading.addActionListener(this.controller);

        /*
         * Add elements to the screen.
         */
        this.mainFrame.add(optionsPanel, BorderLayout.LINE_END); // add options panel to the main frame.
        this.optionsPanelTitleHolder.add(lbloptionsTitle); // add the title label to the title holder container.

        // optionsPanelTitleHolder - Layout formatting.
        this.gbc.gridx = 0;
        this.gbc.gridy = 0;
        this.gbc.gridwidth = 2;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 3;
        this.gbc.weighty = 0;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 0;
        this.gbc.ipady = 0;
        this.gbc.insets = new Insets(0, 0, 0, 0);
        this.optionsPanel.add(this.optionsPanelTitleHolder, this.gbc);

        // btnStart - Layout formatting.
        this.gbc.gridx = 0;
        this.gbc.gridy = 1;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 2;
        this.gbc.weighty = 0;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(0, 0, 20, 0);
        this.optionsPanel.add(this.btnStart, this.gbc);

        // btnStop - Layout formatting.
        this.gbc.gridx = 1;
        this.gbc.gridy = 1;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 2;
        this.gbc.weighty = 0;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(0, 0, 20, 0);
        this.optionsPanel.add(this.btnStop, this.gbc);

        // lblGridSize - Layout formatting.
        this.gbc.gridx = 0;
        this.gbc.gridy = 2;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 0;
        this.gbc.insets = new Insets(0, 20, 0, 0);
        this.optionsPanel.add(this.lblGridSize, this.gbc);

        // sldrGridSize - Layout formatting.
        this.gbc.gridx = 0;
        this.gbc.gridy = 3;
        this.gbc.gridwidth = 2;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = -2;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 0;
        this.gbc.insets = new Insets(0, 20, 0, 20);
        this.optionsPanel.add(this.sldrGridSize, this.gbc);

        // lblIterationSpeed - Layout formatting
        this.gbc.gridx = 0;
        this.gbc.gridy = 4;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = -20;
        this.gbc.insets = new Insets(0, 20, -20, 0);
        this.optionsPanel.add(this.lblIterationSpeed, this.gbc);

        // sldrIterationSpeed - Layout Formatting
        this.gbc.gridx = 0;
        this.gbc.gridy = 5;
        this.gbc.gridwidth = 2;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = -2;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 0;
        this.gbc.insets = new Insets(0, 20, 0, 20);
        this.optionsPanel.add(this.sldrIterationSpeed, this.gbc);

        // lblIterationSaving - Layout formatting.
        this.gbc.gridx = 0;
        this.gbc.gridy = 6;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = -20;
        this.gbc.insets = new Insets(0, 20, 0, 0);
        this.optionsPanel.add(this.lblIterationSaving, this.gbc);

        // chkIterationSaving - Layout formatting.
        this.gbc.gridx = 1;
        this.gbc.gridy = 6;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(0, 0, 0, 0);
        this.optionsPanel.add(this.chkIterationSaving, this.gbc);

        // lblCellShading - Layout formatting.
        this.gbc.gridx = 0;
        this.gbc.gridy = 7;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(0, 20, 0, 0);
        this.optionsPanel.add(this.lblCellShading, this.gbc);

        // chkCellShading - Layout formatting.
        this.gbc.gridx = 1;
        this.gbc.gridy = 7;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(0, 0, 0, 0);
        this.optionsPanel.add(this.chkCellShading, this.gbc);
    }

    /**
     * Instantiate status pane elements and frame, populate the frame and add to main frame.
     */
    private void createStatusPane() {

        /*
            Instantiate elements.
         */
        // statusPanel -> holds all elements in status Pane.
        this.statusPanel = new JPanel();
        this.statusPanel.setLayout(new GridBagLayout());
        this.statusPanel.setBounds(1, 1, SCREEN_WIDTH/3, SCREEN_HEIGHT*2/3);

        // lblIterations -> label describing the numeric count adjacent that tracks the number of iterations
        this.lblIterations = new JLabel();
        this.lblIterations.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // lblIterationCount -> label container holding the number of iterations elapsed in program runtime.
        this.lblIterationCount = new JLabel("0");

        // lblCellsAlive -> label describing the numeric count adjacent that tracks the number of cells alive in a grid.
        this.lblCellsAlive = new JLabel();

        this.lblCellsAlive.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // lblCellsAliveCount -> label container holding the number of alive cells in a grid after each iteration.
        this.lblCellsAliveCount = new JLabel("0");
        this.lblCellsAliveCount.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // lblCellsDead -> label describing the numeric count adjacent that tracks the number of cells dead in a grid.
        this.lblCellsDead = new JLabel();
        this.lblCellsDead.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // lblCellsDeadCount -> label container holding the number of dead cells in a grid after each iteration.
        this.lblCellsDeadCount = new JLabel("0");
        this.lblCellsDeadCount.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // lblProgramStatus -> Label describing the status adjacent that details the program's current state.
        this.lblProgramStatus = new JLabel();
        this.lblProgramStatus.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // lblProgramStatusState -> label container holding the current program state
        this.lblProgramStatusState = new JLabel();
        this.lblProgramStatusState.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // lblLevelOfLife -> Label describing the progress bar detailing the ratio of dead cells to alive
        this.lblLevelOfLife = new JLabel();
        this.lblLevelOfLife.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        // pbarLevelOfLife -> Progress bar detailing the ratio of dead cells to alive cells at each iteration stage
        this.pbarLevelOfLife = new JProgressBar();
        this.pbarLevelOfLife.setValue(1);

        /*
         * Add elements to the screen.
         */

        // add the status Panel to the main Frame.
        this.mainFrame.add(this.statusPanel, BorderLayout.PAGE_END);

        // lblIterations - Layout formatting.
        this.gbc.gridx = 0;
        this.gbc.gridy = 0;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(20, 20, 0, 0);
        this.statusPanel.add(this.lblIterations, this.gbc);

        // lblIterationCount - Layout formatting.
        this.gbc.gridx = 1;
        this.gbc.gridy = 0;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(20, 0, 0, 0);
        this.statusPanel.add(this.lblIterationCount, this.gbc);

        // lblCellsAlive - Layout formatting.
        this.gbc.gridx = 3;
        this.gbc.gridy = 0;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(20, 0, 0, 0);
        this.statusPanel.add(this.lblCellsAlive, this.gbc);

        // lblCellsAliveCount - Layout formatting.
        this.gbc.gridx = 4;
        this.gbc.gridy = 0;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(20, 0, 0, 0);
        this.statusPanel.add(this.lblCellsAliveCount, this.gbc);

        // lblProgram Status - Layout formatting.
        this.gbc.gridx = 0;
        this.gbc.gridy = 1;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(10, 20, 20, 0);
        this.statusPanel.add(this.lblProgramStatus, this.gbc);

        // lblProgramStatusState - Layout formatting.
        this.gbc.gridx = 1;
        this.gbc.gridy = 1;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(10, 0, 20, 0);
        this.statusPanel.add(this.lblProgramStatusState, this.gbc);

        // lblCellsDead - Layout Formatting
        this.gbc.gridx = 3;
        this.gbc.gridy = 1;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(10, 0, 20, 0);
        this.statusPanel.add(this.lblCellsDead, this.gbc);

        // lblCellsDeadCount - Layout Formatting
        this.gbc.gridx = 4;
        this.gbc.gridy = 1;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(10, 0, 20, 0);
        this.statusPanel.add(this.lblCellsDeadCount, this.gbc);

        // lblLevelOfLife - Layout formatting
        this.gbc.gridx = 5;
        this.gbc.gridy = 0;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(20, 30, 0, SCREEN_WIDTH/3);
        this.statusPanel.add(this.lblLevelOfLife, this.gbc);

        // pbarLevelOfLife - Layout Formatting
        this.gbc.gridx = 5;
        this.gbc.gridy = 1;
        this.gbc.gridwidth = 1;
        this.gbc.gridheight = 1;
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.BOTH;
        this.gbc.ipadx = 10;
        this.gbc.ipady = 10;
        this.gbc.insets = new Insets(10, 30, 20, SCREEN_WIDTH/3);
        this.statusPanel.add(this.pbarLevelOfLife, this.gbc);
    }

    /**
     *  Set all panels and the main frame as visible.
     */
    private void showAll() {
        this.optionsPanel.setVisible(true); // options panel
        this.gridPanel.setVisible(true); // grid panel
        this.statusPanel.setVisible(true); // status panel
        this.mainFrame.setVisible(true); // main frame container
    }

    /**
     * Load all language, color and other configuration resources.
     */
    private void loadResources() {
        this.language = ResourceBundle.getBundle("dominiccobo.gameoflife.res.string", Locale.UK);
        this.clr_palette = ResourceBundle.getBundle("dominiccobo.gameoflife.res.color");
    }

    /**
     * Establishes all the loaded dynamic resource properties to their corresponding variables.
     */
    private void execResources() {
        this.mainFrame.setTitle(this.language.getString("app_name"));

        this.menuFile.setText(this.language.getString("menu_file"));
        this.menuEdit.setText(this.language.getString("menu_edit"));
        this.menuHelp.setText(this.language.getString("menu_help"));

        this.menuOptExit.setText(language.getString("menu_opt_exit"));
        this.menuOptSaveAs.setText(language.getString("menu_opt_save_as"));
        this.menuOptClear.setText(language.getString("menu_opt_clear"));
        this.menuOptAbout.setText(language.getString("menu_opt_about"));

        this.gridPanel.setBackground(Color.decode(this.clr_palette.getString("color_grid_holder")));

        this.optionsPanel.setBackground(Color.decode(this.clr_palette.getString("color_side_panes")));
        this.optionsPanelTitleHolder.setBackground(Color.decode(this.clr_palette.getString("color_header_holder")));

        this.lbloptionsTitle.setText(this.language.getString("label_options_title"));

        this.btnStart.setText(this.language.getString("button_start"));
        this.btnStart.setBackground(Color.decode(this.clr_palette.getString("color_button_background")));
        this.btnStart.setForeground(Color.decode(this.clr_palette.getString("color_button_text")));

        this.btnStop.setText(language.getString("button_stop"));
        this.btnStop.setBackground(Color.decode(this.clr_palette.getString("color_button_background")));
        this.btnStop.setForeground(Color.decode(this.clr_palette.getString("color_button_text")));
        this.btnStop.setEnabled(false);

        this.lblGridSize.setText(this.language.getString("label_grid_size"));

        this.lbloptionsTitle.setForeground(Color.decode(this.clr_palette.getString("color_header_text")));
        this.lblGridSize.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));
        this.sldrGridSize.setBackground(Color.decode(this.clr_palette.getString("color_side_panes")));

        this.lblIterationSpeed.setText(language.getString("label_iteration_speed"));
        this.lblIterationSpeed.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));
        this.sldrIterationSpeed.setBackground(Color.decode(this.clr_palette.getString("color_side_panes")));

        this.lblIterationSaving.setText(language.getString("label_iteration_saving"));
        this.lblIterationSaving.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));
        this.chkIterationSaving.setBackground(Color.decode(this.clr_palette.getString("color_side_panes")));

        this.lblCellShading.setText(this.language.getString("label_cell_shading"));
        this.lblCellShading.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));
        this.chkCellShading.setBackground(Color.decode(this.clr_palette.getString("color_side_panes")));

        this.statusPanel.setBackground(Color.decode(clr_palette.getString("color_side_panes")));

        this.lblIterations.setText(this.language.getString("label_iteration_count"));
        this.lblIterations.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));
        this.lblIterationCount.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));

        this.lblCellsAlive.setText(this.language.getString("label_cells_alive"));
        this.lblCellsAlive.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));
        this.lblCellsAliveCount.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));

        this.lblCellsDead.setText(this.language.getString("label_cells_dead"));
        this.lblCellsDead.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));
        this.lblCellsDeadCount.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));

        this.lblProgramStatus.setText(this.language.getString("label_program_status"));
        this.lblProgramStatus.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));
        this.lblProgramStatusState.setText(this.language.getString("status_stopped"));
        this.lblProgramStatusState.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));

        this.lblLevelOfLife.setText(this.language.getString("label_life_level"));
        this.lblLevelOfLife.setForeground(Color.decode(this.clr_palette.getString("color_text_labels")));
        this.pbarLevelOfLife.setForeground(Color.GREEN);

    }

    /**
     * Getter method for mainFrame
     * @return A public reference to the mainFrame object.
     */
    public JFrame getFrame() {
        return this.mainFrame;
    }


    /**
     * Getter method for btnStart.
     * @return A public reference to the btnStart object.
     */
    public JButton getBtnStart() {
        return this.btnStart;
    }

    /**
     * Getter method for btnStop.
     * @return A public reference to the btnStop object.
     */
    public JButton getBtnStop() {
        return this.btnStop;
    }

    /**
     * Getter method for sldrGridSize.
     * @return A public reference to the sldrGridSize object.
     */
    public JSlider getSldrGridSize() {
        return this.sldrGridSize;
    }

    /**
     * Getter method for sldrIterationSpeed
     * @return A public reference to the sldrIterationSpeed object.
     */
    public JSlider getSldrIterationSpeed() {
        return this.sldrIterationSpeed;
    }

    /**
     * Getter method for chkIterationSaving.
     * @return A public reference to the chkIterationSaving object.
     */
    public JCheckBox getChkIterationSaving() {
        return this.chkIterationSaving;
    }

    /**
     * Getter method for chkCellShading.
     * @return A public reference to the chkCellShading object.
     */
    public JCheckBox getChkCellShading() {
        return this.chkCellShading;
    }

    /**
     * Getter method for lblIterationCount.
     * @return A public reference to the lblIterationCount object
     */
    public JLabel getLblIterationCount() {
        return this.lblIterationCount;
    }

    /**
     * Getter method for lblCellsAliveCount.
     * @return A public reference to the lblCellsAliveCount object
     */
    public JLabel getLblCellsAliveCount() {
        return this.lblCellsAliveCount;
    }

    /**
     * Getter method for lblCellsDeadCount.
     * @return A public reference to the lblCellsDeadCount object
     */
    public JLabel getLblCellsDeadCount() {
        return this.lblCellsDeadCount;
    }

    /**
     * Getter method for lblProgramStatusState
     * @return A public reference to the lblProgramStatusState object
     */
    public JLabel getLblProgramStatusState() {
        return this.lblProgramStatusState;
    }

    /**
     * Getter method for pbarLevelOfLife.
     * @return A public reference to the pbarLevelOfLife object.
     */
    public JProgressBar getPbarLevelOfLife() {
        return this.pbarLevelOfLife;
    }

    /**
     * Getter method for grids button array.
     * @return A public reference to the array of grids objects.
     */
    public JButton[][] getGrids() {
        return this.grids;
    }

    /**
     * Getter method for the language resources
     * @param keyName The specific key id of the string to retrieve.
     * @return The string value of the element retrieved from the resources.
     */
    public String getLanguageString(String keyName) {
        return this.language.getString(keyName);
    }

    /**
     * Getter method for the color resources
     * @param keyName The specific key id of the color to retrieve.
     * @return The Color value of the element retrieved from the resources.
     */
    public Color getColors(String keyName) {
        return Color.decode(this.clr_palette.getString(keyName));
    }
}
