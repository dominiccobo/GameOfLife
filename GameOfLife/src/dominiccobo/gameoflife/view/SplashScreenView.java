package dominiccobo.gameoflife.view;

import javax.swing.*;

/**
 *  <p>
 *      Models a "splash screen type-window" using a pure SWING API approach, rather
 *      than the traditional AWT approach.
 *  </p>
 *  <p>
 *      It does not load before the JVM loads all needed classes like traditional
 *      splash-screens, but rather serves as a purely aesthetic addition to the
 *      program initialization.
 *  </p>
 *
 *  @author Dominic Cobo
 *  @since March 2017
 */
public class SplashScreenView {

    /**
     * Instance variable detailing JWindow container for holding the splash view.
     */
    private JWindow             splashWindow        = null;

    /**
     * Instance variable detailing the ImageIcon displaying the splash screen
     * image.
     */
    private ImageIcon           splashImage         = null;

    /**
     * Instance variable detailing the JLabel containing the defined splash
     * image.
     */
    private JLabel              lblSplash           = null;

    /**
     * Default long integer time to display the splash screen for.
     */
    private static final long   SPLASH_TIME         = 6000;


    /**
     * Constant detailing default width for the splash screen, change as necessary
     * according to displayed image's size.
     */
    private final int       SCREEN_WIDTH            = 500;

    /**
     * Constant detailing default height for the splash screen, change as necessary
     * according to the displayed image's size.
     */
    private final int       SCREEN_HEIGHT           = 351;

    /**
     *  Constructor only serves the purpose of calling the initialization method 'init'.
     */
    public SplashScreenView()  {
        init();
    }

    /**
     * Initialize the GUI by calling all creation components.
     */
    private void init() {

        // Compose the GUI.
        createFrame();
    }

    /**
     * Creates, displays and populates the JWindow containing the splash image to be displayed
     * on program startup.
     */
    private void createFrame() {

        splashWindow = new JWindow();
        splashWindow.setBounds(500, 550, SCREEN_WIDTH, SCREEN_HEIGHT);
        splashWindow.setLocationRelativeTo(null);

        // retrieve the image from the resources package.
        splashImage = new ImageIcon(getClass().getClassLoader().getResource("dominiccobo/gameoflife/res/splash.png"));
        lblSplash = new JLabel("", splashImage, SwingConstants.CENTER);
        splashWindow.getContentPane().add(lblSplash);
        splashWindow.setVisible(true);

        // sleep the thread to simulate loading time, yes this is just for visual vibes :)
        try { Thread.sleep(SPLASH_TIME);  }
        catch (InterruptedException e) { e.printStackTrace();  }

        splashWindow.setVisible(false);
        splashWindow.dispose();
    }
}
