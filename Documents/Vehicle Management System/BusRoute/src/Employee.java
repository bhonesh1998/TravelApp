
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.*;
import java.io.*;
import java.sql.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

public class Employee extends JPanel {

    private JScrollPane jsp;
    private static JTable taleEmpList;
    private JButton btnAddEntry,  btnRefresh,  btnUpdate,  btnClose,  btnPrint;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    private Statement stmt;
    JFrame JFParentFrame;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    //private JButton searchButton;
    private static int rowCnt = 0;
    private static int selectedRow;
    private static JTextArea txtInfo = new JTextArea(15, 40);
    private Connection dbconn;
    private static String info;

    public Employee() {
        setSize(1000, 400);
        setLayout(new BorderLayout());
        taleEmpList = new JTable(new AbstractTable());
        javax.swing.table.TableColumn column = null;
        for (int i = 0; i < 8; i++) {
            column = taleEmpList.getColumnModel().getColumn(i);
        }
        jsp = new JScrollPane(taleEmpList);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(jsp, BorderLayout.CENTER);

        btnAddEntry = new JButton("Add Entry", new ImageIcon(ClassLoader.getSystemResource("Images/addnew.png")));
        btnRefresh = new JButton("Refresh", new ImageIcon(ClassLoader.getSystemResource("Images/refresh.png")));
        btnUpdate = new JButton("Update", new ImageIcon(ClassLoader.getSystemResource("Images/update.png")));
        btnPrint = new JButton("Print", new ImageIcon(ClassLoader.getSystemResource("Images/print.png")));
        btnClose = new JButton("Close", new ImageIcon(ClassLoader.getSystemResource("Images/exit.png")));

        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnAddEntry);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnPrint);
        buttonPanel.add(btnClose);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);
        try {
            stmt = DBConnection.getDBConnection().createStatement();
        } catch (Exception excp) {
            JOptionPane.showMessageDialog(null, "Error on database connection", "Statement error", JOptionPane.ERROR_MESSAGE);
        }//try catch closed
        load_taleEmpList();

        btnAddEntry.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                AddNewEntry frm=new AddNewEntry();
                MDIWindow.desktop.add(frm);
                frm.setVisible(true);
                try{
                    frm.setSelected(true);
                }catch(Exception ex){}
            }
        });

        btnUpdate.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                NewEntry frm = new NewEntry(taleEmpList.getValueAt(getSelectedRow(), 0).toString(),
                        taleEmpList.getValueAt(getSelectedRow(), 1).toString(),
                        taleEmpList.getValueAt(getSelectedRow(), 2).toString(),
                        taleEmpList.getValueAt(getSelectedRow(), 3).toString(),
                        taleEmpList.getValueAt(getSelectedRow(), 4).toString(),
                        taleEmpList.getValueAt(getSelectedRow(), 5).toString(),
                        taleEmpList.getValueAt(getSelectedRow(), 6).toString(),
                        taleEmpList.getValueAt(getSelectedRow(), 7).toString(),
                        taleEmpList.getValueAt(getSelectedRow(), 8).toString(),
                        taleEmpList.getValueAt(getSelectedRow(), 9).toString());
                MDIWindow.desktop.add(frm);
                frm.setVisible(true);
                try{
                    frm.setSelected(true);
                }catch(Exception ex){}
            }
        });
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        btnPrint.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                employee_report frm = new employee_report();
                MDIWindow.desktop.add(frm);
                frm.setVisible(true);
                try {
                    frm.setSelected(true);
                } catch (Exception ex) {
                }
            }
        });
        btnRefresh.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                load_taleEmpList();
            }
        });
        tablePanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        tablePanel.setPreferredSize(new java.awt.Dimension(750, 450));
        tablePanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        tablePanel.setPreferredSize(new java.awt.Dimension(1000, 300));
        tablePanel.setBackground(new java.awt.Color(200, 200, 200));
        tablePanel.setBounds(2, 200, 770, 2);
        add(tablePanel);
    }

    public static int getSelectedRow() {
        taleEmpList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        javax.swing.ListSelectionModel rowSel = taleEmpList.getSelectionModel();
        rowSel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                javax.swing.ListSelectionModel sel = (javax.swing.ListSelectionModel) e.getSource();
                if (!sel.isSelectionEmpty()) {
                    selectedRow = sel.getMinSelectionIndex();
                }
            }
        });
        return selectedRow;
    }

    class AbstractTable extends AbstractTableModel {

        private String[] columnNames = {"Driver Number", "Surname", "FirstName", "Last Name",
            "Gender", "DOB", "Designation", "Telephone", "E-mail", "Address"
        };
        private Object[][] data = new Object[100][100];

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }//AbstrctTable Class closed
    public void load_taleEmpList() {
        try {
            Statement statement = DBConnection.getDBConnection().createStatement();
            String temp = ("SELECT * FROM Emp ORDER BY EmpNo");
            int Numrow = 0;
            ResultSet result = statement.executeQuery(temp);
            while (result.next()) {
                taleEmpList.setValueAt(result.getString(1), Numrow, 0);
                taleEmpList.setValueAt(result.getString(2), Numrow, 1);
                taleEmpList.setValueAt(result.getString(3), Numrow, 2);
                taleEmpList.setValueAt(result.getString(4), Numrow, 3);
                taleEmpList.setValueAt(result.getString(5), Numrow, 4);
                taleEmpList.setValueAt(result.getDate(6), Numrow, 5);
                taleEmpList.setValueAt(result.getString(7), Numrow, 6);
                taleEmpList.setValueAt(result.getString(8), Numrow, 7);
                taleEmpList.setValueAt(result.getString(9), Numrow, 8);
                taleEmpList.setValueAt(result.getString(10), Numrow, 9);
                Numrow++;
            }
        } catch (SQLException sqlex) {
            txtInfo.append(sqlex.toString());
        }
    }//load_taleEmpList() closed
}//class closed


