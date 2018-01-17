
// import java libraries
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.text.Highlighter;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ashish
 */
public class EditorPane extends JPanel {
    
    private short count;
    private boolean drag, found;
    private int x, y, startCar, startHighlight, endHighlight;
    private String currString;  // Current String
    
    private final Highlighter highlighter;  // Highlighter for find
    
    public JTextArea lineNumber;
    public RSyntaxTextArea editor;      // This Panel's texting area
    
    public static JPopupMenu autoPop;   // Popupmenu Object
    public static int startCurrent, endCurrent;     // Starting and last index of current word
    public static char[] array = new char[1000];    // Max length possible word
    public static Highlighter.HighlightPainter otherWord;   // Highlighter for other words similar to current word
    
    // Node for Trie Data Structure
    public static class Node {
        
        // Each parent has 78 child ( '0' - 'Z'/'z' )
        Node[] children = new Node[78];
        
        // boolean to check whether any previous word is till this node or not
        boolean endOfWord;
        
        /**
         * Constructor
         */
        public Node() {
            for(int i = 0; i < 78; i++) { 
                children[i] = null; 
            }// end for
            
            endOfWord = false;
        }// end Node() constructor
    }// end Node() class
    
    
    public EditorPane() {
        
        // Ceach tab has a separate Trie DS
        Node head = new Node();
        
        this.drag = false;
        
        setLayout(new BorderLayout());
        
        /**
         * Setup RSyntaxTextArea editor
         * */
        
        editor = new RSyntaxTextArea(20, 60);
        
        // Possible syntaxing Styles
        editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
        editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
        editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        
        
        UndoManager undoManager = new UndoManager();
        ideoneInterface.undoList.add(undoManager);
        editor.getDocument().addUndoableEditListener(undoManager);
        
        /**
         * Whenever the caret position is changed or a char is typed,
         * all the possible words related the current word will be highlighted with 'otherWord' highlighter
         */
        editor.addCaretListener((javax.swing.event.CaretEvent evt) -> {
//            tempFinder(evt);
//            tempFinder();
        });
        
        /**
         * MouseListener Events Handler
         */
        editor.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            
            /**
             * If the right-mouse button is clicked, popup the popMenu on the screen
             */
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if(evt.isPopupTrigger()) {
            
                    if(ideoneInterface.start) ideoneInterface.tabIndex = (short) ideoneInterface.chooseTab.getSelectedIndex();
                    else ideoneInterface.tabIndex = 0;

                    ideoneInterface.popMenu.show(editor,evt.getX(),evt.getY());
                }// end if
                
                /**
                 * Display the current position of the caret and
                 * If the text is highlighted, then display the starting and end
                 * X Y coordinate of the highlighted section
                 */
                printCursor();
                drag = false;
            }// end mouseReleased(MouseEvent)
            
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if(!drag) {
                    startCar = editor.getCaretPosition();
                    startHighlight = editor.getCaretLineNumber();
                    drag = true;
                }// end if
            }// mousePressed(MouseEvent)
        });
        
        /**
         * MouseMotionListener Events Handler
         */
        editor.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            
            /**
             * Perform the action when the mouse is dragged on the textArea
             */
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                
                // If any section of the text is selected
                if(editor.getSelectedText() != null) {
                    
                    endHighlight = editor.getCaretLineNumber();
                    y = editor.getCaretLineNumber();
                    x = editor.getCaretPosition() - editor.getLineStartOffsetOfCurrentLine();
                    
                    /**
                     * Display the current position of the caret and
                     * If the text is highlighted, then display the starting and end
                     * X Y coordinate of the highlighted section
                     */
                    printCursor();
                }// end if
            }// end mouseDragged(MouseEvent)
        });
        
        /**
         * KeyListener Event Handler
         */
        editor.addKeyListener(new java.awt.event.KeyAdapter() {
           @Override
           
           /**
            * If any particular keys are pressed
            */
            public void keyPressed(java.awt.event.KeyEvent evt) {
               
                // If Ctrl and SPACE keys are pressed together, display JPopupMenu
                if ((evt.getKeyCode() == KeyEvent.VK_SPACE) && evt.isControlDown()) {
                   
                    count = 0;
                    autoPop = new javax.swing.JPopupMenu();
                    try {
                        
                        // Add all the new words on the textArea to Trie DS
                        getwords(head);
                        
                        // Find the list of all the possible words similar to current word
                        findPossible(head, currString, 0);
                        
                        // If the count of the possible words is greater than 1,
                        // then display them on the PopUpMenu
                        if(count > 0) {
                            Rectangle rectangle = editor.modelToView(editor.getCaretPosition());
                            autoPop.show(editor, rectangle.x, rectangle.y + rectangle.height);
                        }// end if
                    }catch (BadLocationException ex) {
                    }// end try/catch
                }// end if
                
            }// end KeyPressed(KeyEvent)
            
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tempFinder();
            }
        });
        
        this.highlighter = editor.getHighlighter();
        ideoneInterface.highList.add(this.highlighter);

        otherWord = new DefaultHighlighter.DefaultHighlightPainter(new Color(244, 235, 66));
        
        add(editor, BorderLayout.CENTER);
        
    }// end EditorPane()
    
    /**
     * Method Name : findPossible
     * Purpose : Find all the possible words that are similar to the str
     * @param head - this tab Trie DS head
     * @param str - current String 
     * @param index - indexing position of the current checking char
     */
    public void findPossible(Node head, String str, int index) {
        
        // If the current string is there in the Trie, then print all the
        // possible string with this str starting
        if(index == str.length()) {
            getAll(head, index);
            return;
        }// end if
        
        // Check whether the current index char is there in the Trie word
        char c = str.charAt(index);
        if(head.children[c-'0'] != null) {
            array[index] = c;
            findPossible(head.children[c-'0'], str, index+1);
        }// end if
    }// end findPossible(Node, String, int)
    
    /**
     * Method Name : getAll
     * Purpose : Get all the possible words with the sufix as str String
     * @param head - Current Node
     * @param index - current index of the char
     */
    public void getAll(Node head, int index) {
        
        if(head == null) return;
        
        // If the word upto here is there in the list of words in Trie DS
        if(head.endOfWord == true) {
            count++;
            // Add the word to the popup list of words
            addWord(index);
        }// end if
        
        // Get all the possible words
        for(int i = 0; i < 78; i++) {
            if(head.children[i] != null) {
                array[index] = (char)(i + '0');
                getAll(head.children[i], index+1);
            }// end if
        }// end for
    }// end getAll(Node, int)
    
    /**
     * Method Name : addWord
     * Purpose : Add the word to the popup list
     * @param index - last index position of the possible word in the char array
     */
    public void addWord(int index) {
        
        String name = "";
        for(int i = 0; i < index; i++) {
            name += array[i];
        }// end for
        
        JMenuItem option = new JMenuItem();
        option.setText(name);
        
        option.addActionListener((java.awt.event.ActionEvent evt) -> {
            editor.replaceRange(option.getText(), startCurrent, endCurrent);
        });
        autoPop.add(option);
    }// end addWord(int)
    
    /**
     * Method Name : getWords
     * Purpose : Get the all the current words in the textArea
     * @param head - Trie DS head for insertion
     */
    public void getwords(Node head) {
        
        // Dont  include the current word into the trie
        String before = editor.getText().substring(0, startCurrent);
        String after = editor.getText().substring(endCurrent, editor.getText().length());
        
        String[] temp = (before + " " + after).split("[\"\" \" \" !@#$%^&*()-=+ \n \t {};:'\",<>]+");
        for (String x : temp) {
            
            // Add the word into the Trie DS
            if(x.trim().length() > 0) {
                insert(head, x.trim());
            }// end if
        }// end for
    }// end getWords(Node)
    
    /**
     * Methods Name : insert
     * Purpose : Insert new word into the Trie DS
     * @param head - Trie DS head
     * @param text - word to be inserted into the Trie
     */
    public static void insert(Node head, String text) {
        short x;
        for(int i = 0; i < text.length(); i++) {
            x = (short)(text.charAt(i) - '0');
            if(head.children[x] == null) {
                head.children[x] = new Node();
            }// end if
            head = head.children[x];
        }// end for
        head.endOfWord = true;
    }// end insert(Node, String)
    
    /**
     * Methods Name : printCursor
     * Purpose : Print the current x,y coordinate of the highlighted text
     */
    private void printCursor() {
        
        try
        {
            if(ideoneInterface.start) {
                if(startCar == editor.getCaretPosition()) {
                    ideoneInterface.cursorState.setText((y+1)+":"+(x+1));
                }
                else if(startHighlight < endHighlight) {
                    ideoneInterface.cursorState.setText((y+1)+":"+(x+1)+"/"+(endHighlight-startHighlight+1)+":"+editor.getSelectedText().length());
                }
                else {
                    ideoneInterface.cursorState.setText((y+1)+":"+(x+1)+"/"+(startHighlight-endHighlight+1)+":"+editor.getSelectedText().length());
                }// end if
            }// end if
        }
        catch(Exception unexpectedError) {}
    }// end printCursor()
    
    /**
     * Method Name : tempFinder
     * Purpose : Find current word and find similar words
     */
    public void tempFinder() {
        
        String textEditor = editor.getText();   // get the text
        int i, pos = editor.getCaretPosition(); // current caret position
        char c;
        
        currString = "";    // current String
        
        /**
         * Get the current String
         */
        found = true;
        endCurrent = pos;
        for(i = pos; i < textEditor.length(); i++) {
            
            c = textEditor.charAt(i);
            if(Character.isLetterOrDigit(c) || c == '_') {
                currString = c + currString;
            }
            else {
                endCurrent = i;
                found = false;
                break;
            }// end if
        }
        if(found) endCurrent = textEditor.length();
        
        found = true;
        for(i = pos - 1; i >= 0; i--) {
            
            c = textEditor.charAt(i);
            if(Character.isLetterOrDigit(c) || c == '_') {
                currString = c + currString;
            }
            else {
                startCurrent = i;
                found = false;
                break;
            }// end if
        }// end for
        
        if(found) { 
            startCurrent = 0;
        } else { 
            startCurrent++;
        }// end if
        
        // Find the list of words similar to current String
        findWord(currString);
        
    }// end tempFinder(CaretEvent)
    
    /**
     * Method Name : findWord
     * Purpose : Highlight all the similar words to the given word
     * @param word - given word
     */
    public void findWord(String word) {
        
        int findLen = word.length();
        highlighter.removeAllHighlights();
        
        if(findLen > 0) {
            
            String text = editor.getText();
            int index = text.indexOf(word, 0);
            
            try {
                while(index >= 0) {
                    if(index == startCurrent) { index = text.indexOf(word, endCurrent); }
                    else {
                        highlighter.addHighlight(index, index + findLen, otherWord);
                        index = text.indexOf(word, index + findLen);
                    }// end if
                }// end while
            } catch(BadLocationException ex) {
                ex.printStackTrace();
            }// end try/catch
        }// end if
    }// end findWord(String)
}
