package dominiccobo.gameoflife.view;

import javax.swing.*;

/**
 *  <p>
 *      Class containing a simple implementation of an 'About this
 *      program' view, that simply calls the same visuals as the
 *      splash screen class.
 *  </p>
 *  @author Dominic Cobo
 *  @since March 2017
 */
public class AboutView {

    /**
     * Instance variable JFrame holding the 'about' view data.
     */
    private JFrame          aboutFrame              = null;

    /**
     * Instance variable Image Icon displaying the 'about' splash screen picture.
     */
    private ImageIcon       splashImage             = null;

    /**
     * Instance variable JLabel to which to add the ImageIcon containing the 'about'
     * image.
     */
    private JLabel          lblSplash               = null;

    /**
     * Constant detailing default width for the about screen, change as necessary
     * according to displayed image's size.
     */
    private final int       SCREEN_WIDTH            = 500;

    /**
     * Constant detailing default height for the about screen, change as necessary
     * according to the displayed image's size.
     */
    private final int       SCREEN_HEIGHT           = 351;

    /**
     * <p>
     *     Constructor initializing the about view on object instantiation.
     * </p>
     */
    public AboutView() {
        init(); // initailize the view
    }

    /**
     * <p>
     *     Method initializing the view, by invoking a new thread to display the
     *     swing GUI in, then calling the frame creation method.
     * </p>
     */
    private void init() {
        // Compose the GUI.
        SwingUtilities.invokeLater(this::createFrame);
    }

    /**
     * <p>
     *     Generates a frame layout to display the about view in then displays it.
     * </p>
     */
    private void createFrame() {
        aboutFrame = new JFrame();
        aboutFrame.setBounds(500, 550, SCREEN_WIDTH, SCREEN_HEIGHT);
        aboutFrame.setLocationRelativeTo(null);
        aboutFrame.setResizable(false);

        // retrieve the image from the resources package.
        splashImage = new ImageIcon(getClass().getClassLoader().getResource("dominiccobo/gameoflife/res/splash.png"));
        lblSplash = new JLabel("", splashImage, SwingConstants.CENTER);
        aboutFrame.getContentPane().add(lblSplash);
        aboutFrame.setVisible(true);
        aboutFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
