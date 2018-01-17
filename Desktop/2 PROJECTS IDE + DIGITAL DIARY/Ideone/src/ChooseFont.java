/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ashish
 */
// Required java packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author Ashish
 */
public class ChooseFont implements ActionListener, WindowListener, MouseListener
{

    JFrame fontFrame;

    JPanel mainPanel, typePanel, stylePanel, sizePanel;
    JLabel fontLabel, styleLabel, sizeLabel;

    String[] fontSize = {"8", "9", "10", "11", "12", "14", "16", "18", "20", "22",
            "24", "26", "28", "36", "48", "72"};
    String[] fontStyle = {"Regular", "Bold", "Italic", "Bold Italic"};
    String[] fontType = {"Consolas", "Time New Roman", "Adobe Arabic", "Adobe Fangsong Std",
            "Agency FB", "Arial", "Bauhaus 93", "Bernard MT", "Blackoak Std",
            "Bookman Old Style", "Bradley Hand ITC", "Broadway", "Brush Script MT",
            "Calibri", "Cambria", "Castellar", "Matura MT Script Capitals", "Mistral",
            "Microsoft Sans Serif", "Ravie", "Parchment"};

    DefaultComboBoxModel typeModel, styleModel, sizeModel;
    JList typeJList, sizeJList, styleJList;
    JScrollPane typeScroll, styleScroll, sizeScroll;
    JComboBox sizeCombo, styleCombo, typeCombo;

    JTextField typeText, styleText, sizeText;

    JButton btnOk, btnCancel;

    private int size;
    private String style, type;
    private short tab;
    
    Font defaultFont = new Font("Consolas", Font.PLAIN, 10);

    /**
     * Constructor
     */
    public ChooseFont() {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex){}
        
        fontFrame = new JFrame("Font");

        // Default Font Customization
        defaultFont();

        // Create a main panel for the font table
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(420,270));

        // Set labels
        fontLabel = new JLabel("Font:");
        styleLabel = new JLabel("Font style:");
        sizeLabel = new JLabel("Size:");

        // Set dimensions to the labels
        fontLabel.setBounds(15, 0, 50, 50);
        styleLabel.setBounds(200, 0, 100, 50);
        sizeLabel.setBounds(350, 0, 50, 50);

        //JTextFields

        typeText = new JTextField("Consolas"); typeText.setEditable(false);
        typeText.setPreferredSize(new Dimension(170, 20));
        typeText.setBounds(15, 35, 170, 20);
        typeText.setBackground(Color.WHITE);

        styleText = new JTextField("Regular"); styleText.setEditable(false);
        styleText.setPreferredSize(new Dimension(140, 20));
        styleText.setBounds(200, 35, 135, 20);
        styleText.setBackground(Color.WHITE);

        sizeText = new JTextField("14"); sizeText.setEditable(false);
        sizeText.setPreferredSize(new Dimension(70, 20));
        sizeText.setBounds(350, 35, 65, 20);
        sizeText.setBackground(Color.WHITE);

        //DefaultComboBoxModel
        typeModel = new DefaultComboBoxModel(fontType);
        styleModel = new DefaultComboBoxModel(fontStyle);
        sizeModel = new DefaultComboBoxModel(fontSize);

        //JComboBoxes
        typeCombo = new JComboBox(typeModel); typeCombo.setFont(defaultFont);
        styleCombo = new JComboBox(styleModel); styleCombo.setFont(defaultFont);
        sizeCombo = new JComboBox(sizeModel); sizeCombo.setFont(defaultFont);

        //JList
        typeJList = new JList(typeModel); typeScroll = new JScrollPane(typeJList);
        styleJList = new JList(styleModel); styleScroll = new JScrollPane(styleJList);
        sizeJList = new JList(sizeModel); sizeScroll = new JScrollPane(sizeJList);

        //JScrolls
        typeScroll.setPreferredSize(new Dimension(170,135));
        styleScroll.setPreferredSize(new Dimension(140,135));
        sizeScroll.setPreferredSize(new Dimension(70,120));
        typeScroll.setBounds(15, 55, 170, 135);
        styleScroll.setBounds(200, 55, 135, 135);
        sizeScroll.setBounds(350, 55, 65, 120);

        //JButtons
        btnOk = new JButton("OK"); btnOk.setBounds(200, 220, 100, 25);
        btnCancel = new JButton("Cancel"); btnCancel.setBounds(310, 220, 100, 25);

        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);

        defaultFont();

        mainPanel.add(btnOk);
        mainPanel.add(btnCancel);

        mainPanel.add(fontLabel);
        mainPanel.add(typeText);
        mainPanel.add(typeScroll);

        mainPanel.add(styleLabel);
        mainPanel.add(styleText);
        mainPanel.add(styleScroll);

        mainPanel.add(sizeLabel);
        mainPanel.add(sizeText);
        mainPanel.add(sizeScroll);

        fontFrame.add(mainPanel);

        typeJList.addMouseListener(this);
        styleJList.addMouseListener(this);
        sizeJList.addMouseListener(this);

        typeJList.setSelectedIndex(0); styleJList.setSelectedIndex(0); sizeJList.setSelectedIndex(5);

    }// end ChooseFont() constructor

    /**
     * Default Set
     */
    private void defaultFont() {
        
        type = "Consolas";
        style = "Regular";
        size = 12;
    }// end

    /**
     * @param s - size of the recent font
     */
    public void displayFont(int s) {
        
        fontFrame.pack();
        fontFrame.setFocusableWindowState(true);
        fontFrame.addWindowListener(this);
        fontFrame.setResizable(false);
        fontFrame.setAlwaysOnTop(true);
        fontFrame.setLocationRelativeTo(null);
        fontFrame.setVisible(true);

        typeText.setText(type);
        styleText.setText(style);
        sizeText.setText("" + ideoneInterface.font.getSize());

    }

    /**
     * Purpose : set new Font to the textArea
     * @param type - font name
     * @param style - font style
     * @param size- font size
     */
    public void changeFont(String type, String style, int size) {

        if(style.equalsIgnoreCase("Regular")) {
            ideoneInterface.font = new Font(type, Font.PLAIN, size);
        }

        else if(style.equalsIgnoreCase("Bold")) {
            ideoneInterface.font = new Font(type, Font.BOLD, size);
        }

        else if(style.equalsIgnoreCase("Italic")) {
            ideoneInterface.font = new Font(type, Font.ITALIC, size);
        }

        else {
            ideoneInterface.font = new Font(type, Font.BOLD | Font.ITALIC, size);
        }// end if

    }// end changeFont(String, String, int)


    /***************************************************************************
     * Action Listener
     * @param e
     * ************************************************************************/

    @Override
    public void actionPerformed(ActionEvent e) {
        
        fontFrame.setFocusableWindowState(true);

        typeJList.addMouseListener(this);
        styleJList.addMouseListener(this);
        sizeJList.addMouseListener(this);

        if(e.getSource() == btnOk) {
            
            type = "" + typeJList.getSelectedValue();
            style = "" + styleJList.getSelectedValue();
            size = Integer.parseInt("" + sizeJList.getSelectedValue());

            changeFont(type, style, size);

            typeText.setText(type); styleText.setText(style); sizeText.setText("" + size);
            
            if(ideoneInterface.start) this.tab = (short) ideoneInterface.chooseTab.getSelectedIndex();
            else this.tab = 0;
            
            ideoneInterface.textList.get(this.tab).setFont(ideoneInterface.font);

        }
        
        ideoneInterface.closeIdeone = true;
        fontFrame.dispose();

    }// end actionPerformed(ActionEvent)


    /***************************************************************************
     * Window Listeners
     * @param e
     * ************************************************************************/

    @Override
    public void windowClosing(WindowEvent e) {
        ideoneInterface.closeIdeone = true;
    }

    @Override
    public void windowOpened(WindowEvent e){}

    @Override
    public void windowClosed(WindowEvent e){}

    @Override
    public void windowIconified(WindowEvent e){}

    @Override
    public void windowDeiconified(WindowEvent e){}

    @Override
    public void windowActivated(WindowEvent e){}

    @Override
    public void windowDeactivated(WindowEvent e){}


    /***************************************************************************
     * Mouse Listeners
     * @param e
     * ************************************************************************/

    @Override
    public void mousePressed(MouseEvent e)
    {
        if("" + typeJList.getSelectedValue() != null)
            typeText.setText("" + typeJList.getSelectedValue());

        if("" + styleJList.getSelectedValue() != null)
            styleText.setText("" + styleJList.getSelectedValue());

        if("" + sizeJList.getSelectedValue() != null)
            sizeText.setText("" + sizeJList.getSelectedValue());

    }// end mousePressed(MouseEvents)
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e){}

    @Override
    public void mouseExited(MouseEvent e){}

}// end ChooseFont() Class
