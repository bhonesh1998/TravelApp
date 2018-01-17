
// Import java libraries
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class SaveFile
{
    private String[] lines;
    
    private short tab;
    private File file;
    private FileWriter fileWrite;
    private PrintWriter printWrite;

    /**
     * Constructor
     */
    public SaveFile() { }

    /**
     * Purpose : save the file when path is reset
     *
     * @param filePath - Path of the file in the directory
     */
    public void saveAsFile(String filePath) {
        
        if(ideoneInterface.start) this.tab = (short) ideoneInterface.chooseTab.getSelectedIndex();
        else this.tab = 0;
        
        lines = ideoneInterface.textList.get(this.tab).getText().split("\n");
        
        // Attempt to save the file in the path specified
        try {

            file = new File(filePath);

            //Create a filepath file with an ObjectOutputStream
            fileWrite = new FileWriter(file.getAbsoluteFile());
            printWrite = new PrintWriter(fileWrite);

            // Write the list in the file
            for(String text : lines) {
                printWrite.println(text);
            }// end for

            printWrite.close(); fileWrite.close();
        }

        //Exception warning if the file is not saved or created
        catch(IOException saveError) {
            System.out.println("FILE WAS NOT ABLE TO SAVE PROPERLY...");
        }// end try/catch

    }// saveAsFile(String)


    /**
     * Purpose : Save the text in the file if the location/path of the file is already set
     *
     * @param filePath - The path for the text file name in the directory
     * @throws IOException - Reject any error display
     */
    public void saveInstant(String filePath) throws IOException {

        if(ideoneInterface.start) this.tab = (short) ideoneInterface.chooseTab.getSelectedIndex();
        else this.tab = 0;
        
        // Save the text from the file
        lines = ideoneInterface.textList.get(this.tab).getText().split("\n");
        
        try {
            
            file = new File(filePath);
            
            if(!file.exists()) {
                file.createNewFile();
            }// end if
            
            //Create a filepath file with an ObjectOutputStream
            fileWrite = new FileWriter(file.getAbsoluteFile());
            printWrite = new PrintWriter(fileWrite);
            
            // Write the list in the file
            for(String text : lines) {
                printWrite.println(text);
            }// end for

            printWrite.close(); fileWrite.close();
        }
            
        //Exception warning if the file is not saved or created
        catch(IOException e) {
            System.out.println("<Save Instant Error>");
        }// end try/catch
        
    }// SaveInstant(String)

}// SaveFile() Class