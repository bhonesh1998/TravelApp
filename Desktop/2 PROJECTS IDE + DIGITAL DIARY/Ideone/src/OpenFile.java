
// import java libraries
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class OpenFile
{
    private int itr;
    private short tab;
    private String textLine;
    private BufferedReader br;
    private FileInputStream is;
    private ArrayList<String> openList;

    /**
     * Constructor
     */
    public OpenFile() {
        openList = new ArrayList<String>();
    }// end OpenFile() constructor

    public void openFile(String filePath) {

        // Attempt to open the preferred text file
        try {

            // Create a filepath file with an ObjectOutputStream
            is = new FileInputStream(filePath);
            br = new BufferedReader(new InputStreamReader(is));

            openList.clear();
            
            // Read till the end
            while((textLine = br.readLine()) != null) {
                openList.add("\n");
                openList.add(textLine);
            }// end while()
            
            if(ideoneInterface.start) {
                this.tab = (short) ideoneInterface.chooseTab.getSelectedIndex();
            }
            else {
                this.tab = 0;
            }// end if
            
            for(itr = 1; itr < openList.size(); itr++) {
                ideoneInterface.textList.get(this.tab).append(openList.get(itr));
            }// end for()

            br.close(); is.close();
        
        } catch(IOException e) {
            System.out.println("Error in Open File");
        }// end try/catch

    }// end openFile(String)

}// end OpenFile()

