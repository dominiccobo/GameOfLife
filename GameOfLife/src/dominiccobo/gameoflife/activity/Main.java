package dominiccobo.gameoflife.activity;

import dominiccobo.gameoflife.view.MainView;
import dominiccobo.gameoflife.view.SplashScreenView;

/**
 *  @author Dominic Cobo
 *  @since March 2017
 *  @version 1.0
 */
public class Main {

    /**
     * Standard defined Java program entry point for the JVM.
     * @param args Arguments not used.
     */
    public static void main(String[] args) {

        new SplashScreenView();
        new MainView();
    }

}
