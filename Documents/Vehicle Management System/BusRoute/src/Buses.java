
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.*;
import java.io.*;
import java.text.*;
import java.sql.*;
import java.awt.print.*;
import javax.swing.table.AbstractTableModel;

public class Buses extends JPanel implements Printable {

    private static JTable tblBusList;
    private JScrollPane jsp;
    private JButton btnAddNew,  btnRefresh,  btnClose,  btnUpdate,  btnPrint;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private Statement stmt;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private static int selectedRow;
    /*JFrame JFParentFrame;
    private static int rowCnt = 0;    
    private static JTextArea txtInfo = new JTextArea(15, 40);
    private Connection dbconn;
    private static String info;*/

    public Buses() {
        setSize(1000, 400);
        setLayout(new BorderLayout());

        tblBusList = new JTable(new AbstractTable());
        javax.swing.table.TableColumn column = null;
        for (int i = 0; i < 7; i++) {
            column = tblBusList.getColumnModel().getColumn(i);
            if (i == 4) {
                sdf.format(i);
            }//if btnClosed
        }//for btnClosed
        jsp = new JScrollPane(tblBusList);
        tablePanel = new JPanel(new GridLayout());
        tablePanel.add(jsp);

        btnAddNew = new JButton("Add New", new ImageIcon(ClassLoader.getSystemResource("Images/addnew.png")));
        btnUpdate = new JButton("Update", new ImageIcon(ClassLoader.getSystemResource("Images/Update.png")));
        btnRefresh = new JButton("Refresh", new ImageIcon(ClassLoader.getSystemResource("Images/Refresh.png")));
        btnClose = new JButton("Close", new ImageIcon(ClassLoader.getSystemResource("Images/exit.png")));
        btnPrint = new JButton("Print", new ImageIcon(ClassLoader.getSystemResource("Images/print.png")));
        buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout());
        buttonPanel.add(btnAddNew);
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

        reloaded();
        btnAddNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                AddEntry frm = new AddEntry();
                MDIWindow.desktop.add(frm);
                frm.setVisible(true);
                try {
                    frm.setSelected(true);
                } catch (Exception ex) {
                }
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                setVisible(false);
            }
        });
        btnRefresh.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                reloaded();
                setVisible(true);
            }
            });
        btnPrint.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Bus_Details frm = new Bus_Details();
                MDIWindow.desktop.add(frm);
                frm.setVisible(true);
                try {
                    frm.setSelected(true);
                } catch (Exception ex) {
                }
            }
        });
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                String regNo, busNo, Model, capacity, db, is, ie, id;
                regNo = tblBusList.getValueAt(getSelectedRow(), 0).toString();
                busNo = tblBusList.getValueAt(getSelectedRow(), 1).toString();
                Model = tblBusList.getValueAt(getSelectedRow(), 2).toString();
                capacity = tblBusList.getValueAt(getSelectedRow(), 3).toString();
                db = tblBusList.getValueAt(getSelectedRow(), 4).toString();
                is = tblBusList.getValueAt(getSelectedRow(), 5).toString();
                ie = tblBusList.getValueAt(getSelectedRow(), 6).toString();
                id = tblBusList.getValueAt(getSelectedRow(), 7).toString();
                UpdateEntry frm = new UpdateEntry(regNo, busNo, Model, capacity, db, is, ie, id);
                MDIWindow.desktop.add(frm);
                frm.setVisible(true);
            }
        });
    }//constructor closed
    public static int getSelectedRow() {
        tblBusList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        javax.swing.ListSelectionModel rowSel = tblBusList.getSelectionModel();
        rowSel.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                javax.swing.ListSelectionModel sel = (ListSelectionModel) e.getSource();
                if (!sel.isSelectionEmpty()) {
                    selectedRow = sel.getMinSelectionIndex();
                }
            }
        });

        return selectedRow;
    }

    class AbstractTable extends AbstractTableModel {

        private String[] columnNames = {"RegNo", "BusNo", "Model", "Capacity",
            "Date purchased", "Insurance Status", "Date Insured", "Expiry Date"
        };
        private Object[][] data = new Object[50][50];

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
    }

    public void reloaded() {
        try {
            String sql = ("SELECT * FROM Buses ORDER BY BusNo");
            int Numrow = 0;
            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                tblBusList.setValueAt(result.getString(1).trim(), Numrow, 0);
                tblBusList.setValueAt(result.getString(2).trim(), Numrow, 1);
                tblBusList.setValueAt(result.getString(3).trim(), Numrow, 2);
                tblBusList.setValueAt(result.getString(4).trim(), Numrow, 3);
                tblBusList.setValueAt(result.getDate(5), Numrow, 4);
                tblBusList.setValueAt(result.getString(6).trim(), Numrow, 5);
                tblBusList.setValueAt(result.getDate(7), Numrow, 6);
                tblBusList.setValueAt(result.getDate(8), Numrow, 7);
                sdf.format(7);
                Numrow++;
            }//while closed
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Error on retrieving values", "Error", JOptionPane.ERROR_MESSAGE);
        }//try catch closed
    }//reloaded() closed
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        int fontHeight = g2.getFontMetrics().getHeight();
        int fontDesent = g2.getFontMetrics().getDescent();

        //leave room for page number
        double pageHeight = pageFormat.getImageableHeight() - fontHeight;
        double pageWidth = pageFormat.getImageableWidth();
        double tableWidth = (double) tblBusList.getColumnModel().getTotalColumnWidth();
        double scale = 1;
        if (tableWidth >= pageWidth) {
            scale = pageWidth / tableWidth;
        }

        double headerHeightOnPage =
                tblBusList.getTableHeader().getHeight() * scale;
        double tableWidthOnPage = tableWidth * scale;

        double oneRowHeight = (tblBusList.getRowHeight() +
                tblBusList.getRowMargin()) * scale;
        int numRowsOnAPage =
                (int) ((pageHeight - headerHeightOnPage) / oneRowHeight);
        double pageHeightForTable = oneRowHeight * numRowsOnAPage;
        int totalNumPages = (int) Math.ceil(((double) tblBusList.getRowCount()) / numRowsOnAPage);
        if (pageIndex >= totalNumPages) {
            return NO_SUCH_PAGE;
        }

        g2.translate(pageFormat.getImageableX(),
                pageFormat.getImageableY());
        g2.drawString("Page: " + (pageIndex + 1), (int) pageWidth / 2 - 35,
                (int) (pageHeight + fontHeight - fontDesent));//bottom center

        g2.translate(0f, headerHeightOnPage);
        g2.translate(0f, -pageIndex * pageHeightForTable);
        if (pageIndex + 1 == totalNumPages) {
            int lastRowPrinted = numRowsOnAPage * pageIndex;
            int numRowsLeft = tblBusList.getRowCount() - lastRowPrinted;
            g2.setClip(0, (int) (pageHeightForTable * pageIndex),
                    (int) Math.ceil(tableWidthOnPage),
                    (int) Math.ceil(oneRowHeight * numRowsLeft));
        } else {
            g2.setClip(0, (int) (pageHeightForTable * pageIndex),
                    (int) Math.ceil(tableWidthOnPage),
                    (int) Math.ceil(pageHeightForTable));
        }
        g2.scale(scale, scale);
        tblBusList.paint(g2);
        g2.scale(1 / scale, 1 / scale);
        g2.translate(0f, pageIndex * pageHeightForTable);
        g2.translate(0f, -headerHeightOnPage);
        g2.setClip(0, 0, (int) Math.ceil(tableWidthOnPage),
                (int) Math.ceil(headerHeightOnPage));
        g2.scale(scale, scale);
        tblBusList.getTableHeader().paint(g2);//paint header at top

        return Printable.PAGE_EXISTS;
    }
}//class closed

