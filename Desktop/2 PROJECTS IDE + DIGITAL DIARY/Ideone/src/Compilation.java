// import java libraries
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JTextArea;


public class Compilation {
    
    private long startTime;
    private long endTime;
    
    private String file;
    private String exe;
    private String parent;
    
    private short tab;
    private boolean compiled;
    
    public Compilation()
    {
        this.tab = 0;
        this.compiled = false;
    }// end Compilation() constructor
    
    public void compileC(String filepath, JTextArea output) {
        
        if(filepath.length() > 0) {
        
            if(ideoneInterface.start) {
                this.tab = (short) ideoneInterface.chooseTab.getSelectedIndex();
            } else {
                this.tab = 0;
            }// end if
            
            File path = new File(filepath);
            getData(path);
            
            try {
                this.startTime = System.currentTimeMillis();
                
                ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/C", "gcc " + "\"" + this.parent + "\\" + this.file + "\"" + " -o \"" + "test" + "\"");
                processBuilder.directory(new File(this.parent));
                
                Process process = processBuilder.start();
                process.waitFor();
                int x = process.exitValue();
                
                this.endTime = System.currentTimeMillis();
                
                // If the code has compiled successfully - normal execution
                if (x == 0) {
                    output.setForeground(Color.BLUE);
                    
                    output.append("- Warning: 0\n");
                    output.append("- Output Filename : " + filepath + "\n");
                    output.append("- Compilation Time : " + (this.endTime - this.startTime)/1000.0 + "s\n");
                    
                    this.compiled = true;
                }
                
                // Error Compiling code
                else {
                    this.compiled = false;
                   
                    output.setForeground(Color.RED);
        
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                    String error;
                    while((error = br.readLine()) != null) { // throw readLine exception
                        output.append(error + "\n");
                    }// end while()
                }// end if
            }
            catch(IOException | InterruptedException e) {}
            
        }
        else {
            this.compiled = false;
            System.out.println("file not created...");
        }// end if
    }// end compileC(String, JTextArea)
    
    public void runC(String filepath, JTextArea output, JTextArea input) throws IOException {
        
        short temp;
        if(ideoneInterface.start) {
            temp = (short) ideoneInterface.chooseTab.getSelectedIndex();
        } else {
            temp = 0;
        }// end if
        
        if(this.tab == temp) {
            
            String[] inputList = input.getText().split("[ \n]");
            Process process;
            
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec("cmd /c start " + this.parent + "\\test.exe");
            
        }// end if
    }// end runC(String, JTextArea, JTextArea)

    public void compileJava(String filepath, JTextArea output) {
        
        if(filepath.length() > 0) {
        
            if(ideoneInterface.start) {
                this.tab = (short) ideoneInterface.chooseTab.getSelectedIndex();
            } else {
                this.tab = 0;
            }// end if
            
            File path = new File(filepath);
            getData(path);
            
            try {
                this.startTime = System.currentTimeMillis();
                
                ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/C", "javac " + "\"" + this.parent + "\\" + this.file + "\"");
                processBuilder.directory(new File(this.parent));
                
                Process process = processBuilder.start();
                process.waitFor();
                int x = process.exitValue();
                
                this.endTime = System.currentTimeMillis();
                
                // If the code has compiled successfully - normal execution
                if (x == 0) {
                    output.setForeground(Color.BLUE);
                    
                    output.append("- Warning: 0\n");
                    output.append("- Output Filename : " + filepath + "\n");
                    output.append("- Compilation Time : " + (this.endTime - this.startTime)/1000.0 + "s\n");
                    
                    this.compiled = true;
                }
                
                // Output Error
                else {
                    this.compiled = false;
                    output.setForeground(Color.RED);
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                    String error;
                    while((error = br.readLine()) != null) { // throw readLine exception
                        output.append(error + "\n");
                    }// end while()
                }// end if
            }
            catch(IOException | InterruptedException e) {}
            
        }
        else {
            this.compiled = false;
            System.out.println("file not created...");
        }// end if
    }// end compileJava(String, JTextArea)
    
    public void runJava(String filepath, JTextArea output, JTextArea input) throws IOException {
    
        short temp;
        if(ideoneInterface.start) {
            temp = (short) ideoneInterface.chooseTab.getSelectedIndex();
        } else {
            temp = 0;
        }// end if
        
        if(this.tab == temp) {
            
            String[] inputList = input.getText().split("[ \n]");
            Process process;
            
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec("cmd /c start " + "java " + file.substring(0, file.indexOf(".")));
            
        }// end if
    }// end runJava(String, JTextArea, JTextArea)
    
    public void compileCpp(String filepath, JTextArea output) {
        
        if(filepath.length() > 0) {
        
            if(ideoneInterface.start) {
                this.tab = (short) ideoneInterface.chooseTab.getSelectedIndex();
            } else {
                this.tab = 0;
            }// end if
            
            File path = new File(filepath);
            getData(path);
            
            try {
                this.startTime = System.currentTimeMillis();
                
                ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/C", "g++ " + "\"" + this.parent + "\\" + this.file + "\"" + " -o \"" + "test" + "\"");
                processBuilder.directory(new File(this.parent));
                
                Process process = processBuilder.start();
                process.waitFor();
                int x = process.exitValue();
                
                this.endTime = System.currentTimeMillis();
                
                // If the code has compiled successfully - normal execution
                if (x == 0) {
                    output.setForeground(Color.BLUE);
                    
                    output.append("- Warning: 0\n");
                    output.append("- Output Filename : " + filepath + "\n");
                    output.append("- Compilation Time : " + (this.endTime - this.startTime)/1000.0 + "s\n");
                    
                    this.compiled = true;
                }
                
                // Output Error
                else {
                    this.compiled = false;
                    output.setForeground(Color.RED);
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    
                    String error;
                    while((error = br.readLine()) != null) { // throw readLine exception
                        output.append(error + "\n");
                    }// end while()
                }// end if
            }
            catch(IOException | InterruptedException e) {}
            
        }
        else {
            this.compiled = false;
            System.out.println("file not created...");
        }// end if
    }// end compileCpp(String, JTextArea)
    
    public void runCpp(String filepath, JTextArea output, JTextArea input) throws IOException {
        
        short temp;
        if(ideoneInterface.start) {
            temp = (short) ideoneInterface.chooseTab.getSelectedIndex();
        } else {
            temp = 0;
        }// end if
        
        if(this.tab == temp) {
            
            String[] inputList = input.getText().split("[ \n]");
            Process process;
            
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec("cmd /c start " + this.parent + "\\test.exe");
            
        }// end if
    }// end runCpp(String, JTextArea, JTextArea)
    
    /**
     * Method Name : getData
     * Purpose : Get all the required Strings from the given file
     *           parent path, .exe path and file name
     * @param path - current file path 
     */
    private void getData(File path) {
        
        this.file = path.getName();
        this.parent = path.getParent();
        this.exe = parent + "\\" + file.substring(0, file.indexOf(".")) + ".exe";

    }// end getData(File)

}// end Compilation() Class
