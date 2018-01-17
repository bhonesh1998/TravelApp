/**
 * ideoneInterface
 * 
 * Version info : The application is a combinational interface of a typical IDE and
 *                and an online compiler
 */

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.undo.UndoManager;

public class ideoneInterface extends javax.swing.JFrame {
    
    JFrame tempFrame;
    
    public static JTabbedPane multiTab, chooseTab;
    
    public static ArrayList<UndoManager> undoList;      // Undo manager array
    public static ArrayList<JLabel> labelList;          // tab name store list
    public static ArrayList<RSyntaxTextArea> textList;  // Collection of all tabs
    public static ArrayList<String> pathList;           // Collect all filepath
    public static ArrayList<Highlighter> highList;      // Highlighter List
    
    Date date;
    SimpleDateFormat dateFormat;
    
    Color color, tempColor;
    
    Font tempFont; // Store the previous font 
    static Font font; 
    
    JFileChooser fileChooser;
    
    short language; // Language option variable
    public static short tabIndex, tabOpen; // Tab variables
    
    SaveFile SaveClass;         // Class to handle Save File Operations
    OpenFile OpenClass;         // Class to handle opening a Open File
    ChooseFont ChooseFont;      // Class to handle Font preferences
    Compilation Compiler;       // Class to compile the code
    PreferenceClass PrefStyle;  // Class to handle tab preferences
    
    private String textSelected;
    
    private int index;
    public static boolean wrapText, closeIdeone;
    private boolean selectBol;
    public static boolean start;
     
    private final JList<String> recentList;             // Recent Files JList
    private final DefaultListModel<String> listAccess;  // To access and modify JList of recent files
    private final ArrayList<String> recentArray;        // List to store recent files
    
    public static ImageIcon closeWhite, closeRed;   // Close option image on tab
    public static Dimension closeDimension;         // Dimension of close image
    
    public ideoneInterface() {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | IllegalAccessException |
              InstantiationException | UnsupportedLookAndFeelException ex){}
        
        initComponents();
        
        // Create a JTree of all the files of this project
        try {
            File currentDirectory = new File(new File(".").getAbsolutePath());
            treeFormat.setModel(new FileSystem(new File(currentDirectory.getCanonicalPath())));
        }
        catch (IOException e) { }
        
        closeWhite = new ImageIcon("close.png");
        closeRed = new ImageIcon("change.png");
        closeDimension = new Dimension(closeWhite.getIconWidth()+2, closeWhite.getIconHeight()+2);
        
        listAccess = new DefaultListModel<String>(); // To add elements to recent list
        recentList = new JList<>(listAccess);        // Dispplay list of recent open files
        recentPane.setViewportView(recentList);         
        recentArray = new ArrayList<String>();       // ArrayList to have list of all recent files
        
        recentFileAppend();     // Add all the previously recently opened files to recent List
        
        tempFrame = new JFrame("CodEditor v7.2.0");
        
        undoList = new ArrayList<UndoManager>();
        pathList = new ArrayList<String>();          // Store filepath of all tab
        multiTab = new JTabbedPane();                // Store all opened tabs
        textList = new ArrayList<RSyntaxTextArea>(); // Store textarea of all tab
        highList = new ArrayList<Highlighter>();     // Store Highlighter of all tab
        labelList= new ArrayList<JLabel>();          // Store the tab name
        
        createTab("Untitled");          // Add a new tab
        multiTab.setSelectedIndex(0);   // Be default open the first tab
        
        topRight.add(multiTab, BorderLayout.CENTER);
        
        font = new Font("Courier New", Font.PLAIN, 12);
        tempFont = new Font("Courier New", Font.PLAIN, 12);
        
        SaveClass  = new SaveFile();        
        OpenClass  = new OpenFile();        
        Compiler   = new Compilation();     
        ChooseFont = new ChooseFont();      
        PrefStyle  = new PreferenceClass(); 
        
        fileChooser = new JFileChooser();
        
        wrapText = false;   // 
        closeIdeone = true; // 
        start = false;      // Check whether the user has started typing
        
        findReplace(false); // Disable the find/replace by default
        
        tabOpen = 0;    // Number of tabs open
        tabIndex = 0;   // Current tab position on the JTabbedPane
        language = 1;   // Laguange type
        
        date = new Date();
        dateFormat = new SimpleDateFormat("hh:mm a d/M/yyyy");
        outputArea.setText("");
        
        // ChangeListener to handle the index position of the current open tab
        ChangeListener changeListener = (ChangeEvent changeEvent) -> {
            start = true;
            chooseTab = (JTabbedPane) changeEvent.getSource();
            
            if(tabOpen > 0) {
                coordinate(chooseTab.getSelectedIndex());
            }// end if
        };
        
        multiTab.addChangeListener(changeListener);
    }// end ideoneInterface() constructor
    
    /**
     * Method Name : recentFileAppend
     * Purpose     : Read recently opened filepath from the file.txt
     * */
    private void recentFileAppend() {
        try {
            BufferedReader read = new BufferedReader(new FileReader("file.txt"));    
  
            String text;
            while((text = read.readLine()) != null){
                listAccess.addElement((new File(text.trim())).getName());
                recentArray.add(text);
            }// end while()
            read.close();
        } catch(IOException fileRead) { }
    }// end recentFileAppend()
    
    /**
     * Method Name : fileReplace
     * Purpose     : Enable/Disable swing object of find/replace features
     * */
    private void findReplace(boolean state) {
        
        findField.setText("");
        replaceField.setText("");
        findField.setEditable(state);
        replaceField.setEditable(state);
        
        nextButton.setEnabled(state);
        replaceButton.setEnabled(state);
        replaceAllButton.setEnabled(state);
    }// end findReplace(boolean)
    
    /**
     * Method Name : createTab
     * Purpose     : Add a new tab
     * */
    public static void createTab(String text) {
        
        tabOpen++;  // Increase the number of tab opened by 1
        
        JPanel closeTab = new JPanel(new BorderLayout());   // Create a closeTab
        closeTab.setOpaque(false);                          
        JButton closeButton = new JButton(closeWhite);      // Create a close button for tab
        closeButton.setPreferredSize(closeDimension);       // Set dimension of the close button
        
        JLabel fileLabel = new JLabel(text+"     ");
        labelList.add(fileLabel);
        
        EditorPane textArea = new EditorPane(); // Import a textArea (panel) to the tab
        
        // Add Scrolling feature to the textArea
        JScrollPane scroll = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                             JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        textList.add(textArea.editor);
        pathList.add("");
        
        
        // MouseListener to handle events when mouse is hovered over it
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setIcon(closeRed);    /* Change the icon to red   */
            }// end mouseEntered(MouseEvent)

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setIcon(closeWhite);  /* Change the icon to white */
            }// end mouseExited(MouseEvent)
        });
        
        // When close button is clicked
        closeButton.addActionListener((ActionEvent e) -> {
            int tabNum = multiTab.indexOfComponent(scroll);
            
            // Delete the current tab from the application
            
            undoList.remove(tabNum);
            textList.remove(tabNum);
            pathList.remove(tabNum);
            highList.remove(tabNum);
            multiTab.removeTabAt(tabNum);
            labelList.remove(tabNum);
            tabOpen--;
            
            // If there is no tab, add a tab by default
            if(textList.isEmpty()) { createTab("Untitled"); }
        });
        
        //
        textArea.editor.addCaretListener((javax.swing.event.CaretEvent evt) -> {
            if(start) {
                coordinate(chooseTab.getSelectedIndex());
            }
            else { coordinate(0); } // end if
        });
        
        textArea.editor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                
                // If the char is any closing type bracket - ] )
                char c = evt.getKeyChar();
                if( (c == ')' || c == ']') && textArea.editor.getText().length() > 0) {
                    try {
                        char next = textArea.editor.getText().charAt(textArea.editor.getCaretPosition());

                        if(c == next) {
                            textArea.editor.replaceRange("", textArea.editor.getCaretPosition(), textArea.editor.getCaretPosition()+1);
                        }// end if
                    }catch(Exception outOfBound) {}
                }
                
                else {
                    try {
                        switch(c) {
                            
                            case '{':
                                insertCurl(textArea.editor, textArea.editor.getCaretPosition());
                                textArea.editor.insert("\n", textArea.editor.getCaretPosition());
                                textArea.editor.setCaretPosition(textArea.editor.getCaretPosition()-1);
                                break;

                            case '(' : 
                                textArea.editor.insert(")", textArea.editor.getCaretPosition());
                                textArea.editor.setCaretPosition(textArea.editor.getCaretPosition()-1);
                                break;
                                
                            case '[' : 
                                textArea.editor.insert("]", textArea.editor.getCaretPosition());
                                textArea.editor.setCaretPosition(textArea.editor.getCaretPosition()-1);
                                break;
                                
                            case '\"' : 
                                textArea.editor.insert("\"", textArea.editor.getCaretPosition());
                                textArea.editor.setCaretPosition(textArea.editor.getCaretPosition()-1);
                                break;
                                
                            case '\'' : 
                                textArea.editor.insert("\'", textArea.editor.getCaretPosition());
                                textArea.editor.setCaretPosition(textArea.editor.getCaretPosition()-1);
                                break;
                        }// end switch()
                    } catch (BadLocationException ex) { System.out.println("location error"); }
                }// end if
            }// end KeyPressed(KeyEvent)
            
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                try {
                    switch(evt.getKeyChar()) {
                        
                        // When ENTER is released
                        case '\n':
                            insertTab(textArea.editor, textArea.editor.getCaretPosition());
                            break;
                    }
                } catch (BadLocationException errorKeyRelease) {
                    System.out.println("Error Key Released");
                }// end try/catch
            }// end keyReleased(KeyEvent)
        });
        
        closeTab.add(fileLabel, BorderLayout.WEST);
        closeTab.add(closeButton, BorderLayout.EAST);
        
        multiTab.addTab(null, scroll);
        multiTab.setSelectedIndex(multiTab.getTabCount()-1);
        multiTab.setTabComponentAt(multiTab.getTabCount()-1, closeTab);
    }// end createTab(String)
    
    /**
     * Method Name     : inserTab
     * Purpose         : Determine how many tabs to insert to a newLine before typing
     * @param textArea : textArea of the current tab
     * @param pos      : Caret position on the textArea
     * @throws BadLocationException 
     */
    public static void insertTab(RSyntaxTextArea textArea, int pos) throws BadLocationException {
        
        String tab = "";
        String text = textArea.getText(0, pos);     // Get the text upto caret position
        
        int tabify = 0;
        for(int i = 0; i < pos; i++) {
            switch(text.charAt(i)) {
                case '{' : tabify++; break;
                case '}' : tabify--; break;
            }// end switch()
        }// end for
        for(int i = 0; i < tabify; i++) { tab+="\t"; }
        
        textArea.insert(tab, textArea.getCaretPosition());
        textArea.setCaretPosition(textArea.getCaretPosition()- tabify);
        textArea.setCaretPosition(textArea.getCaretPosition()+tabify);
    }// end insertTab(RSyntaxTextArea, int)
    
    /**
     * Method Name     : insertCurl
     * Purpose         : Determine number of tabs in the nested curly brackets for newLine
     * @param textArea : textArea of the current tab
     * @param pos      : Caret position on the textArea
     * @throws BadLocationException 
     */
    public static void insertCurl(RSyntaxTextArea textArea, int pos) throws BadLocationException{
        
        String tab  = "";
        String text = textArea.getText(0, pos);
        int tabify = 0;
        
        for(int i = 0; i < pos; i++) {
            switch(text.charAt(i)) {
                case '{' : tabify++; break;
                case '}' : tabify--; break;
            }// end switch()
        }// end for
        for(int i = 0; i < tabify; i++) { tab+="\t"; }
        tab+="}";
        
        textArea.insert(tab, textArea.getCaretPosition());
        textArea.setCaretPosition(textArea.getCaretPosition()- (1 + tabify));
    }// end insertCurl(RSyntaxTextArea, int)
    
    /**
     * Method Name : coordinates
     * Purpose     : Display the current (x,y) coordinate of the caret
     * @param tab  : Current index position of the tab on the tabList
     */
    public static void coordinate(int tab) {
        
        int y = textList.get(tab).getCaretLineNumber();
        int x = textList.get(tab).getCaretPosition() - textList.get(tab).getLineStartOffsetOfCurrentLine();
        cursorState.setText((y+1) + ":" + (x+1));
    }// end coordinate(int)
    
    /**
     * Method Name : openFunction
     * Purpose : Open a new file or an existing in the application
     * @param textFile - Where to import the file
     */
    public void openFunction(File textFile) {
        
        Boolean filePresent;
        String fileName = ""+textFile;
        String thisFile = textFile.getName();
        
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        
        // Check for whether the file is open in any tab or not
        
        filePresent = false;
        for(int i = 0; i < labelList.size(); i++) {
        
            // If the file is opened in any tab, open that tab
            if(thisFile.equals(labelList.get(i).getText().trim())) {
                filePresent = true;
                multiTab.setSelectedIndex(i);
                break;
            }// end if
        }// end for
        
        // If the file opened is not accessed, open in new Tab
        if(!filePresent) {
            createTab(thisFile);
            pathList.set(pathList.size() - 1, fileName);
            OpenClass.openFile(fileName);
        }// end if

        // Update the recently file Area
        index = recentArray.indexOf(fileName);
        if(index >= 0) {
            listAccess.remove(index);
            recentArray.remove(index);
        }// end if
        
        recentArray.add(0,fileName);
        listAccess.add(0, textFile.getName());
        
    }// end openFunction(JTextArea)
    
    public void closeFunction(String type) {
        
        tabIndex = (short) chooseTab.getSelectedIndex();
        
        // If the notepad is not empty
        if(textList.get(tabIndex).getText().length() > 0)
        {
            String[] defaultOption = { "Save", "Don't Save", "Cancel"};
            int choice = JOptionPane.showOptionDialog(null, "Do you want to save changed to notepad?", "Notepad", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, defaultOption, defaultOption[0]);

            // Save the Notepad
            if(choice == 0) { // Save
                if(type.equalsIgnoreCase("new")) {
                    saveFunction(pathList.get(tabIndex));
                }

                // Save the current file and then close it
                else if(type.equalsIgnoreCase("exit")) {
                    System.exit(0);
                }// end if

                pathList.set(tabIndex, "");
                textList.get(tabIndex).setText("");
            }

            // Close the Notepad
            else if(choice == 1) { //Don't Save
                if(type.equalsIgnoreCase("new")) {
                    textList.get(tabIndex).setText("");
                }
                else if(type.equalsIgnoreCase("exit")) {
                    System.exit(0);
                }// end if
            }// end if
        }

        // If the notepad has no text in it
        else {
            // Clear the text if "new" selected
            if(type.equalsIgnoreCase("new")) {
                textList.get(tabIndex).setText("");
            }
            else if (type.equalsIgnoreCase("exit")) {
                System.exit(0);
            }// end if
        }// end if

    }// end closeFunction(String)
    
    /**
     * Method Name     : saveFunction
     * Purpose         : Save the textArea contents in the file
     * @param filePath : path directory of the file on the system
     */
    public void saveFunction(String filePath) {
        
        // If the filepath of the textArea has been set
        if(filePath.length() != 0 ){
            try {
                SaveClass.saveInstant(filePath);
            } catch (IOException ex) {
                System.out.println("Save Instant Error");
            }
        }
        // Save the file with new filepath on the system
        else {
            saveAsFunction();
        }// end if
    }// end saveFunction(String)
    
    /**
     * Method Name : saveAsFunction
     * Purpose : Save the file to new location in the system
     */
    public void saveAsFunction() {
        
        fileChooser.setDialogTitle("Save As");
        short userSave = (short) fileChooser.showSaveDialog(tempFrame);
        
        // If the user want to save the file to a specific location
        if(userSave == JFileChooser.APPROVE_OPTION) {
            
            File textFile = fileChooser.getSelectedFile();
            
            if(start) tabIndex = (short) chooseTab.getSelectedIndex();
            else tabIndex = 0;
            
            labelList.get(tabIndex).setText(textFile.getName() + "      ");
            pathList.set(tabIndex, ""+textFile);
            SaveClass.saveAsFile(""+textFile);

        }// end if
    }// end saveAsFunction()
    
    /**
     * Method Name : findWord
     * Purpose     : highlight the words which are similar to the find word
     */
    private void findWord() {
        
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        
        String word = findField.getText();  // get the word from the find JTextField
        int findLen = word.length();        // Get the length of the word to be found
        
        highList.get(tabIndex).removeAllHighlights();
        
        // Highlight all the similar word in the textArea
        if(findLen > 0) {
            int count = 0;
            String text = textList.get(tabIndex).getText();

            index = text.indexOf(word, 0);
            while(index >= 0) {
                try {    
                    count++;
                    highList.get(tabIndex).addHighlight(index, index + findLen, DefaultHighlighter.DefaultPainter);
                    index = text.indexOf(word, index + findLen);
                } catch(BadLocationException ex) {
                    System.out.println("Highlight text error");
                }// end try/catch
            }// end while()
            
            // Display the number of similar words found
            countLabel.setText("" + count);
        }
        else { 
            countLabel.setText(null); 
        }// end if
    }// end findWord()
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        langGroup = new javax.swing.ButtonGroup();
        popMenu = new javax.swing.JPopupMenu();
        compilePop = new javax.swing.JMenuItem();
        runPop = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        cutPop = new javax.swing.JMenuItem();
        copyPop = new javax.swing.JMenuItem();
        pastePop = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        selectPop = new javax.swing.JMenuItem();
        colorGroup = new javax.swing.ButtonGroup();
        dialog = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        helpOK = new javax.swing.JButton();
        container = new javax.swing.JPanel();
        toolPanel = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37));
        newTool = new javax.swing.JButton();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37));
        openTool = new javax.swing.JButton();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37));
        saveTool = new javax.swing.JButton();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37));
        jToolBar2 = new javax.swing.JToolBar();
        filler10 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37));
        undoTool = new javax.swing.JButton();
        filler11 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37));
        redoTool = new javax.swing.JButton();
        filler16 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37));
        jToolBar3 = new javax.swing.JToolBar();
        filler12 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37));
        compileTool = new javax.swing.JButton();
        filler13 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37));
        compile_runTool = new javax.swing.JButton();
        filler15 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37));
        runTool = new javax.swing.JButton();
        filler14 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37), new java.awt.Dimension(2, 37));
        mainSplit = new javax.swing.JSplitPane();
        leftSplit = new javax.swing.JSplitPane();
        top = new javax.swing.JPanel();
        FilePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fileDisplayPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeFormat = new javax.swing.JTree();
        bottom = new javax.swing.JPanel();
        recentPane = new javax.swing.JScrollPane();
        recentFileBottom = new javax.swing.JPanel();
        accessButton = new javax.swing.JButton();
        rightSplit = new javax.swing.JSplitPane();
        topRight = new javax.swing.JPanel();
        motionPanel = new javax.swing.JPanel();
        motionLeft = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        findPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        findField = new javax.swing.JTextField();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 23), new java.awt.Dimension(5, 23), new java.awt.Dimension(5, 23));
        nextButton = new javax.swing.JButton();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 23), new java.awt.Dimension(5, 23), new java.awt.Dimension(5, 23));
        findToggle = new javax.swing.JToggleButton();
        replacePanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        replaceField = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 23), new java.awt.Dimension(5, 23), new java.awt.Dimension(5, 23));
        replaceButton = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 23), new java.awt.Dimension(5, 23), new java.awt.Dimension(5, 23));
        replaceAllButton = new javax.swing.JButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        motionRight = new javax.swing.JPanel();
        matchPanel = new javax.swing.JPanel();
        matchCount = new javax.swing.JPanel();
        countLabel = new javax.swing.JLabel();
        matchName = new javax.swing.JPanel();
        matchLabel = new javax.swing.JLabel();
        cursorPanel = new javax.swing.JPanel();
        cursorState = new javax.swing.JLabel();
        freeSpace = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        bottomRight = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        inputPanel = new javax.swing.JPanel();
        inputScroll = new javax.swing.JScrollPane();
        inputArea = new javax.swing.JTextArea();
        outputPanel = new javax.swing.JPanel();
        outputScroll = new javax.swing.JScrollPane();
        outputArea = new javax.swing.JTextArea();
        buttonPanel = new javax.swing.JPanel();
        langPanel = new javax.swing.JPanel();
        javaRadio = new javax.swing.JRadioButton();
        cRadio = new javax.swing.JRadioButton();
        cppRadio = new javax.swing.JRadioButton();
        pyRadio = new javax.swing.JRadioButton();
        runCompile = new javax.swing.JPanel();
        runButton = new javax.swing.JButton();
        compileButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        fileNew = new javax.swing.JMenuItem();
        fileOpen = new javax.swing.JMenuItem();
        fileSave = new javax.swing.JMenuItem();
        fileSaveAs = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        fileExit = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        editUndo = new javax.swing.JMenuItem();
        editRedo = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        editCut = new javax.swing.JMenuItem();
        editCopy = new javax.swing.JMenuItem();
        editPaste = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        editFind = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        editClear = new javax.swing.JMenuItem();
        editSelect = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        editTime = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        viewZoomIn = new javax.swing.JMenuItem();
        viewZoomOut = new javax.swing.JMenuItem();
        preferenceMenu = new javax.swing.JMenu();
        formatFont = new javax.swing.JMenuItem();
        colorDisplay = new javax.swing.JMenu();
        blackboard = new javax.swing.JCheckBoxMenuItem();
        cobalt = new javax.swing.JCheckBoxMenuItem();
        idle = new javax.swing.JCheckBoxMenuItem();
        mac = new javax.swing.JCheckBoxMenuItem();
        monokai = new javax.swing.JCheckBoxMenuItem();
        sunburst = new javax.swing.JCheckBoxMenuItem();
        helpMenu = new javax.swing.JMenu();
        help = new javax.swing.JMenuItem();

        compilePop.setText("Compile");
        compilePop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compilePopActionPerformed(evt);
            }
        });
        popMenu.add(compilePop);

        runPop.setText("Run");
        runPop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runPopActionPerformed(evt);
            }
        });
        popMenu.add(runPop);
        popMenu.add(jSeparator7);

        cutPop.setText("Cut");
        cutPop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cutPopActionPerformed(evt);
            }
        });
        popMenu.add(cutPop);

        copyPop.setText("Copy");
        copyPop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyPopActionPerformed(evt);
            }
        });
        popMenu.add(copyPop);

        pastePop.setText("Paste");
        pastePop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pastePopActionPerformed(evt);
            }
        });
        popMenu.add(pastePop);
        popMenu.add(jSeparator6);

        selectPop.setText("Select All");
        selectPop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPopActionPerformed(evt);
            }
        });
        popMenu.add(selectPop);

        dialog.setMinimumSize(new java.awt.Dimension(425, 405));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(425, 367));
        jPanel2.setMinimumSize(new java.awt.Dimension(425, 367));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("  Author :");

        jLabel5.setText("CodEditor v7.5.1        (64 bit)");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo.png"))); // NOI18N

        jLabel7.setForeground(new java.awt.Color(0, 51, 255));
        jLabel7.setText("SoftLovers team");

        jLabel8.setText("Build time : Oct 08 2017 - 02:38:49");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel4))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addGap(32, 32, 32))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(16, 16, 16)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DS Cyberquest", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setTabSize(4);
        jTextArea1.setText("This program is free software; you can distribute it under the DQ Cyberquest as published by the Free Software Development Association (FSDA).\n\nThis editor is distributed and buidl in the hope that it will be useful, but WITHOUT ANY WARRANTY.\n\nYou should have recieved the full version of CodEditor, if not, write to the FSDA, Inc., Tagore Hostel, MNNIT, Teliarganj, UP, 211004, India.\n");
        jTextArea1.setWrapStyleWord(true);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        helpOK.setText("OK");
        helpOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(helpOK, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(helpOK, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dialogLayout = new javax.swing.GroupLayout(dialog.getContentPane());
        dialog.getContentPane().setLayout(dialogLayout);
        dialogLayout.setHorizontalGroup(
            dialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        dialogLayout.setVerticalGroup(
            dialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Code Writer");
        setMinimumSize(new java.awt.Dimension(1, 1));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                restoreRecent(evt);
            }
        });

        container.setLayout(new java.awt.BorderLayout());

        toolPanel.setLayout(new javax.swing.BoxLayout(toolPanel, javax.swing.BoxLayout.LINE_AXIS));

        jToolBar1.setRollover(true);
        jToolBar1.add(filler9);

        newTool.setForeground(new java.awt.Color(255, 255, 255));
        newTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/newFile.png"))); // NOI18N
        newTool.setToolTipText("New File... (Ctrk + N)");
        newTool.setFocusable(false);
        newTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newTool.setIconTextGap(0);
        newTool.setMargin(new java.awt.Insets(0, 0, 0, 0));
        newTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newToolActionPerformed(evt);
            }
        });
        jToolBar1.add(newTool);
        jToolBar1.add(filler6);

        openTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/openFile.png"))); // NOI18N
        openTool.setToolTipText("Open File...  (Ctrl + O)");
        openTool.setFocusable(false);
        openTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openTool.setMargin(new java.awt.Insets(0, 0, 0, 0));
        openTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openToolActionPerformed(evt);
            }
        });
        jToolBar1.add(openTool);
        jToolBar1.add(filler7);

        saveTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/saveFile.png"))); // NOI18N
        saveTool.setToolTipText("Save... (Ctrl + S)");
        saveTool.setFocusable(false);
        saveTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveTool.setMargin(new java.awt.Insets(0, 0, 0, 0));
        saveTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveToolActionPerformed(evt);
            }
        });
        jToolBar1.add(saveTool);
        jToolBar1.add(filler8);

        toolPanel.add(jToolBar1);

        jToolBar2.setRollover(true);
        jToolBar2.add(filler10);

        undoTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/undo.png"))); // NOI18N
        undoTool.setToolTipText("Undo (Ctrl + Z)");
        undoTool.setFocusable(false);
        undoTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        undoTool.setMargin(new java.awt.Insets(0, 0, 0, 0));
        undoTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        undoTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoToolActionPerformed(evt);
            }
        });
        jToolBar2.add(undoTool);
        jToolBar2.add(filler11);

        redoTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/redo.png"))); // NOI18N
        redoTool.setToolTipText("Redo (Ctrl + Y)");
        redoTool.setFocusable(false);
        redoTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        redoTool.setMargin(new java.awt.Insets(0, 0, 0, 0));
        redoTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        redoTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoToolActionPerformed(evt);
            }
        });
        jToolBar2.add(redoTool);
        jToolBar2.add(filler16);

        toolPanel.add(jToolBar2);

        jToolBar3.setRollover(true);
        jToolBar3.add(filler12);

        compileTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/compile.png"))); // NOI18N
        compileTool.setToolTipText("Compile Code");
        compileTool.setFocusable(false);
        compileTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        compileTool.setMargin(new java.awt.Insets(0, 0, 0, 0));
        compileTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        compileTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileToolActionPerformed(evt);
            }
        });
        jToolBar3.add(compileTool);
        jToolBar3.add(filler13);

        compile_runTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/compileRun.png"))); // NOI18N
        compile_runTool.setToolTipText("Compile and Run Code");
        compile_runTool.setFocusable(false);
        compile_runTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        compile_runTool.setMargin(new java.awt.Insets(0, 0, 0, 0));
        compile_runTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        compile_runTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compile_runToolActionPerformed(evt);
            }
        });
        jToolBar3.add(compile_runTool);
        jToolBar3.add(filler15);

        runTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/run.png"))); // NOI18N
        runTool.setToolTipText("Run Code");
        runTool.setFocusable(false);
        runTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        runTool.setIconTextGap(0);
        runTool.setMargin(new java.awt.Insets(0, 0, 0, 0));
        runTool.setMaximumSize(new java.awt.Dimension(33, 33));
        runTool.setMinimumSize(new java.awt.Dimension(33, 33));
        runTool.setPreferredSize(new java.awt.Dimension(33, 33));
        runTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        runTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runToolActionPerformed(evt);
            }
        });
        jToolBar3.add(runTool);
        jToolBar3.add(filler14);

        toolPanel.add(jToolBar3);

        container.add(toolPanel, java.awt.BorderLayout.NORTH);

        leftSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        top.setMinimumSize(new java.awt.Dimension(150, 250));
        top.setPreferredSize(new java.awt.Dimension(150, 300));
        top.setLayout(new java.awt.BorderLayout());

        FilePanel.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        FilePanel.setMinimumSize(new java.awt.Dimension(91, 20));
        FilePanel.setPreferredSize(new java.awt.Dimension(91, 20));
        FilePanel.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(" File Manager");
        jLabel1.setAlignmentX(0.5F);
        jLabel1.setMaximumSize(new java.awt.Dimension(91, 18));
        jLabel1.setMinimumSize(new java.awt.Dimension(91, 18));
        jLabel1.setPreferredSize(new java.awt.Dimension(91, 18));
        FilePanel.add(jLabel1, java.awt.BorderLayout.PAGE_END);

        top.add(FilePanel, java.awt.BorderLayout.NORTH);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(1, 1));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(150, 300));

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Ideone");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Source Packages");
        javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("<default packages>");
        javax.swing.tree.DefaultMutableTreeNode treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("ChooseFont.java");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("ClosableTabbedPane.java");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("Compilation.java");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("EditorPane.java");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("OpenFile.java");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("SaveFile.java");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("TextReplace.java");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("ideoneInterface.java");
        treeNode4.add(treeNode5);
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("ideone");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Ideone.java");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Test Packages");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("<default packages>");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Libraries");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Absolute Layout - AbsoluoteLayout.jar");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("rsyntaxtextarea-3.0.0-SNAPSHOT.jar");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("JDK 1.8 (Default)");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Test Libraries");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("build");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("classes");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("com");
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("company");
        treeNode4.add(treeNode5);
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode(".netbeans_automatic_build");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode(".netbeans_update_resources");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("ChooseFont.class");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("ClosableTabbedPane.class");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Compilation.class");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Execute.class");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("ideoneInterface.class");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("ideoneInterface.form");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("OpenFile.class");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("SaveFile.class");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("TextReplace.class");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("nbproject");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("private");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("private.properties");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("build-impl.xml");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("genfiles.properties");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("project.properties");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("project.xml");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("src");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("ChooseFont.java");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("ClosableTabbedPane.java");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Compilation.java");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("ideoneInterface.form");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("ideoneInterface.java");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("OpenFile.java");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("SaveFile.java");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("TextReplace.java");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("test");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("build.xml");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("file.class");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Ideone.class");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Ideone.c");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Ideone.cpp");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Ideone.java");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("manifest.mf");
        treeNode1.add(treeNode2);
        treeFormat.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(treeFormat);

        javax.swing.GroupLayout fileDisplayPanelLayout = new javax.swing.GroupLayout(fileDisplayPanel);
        fileDisplayPanel.setLayout(fileDisplayPanelLayout);
        fileDisplayPanelLayout.setHorizontalGroup(
            fileDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        fileDisplayPanelLayout.setVerticalGroup(
            fileDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
        );

        top.add(fileDisplayPanel, java.awt.BorderLayout.CENTER);

        leftSplit.setTopComponent(top);

        bottom.setMinimumSize(new java.awt.Dimension(150, 50));
        bottom.setPreferredSize(new java.awt.Dimension(150, 400));
        bottom.setLayout(new java.awt.BorderLayout());

        recentPane.setBackground(new java.awt.Color(255, 255, 255));
        bottom.add(recentPane, java.awt.BorderLayout.CENTER);

        accessButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        accessButton.setText("ACCESS");
        accessButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        accessButton.setPreferredSize(new java.awt.Dimension(87, 23));
        accessButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accessButtonActionPerformed(evt);
            }
        });
        recentFileBottom.add(accessButton);

        bottom.add(recentFileBottom, java.awt.BorderLayout.SOUTH);

        leftSplit.setRightComponent(bottom);

        mainSplit.setLeftComponent(leftSplit);

        rightSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        topRight.setBackground(new java.awt.Color(255, 255, 255));
        topRight.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        topRight.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        topRight.setMinimumSize(new java.awt.Dimension(300, 300));
        topRight.setPreferredSize(new java.awt.Dimension(750, 320));
        topRight.setLayout(new java.awt.BorderLayout());

        motionPanel.setMaximumSize(new java.awt.Dimension(32767, 49));
        motionPanel.setMinimumSize(new java.awt.Dimension(100, 49));
        motionPanel.setPreferredSize(new java.awt.Dimension(100, 49));
        motionPanel.setLayout(new java.awt.GridLayout(1, 0));

        motionLeft.setToolTipText("");
        motionLeft.setRequestFocusEnabled(false);
        motionLeft.setVerifyInputWhenFocusTarget(false);

        jPanel5.setLayout(new java.awt.GridLayout(2, 0));

        findPanel.setLayout(new javax.swing.BoxLayout(findPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText("  Find What : ");
        jLabel2.setMaximumSize(new java.awt.Dimension(80, 15));
        jLabel2.setMinimumSize(new java.awt.Dimension(80, 15));
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 15));
        findPanel.add(jLabel2);

        findField.setFocusTraversalPolicyProvider(true);
        findField.setMaximumSize(new java.awt.Dimension(170, 23));
        findField.setMinimumSize(new java.awt.Dimension(170, 23));
        findField.setPreferredSize(new java.awt.Dimension(170, 23));
        findField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                findFieldKeyReleased(evt);
            }
        });
        findPanel.add(findField);
        findPanel.add(filler4);

        nextButton.setLabel("Next");
        nextButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        nextButton.setMaximumSize(new java.awt.Dimension(52, 23));
        nextButton.setMinimumSize(new java.awt.Dimension(52, 23));
        nextButton.setPreferredSize(new java.awt.Dimension(52, 23));
        findPanel.add(nextButton);
        findPanel.add(filler5);

        findToggle.setText("Enable");
        findToggle.setMaximumSize(new java.awt.Dimension(68, 23));
        findToggle.setMinimumSize(new java.awt.Dimension(68, 23));
        findToggle.setPreferredSize(new java.awt.Dimension(68, 23));
        findToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findToggleActionPerformed(evt);
            }
        });
        findPanel.add(findToggle);

        jPanel5.add(findPanel);

        replacePanel.setLayout(new javax.swing.BoxLayout(replacePanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setText("  Replace With : ");
        jLabel3.setMaximumSize(new java.awt.Dimension(80, 15));
        jLabel3.setMinimumSize(new java.awt.Dimension(80, 15));
        jLabel3.setPreferredSize(new java.awt.Dimension(80, 15));
        replacePanel.add(jLabel3);

        replaceField.setMaximumSize(new java.awt.Dimension(170, 23));
        replaceField.setMinimumSize(new java.awt.Dimension(170, 23));
        replaceField.setPreferredSize(new java.awt.Dimension(170, 23));
        replacePanel.add(replaceField);
        replacePanel.add(filler1);

        replaceButton.setText("Replace");
        replaceButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        replaceButton.setMaximumSize(new java.awt.Dimension(52, 23));
        replaceButton.setMinimumSize(new java.awt.Dimension(52, 23));
        replaceButton.setPreferredSize(new java.awt.Dimension(52, 23));
        replaceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replaceButtonActionPerformed(evt);
            }
        });
        replacePanel.add(replaceButton);
        replacePanel.add(filler2);

        replaceAllButton.setText("Replace All");
        replaceAllButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        replaceAllButton.setMaximumSize(new java.awt.Dimension(68, 23));
        replaceAllButton.setMinimumSize(new java.awt.Dimension(68, 23));
        replaceAllButton.setPreferredSize(new java.awt.Dimension(68, 23));
        replaceAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replaceAllButtonActionPerformed(evt);
            }
        });
        replacePanel.add(replaceAllButton);
        replacePanel.add(filler3);

        jPanel5.add(replacePanel);

        javax.swing.GroupLayout motionLeftLayout = new javax.swing.GroupLayout(motionLeft);
        motionLeft.setLayout(motionLeftLayout);
        motionLeftLayout.setHorizontalGroup(
            motionLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        motionLeftLayout.setVerticalGroup(
            motionLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
        );

        motionPanel.add(motionLeft);

        motionRight.setLayout(new java.awt.GridLayout(2, 2, 2, 2));

        matchPanel.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        matchCount.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 5));
        matchCount.add(countLabel);

        matchPanel.add(matchCount);

        matchName.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        matchLabel.setText("Matches");
        matchName.add(matchLabel);

        matchPanel.add(matchName);

        motionRight.add(matchPanel);

        cursorPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 2));

        cursorState.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cursorState.setMaximumSize(new java.awt.Dimension(90, 22));
        cursorState.setMinimumSize(new java.awt.Dimension(90, 22));
        cursorState.setPreferredSize(new java.awt.Dimension(90, 22));
        cursorPanel.add(cursorState);

        freeSpace.setForeground(new java.awt.Color(240, 240, 240));
        freeSpace.setDoubleBuffered(false);
        freeSpace.setEnabled(false);
        freeSpace.setFocusable(false);
        freeSpace.setMaximumSize(new java.awt.Dimension(28, 23));
        freeSpace.setMinimumSize(new java.awt.Dimension(28, 23));
        freeSpace.setPreferredSize(new java.awt.Dimension(28, 23));

        javax.swing.GroupLayout freeSpaceLayout = new javax.swing.GroupLayout(freeSpace);
        freeSpace.setLayout(freeSpaceLayout);
        freeSpaceLayout.setHorizontalGroup(
            freeSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        freeSpaceLayout.setVerticalGroup(
            freeSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        cursorPanel.add(freeSpace);

        motionRight.add(cursorPanel);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 196, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        motionRight.add(jPanel6);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 196, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        motionRight.add(jPanel1);

        motionPanel.add(motionRight);

        topRight.add(motionPanel, java.awt.BorderLayout.SOUTH);

        rightSplit.setTopComponent(topRight);

        bottomRight.setLayout(new java.awt.BorderLayout());

        inputScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        inputArea.setColumns(20);
        inputArea.setRows(5);
        inputScroll.setViewportView(inputArea);

        javax.swing.GroupLayout inputPanelLayout = new javax.swing.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(inputScroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
        );
        inputPanelLayout.setVerticalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(inputScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
        );

        tabbedPane.addTab(" Enter Input (stdin)", inputPanel);

        outputPanel.setLayout(new java.awt.BorderLayout());

        outputScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        outputArea.setEditable(false);
        outputArea.setColumns(20);
        outputArea.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        outputArea.setLineWrap(true);
        outputArea.setRows(5);
        outputScroll.setViewportView(outputArea);

        outputPanel.add(outputScroll, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab(" stdout ", outputPanel);

        bottomRight.add(tabbedPane, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new java.awt.GridLayout(1, 0));

        langPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        langGroup.add(javaRadio);
        javaRadio.setSelected(true);
        javaRadio.setText("Java 8");
        javaRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javaRadioActionPerformed(evt);
            }
        });
        langPanel.add(javaRadio);

        langGroup.add(cRadio);
        cRadio.setText("C ");
        cRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cRadioActionPerformed(evt);
            }
        });
        langPanel.add(cRadio);

        langGroup.add(cppRadio);
        cppRadio.setText("C++");
        cppRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cppRadioActionPerformed(evt);
            }
        });
        langPanel.add(cppRadio);

        langGroup.add(pyRadio);
        pyRadio.setText("Python");
        pyRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pyRadioActionPerformed(evt);
            }
        });
        langPanel.add(pyRadio);

        buttonPanel.add(langPanel);

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5);
        flowLayout1.setAlignOnBaseline(true);
        runCompile.setLayout(flowLayout1);

        runButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        runButton.setText("RUN");
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });
        runCompile.add(runButton);

        compileButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        compileButton.setText("COMPILE");
        compileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileButtonActionPerformed(evt);
            }
        });
        runCompile.add(compileButton);

        buttonPanel.add(runCompile);

        bottomRight.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        rightSplit.setRightComponent(bottomRight);

        mainSplit.setRightComponent(rightSplit);

        container.add(mainSplit, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");

        fileNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        fileNew.setText("New");
        fileNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileNewActionPerformed(evt);
            }
        });
        fileMenu.add(fileNew);

        fileOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        fileOpen.setText("Open");
        fileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileOpenActionPerformed(evt);
            }
        });
        fileMenu.add(fileOpen);

        fileSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        fileSave.setText("Save");
        fileSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileSaveActionPerformed(evt);
            }
        });
        fileMenu.add(fileSave);

        fileSaveAs.setText("Save As");
        fileSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileSaveAsActionPerformed(evt);
            }
        });
        fileMenu.add(fileSaveAs);
        fileMenu.add(jSeparator8);

        fileExit.setText("Exit");
        fileExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileExitActionPerformed(evt);
            }
        });
        fileMenu.add(fileExit);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        editUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        editUndo.setText("Undo");
        editUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editUndoActionPerformed(evt);
            }
        });
        editMenu.add(editUndo);

        editRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        editRedo.setText("Redo");
        editRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRedoActionPerformed(evt);
            }
        });
        editMenu.add(editRedo);
        editMenu.add(jSeparator2);

        editCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        editCut.setText("Cut");
        editCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCutActionPerformed(evt);
            }
        });
        editMenu.add(editCut);

        editCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        editCopy.setText("Copy");
        editCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCopyActionPerformed(evt);
            }
        });
        editMenu.add(editCopy);

        editPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        editPaste.setText("Paste");
        editPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPasteActionPerformed(evt);
            }
        });
        editMenu.add(editPaste);
        editMenu.add(jSeparator4);

        editFind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        editFind.setText("Find / Replace...");
        editFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFindActionPerformed(evt);
            }
        });
        editMenu.add(editFind);
        editMenu.add(jSeparator3);

        editClear.setText("ClearAll");
        editClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editClearActionPerformed(evt);
            }
        });
        editMenu.add(editClear);

        editSelect.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        editSelect.setText("SelectAll");
        editSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSelectActionPerformed(evt);
            }
        });
        editMenu.add(editSelect);
        editMenu.add(jSeparator5);

        editTime.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        editTime.setText("Time/Date");
        editTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editTimeActionPerformed(evt);
            }
        });
        editMenu.add(editTime);

        menuBar.add(editMenu);

        viewMenu.setText("View");

        viewZoomIn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, java.awt.event.InputEvent.CTRL_MASK));
        viewZoomIn.setText("Zoom In");
        viewZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewZoomInActionPerformed(evt);
            }
        });
        viewMenu.add(viewZoomIn);

        viewZoomOut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.CTRL_MASK));
        viewZoomOut.setText("Zoom Out");
        viewZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewZoomOutActionPerformed(evt);
            }
        });
        viewMenu.add(viewZoomOut);

        menuBar.add(viewMenu);

        preferenceMenu.setText("Preferences");

        formatFont.setText("Font");
        formatFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formatFontActionPerformed(evt);
            }
        });
        preferenceMenu.add(formatFont);

        colorDisplay.setText("Color Scheme          ");

        colorGroup.add(blackboard);
        blackboard.setText("Blackboard");
        blackboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blackboardActionPerformed(evt);
            }
        });
        colorDisplay.add(blackboard);

        colorGroup.add(cobalt);
        cobalt.setText("Cobalt");
        cobalt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cobaltActionPerformed(evt);
            }
        });
        colorDisplay.add(cobalt);

        colorGroup.add(idle);
        idle.setSelected(true);
        idle.setText("IDLE");
        idle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idleActionPerformed(evt);
            }
        });
        colorDisplay.add(idle);

        colorGroup.add(mac);
        mac.setText("Mac Classic");
        mac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                macActionPerformed(evt);
            }
        });
        colorDisplay.add(mac);

        colorGroup.add(monokai);
        monokai.setText("Monokai Bright");
        monokai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monokaiActionPerformed(evt);
            }
        });
        colorDisplay.add(monokai);

        colorGroup.add(sunburst);
        sunburst.setText("Sunburst");
        sunburst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sunburstActionPerformed(evt);
            }
        });
        colorDisplay.add(sunburst);

        preferenceMenu.add(colorDisplay);

        menuBar.add(preferenceMenu);

        helpMenu.setText("Help");

        help.setText("About CodEditor");
        help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        helpMenu.add(help);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Purpose : Create a new tab of Untitled
     * @param evt 
     */
    private void fileNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileNewActionPerformed
        createTab("Untitled");
    }//GEN-LAST:event_fileNewActionPerformed

    /**
     * Purpose : Perform Undo operation using Ctrl + Z
     * @param evt 
     */
    private void editUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editUndoActionPerformed
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        
        undoList.get(tabIndex).undo();
    }//GEN-LAST:event_editUndoActionPerformed

    private void editPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPasteActionPerformed

    }//GEN-LAST:event_editPasteActionPerformed

    /**
     * Purpose : Add today's date and time on the textArea, if needed
     * @param evt 
     */
    private void editTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editTimeActionPerformed
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        
        if(selectBol == true) {
            textList.get(tabIndex).setText(null);
            selectBol = false;
        }
        textList.get(tabIndex).append(dateFormat.format(date));
    }//GEN-LAST:event_editTimeActionPerformed
    
    /**
     * Purpose : Change the font ands style of the fond using ChooseFont class
     * @param evt 
     */
    private void formatFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formatFontActionPerformed
        closeIdeone = false;
        ChooseFont.displayFont(tempFont.getSize());
    }//GEN-LAST:event_formatFontActionPerformed

    /**
     * Purpose : Open a new file from the directory using JFileChooser
     * @param evt 
     */
    private void fileOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileOpenActionPerformed
        fileChooser.setDialogTitle("Open File");
        short userOpen = (short) fileChooser.showOpenDialog(tempFrame);

        if(userOpen == JFileChooser.APPROVE_OPTION) {
            File textFile = fileChooser.getSelectedFile();
            openFunction(textFile);
        }// end if
    }//GEN-LAST:event_fileOpenActionPerformed

    /**
     * Purpose : Save the currently opened file
     * @param evt 
     */
    private void fileSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveActionPerformed
        if(start){
            saveFunction(pathList.get(chooseTab.getSelectedIndex()));
        } else {
            saveFunction(pathList.get(0));
        }// end if
    }//GEN-LAST:event_fileSaveActionPerformed
    
    /**
     * Purpose : Save the currently opened file to a different path
     * @param evt 
     */
    private void fileSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveAsActionPerformed
        saveAsFunction();
    }//GEN-LAST:event_fileSaveAsActionPerformed
    
    private void fileExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileExitActionPerformed
        closeFunction("exit");
    }//GEN-LAST:event_fileExitActionPerformed

    private void editCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCutActionPerformed
        System.out.println("Edit Cut");
    }//GEN-LAST:event_editCutActionPerformed

    private void editCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCopyActionPerformed
        System.out.println("Edit Copy");
    }//GEN-LAST:event_editCopyActionPerformed

    /**
     * Purpose : Clear the currently opened tab
     * @param evt 
     */
    private void editClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editClearActionPerformed
        if(start) {
            textList.get(chooseTab.getSelectedIndex()).setText("");
        } else {
            textList.get(0).setText("");
        }// end if
    }//GEN-LAST:event_editClearActionPerformed

    /**
     * Purpose : Perform selectAll on the recently opened Tab
     * @param evt 
     */
    private void editSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSelectActionPerformed
        if(start) {
            tabIndex = (short) chooseTab.getSelectedIndex();
        } else {
            tabIndex = 0;
        }// end if
        textList.get(tabIndex).requestFocusInWindow();
        textList.get(tabIndex).selectAll();
        selectBol = true;
    }//GEN-LAST:event_editSelectActionPerformed

    /**
     * Purpose : Zoom in the tab font
     * @param evt 
     */
    private void viewZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewZoomInActionPerformed
        if(start) {
            tabIndex = (short) chooseTab.getSelectedIndex();
        } else {
            tabIndex = 0;
        }
        font = textList.get(tabIndex).getFont();
        tempFont = new Font(font.getFontName(), font.getStyle(), font.getSize() + 2);
        textList.get(tabIndex).setFont(tempFont);
    }//GEN-LAST:event_viewZoomInActionPerformed

    /**
     * Purpose : Zoom out the tab font
     * @param evt 
     */
    private void viewZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewZoomOutActionPerformed
        if(start) {
            tabIndex = (short) chooseTab.getSelectedIndex();
        } else {
            tabIndex = 0;
        }
        font = textList.get(tabIndex).getFont();
        tempFont = new Font(font.getFontName(), font.getStyle(), font.getSize() - 2);
        textList.get(tabIndex).setFont(tempFont);
    }//GEN-LAST:event_viewZoomOutActionPerformed
    
    /**
     * Method Name : runCode
     * Purpose : Run the currently opened tab in the compiler
     */
    private void runCode() {
        
        if(start) {
            tabIndex = (short) chooseTab.getSelectedIndex();
        } else {
            tabIndex = 0;
        }// end if
        
        outputArea.setText("");         // clear the outputArea
        tabbedPane.setSelectedIndex(1); // open the outputArea
        
        try {
            switch(language) {
                case 1: Compiler.runJava(pathList.get(tabIndex), outputArea, inputArea); break;
                case 2: Compiler.runC(pathList.get(tabIndex), outputArea, inputArea); break;
                case 3: Compiler.runCpp(pathList.get(tabIndex), outputArea, inputArea); break;
            }
        } catch(IOException e) {}
    }// end runCode()
    
    /**
     * Purpose : Run the Code when bottom right RUN button is clicked
     */
    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        runCode();
    }//GEN-LAST:event_runButtonActionPerformed
    
    /**
     * Method Name : compileCode
     * Purpose : compile the currently opened tab in the compiler and check
     *           for errors
     */
    private void compileCode()
    {
        if(start) {
            tabIndex = (short) chooseTab.getSelectedIndex();
        } else {
            tabIndex = 0;
        }
        
        saveFunction(pathList.get(tabIndex));
        
        outputArea.setText("");
        tabbedPane.setSelectedIndex(1);
        
        switch(language) {
            case 1: Compiler.compileJava(pathList.get(tabIndex), outputArea); break;
            case 2: Compiler.compileC(pathList.get(tabIndex), outputArea); break;
            case 3: Compiler.compileCpp(pathList.get(tabIndex), outputArea); break;
        }
        
    }
    
    private void compileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compileButtonActionPerformed
        compileCode();
    }//GEN-LAST:event_compileButtonActionPerformed
    
    /**
     * Purpose : Enable the find/Replace area on the application to
     *           allow the user to use it
     * @param evt 
     */
    private void editFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editFindActionPerformed
        findReplace(true);
    }//GEN-LAST:event_editFindActionPerformed
    
    /**
     * Purpose : Enable the user to close the find/Replace feature at any time
     * @param evt 
     */
    private void findToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findToggleActionPerformed
        if(findToggle.isSelected()) {
            findToggle.setText("Disable");
            findReplace(true);              // Enable the feature
        }
        else {
            findToggle.setText("Enable");
            countLabel.setText("");
            findReplace(false);             // Disable the feature
            try {
                highList.get(tabIndex).removeAllHighlights();
            } catch(Exception e) { System.out.println("Toggle Error"); }
        }
    }//GEN-LAST:event_findToggleActionPerformed

    /**
     * Purpose : Find the similar word on the tab whenever the key is released on the tab
     */
    private void findFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_findFieldKeyReleased
        findWord();
    }//GEN-LAST:event_findFieldKeyReleased
    
    /**
     * Purpose : The current tab will be running Cpp files
     */
    private void cppRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cppRadioActionPerformed
        language = 3;
    }//GEN-LAST:event_cppRadioActionPerformed

    /**
     * Purpose : The current tab will be running C files
     */
    private void cRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cRadioActionPerformed
        language = 2;
    }//GEN-LAST:event_cRadioActionPerformed
    
    /**
     * Purpose : The current tab will be running java files
     */
    private void javaRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javaRadioActionPerformed
        language = 1;
    }//GEN-LAST:event_javaRadioActionPerformed
    
    /**
     * Purpose : The current tab will be running python files - not working
     */
    private void pyRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pyRadioActionPerformed
        language = 4;
    }//GEN-LAST:event_pyRadioActionPerformed
    
    /**
     * Purpose : Cut the highlighted text on the textArea
     * @param evt 
     */
    private void cutPopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cutPopActionPerformed
        textSelected = textList.get(tabIndex).getSelectedText();
        textList.get(tabIndex).replaceRange("", textList.get(tabIndex).getSelectionStart(), textList.get(tabIndex).getSelectionEnd());
    }//GEN-LAST:event_cutPopActionPerformed

    /**
     * Purpose : Copy the highlighted text on the textArea
     * @param evt 
     */
    private void copyPopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyPopActionPerformed
        textSelected = textList.get(tabIndex).getSelectedText();
    }//GEN-LAST:event_copyPopActionPerformed

    /**
     * Purpose : Paste the highlighted text on the textArea
     * @param evt 
     */
    private void pastePopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pastePopActionPerformed
        textList.get(tabIndex).insert(textSelected, textList.get(tabIndex).getCaretPosition());
    }//GEN-LAST:event_pastePopActionPerformed

    /**
     * Purpose : SelectAl in the textArea
     * @param evt 
     */
    private void selectPopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPopActionPerformed
        textList.get(tabIndex).selectAll();
    }//GEN-LAST:event_selectPopActionPerformed

    /**
     * Purpose : Replace First similar word in the textArea
     * @param evt 
     */
    private void replaceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceButtonActionPerformed
        if(findField.getText().trim().length() > 0) {
        
            if (start) tabIndex = (short) chooseTab.getSelectedIndex();
            else tabIndex = 0;
        
            String text = textList.get(tabIndex).getText().
            replaceAll(findField.getText(), replaceField.getText());
            textList.get(tabIndex).setText(text);
            findWord();
        }
    }//GEN-LAST:event_replaceButtonActionPerformed
    
    /**
     * Purpose : Replace All similar word in the textArea
     * @param evt 
     */
    private void replaceAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceAllButtonActionPerformed
        if(findField.getText().trim().length() > 0) {
            if(start) tabIndex = (short) chooseTab.getSelectedIndex();
            else tabIndex = 0;
            
            String text = textList.get(tabIndex).getText().
            replaceAll(findField.getText(), replaceField.getText()); 
            textList.get(tabIndex).setText(text);
        }
    }//GEN-LAST:event_replaceAllButtonActionPerformed
    
    /**
     * Purpose : Open the selected file in the recent area when access button is clicked
     * @param evt 
     */
    private void accessButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accessButtonActionPerformed
        try { openFunction(new File(recentArray.get(recentList.getSelectedIndex()))); }
        catch(Exception e) { System.out.println("Access Recent Error"); }
    }//GEN-LAST:event_accessButtonActionPerformed

    /**
     * Purpose : Open file when open option is clicked from toolBar
     * @param evt 
     */
    private void openToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openToolActionPerformed
        fileChooser.setDialogTitle("Open File");
        short userOpen = (short) fileChooser.showOpenDialog(tempFrame);

        if(userOpen == JFileChooser.APPROVE_OPTION) {
            File textFile = fileChooser.getSelectedFile();
            openFunction(textFile);
        }
    }//GEN-LAST:event_openToolActionPerformed

    /**
     * Purpose : Save file when save option is clicked from toolBar
     * @param evt 
     */
    private void saveToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveToolActionPerformed
        if(start) saveFunction(pathList.get(chooseTab.getSelectedIndex()));
        else saveFunction(pathList.get(0));
    }//GEN-LAST:event_saveToolActionPerformed

    /**
     * Purpose : Run the currently tab in the compiler when run button is clicked on toolBar
     * @param evt 
     */
    private void runToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runToolActionPerformed
        runCode();
    }//GEN-LAST:event_runToolActionPerformed

    /**
     * Purpose : Compile the currently tab in the compiler when compile button is clicked on toolBar
     * @param evt 
     */
    private void compileToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compileToolActionPerformed
        compileCode();
    }//GEN-LAST:event_compileToolActionPerformed

    /**
     * Purpose : Open a new file when open is clicked on the toolbar
     * @param evt 
     */
    private void newToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newToolActionPerformed
        createTab("Untitled");
    }//GEN-LAST:event_newToolActionPerformed

    /**
     * Purpose : Run and compile the currently tab in the compiler when run/compile button is clicked on toolBar
     * @param evt 
     */
    private void compile_runToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compile_runToolActionPerformed
        compileCode();
        runCode();
    }//GEN-LAST:event_compile_runToolActionPerformed

    /**
     * Purpose : Compile the code when 'compile' is clicked in the popUpmenu on right click
     * @param evt 
     */
    private void compilePopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compilePopActionPerformed
        compileCode();
    }//GEN-LAST:event_compilePopActionPerformed

    /**
     * Purpose : run the code when 'Run' is clicked in the popUpmenu on right click
     * @param evt 
     */
    private void runPopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runPopActionPerformed
        runCode();
    }//GEN-LAST:event_runPopActionPerformed

    /**
     * Purpose : Change the tab preference to blackboard style
     * @param evt 
     */
    private void blackboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blackboardActionPerformed
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        PrefStyle.blackboard(textList.get(tabIndex));
    }//GEN-LAST:event_blackboardActionPerformed

    /**
     * Purpose : Change the tab preference to cobalt style
     * @param evt 
     */
    private void cobaltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cobaltActionPerformed
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        PrefStyle.cobalt(textList.get(tabIndex));
    }//GEN-LAST:event_cobaltActionPerformed

    /**
     * Purpose : Change the tab preference to idle style
     * @param evt 
     */
    private void idleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idleActionPerformed
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        PrefStyle.idle(textList.get(tabIndex));
    }//GEN-LAST:event_idleActionPerformed

    /**
     * Purpose : Change the tab preference to mac style
     * @param evt 
     */
    private void macActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_macActionPerformed
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        PrefStyle.mac(textList.get(tabIndex));
    }//GEN-LAST:event_macActionPerformed

    /**
     * Purpose : Change the tab preference to monokai style
     * @param evt 
     */
    private void monokaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monokaiActionPerformed
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        PrefStyle.monokai(textList.get(tabIndex));
    }//GEN-LAST:event_monokaiActionPerformed

    /**
     * Purpose : Change the tab preference to sunburst style
     * @param evt 
     */
    private void sunburstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sunburstActionPerformed
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        PrefStyle.sunburst(textList.get(tabIndex));
    }//GEN-LAST:event_sunburstActionPerformed

    /**
     * Purpose : Close the About app when ok is clicked
     * @param evt 
     */
    private void helpOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpOKActionPerformed
        dialog.setVisible(false);
        dialog.setAutoRequestFocus(false);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_helpOKActionPerformed

    /**
     * Purpose : Display the about app
     * @param evt 
     */
    private void helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpActionPerformed
        dialog.setVisible(true);
        dialog.setAutoRequestFocus(true);
        dialog.setLocationRelativeTo(null);
    }//GEN-LAST:event_helpActionPerformed
    
    /**
     * Purpose : Perform redo operation on Ctrl + Y
     * @param evt 
     */
    private void editRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRedoActionPerformed
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        
        undoList.get(tabIndex).redo();
    }//GEN-LAST:event_editRedoActionPerformed
    
    /**
     * Method Name : restoreRecent
     * Purpose : Save the recently opened files in the file.txt
     * @param evt
     */
    private void restoreRecent(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_restoreRecent
        
        try {
            FileWriter writer = new FileWriter("file.txt");
            BufferedWriter buffer = new BufferedWriter(writer);
            
            int max = recentArray.size();       // Maximum files to be saved in the recent files txt
            
            if (max > 11) {             // Limit the storing path to 10
                max = 10;
            }// end if
            
            for(int i = 0; i < max; i++) {
                buffer.write(recentArray.get(i));
                buffer.newLine();
            }// end for
            
            buffer.close();
            
        } catch (Exception e) {
            System.out.println("Recent files restore error");
        }// end try/catch
    }//GEN-LAST:event_restoreRecent
    
    /**
     * Purpose : Perform undo operation when undo button is pressed in the toolbar
     * @param evt 
     */
    private void undoToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoToolActionPerformed
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        
        undoList.get(tabIndex).undo();
    }//GEN-LAST:event_undoToolActionPerformed
    
    /**
     * Purpose : Perform redo operation when redo button is pressed in the toolbar
     * @param evt 
     */
    private void redoToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoToolActionPerformed
        if(start) tabIndex = (short) chooseTab.getSelectedIndex();
        else tabIndex = 0;
        
        undoList.get(tabIndex).redo();
    }//GEN-LAST:event_redoToolActionPerformed
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ideoneInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ideoneInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ideoneInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ideoneInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        java.awt.EventQueue.invokeLater(() -> {
            new splash().setVisible(true);
        });
    }
    
    public static void runIdeone() {
        java.awt.EventQueue.invokeLater(() -> {
            
            new ideoneInterface().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel FilePanel;
    private javax.swing.JButton accessButton;
    private javax.swing.JCheckBoxMenuItem blackboard;
    private javax.swing.JPanel bottom;
    private javax.swing.JPanel bottomRight;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JRadioButton cRadio;
    private javax.swing.JCheckBoxMenuItem cobalt;
    private javax.swing.JMenu colorDisplay;
    private javax.swing.ButtonGroup colorGroup;
    private javax.swing.JButton compileButton;
    private javax.swing.JMenuItem compilePop;
    private javax.swing.JButton compileTool;
    private javax.swing.JButton compile_runTool;
    private javax.swing.JPanel container;
    private javax.swing.JMenuItem copyPop;
    private javax.swing.JLabel countLabel;
    private javax.swing.JRadioButton cppRadio;
    private javax.swing.JPanel cursorPanel;
    public static javax.swing.JLabel cursorState;
    private javax.swing.JMenuItem cutPop;
    private javax.swing.JDialog dialog;
    private javax.swing.JMenuItem editClear;
    private javax.swing.JMenuItem editCopy;
    private javax.swing.JMenuItem editCut;
    private javax.swing.JMenuItem editFind;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem editPaste;
    private javax.swing.JMenuItem editRedo;
    private javax.swing.JMenuItem editSelect;
    private javax.swing.JMenuItem editTime;
    private javax.swing.JMenuItem editUndo;
    private javax.swing.JPanel fileDisplayPanel;
    private javax.swing.JMenuItem fileExit;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem fileNew;
    private javax.swing.JMenuItem fileOpen;
    private javax.swing.JMenuItem fileSave;
    private javax.swing.JMenuItem fileSaveAs;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler10;
    private javax.swing.Box.Filler filler11;
    private javax.swing.Box.Filler filler12;
    private javax.swing.Box.Filler filler13;
    private javax.swing.Box.Filler filler14;
    private javax.swing.Box.Filler filler15;
    private javax.swing.Box.Filler filler16;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JTextField findField;
    private javax.swing.JPanel findPanel;
    private javax.swing.JToggleButton findToggle;
    private javax.swing.JMenuItem formatFont;
    private javax.swing.JPanel freeSpace;
    private javax.swing.JMenuItem help;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JButton helpOK;
    private javax.swing.JCheckBoxMenuItem idle;
    private javax.swing.JTextArea inputArea;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JScrollPane inputScroll;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JRadioButton javaRadio;
    private javax.swing.ButtonGroup langGroup;
    private javax.swing.JPanel langPanel;
    private javax.swing.JSplitPane leftSplit;
    private javax.swing.JCheckBoxMenuItem mac;
    private javax.swing.JSplitPane mainSplit;
    private javax.swing.JPanel matchCount;
    private javax.swing.JLabel matchLabel;
    private javax.swing.JPanel matchName;
    private javax.swing.JPanel matchPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JCheckBoxMenuItem monokai;
    private javax.swing.JPanel motionLeft;
    private javax.swing.JPanel motionPanel;
    private javax.swing.JPanel motionRight;
    private javax.swing.JButton newTool;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton openTool;
    public static javax.swing.JTextArea outputArea;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JScrollPane outputScroll;
    private javax.swing.JMenuItem pastePop;
    public static javax.swing.JPopupMenu popMenu;
    private javax.swing.JMenu preferenceMenu;
    private javax.swing.JRadioButton pyRadio;
    private javax.swing.JPanel recentFileBottom;
    private javax.swing.JScrollPane recentPane;
    private javax.swing.JButton redoTool;
    private javax.swing.JButton replaceAllButton;
    private javax.swing.JButton replaceButton;
    private javax.swing.JTextField replaceField;
    private javax.swing.JPanel replacePanel;
    private javax.swing.JSplitPane rightSplit;
    private javax.swing.JButton runButton;
    private javax.swing.JPanel runCompile;
    private javax.swing.JMenuItem runPop;
    private javax.swing.JButton runTool;
    private javax.swing.JButton saveTool;
    private javax.swing.JMenuItem selectPop;
    private javax.swing.JCheckBoxMenuItem sunburst;
    public static javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel toolPanel;
    private javax.swing.JPanel top;
    private javax.swing.JPanel topRight;
    private javax.swing.JTree treeFormat;
    private javax.swing.JButton undoTool;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenuItem viewZoomIn;
    private javax.swing.JMenuItem viewZoomOut;
    // End of variables declaration//GEN-END:variables
}