
// Import java libraries
import java.awt.Color;
import org.fife.ui.rsyntaxtextarea.*;


public class PreferenceClass {
    
    /**
     * Constructor
     */
    public PreferenceClass() { }
    
    
    /**
     * Method Name : blackboard
     * Purpose : Set the current tab to backboard style
     * @param textArea - current tab's textArea 
     */
    public void blackboard(RSyntaxTextArea textArea)
    {
        SyntaxScheme scheme = textArea.getSyntaxScheme();
        
        textArea.setBackground(new Color(0, 51, 102));
        textArea.setForeground(Color.WHITE);
        
        scheme.getStyle(Token.IDENTIFIER).foreground = Color.WHITE;
        scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = Color.GREEN;
        scheme.getStyle(Token.COMMENT_KEYWORD).foreground = Color.WHITE;
        scheme.getStyle(Token.RESERVED_WORD).foreground = new Color(255, 204, 0);
        scheme.getStyle(Token.RESERVED_WORD_2).foreground = new Color(255, 204, 0);
        
        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = new Color(255, 204, 0);
        scheme.getStyle(Token.DATA_TYPE).foreground = new Color(255, 204, 0);
        scheme.getStyle(Token.LITERAL_BOOLEAN).foreground = new Color(255, 204, 0);
        scheme.getStyle(Token.OPERATOR).foreground = new Color(255, 204, 0);
        scheme.getStyle(Token.VARIABLE).foreground = Color.WHITE;
        
        textArea.revalidate();
    
    }// end blackboard(RSyntaxTextArea)
    
    /**
     * Method Name : cobalt
     * Purpose : Set the current tab to cobalt style
     * @param textArea - current tab's textArea 
     */
    public void cobalt(RSyntaxTextArea textArea)
    {
        SyntaxScheme scheme = textArea.getSyntaxScheme();
        
        textArea.setBackground(new Color(21, 20, 53));
        textArea.setForeground(Color.WHITE);
        
        scheme.getStyle(Token.IDENTIFIER).foreground = Color.WHITE;
        scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = Color.GREEN;
        scheme.getStyle(Token.COMMENT_KEYWORD).foreground = Color.WHITE;
        scheme.getStyle(Token.RESERVED_WORD).foreground = new Color(239, 206, 40);
        scheme.getStyle(Token.RESERVED_WORD_2).foreground = new Color(239, 206, 40);
        
        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = new Color(239, 206, 40);
        scheme.getStyle(Token.DATA_TYPE).foreground = new Color(239, 206, 40);
        scheme.getStyle(Token.LITERAL_BOOLEAN).foreground = new Color(239, 206, 40);
        scheme.getStyle(Token.OPERATOR).foreground = Color.ORANGE;
        scheme.getStyle(Token.VARIABLE).foreground = Color.WHITE;
        
        textArea.revalidate();
    
    }// end cobalt(RSyntaxTextArea)
    
    /**
     * Method Name : idle
     * Purpose : Set the current tab to IDLE style
     * @param textArea - current tab's textArea 
     */
    public void idle(RSyntaxTextArea textArea)
    {
        SyntaxScheme scheme = textArea.getSyntaxScheme();
        
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        
        scheme.getStyle(Token.IDENTIFIER).foreground = Color.BLACK;
        scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = new Color(32, 145, 13);
        scheme.getStyle(Token.COMMENT_KEYWORD).foreground = Color.BLACK;
        scheme.getStyle(Token.RESERVED_WORD).foreground = new Color(255, 123, 22);
        scheme.getStyle(Token.RESERVED_WORD_2).foreground = new Color(255, 123, 22);
        
        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = new Color(255, 123, 22);
        scheme.getStyle(Token.DATA_TYPE).foreground = new Color(255, 123, 22);
        scheme.getStyle(Token.LITERAL_BOOLEAN).foreground = new Color(255, 123, 22);
        scheme.getStyle(Token.OPERATOR).foreground = new Color(255, 123, 22);
        scheme.getStyle(Token.VARIABLE).foreground = Color.BLACK;
        
        textArea.revalidate();
    
    }// end idle(RSyntaxTextArea)
    
    /**
     * Method Name : mac
     * Purpose : Set the current tab to mac style
     * @param textArea - current tab's textArea 
     */
    public void mac(RSyntaxTextArea textArea)
    {
        SyntaxScheme scheme = textArea.getSyntaxScheme();
        
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        
        scheme.getStyle(Token.IDENTIFIER).foreground = Color.BLACK;
        scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = new Color(32, 145, 13);
        scheme.getStyle(Token.COMMENT_KEYWORD).foreground = Color.BLACK;
        scheme.getStyle(Token.RESERVED_WORD).foreground = Color.BLUE;
        scheme.getStyle(Token.RESERVED_WORD_2).foreground = Color.BLUE;
        
        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = Color.BLUE;
        scheme.getStyle(Token.DATA_TYPE).foreground = Color.BLUE;
        scheme.getStyle(Token.LITERAL_BOOLEAN).foreground = Color.BLUE;
        scheme.getStyle(Token.OPERATOR).foreground = Color.BLUE;
        scheme.getStyle(Token.VARIABLE).foreground = Color.BLACK;
        
        textArea.revalidate();
    
    }// end mac(RSyntaxTextArea)
    
    /**
     * Method Name : monokai
     * Purpose : Set the current tab to monokai style
     * @param textArea - current tab's textArea 
     */
    public void monokai(RSyntaxTextArea textArea)
    {
        SyntaxScheme scheme = textArea.getSyntaxScheme();
        
        textArea.setBackground(new Color(25, 23, 25));
        textArea.setForeground(Color.WHITE);
        
        scheme.getStyle(Token.IDENTIFIER).foreground = Color.WHITE;
        scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = new Color(216, 214, 75);
        scheme.getStyle(Token.COMMENT_KEYWORD).foreground = Color.WHITE;
        scheme.getStyle(Token.RESERVED_WORD).foreground = new Color(175, 24, 130);
        scheme.getStyle(Token.RESERVED_WORD_2).foreground = new Color(175, 24, 130);
        
        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = Color.WHITE;
        scheme.getStyle(Token.DATA_TYPE).foreground = new Color(49, 199, 249);
        scheme.getStyle(Token.LITERAL_BOOLEAN).foreground = Color.WHITE;
        scheme.getStyle(Token.OPERATOR).foreground = Color.WHITE;
        scheme.getStyle(Token.VARIABLE).foreground = Color.WHITE;
        
        textArea.revalidate();
    
    }// end monokai(RSyntaxTextArea)
    
    /**
     * Method Name : sunburst
     * Purpose : Set the current tab to sunburst style
     * @param textArea - current tab's textArea 
     */
    public void sunburst(RSyntaxTextArea textArea)
    {
        SyntaxScheme scheme = textArea.getSyntaxScheme();
        
        textArea.setBackground(new Color(66, 66, 66));
        textArea.setForeground(Color.WHITE);
        
        scheme.getStyle(Token.IDENTIFIER).foreground = Color.WHITE;
        scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = Color.RED;
        scheme.getStyle(Token.COMMENT_KEYWORD).foreground = Color.WHITE;
        scheme.getStyle(Token.RESERVED_WORD).foreground = Color.WHITE;
        scheme.getStyle(Token.RESERVED_WORD_2).foreground = Color.WHITE;
        
        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = Color.WHITE;
        scheme.getStyle(Token.DATA_TYPE).background = new Color(109, 141, 255); 
        scheme.getStyle(Token.LITERAL_BOOLEAN).foreground = Color.WHITE;
        scheme.getStyle(Token.OPERATOR).foreground = Color.WHITE;
        scheme.getStyle(Token.VARIABLE).foreground = Color.WHITE;
        
        textArea.revalidate();
    
    }// end sunburst(RSyntaxTextArea)
    
}
