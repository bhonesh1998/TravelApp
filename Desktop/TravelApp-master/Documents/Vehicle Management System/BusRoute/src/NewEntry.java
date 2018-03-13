
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.*;
import java.io.*;
import java.sql.*;

public class NewEntry extends JInternalFrame {

    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    private JLabel empNo,  Sname,  Fname,  Lname,  Gender,  Designation,  telephone,  lblEmplPic,  email,  address,  DOB;
    private JTextField txtEmpNo,  txtSname,  txtFname,  txtLname,  txtDesignation,  txttelephone,  txtemail,  txtaddress;
    private JButton Update,  Search,  Clear,  Delete,  AddPic;
    String sname, fname, lname, gender, designation, tele, ema, dob, adres;
    int number;
    private JButton Cancel;
    private JPanel jPanel1,  pics;
    private JPanel pic;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JComboBox cbogender;
    private DateButton dobs;
    private static JTextArea txtInfo = new JTextArea(15, 40);
    private Connection dbconn;
    private static String info;
    final JFileChooser fc = new JFileChooser();
    String getPicture;

    public NewEntry(String emp, String sname, String fname, String lname, String gender,
            String dob, String designation, String tele, String ema, String adres) {
        super("Update Employee's Details.",false,true,false,true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        empNo = new JLabel("Employee Number ");
        Sname = new JLabel("Surname ");
        Fname = new JLabel("First Name ");
        Lname = new JLabel("Last Name ");
        Gender = new JLabel("Gender ");
        Designation = new JLabel("Designation ");
        telephone = new JLabel("Telephone Number");
        email = new JLabel("E-mail Address");
        address = new JLabel("Address");
        DOB = new JLabel("DOB");

        lblEmplPic = new JLabel(new ImageIcon(ClassLoader.getSystemResource("Images/defaultpic.png")));
        txtEmpNo = new JTextField(10);
        txtSname = new JTextField(10);
        txtFname = new JTextField(10);
        txtLname = new JTextField(10);
        cbogender = new JComboBox();
        txtDesignation = new JTextField(10);
        txttelephone = new JTextField(10);
        txtemail = new JTextField(10);
        txtaddress = new JTextField(10);

        dobs = new DateButton();
        txtEmpNo.setText(emp);
        txtSname.setText(sname);
        txtFname.setText(fname);
        txtLname.setText(lname);
        cbogender.setSelectedItem(gender);
        dobs.setText(dob);
        txtDesignation.setText(designation);
        txttelephone.setText(tele);
        txtemail.setText(ema);
        txtaddress.setText(adres);
        lblEmplPic.setIcon(new ImageIcon("Emp/" + txtEmpNo.getText() + ".jpg"));
        cbogender.addItem("Male");
        cbogender.addItem("Female");

        Update = new JButton("Update", new ImageIcon(ClassLoader.getSystemResource("Images/update.png")));
        Search = new JButton("Search", new ImageIcon(ClassLoader.getSystemResource("Images/search.png")));
        Clear = new JButton("Clear", new ImageIcon(ClassLoader.getSystemResource("Images/clear.png")));
        Delete = new JButton("Delete", new ImageIcon(ClassLoader.getSystemResource("Images/delete.png")));
        Cancel = new JButton("Cancel", new ImageIcon(ClassLoader.getSystemResource("Images/exit.png")));
        AddPic = new JButton("Select pic");

        pics = new JPanel();
        pics.setPreferredSize(new Dimension(150, 250));
        pics.add(lblEmplPic);
        jPanel1 = new javax.swing.JPanel(new java.awt.GridLayout(11, 2));
        jPanel1.setPreferredSize(new Dimension(350, 250));
        jPanel1.add(empNo);
        jPanel1.add(txtEmpNo);
        jPanel1.add(Sname);
        jPanel1.add(txtSname);
        jPanel1.add(Fname);
        jPanel1.add(txtFname);
        jPanel1.add(Lname);
        jPanel1.add(txtLname);
        jPanel1.add(Gender);
        jPanel1.add(cbogender);
        jPanel1.add(DOB);
        jPanel1.add(dobs);
        jPanel1.add(telephone);
        jPanel1.add(txttelephone);
        jPanel1.add(email);
        jPanel1.add(txtemail);
        jPanel1.add(address);
        jPanel1.add(txtaddress);
        jPanel1.add(Designation);
        jPanel1.add(txtDesignation);

        jPanel4 = new JPanel();
        jPanel4.add(Update);
        jPanel4.add(Cancel);
        jPanel4.add(Search);
        jPanel4.add(Delete);
        jPanel4.add(Clear);

        jPanel3 = new JPanel();
        jPanel3.add(jPanel1);
        jPanel3.add(pics);
        jPanel3.add(jPanel4);
        add(jPanel3);
        setSize(400, 250);
        setLocation((screen.width - 500) / 2, ((screen.height - 350) / 2));
        setResizable(false);
        txtSname.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isLetter(c) ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    txtSname.requestFocus();
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "This Field Only acept text", "Error",JOptionPane.DEFAULT_OPTION);
                    e.consume();
                }
            }
        });
        txtDesignation.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isLetter(c) ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    txtDesignation.requestFocus();
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "This Field Only acept text", "Error",
                            JOptionPane.DEFAULT_OPTION);
                    e.consume();
                }
            }
        });
        txtFname.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isLetter(c) ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {

                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "This Field Only acept text", "Error",
                            JOptionPane.DEFAULT_OPTION);
                    e.consume();
                }
            }
        });
        txtLname.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isLetter(c) ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "This Field Only acept text", "Error",
                            JOptionPane.DEFAULT_OPTION);
                    e.consume();
                }
            }
        });
        txttelephone.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                JTextField textField =
                        (JTextField) e.getSource();
                String content = textField.getText();
                if (content.length() != 0) {
                    try {
                        Integer.parseInt(content);
                    } catch (NumberFormatException nfe) {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Invalid data entry", "Error",
                                JOptionPane.DEFAULT_OPTION);
                        textField.requestFocus();
                        txttelephone.setText("");
                    }
                }
            }
        });

        try {

            Statement s = DBConnection.getDBConnection().createStatement();
        } catch (Exception excp) {
            excp.printStackTrace();
        }
        Update.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (txtEmpNo.getText() == null ||txtEmpNo.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Employee Number", "Error", JOptionPane.DEFAULT_OPTION);
                    txtEmpNo.requestFocus();
                    return;
                }
                if (txtSname.getText() == null ||txtSname.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Employee Surname", "Error", JOptionPane.DEFAULT_OPTION);
                    txtSname.requestFocus();
                    return;
                }
                if (txtFname.getText() == null ||txtFname.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Employee First Name", "Error",JOptionPane.DEFAULT_OPTION);
                    txtFname.requestFocus();
                    return;
                }
                if (txtLname.getText() == null ||txtLname.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Employee Last Name", "Error", JOptionPane.DEFAULT_OPTION);
                    txtLname.requestFocus();
                    return;
                }
                if (txttelephone.getText() == null ||txttelephone.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter telphone number","Error", JOptionPane.DEFAULT_OPTION);
                    txttelephone.requestFocus();
                    return;
                }
                if (txtemail.getText() == null ||txtemail.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter E-mail address", "Error", JOptionPane.DEFAULT_OPTION);
                    txtemail.requestFocus();
                    return;
                }
                if (txtaddress.getText() == null || txtaddress.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Address","Error", JOptionPane.DEFAULT_OPTION);
                    txtaddress.requestFocus();
                    return;
                }
                if (txtDesignation.getText() == null ||txtDesignation.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Employee Designation","Error", JOptionPane.DEFAULT_OPTION);
                    Designation.requestFocus();
                    return;
                }

                try {
                    Statement statement = DBConnection.getDBConnection().createStatement();
                    {

                        String temp = "UPDATE Emp SET " +
                                "  Sname      ='" + txtSname.getText() +
                                "',Fname      ='" + txtFname.getText() +
                                "',Lname      ='" + txtLname.getText() +
                                "',Gender     ='" + cbogender.getSelectedItem() +
                                "',DOB        ='" + dobs.getText() +
                                "',Designation='" + txtDesignation.getText() +
                                "',Telephone  ='" + txttelephone.getText() +
                                "',E_Mail     ='" + txtemail.getText() +
                                "',Address    ='" + txtaddress.getText() +
                                "' WHERE empNo LIKE'" + txtEmpNo.getText() + "'";

                        int result = statement.executeUpdate(temp);

                        setVisible(false);
                        dispose();
                    }

                } catch (SQLException sqlex) {
                    JOptionPane.showMessageDialog(null,"Error on database operation","Error",JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        Cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        AddPic.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        Clear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                txtEmpNo.setText("");
                txtSname.setText("");
                txtFname.setText("");
                txtLname.setText("");
                txtDesignation.setText("");
                txtemail.setText("");
                txttelephone.setText("");
                txtaddress.setText("");

            }
        });

        Delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent r) {

                int DResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Record?");
                if (DResult == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "Deletion Cancel","DELETION", JOptionPane.DEFAULT_OPTION);
                }

                if (DResult == JOptionPane.YES_OPTION) {
                    try {
                        Statement statement = DBConnection.getDBConnection().createStatement();
                        if (!txtEmpNo.equals("")) {

                            String query = ("DELETE  FROM Emp where EmpNo='" + txtEmpNo.getText() + "'");
                            int result = statement.executeUpdate(query);
                            if (result == 0) {
                                JOptionPane.showMessageDialog(null, "Record Deleted","DELETION", JOptionPane.DEFAULT_OPTION);
                            } else {
                                //txtInfo.append( "\nDeletion failed\n" );
                                txtEmpNo.setText("");
                                txtSname.setText("");
                                txtFname.setText("");
                                txtLname.setText("");
                                //txtGender.setText ("");
                                txtDesignation.setText("");
                                txttelephone.setText("");
                                txtemail.setText("");
                                txtaddress.setText("");
                            }
                            statement.close();
                        }
                    } catch (SQLException sqlex) {
                        sqlex.printStackTrace();
                    }
                }
            }
        });
        Search.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (!txtEmpNo.equals("")) {

                        Statement statement = DBConnection.getDBConnection().createStatement();
                        String query = ("SELECT * FROM Emp where empNo ='" + txtEmpNo.getText() + "'");
                        ResultSet rs = statement.executeQuery(query);
                        display(rs);
                        lblEmplPic.setIcon(new ImageIcon("Emp/" + txtEmpNo.getText() + ".jpg"));
                        statement.close();
                    }
                } catch (SQLException sqlex) {
                    txtInfo.append(sqlex.toString() + sqlex.getMessage());
                }
                setVisible(true);
            }
        });


        jPanel5 = new javax.swing.JPanel(new java.awt.BorderLayout());

        jPanel5.add(jPanel3, java.awt.BorderLayout.CENTER);
        jPanel5.add(jPanel4, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel5);

        pack();
        setVisible(true);
    }

    public void display(ResultSet rs) {
        try {

            boolean recordNumber = rs.next();
            if (recordNumber) {
                number = rs.getInt(1);
                sname = rs.getString(2);
                fname = rs.getString(3);
                lname = rs.getString(4);
                gender = rs.getString(5);
                dob = rs.getString(6);
                designation = rs.getString(7);
                tele = rs.getString(8);
                ema = rs.getString(9);
                adres = rs.getString(10);
                txtSname.setText(sname);
                txtFname.setText(fname);
                txtLname.setText(lname);
                cbogender.setSelectedItem(gender);
                txttelephone.setText(tele);
                txtemail.setText(ema);
                txtaddress.setText(adres);
                dobs.setText(dob);
                txtDesignation.setText(designation);
            } else {
                JOptionPane.showMessageDialog(null, "Record Not found", "ERROR",JOptionPane.DEFAULT_OPTION);
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }

    }


    private void openFile() {
        int returnVal = fc.showOpenDialog(NewEntry.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File dialog = fc.getSelectedFile();
            getPicture = dialog.getPath();
            lblEmplPic.setIcon(new ImageIcon(getPicture));
        }
    }
}
