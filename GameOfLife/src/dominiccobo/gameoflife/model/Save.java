package dominiccobo.gameoflife.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *  <p>
 *      Class creates an ArrayList saver, adapted to work with the iterations
 *      saved by this program.
 *  </p>
 *
 *  @author Dominic Cobo
 *  @since March 2017
 */
public class Save {

    /**
     *
     * Instance variable detailing the filename + path of the content to be saved.
     */
    private String fname;

    /**
     * Instance variable detailing the grid iterator to retrieve the content that
     * requires saving.
     */
    private GridIterator gridIterator;


    /**
     * Loads resource bundle containing all output strings, this is separate from the
     * normal view strings.
     */
    private ResourceBundle outputStr = ResourceBundle.getBundle("dominiccobo.gameoflife.res.output",  Locale.UK);

    /**
     * <p>
     * Constructor for the Save class, specifies the grid iterator of which to retrieve the
     * saveable content from.
     * </p>
     * @param gridIterator The grid iterator to pass to this class for saving.
     */
    public Save(GridIterator gridIterator){
        this.gridIterator = gridIterator;
    }

    /**
     * Method specifies the filename and directory where it should be saved.
     * @param fname The file name + directory of the saved file.
     */
    public void setFile(String fname) {
        this.fname = fname;
    }

    /**
     * <p>
     * Method takes the content saved in the iterator array list and outputs it to the requested directory.
     * </p>
     */
    public void write() {

        // initialize our values and declare them as null, so we can use this to see whether they are set for error checking.
        File file;
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;

        try {
            file = new File(fname); // convert str_File into a File data type.

            if(!file.exists()) { // if file doesn't exist create it, save any issues with writing to non-existent files.
                file.createNewFile();
            }

            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(outputStr.getString("save_iterations") + gridIterator.getIteratorCount());
            bufferedWriter.newLine();
            bufferedWriter.write(outputStr.getString("save_gridspan") + gridIterator.getCurrentGrid().getGridDimensions());
            bufferedWriter.newLine();

            for(int i = 0; i < gridIterator.getPreviousIterations().size(); i++) {
                bufferedWriter.write(outputStr.getString("save_iteration_no") + i);
                bufferedWriter.newLine();
                bufferedWriter.write(gridIterator.getPreviousIterations().get(i)); // write the item to the file buffer...
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }

        }
        catch(IOException $e) {
            System.err.println($e.getMessage());
        }
        // finally will always run regardless of what occurs above, not even return statements will stop it from running.
        finally {

            // if the buffered writer was never initialized successfully, closing it will only cause errors...
            if(bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                }
                catch(IOException $e) {
                    System.err.println($e.getMessage());
                }
            }

            if (fileWriter != null) {
                try {
                    fileWriter.close();
                }
                catch(IOException $e) {
                    System.err.println($e.getMessage());
                }
            }
        }

    }
}
