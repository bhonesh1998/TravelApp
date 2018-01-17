
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.*;
import java.io.*;
import java.sql.*;

public class AddEntry extends JInternalFrame {

    private JLabel lblBusNo,  lblRegNo,  lblModel,  lblCapacity,  lblDOP,  lblInsuranceStatus,  lblDOI,  lblDOIE;
    private JTextField txtBusNo,  txtRegNo,  txtModel,  txtCapacity,  txtIStatus;
    private JButton btnAddNew,  btnCancel,  btnClear,  btnNext;
    private JPanel fieldsPanel;
    private JPanel jPanel3;
    private JPanel buttonPanel;
    private JPanel jPanel5;
    private static JTextArea txtInfo = new JTextArea(15, 40);
    private Connection dbconn;
    private static String info;
    private DateButton date_bought;
    private DateButton date_ins;
    private DateButton date_expiry;
    private Date startDate;
    private Date endDate;

    public AddEntry() {
        super("New Bus Entry", false, true, false, true);
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - 500) / 2, ((screen.height - 350) / 2));

        lblBusNo = new JLabel("  Bus Number ");
        lblRegNo = new JLabel("  Reg Number ");
        lblModel = new JLabel("  Model ");
        lblCapacity = new JLabel("  Capacity ");
        lblDOP = new JLabel("  Date Purchased");
        lblInsuranceStatus = new JLabel("  Insurance Status");
        lblDOI = new JLabel("  Date Insured");
        lblDOIE = new JLabel("  Insurance Expiry Date");
        txtBusNo = new JTextField(10);
        txtRegNo = new JTextField(10);
        txtModel = new JTextField(10);
        txtCapacity = new JTextField(10);
        txtIStatus = new JTextField(10);
        txtBusNo.setForeground(Color.blue);
        btnAddNew = new JButton("Add Record", new ImageIcon(ClassLoader.getSystemResource("Images/addnew.png")));
        btnCancel = new JButton("Cancel", new ImageIcon(ClassLoader.getSystemResource("Images/exit.png")));
        btnClear = new JButton("Clear", new ImageIcon(ClassLoader.getSystemResource("Images/clear.png")));
        date_bought = new DateButton();
        date_ins = new DateButton();
        date_expiry = new DateButton();

        date_ins.setForeground(Color.red);
        date_bought.setForeground(Color.red);
        date_expiry.setForeground(Color.red);

        fieldsPanel = new JPanel(new GridLayout(8, 2));
        buttonPanel = new JPanel(new FlowLayout());

        fieldsPanel.setPreferredSize(new Dimension(400, 250));
        fieldsPanel.add(lblBusNo);
        fieldsPanel.add(txtBusNo);
        fieldsPanel.add(lblRegNo);
        fieldsPanel.add(txtRegNo);
        fieldsPanel.add(lblModel);
        fieldsPanel.add(txtModel);
        fieldsPanel.add(lblCapacity);
        fieldsPanel.add(txtCapacity);
        fieldsPanel.add(lblDOP);
        fieldsPanel.add(date_bought);
        fieldsPanel.add(lblInsuranceStatus);
        fieldsPanel.add(txtIStatus);
        fieldsPanel.add(lblDOI);
        fieldsPanel.add(date_ins);
        fieldsPanel.add(lblDOIE);
        fieldsPanel.add(date_expiry);

        buttonPanel.add(btnAddNew);
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnClear);

        getContentPane().add(fieldsPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
        pack();
        generator();

        btnAddNew.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (txtBusNo.getText() == null || txtBusNo.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter bus number", "ERROR", JOptionPane.ERROR_MESSAGE);
                    txtBusNo.requestFocus();
                    return;
                }

                if (txtRegNo.getText() == null || txtRegNo.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Reg Number", "ERROR", JOptionPane.ERROR_MESSAGE);
                    txtRegNo.requestFocus();
                    return;
                }
                if (txtModel.getText() == null || txtModel.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, " Model Field is required", "ERROR", JOptionPane.ERROR_MESSAGE);
                    txtModel.requestFocus();
                    return;
                }
                if (txtCapacity.getText() == null || txtCapacity.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, " Enter bus capacity", "ERROR", JOptionPane.ERROR_MESSAGE);
                    txtCapacity.requestFocus();
                    return;
                }

                if (txtIStatus.getText() == null || txtIStatus.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Insurance Status entry is required", "ERROR", JOptionPane.ERROR_MESSAGE);
                    txtIStatus.requestFocus();
                    return;
                }
                try {
                    Statement stmt = DBConnection.getDBConnection().createStatement();

                    String sql = "INSERT INTO Buses (BusNo, Bus_RegNo, Model, Capacity, DateBought,Insurance_Status,Date_Insured,Insurance_Expiry) VALUES ('" +
                            txtBusNo.getText() + "', '" +
                            txtRegNo.getText() + "', '" +
                            txtModel.getText() + "', '" +
                            txtCapacity.getText() + "', '" +
                            date_bought.getText() + "', '" +
                            txtIStatus.getText() + "', '" +
                            date_ins.getText() + "', '" +
                            date_expiry.getText() + "')";

                    int result = stmt.executeUpdate(sql);
                    String ObjButtons[] = {"Yes", "No"};
                    int PromptResult = JOptionPane.showOptionDialog(null, "Record succesfully added.Do you want to add another?",
                            "Success", JOptionPane.INFORMATION_MESSAGE, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
                    if (PromptResult == 0) {
                        generator();
                        txtRegNo.setText("");
                        txtModel.setText("");
                        txtCapacity.setText("");
                        txtIStatus.setText("");
                    } else {
                        dispose();
                    }
                } catch (SQLException sqlex) {
                    JOptionPane.showMessageDialog(null, "Error on database operation", "Failure", JOptionPane.ERROR_MESSAGE);
                }//try catch closed
            }
        });

        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnClear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                txtBusNo.setText("");
                txtRegNo.setText("");
                txtModel.setText("");
                txtCapacity.setText("");
                txtIStatus.setText("");
            }
        });
    }//constructor closed
    private void generator() {
        try {
            Statement stmt = DBConnection.getDBConnection().createStatement();
            ResultSet rst = stmt.executeQuery("select * from Buses where BusNo =(SELECT Max(Buses.BusNo) AS MaxOfBusNo FROM Buses)");
            txtBusNo.setText("1000");
            while (rst.next()) {
                String s;
                int number = rst.getInt(2);
                number = number + 1;
                s = "" + number;
                txtBusNo.setText(s);
            }
        } catch (Exception n) {
            JOptionPane.showMessageDialog(null, "Error on generator" + n.toString());
        }//try catch closed
    }//generator() closed
}//class closed

