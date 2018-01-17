
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import java.text.*;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import java.text.*;
import java.util.Date;

class Receipt extends JInternalFrame {

    public Container content;
    public JPanel reportingPanel;
    public JTabbedPane listsTabs;
    public JPanel chartPanel;
    public JButton hide;
    public JTextArea listPane;
    public JPanel reportPanel;
    public JButton drawGraphButton;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    public int ID;
    public Color skyblue = new Color(150, 190, 255);
    public final ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("Images/appicon.png"));
    private static Connection dbcon = null;
    private JButton print,  cancel;
    private JPanel panel;
    Statement stmt = null;

    public Receipt() {

        super("Receipt",true,true,true,true);

        content = getContentPane();
        content.setBackground(skyblue);

        print = new JButton("Print ", new ImageIcon(ClassLoader.getSystemResource("Images/print.png")));
        cancel = new JButton("Cancel", new ImageIcon(ClassLoader.getSystemResource("Images/exit.png")));
        panel = new JPanel();
        panel.add(print);
        panel.add(cancel);
        reportingPanel = new JPanel();
        reportingPanel.setLayout(new BorderLayout());
        reportingPanel.setBorder(BorderFactory.createEtchedBorder());
        reportingPanel.add(new JLabel("Receipt for Payment"), BorderLayout.NORTH);
        reportingPanel.add(panel, BorderLayout.SOUTH);
        reportPanel = new JPanel();
        reportPanel.setLayout(new GridLayout(1, 1));
        reportPanel.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.blue));
        reportPanel.setBackground(Color.white);

        reportingPanel.add(new JScrollPane(reportPanel), BorderLayout.CENTER);
        produceCertificate();
        listPane.setEditable(false);
        listPane.setFont(new Font("Serif", Font.PLAIN, 12));
        listPane.setForeground(Color.black);

        listPane.setLineWrap(true);
        listPane.setWrapStyleWord(true);
        reportPanel.add(listPane);
        setLocation((screen.width - 1270) / 2, ((screen.height - 740) / 2));
        setResizable(false);
        try {

            Statement s = DBConnection.getDBConnection().createStatement();
        } catch (Exception excp) {
            excp.printStackTrace();
        }

        JPanel dpanel = new JPanel();
        dpanel.setBorder(BorderFactory.createLoweredBevelBorder());
        dpanel.setLayout(new GridLayout(1, 1));
        DateFormat defaultDate = DateFormat.getDateInstance(DateFormat.FULL);
        content.add(reportingPanel, BorderLayout.CENTER);

        setLocation(5, 0);
        setSize(500, 400);

    }

    public void produceCertificate() {
        listPane = new JTextArea() {

            Image image = imageIcon.getImage();

            {
                setOpaque(false);
            }

            public void paint(Graphics g) {
                g.setColor(Color.black);
                g.drawString("PAYMENT RECEIPT", 200, 50);
                g.drawString("Payment Number      " + new Payment().text1.getText(), 80, 100);
                g.drawString("Name of Passenger   " + new Payment().combo2.getSelectedItem(), 80, 130);
                g.drawString("Amount Paid Rs   " + new Payment().combo8.getSelectedItem(), 80, 160);
                g.drawString("Pay on              " + new Payment().combo4.getSelectedItem(), 80, 190);
                g.drawString("Date Paid      " + new Payment().p_date.getText(), 280, 220);
                g.drawString("Received By         " + new Payment().combo3.getSelectedItem(), 80, 220);
                g.setColor(Color.red);
                g.drawString("Welcome back....and..... Safe Journey", 200, 260);
                super.paint(g);
            }
        };
    }
}
