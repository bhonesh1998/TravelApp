
import java.sql.*;
import javax.swing.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;

class Bus_Details extends JInternalFrame {

    public Container content;
    public JPanel reportingPanel;
    public JTabbedPane listsTabs;
    public JTextArea listPane;
    public JPanel reportPanel;
    public static final int SET_SIZE = 0,  PRINT = 1,  CLOSE = 2;
    public JPanel statusPanel;
    public JComboBox graphTypesCombo;
    public Color skyblue = new Color(150, 190, 255);
    public final ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("Images/appicon.png"));
    private static Connection dbcon = null;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Statement stmt = null;
    private JButton print,  cancel;
    private JPanel panel;

    public Bus_Details() {

        super("Buses Report", true, true, true, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        content = getContentPane();
        content.setBackground(skyblue);
        listsTabs = new JTabbedPane();
        print = new JButton("Print ", new ImageIcon(ClassLoader.getSystemResource("Images/print.png")));
        cancel = new JButton("Cancel", new ImageIcon(ClassLoader.getSystemResource("Images/exit.png")));
        panel = new JPanel();
        panel.add(print);
        panel.add(cancel);

        reportingPanel = new JPanel();
        reportingPanel.setLayout(new BorderLayout());
        reportingPanel.setBorder(BorderFactory.createEtchedBorder());
        reportingPanel.add(new JLabel("Bus Report"), BorderLayout.NORTH);
        reportingPanel.add(panel, BorderLayout.SOUTH);

        reportPanel = new JPanel();
        reportPanel.setLayout(new GridLayout(1, 1));
        reportPanel.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.blue));
        reportPanel.setBackground(Color.white);

        reportingPanel.add(new JScrollPane(reportPanel), BorderLayout.CENTER);
        listsTabs.add(reportingPanel);
        setLocation((screen.width - 1270) / 2, ((screen.height - 740) / 2));
        setResizable(false);

        listPane = new JTextArea() {
            Image image = imageIcon.getImage();

            {
                setOpaque(false);
            }

            public void paint(Graphics g) {
                super.paint(g);
            }
        };


        listPane.setEditable(false);
        listPane.setFont(new Font("Serif", Font.BOLD, 12));
        listPane.setForeground(Color.black);

        listPane.setLineWrap(true);
        listPane.setWrapStyleWord(true);
        reportPanel.add(listPane);

        try {
            Statement s = DBConnection.getDBConnection().createStatement();
        } catch (Exception excp) {
            excp.printStackTrace();
        }
        printList();
        content.add(listsTabs, BorderLayout.CENTER);
        cancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();

            }
        });


        print.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Printer Not Found", "PRINT ERROR", JOptionPane.DEFAULT_OPTION);
            }
        });
        setSize(1000, 720);
    }//constructor closed
    private void printList() {
        try {

            ResultSet rst = DBConnection.getDBConnection().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE).executeQuery("select Bus_RegNo,Model,Capacity,insurance_Status,Insurance_Expiry from Buses");
            listPane.append("\n");
            listPane.append("       ");
            listPane.append("Bus RegNo" + "\t\t" + "Model" + "\t\t" + "Capacity" + "\t\t" + "Insurance_Status\t" + "Expiry Date\n");
            listPane.append("\n");
            while (rst.next()) {
                listPane.append("       ");
                listPane.append(rst.getString(1).trim());
                listPane.append("\t\t");
                listPane.append(rst.getString(2).trim());
                listPane.append("\t\t");
                listPane.append(rst.getString(3).trim());
                listPane.append("\t\t");
                listPane.append(rst.getString(4).trim());
                listPane.append("\t\t");
                listPane.append(rst.getString(5).trim());
                listPane.append("\n\n");
            }
            if (rst != null) {
                rst.close();
            }

        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, " No Records found" + sqle.getMessage());
            return;
        }
    }
}//class closed

