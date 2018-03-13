
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.*;
import java.io.*;
import java.sql.*;
import javax.swing.plaf.metal.*;
import java.text.*;

public class Schedule extends JInternalFrame {

    private JLabel BusNo,  RegNo,  RouteNo,  RouteName,  DriverNo,  DriverName,  DeptTime,  date,  Trip;
    private JComboBox cboBusNo,  cboRouteNo,  cboRouteName,  cboDriverNo,  cboDriverName,  cboRegNo,  cboTrip;
    private JTextField txtDepTime,  txtdate;
    private JButton Check,  Schedule,  Cancel,  btnPrint;
    private DateButton s_date;
    int Year;
    String is;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    private JPanel jPanel1;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private static JTextArea txtInfo = new JTextArea(15, 40);
    private Connection dbconn;
    private static String info;

    public Schedule() {
        super("Scheduling Process", false, true, false, true);
        BusNo = new JLabel("Bus Number ");
        RegNo = new JLabel("Reg Number ");
        RouteNo = new JLabel("Route Number");
        RouteName = new JLabel("Route Name ");
        DriverNo = new JLabel("Driver Number");
        DriverName = new JLabel("Driver Name");
        date = new JLabel("Date Scheduled");
        Trip = new JLabel("Trip Number");
        DeptTime = new JLabel("Departure Time");

        cboBusNo = new JComboBox();
        cboRegNo = new JComboBox();
        cboRouteNo = new JComboBox();
        cboRouteName = new JComboBox();
        cboDriverNo = new JComboBox();
        cboDriverName = new JComboBox();
        txtDepTime = new JTextField(10);
        cboTrip = new JComboBox();
        Check = new JButton("View Shedules", new ImageIcon(ClassLoader.getSystemResource("Images/viewschd.png")));
        Schedule = new JButton("Schedule", new ImageIcon(ClassLoader.getSystemResource("Images/schd.png")));
        Cancel = new JButton("Cancel", new ImageIcon(ClassLoader.getSystemResource("Images/exit.png")));
        btnPrint = new JButton("Print", new ImageIcon(ClassLoader.getSystemResource("Images/print.png")));
        s_date = new DateButton();
        cboTrip.addItem("1");
        cboTrip.addItem("2");
        //Labels Settings
        BusNo.setFont(new Font("sansserif", Font.ITALIC, 14));
        RegNo.setFont(new Font("sansserif", Font.ITALIC, 14));
        RouteNo.setFont(new Font("sansserif", Font.ITALIC, 14));
        RouteName.setFont(new Font("sansserif", Font.ITALIC, 14));
        DriverNo.setFont(new Font("sansserif", Font.ITALIC, 14));
        date.setFont(new Font("sansserif", Font.ITALIC, 14));
        Trip.setFont(new Font("sansserif", Font.ITALIC, 14));
        DeptTime.setFont(new Font("sansserif", Font.ITALIC, 14));
        DriverName.setFont(new Font("sansserif", Font.ITALIC, 14));
        DriverName.setForeground(Color.blue);
        DeptTime.setForeground(Color.blue);
        BusNo.setForeground(Color.blue);
        RegNo.setForeground(Color.blue);
        RouteNo.setForeground(Color.blue);
        RouteName.setForeground(Color.blue);
        DriverNo.setForeground(Color.blue);
        DriverName.setForeground(Color.blue);
        date.setForeground(Color.blue);
        Trip.setForeground(Color.blue);

        jPanel1 = new JPanel(new java.awt.GridLayout(9, 2));
        jPanel1.add(BusNo);
        jPanel1.add(cboBusNo);
        jPanel1.add(RegNo);
        jPanel1.add(cboRegNo);
        jPanel1.add(RouteNo);
        jPanel1.add(cboRouteNo);
        jPanel1.add(RouteName);
        jPanel1.add(cboRouteName);
        jPanel1.add(DriverNo);
        jPanel1.add(cboDriverNo);
        jPanel1.add(DriverName);
        jPanel1.add(cboDriverName);
        jPanel1.add(DeptTime);
        jPanel1.add(txtDepTime);
        jPanel1.add(date);
        jPanel1.add(s_date);
        jPanel1.add(Trip);
        jPanel1.add(cboTrip);

        cboRouteNo.addItem("Select");
        cboBusNo.addItem("Select");
        cboRouteName.addItem("Select");
        cboDriverNo.addItem("Select");
        cboDriverName.addItem("Select");
        cboRegNo.addItem("Select");
        //Labels Settings

        jPanel3 = new javax.swing.JPanel(new java.awt.FlowLayout());

        jPanel3.add(jPanel1);

        jPanel4 = new javax.swing.JPanel(new java.awt.FlowLayout());
//		jPanel4.add(Enable);
        jPanel4.add(Check);
        jPanel4.add(Schedule);
        jPanel4.add(Cancel);
        jPanel4.add(btnPrint);
        setSize(550, 330);
        add(jPanel3);
        setLocation((screen.width - 500) / 2, ((screen.height - 350) / 2));
        setResizable(false);
        try {

            Statement s = DBConnection.getDBConnection().createStatement();
        } catch (Exception excp) {
            excp.printStackTrace();
            info = info + excp.toString();
        }
        setCbx();
        setCombo();
        setrt();

        Schedule.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                validator();
            }
        });
        
        Check.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                Show_schedules frm=new Show_schedules();
                MDIWindow.desktop.add(frm);
                frm.setVisible(true);
                try{
                    frm.setSelected(true);
                }catch(Exception ex){}
            new Show_schedules().setVisible(true);
            }
        });
        btnPrint.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                Scheduling_report frm=new Scheduling_report();
                MDIWindow.desktop.add(frm);
                frm.setVisible(true);
                try{
                    frm.setSelected(true);
                }catch(Exception ex){}                
            }
        });
        cboBusNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                cboRegNo.setSelectedIndex(cboBusNo.getSelectedIndex());

            }
        });
        cboRegNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                cboBusNo.setSelectedIndex(cboRegNo.getSelectedIndex());

            }
        });

        cboRouteNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                cboRouteName.setSelectedIndex(cboRouteNo.getSelectedIndex());

            }
        });
        cboRouteName.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                cboRouteNo.setSelectedIndex(cboRouteName.getSelectedIndex());

            }
        });
        cboDriverName.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                cboDriverNo.setSelectedIndex(cboDriverName.getSelectedIndex());

            }
        });
        cboDriverNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                cboDriverName.setSelectedIndex(cboDriverNo.getSelectedIndex());

            }
        });
        Cancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();

            }
        });




        jPanel5 = new javax.swing.JPanel(new java.awt.BorderLayout());

        jPanel5.add(jPanel3, BorderLayout.CENTER);
        jPanel5.add(jPanel4, BorderLayout.SOUTH);
        add(jPanel5);

    }

    private void setCbx() {
        try {
            ResultSet rst = DBConnection.getDBConnection().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT Bus_RegNo,BusNo FROM Buses order by BusNo");

            while (rst.next()) {

                cboRegNo.addItem(rst.getString(1));
                cboBusNo.addItem(rst.getString(2));
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
    }

    private void setrt() {
        try {
            ResultSet rst = DBConnection.getDBConnection().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT Route_No,RouteName FROM Route ");

            while (rst.next()) {
                cboRouteNo.addItem(rst.getString(1));
                cboRouteName.addItem(rst.getString(2));


            }
        } catch (Exception n) {
            n.printStackTrace();
        }
    }

    private void setCombo() {
        String dr;
        dr = "Driver";
        try {
            ResultSet rst = DBConnection.getDBConnection().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT Emp.empNo, Emp.Sname, Emp.Fname, Emp.Lname, Emp.Designation FROM Emp WHERE Emp.Designation='Driver'");
            while (rst.next()) {
                cboDriverNo.addItem(rst.getString(1));
                cboDriverName.addItem(rst.getString(2));
            }
        } catch (Exception n) {
            n.printStackTrace();
        }


    }

    public void validator() {
        String SQL;        
        SQL = ("SELECT * FROM Validator WHERE Bus_No='" + cboBusNo.getSelectedItem() + "' AND Trip_No='" + cboTrip.getSelectedItem() + "'AND Date_Schedule='" + s_date.getText() + "'OR DriverNo='" + cboDriverNo.getSelectedItem() + "' AND Trip_No='" + cboTrip.getSelectedItem() + "'AND Date_Schedule='" + s_date.getText() + "'");
        try {
            Statement stmt = DBConnection.getDBConnection().createStatement();
            stmt.execute(SQL);
            ResultSet rs = stmt.getResultSet();
            boolean recordfound = rs.next();
            if (recordfound) {

                JOptionPane.showMessageDialog(null, "Either You are Trying Give a driver \n" +
                        " Another bus or giving one bus two \n" +
                        "Drivers at the same time.", "Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            } else {
                try {
                    if (cboRouteNo.getSelectedItem() == ("Select")) {
                        JOptionPane.showMessageDialog(null, "please choose route number", "INFORMATION",
                                JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    if (cboRouteName.getSelectedItem() == ("Select")) {
                        JOptionPane.showMessageDialog(null, "please Choose RouteName", "INFORMATION",
                                JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    if (cboBusNo.getSelectedItem() == ("Select")) {
                        JOptionPane.showMessageDialog(null, "please choose bus number", "INFORMATION",
                                JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    if (cboRegNo.getSelectedItem() == ("Select")) {
                        JOptionPane.showMessageDialog(null, "please choose registration number", "INFORMATION",
                                JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    if (cboDriverNo.getSelectedItem() == ("Select")) {
                        JOptionPane.showMessageDialog(null, "please choose driver number", "INFORMATION",
                                JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    if (cboDriverName.getSelectedItem() == ("Select")) {
                        JOptionPane.showMessageDialog(null, "please choose DriverName", "INFORMATION",
                                JOptionPane.DEFAULT_OPTION);
                        return;
                    }

                    Statement statement = DBConnection.getDBConnection().createStatement();
                    {
                        String temp = "INSERT INTO Schedules (Bus_No,Bus_RegNo, Route_No, Route_Name,empNo,Driver_Name,Date_Scheduled,Trip_No,Dept_Time) VALUES ('" +
                                cboBusNo.getSelectedItem() + "', '" +
                                cboRegNo.getSelectedItem() + "', '" +
                                cboRouteNo.getSelectedItem() + "', '" +
                                cboRouteName.getSelectedItem() + "', '" +
                                cboDriverNo.getSelectedItem() + "', '" +
                                cboDriverName.getSelectedItem() + "', '" +
                                s_date.getText() + "', '" +
                                cboTrip.getSelectedItem() + "', '" +
                                txtDepTime.getText() + "')";
                        int result = statement.executeUpdate(temp);
                        String temp2 = "INSERT INTO Validator(Bus_No,DriverNo,RouteNo,Date_Schedule,Trip_No)VALUES('" +
                                cboBusNo.getSelectedItem() + "', '" +
                                cboDriverNo.getSelectedItem() + "', '" +
                                cboRouteNo.getSelectedItem() + "' ,'" +
                                s_date.getText() + "' ,'" +
                                cboTrip.getSelectedItem() + "')";

                        int results = statement.executeUpdate(temp2);

                        JOptionPane.showMessageDialog(null, "Scheduling Succesfully done", "Sucess??",
                                JOptionPane.DEFAULT_OPTION);
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex.toString());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
