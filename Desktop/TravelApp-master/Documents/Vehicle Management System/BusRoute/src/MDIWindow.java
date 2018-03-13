
import java.awt.*;
import java.text.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.sql.*;
import javax.swing.plaf.metal.*;

public class MDIWindow extends JFrame implements WindowListener {

    private JMenu mnuOperations,  mnuFiles,  mnuReports,  mnuProcesses,  mnuTools,  mnuHelp;
    private JMenuItem mnuNewuser,  mnuExit;
    private JMenuItem mnuBuses,  mnuEmps,  mnuRoutes,  mnuPassengers;
    private JMenuItem mnuBooking,  mnuScheduling,  mnuPayment;
    private JMenuItem mnuBusRpt,  mnuEmpRpt,  mnuSchedRpt,  mnuBookRpt;
    private JMenuItem mnuCalculator,  mnuNotepad;
    private JMenuItem mnuUsrMannual;
    private JLabel welcome;
    public static JDesktopPane desktop;
    String StrBusinesTitle;
    public JButton NewJButton;
    Connection getConnection;
    //Main mnuBuses;
    /*Employee Emp;
    Author aut;
    Schedule Sched;
    Route Rut;
    PASS pass;
    frmSplash FormSplash = new frmSplash();
    ResultSet rsLogin;
    Thread ThFormSplash = new Thread(FormSplash);*/
    public MDIWindow() {
        super("Bus Scheduling System");

        this.setJMenuBar(CreateJMenuBar());
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/appicon.png")).getImage());
        this.setLocation(0, 0);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.addWindowListener(this);

        welcome = new JLabel("Welcome:  Today is " + new java.util.Date() + " ", JLabel.CENTER);
        welcome.setFont(new Font("monospaced", Font.PLAIN, 12));
        welcome.setForeground(Color.blue);
        desktop = new JDesktopPane();
        desktop.setBorder(BorderFactory.createEmptyBorder());
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        getContentPane().add(welcome, BorderLayout.PAGE_END, JLabel.CENTER);
        getContentPane().add(desktop, BorderLayout.CENTER);

        setVisible(true);
    }//Constructor closed
    protected JMenuBar CreateJMenuBar() {
        JMenuBar menubar = new JMenuBar();
        /**********CREATING OPERATIONS MENU***********************/
        mnuOperations = new JMenu("Operations");
        mnuOperations.setForeground((Color.blue));
        mnuOperations.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuOperations.setMnemonic('O');
        mnuOperations.setEnabled(false);

        mnuNewuser = new JMenuItem("AddNew User");
        mnuNewuser.setForeground(Color.blue);
        mnuNewuser.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuNewuser.setMnemonic('L');
        mnuNewuser.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/users.png")));
        mnuNewuser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        mnuNewuser.setActionCommand("newuser");
        mnuNewuser.addActionListener(menulistener);

        mnuExit = new JMenuItem("Exit");
        mnuExit.setForeground(Color.blue);
        mnuExit.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuExit.setMnemonic('E');
        mnuExit.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/exit.png")));
        mnuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        mnuExit.setActionCommand("exit");
        mnuExit.addActionListener(menulistener);

        mnuOperations.add(mnuNewuser);
        mnuOperations.addSeparator();
        mnuOperations.add(mnuExit);
        menubar.add(mnuOperations);

        /****************CREATING FILES MENU ********************/
        mnuFiles = new JMenu("Files");
        mnuFiles.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuFiles.setForeground((Color.blue));
        mnuFiles.setMnemonic('F');
        mnuFiles.setEnabled(false);

        mnuBuses = new JMenuItem("Buses");
        mnuBuses.setForeground(Color.blue);
        mnuBuses.setEnabled(true);
        mnuBuses.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuBuses.setMnemonic('B');
        mnuBuses.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Buses.png")));
        mnuBuses.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        mnuBuses.setActionCommand("buses");
        mnuBuses.addActionListener(menulistener);

        mnuEmps = new JMenuItem("Employees");
        mnuEmps.setForeground(Color.blue);
        mnuEmps.setEnabled(true);
        mnuEmps.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuEmps.setMnemonic('E');
        mnuEmps.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Emps.png")));
        mnuEmps.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        mnuEmps.setActionCommand("employees");
        mnuEmps.addActionListener(menulistener);

        mnuRoutes = new JMenuItem("Routes");
        mnuRoutes.setEnabled(true);
        mnuRoutes.setForeground(Color.blue);
        mnuRoutes.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuRoutes.setMnemonic('R');
        mnuRoutes.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Routes.png")));
        mnuRoutes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        mnuRoutes.setActionCommand("routes");
        mnuRoutes.addActionListener(menulistener);

        mnuPassengers = new JMenuItem("Passengerss");
        mnuPassengers.setForeground(Color.blue);
        mnuPassengers.setEnabled(false);
        mnuPassengers.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuPassengers.setMnemonic('P');
        mnuPassengers.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/pass.png")));
        mnuPassengers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        mnuPassengers.setActionCommand("passengers");
        mnuPassengers.addActionListener(menulistener);

        mnuFiles.add(mnuBuses);
        mnuFiles.add(mnuEmps);
        mnuFiles.add(mnuRoutes);
        mnuFiles.add(mnuPassengers);
        menubar.add(mnuFiles);

        /********************* CREATING PROCESSES MENU ***********************/
        mnuProcesses = new JMenu("Processes ");
        mnuProcesses.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuProcesses.setForeground((Color.blue));
        mnuProcesses.setMnemonic('P');

        mnuScheduling = new JMenuItem("Scheduling");
        mnuScheduling.setEnabled(false);
        mnuScheduling.setForeground(Color.blue);
        mnuScheduling.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuScheduling.setMnemonic('S');
        mnuScheduling.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/schedule.png")));
        mnuScheduling.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        mnuScheduling.setActionCommand("scheduling");
        mnuScheduling.addActionListener(menulistener);

        mnuBooking = new JMenuItem("Booking");
        mnuBooking.setEnabled(false);
        mnuBooking.setForeground(Color.blue);
        mnuBooking.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuBooking.setMnemonic('B');
        mnuBooking.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Booking.png")));
        mnuBooking.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
        mnuBooking.setActionCommand("booking");
        mnuBooking.addActionListener(menulistener);

        mnuPayment = new JMenuItem("Payments");
        mnuPayment.setForeground(Color.blue);
        mnuPayment.setEnabled(false);
        mnuPayment.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuPayment.setMnemonic('P');
        mnuPayment.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/Payments.png")));
        mnuPayment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        mnuPayment.setActionCommand("payments");
        mnuPayment.addActionListener(menulistener);

        mnuProcesses.add(mnuScheduling);
        mnuProcesses.add(mnuBooking);
        mnuProcesses.add(mnuPayment);
        menubar.add(mnuProcesses);

        /************************* CREATING REPORTS MENU ********************/
        mnuReports = new JMenu("Reports ");
        mnuReports.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuReports.setForeground(Color.blue);
        mnuReports.setMnemonic('R');


        mnuBusRpt = new JMenuItem("Bus Report");
        mnuBusRpt.setForeground(Color.blue);
        mnuBusRpt.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuBusRpt.setMnemonic('P');
        mnuBusRpt.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/busreport.png")));
        mnuBusRpt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        mnuBusRpt.setActionCommand("busreport");
        mnuBusRpt.addActionListener(menulistener);

        mnuEmpRpt = new JMenuItem("Employee Report");
        mnuEmpRpt.setForeground(Color.blue);
        mnuEmpRpt.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuEmpRpt.setMnemonic('P');
        mnuEmpRpt.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/empreport.png")));
        mnuEmpRpt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        mnuEmpRpt.setActionCommand("empreport");
        mnuEmpRpt.addActionListener(menulistener);

        mnuSchedRpt = new JMenuItem("Scheduling Report");
        mnuSchedRpt.setForeground(Color.blue);
        mnuSchedRpt.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuSchedRpt.setMnemonic('S');
        mnuSchedRpt.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/schedreport.png")));
        mnuSchedRpt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        mnuSchedRpt.setActionCommand("schedulereport");
        mnuSchedRpt.addActionListener(menulistener);

        mnuBookRpt = new JMenuItem("Booking Report");
        mnuBookRpt.setForeground(Color.blue);
        mnuBookRpt.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuBookRpt.setMnemonic('B');
        mnuBookRpt.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/bookreport.png")));
        mnuBookRpt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        mnuBookRpt.setActionCommand("bookrepoort");
        mnuBookRpt.addActionListener(menulistener);

        mnuReports.add(mnuBusRpt);
        mnuReports.add(mnuEmpRpt);
        mnuReports.add(mnuSchedRpt);
        mnuReports.add(mnuBookRpt);
        menubar.add(mnuReports);

        /******************* CREATING TOOLS MENU ***************************/
        mnuTools = new JMenu("Tools ");
        mnuTools.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuTools.setForeground(Color.blue);
        mnuTools.setMnemonic('T');

        mnuCalculator = new JMenuItem("Calculator");
        mnuCalculator.setForeground(Color.blue);
        mnuCalculator.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuCalculator.setMnemonic('C');
        mnuCalculator.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/calc.png")));
        mnuCalculator.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        mnuCalculator.setActionCommand("calculator");
        mnuCalculator.addActionListener(menulistener);

        mnuNotepad = new JMenuItem("Notepad");
        mnuNotepad.setForeground(Color.blue);
        mnuNotepad.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuNotepad.setMnemonic('N');
        mnuNotepad.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/notepad.png")));
        mnuNotepad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        mnuNotepad.setActionCommand("notepad");
        mnuNotepad.addActionListener(menulistener);

        mnuTools.add(mnuCalculator);
        mnuTools.add(mnuNotepad);
        menubar.add(mnuTools);

        /*********************** CREATING HELP MENU **************************/
        mnuHelp = new JMenu("Help ");
        mnuHelp.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuHelp.setForeground(Color.blue);
        mnuHelp.setMnemonic('H');

        mnuUsrMannual = new JMenuItem("User Manual");
        mnuUsrMannual.setForeground(Color.blue);
        mnuUsrMannual.setFont(new Font("monospaced", Font.PLAIN, 12));
        mnuUsrMannual.setMnemonic('U');
        mnuUsrMannual.setIcon(new ImageIcon(ClassLoader.getSystemResource("images/help.png")));
        mnuUsrMannual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        mnuUsrMannual.setActionCommand("mannual");
        mnuUsrMannual.addActionListener(menulistener);

        mnuHelp.add(mnuUsrMannual);
        menubar.add(mnuHelp);
        return menubar;
    }//CreateJMenuBar()closed
    
    ActionListener menulistener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            String ActCmd = e.getActionCommand();
            if (ActCmd.equalsIgnoreCase("calculator")) {
                try {
                    Runtime.getRuntime().exec("calc.exe");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error,Cannot start calculator", "Applicaton Error", JOptionPane.ERROR_MESSAGE);
                }//try catch closed
            } else if (ActCmd.equalsIgnoreCase("notepad")) {
                try {
                    Runtime.getRuntime().exec("notepad.exe");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error,Cannot start notepad", "Applicaton Error", JOptionPane.ERROR_MESSAGE);
                }//try catch closed
            } else if (ActCmd.equalsIgnoreCase("newuser")) {
                NewUser frm = new NewUser();
                desktop.add(frm);
                frm.setVisible(true);
            } else if (ActCmd.equalsIgnoreCase("exit")) {
                ConfirmExit();
            } else if (ActCmd.equalsIgnoreCase("buses")) {
                Buses frm = new Buses();
                desktop.add(frm);
                frm.setVisible(true);
            } else if (ActCmd.equalsIgnoreCase("employees")) {
                Employee frm = new Employee();
                desktop.add(frm);
                frm.setVisible(true);
            } else if (ActCmd.equalsIgnoreCase("routes")) {
                Route frm = new Route();
                desktop.add(frm);
                frm.setVisible(true);
            } else if (ActCmd.equalsIgnoreCase("passengers")) {
                Passengers frm = new Passengers();
                desktop.add(frm);
                frm.setVisible(true);
            } else if (ActCmd.equalsIgnoreCase("scheduling")) {
                Schedule frm = new Schedule();
                desktop.add(frm);
                frm.setVisible(true);
            } else if (ActCmd.equalsIgnoreCase("booking")) {
                Booking frm = new Booking();
                desktop.add(frm);
                frm.setVisible(true);
            } else if (ActCmd.equalsIgnoreCase("payments")) {
                Payment frm=new Payment();
                desktop.add(frm);
                frm.setVisible(true);
            }else if(ActCmd.equalsIgnoreCase("busreport")){
                Bus_Details frm=new Bus_Details();
                desktop.add(frm);
                frm.setVisible(true);
            }else if(ActCmd.equalsIgnoreCase("empreport")){
                employee_report frm=new employee_report();
                desktop.add(frm);
                frm.setVisible(true);
            }else if(ActCmd.equalsIgnoreCase("schedulereport")){
                Scheduling_report frm=new Scheduling_report();
                desktop.add(frm);
                frm.setVisible(true);
            }else if(ActCmd.equalsIgnoreCase("bookrepoort")){
                Booking_report frm=new Booking_report();
                desktop.add(frm);
                frm.setVisible(true);
            }
        }
    };

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {


        ConfirmExit();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(
            WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    private void ConfirmExit() {
        String ObjButtons[] = {"Yes", "No"};
        int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure to exit?",
                "Confirm exit", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
        if (PromptResult == 0) {
            System.exit(0);
        }//if closed
    }//ConfirmExit() closed
    protected boolean isLoaded(String FormTitle) {
        JInternalFrame Form[] = desktop.getAllFrames();
        for (int i = 0; i < Form.length; i++) {
            if (Form[i].getTitle().equalsIgnoreCase(FormTitle)) {
                Form[i].show();
                try {
                    Form[i].setIcon(true);
                    Form[i].setSelected(true);
                } catch (Exception e) {
                }
                return true;
            }
        }
        return false;
    }//isLoaded() closed

    /*protected void runComponents(String sComponents) {
    Runtime rt = Runtime.getRuntime();
    try {
    rt.exec(sComponents);
    } catch (IOException evt) {
    JOptionPane.showMessageDialog(null, evt.getMessage(), "Error Found", JOptionPane.ERROR_MESSAGE);
    }
    }
    protected void runURL(String sURL) {
    try {
    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + sURL);
    } catch (Exception ex) {
    }
    }*/
    public void LoginManager() {
        mnuOperations.setEnabled(true);
        mnuFiles.setEnabled(true);
        mnuBooking.setEnabled(true);
        mnuScheduling.setEnabled(true);
        mnuPayment.setEnabled(true);
        mnuRoutes.setEnabled(true);
        mnuEmps.setEnabled(true);
        mnuBuses.setEnabled(true);
        mnuPassengers.setEnabled(true);
    }//LoginManager() closed
    public void LoginSupervisor() {
        mnuFiles.setEnabled(true);
        mnuScheduling.setEnabled(true);
        mnuPayment.setEnabled(true);
        mnuRoutes.setEnabled(true);
        mnuEmps.setEnabled(true);
        mnuBuses.setEnabled(true);
    }//LoginSupervisor() closed
    public void LoginClerk() {
        mnuBooking.setEnabled(true);
        mnuPayment.setEnabled(true);
        mnuFiles.setEnabled(true);
        mnuPassengers.setEnabled(true);
    }//LoginClerk() closed
    }//class closed

