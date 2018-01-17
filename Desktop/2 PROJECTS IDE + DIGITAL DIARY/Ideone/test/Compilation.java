
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JTextArea;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ashish
 */
public class Compilation {
    
    private long startTime;
    private long endTime;
    
    private String file;
    private String exe;
    private String parent;
    
    private Process process;
    private ProcessBuilder processBuilder;
    
    public Compilation()
    {
        
    }
    
    void compileC(String filepath, JTextArea output) {
        
        if(filepath.length() > 0) {
            
            File path = new File(filepath);
            getData(path);
            
            try {
                this.startTime = System.currentTimeMillis();
                
                this.processBuilder = new ProcessBuilder("cmd", "/C", "gcc " + "\"" + this.parent + "\\" + this.file + "\"" + " -o \"" + "test" + "\"");
                this.processBuilder.directory(new File(this.parent));
                
                this.process = this.processBuilder.start();
                this.process.waitFor();
                int x = this.process.exitValue();
                
                this.endTime = System.currentTimeMillis();
                
                // If the code has compiled successfully - normal execution
                if (x == 0) {
                    output.setForeground(Color.BLUE);
                    
                    output.append("- Warning: 0\n");
                    output.append("- Output Filename : " + filepath + "\n");
                    output.append("- Compilation Time : " + (this.endTime - this.startTime)/1000.0 + "s\n");
                    
                    Runtime runtime = Runtime.getRuntime();
                    Process p = runtime.exec("cmd /c start " + this.parent + "\\test.exe");
                }
                
                else {
                    ErrorDisplay(output);
                }
            }
            catch(IOException | InterruptedException e) {}
            
        }
        else {
            System.out.println("file not created...");
        }
    }
    
    void runC(String filepath, JTextArea output, JTextArea input) throws IOException {
        
        if(filepath.length() > 0) {
            
            File path = new File(filepath);
            getData(path);
            
            processBuilder.command(parent + "\\" + "test.exe");
            processBuilder.start();
            
        }
    }

    void compileJava(String filepath, JTextArea output) {
        
    }

    void compileCpp(String filepath, JTextArea output) {
        
    }

    private void getData(File path) {
        
        this.file = path.getName();
        this.parent = path.getParent();
        this.exe = parent + "\\" + file.substring(0, file.indexOf(".")) + ".exe";

    }

    private void ErrorDisplay(JTextArea output) throws IOException {
        
        output.setForeground(Color.RED);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String error;
        while((error = br.readLine()) != null) { // throw readLine exception
            output.append(error + "\n");
        }
    }

}
