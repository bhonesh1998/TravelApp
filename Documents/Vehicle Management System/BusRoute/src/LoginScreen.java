
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginScreen extends JFrame {

    private JLabel lblUsername,  lblPasswd,  lblCat;
    public JTextField txtUser;
    private JPasswordField txtPasswd;
    private JButton btnLogin,  btnCancel;
    private JComboBox cmbCat;
    private Connection con;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    public LoginScreen() {
        super("System Login");
        this.getContentPane().setLayout(null);
        this.setSize(370, 250);
        this.setResizable(false);
        this.setLocation((screen.width - 500) / 2, ((screen.height - 350) / 2));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        lblUsername = new JLabel("Username");
        lblPasswd = new JLabel("Password");
        txtUser = new JTextField();
        txtPasswd = new JPasswordField();
        lblCat = new JLabel("Login As");
        cmbCat = new JComboBox();
        cmbCat.addItem("Manager");
        cmbCat.addItem("Supervisor");
        cmbCat.addItem("Booking Clerk");
        btnLogin = new JButton("Login", new ImageIcon(ClassLoader.getSystemResource("images\\Login.png")));
        btnCancel = new JButton("Cancel", new ImageIcon(ClassLoader.getSystemResource("images\\Cancel.png")));

        lblUsername.setBounds(40, 30, 100, 25);
        lblPasswd.setBounds(40, 65, 100, 25);
        lblCat.setBounds(40, 100, 100, 25);
        txtUser.setBounds(150, 30, 160, 25);
        txtPasswd.setBounds(150, 65, 160, 25);
        cmbCat.setBounds(150, 100, 160, 25);
        btnLogin.setBounds(70, 150, 100, 25);
        btnCancel.setBounds(190, 150, 100, 25);

        lblUsername.setFont(new Font("monospaced", Font.BOLD, 16));
        lblPasswd.setFont(new Font("monospaced", Font.BOLD, 16));
        lblCat.setFont(new Font("monospaced", Font.BOLD, 16));
        cmbCat.setFont(new Font("monospaced", Font.BOLD, 16));
        txtUser.setFont(new Font("monospaced", Font.CENTER_BASELINE, 16));
        txtPasswd.setFont(new Font("monospaced", Font.CENTER_BASELINE, 16));
        this.add(lblUsername);
        this.add(txtUser);
        this.add(lblPasswd);
        this.add(txtPasswd);
        this.add(btnLogin);
        this.add(btnCancel);
        this.add(lblCat);
        this.add(cmbCat);
        this.add(btnLogin);
        this.add(btnCancel);

        ButtonListener listener = new ButtonListener();
        btnLogin.addActionListener(listener);
        btnCancel.addActionListener(listener);
        con = DBConnection.getDBConnection();
        if (con == null) {
            JOptionPane.showMessageDialog(null, "Error on establishing database connection", "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }//constructor closed

    public void login() {
        String username = txtUser.getText();
        String password = txtPasswd.getText();
        String SQL;
        String category = cmbCat.getSelectedItem().toString();
        SQL = "SELECT * FROM users WHERE username='" + username + "'  AND password='" +
                password + "'AND Category='" + category + "'";
        try {
            Statement stmt = con.createStatement();
            stmt.execute(SQL);
            ResultSet rs = stmt.getResultSet();
            boolean recordfound = rs.next();
            if (recordfound) {
                LoadMDIWindow();                
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "The system could not log you in.\n" +
                        " Please make sure your username and password are correct", "Login Failure", JOptionPane.INFORMATION_MESSAGE);
                txtUser.setText("");
                txtPasswd.setText("");
                txtUser.requestFocus();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error on login operation", "Login Error", JOptionPane.ERROR_MESSAGE);
        }//try catch closed
    }//Login() closed
        public void LoadMDIWindow() {
        if (cmbCat.getSelectedItem().equals("Manager")) {
            new MDIWindow().LoginManager();            
        } else if (cmbCat.getSelectedItem().equals("Supervisor")) {
            new MDIWindow().LoginSupervisor();
        } else {
            new MDIWindow().LoginClerk();
        }
    }//LoginValidity() closed
        
    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnLogin) {
                if (txtUser.getText() == null || txtUser.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter username", "Missing field", JOptionPane.DEFAULT_OPTION);
                    txtUser.requestFocus();
                    return;
                }
                if (txtPasswd.getText() == null || txtPasswd.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter password", "Missing field", JOptionPane.DEFAULT_OPTION);
                    txtPasswd.requestFocus();
                    return;
                }
                login();
            } else if (e.getSource() == btnCancel) {
                System.exit(0);
            }//if else closed
        }//actionPerformed() closed
    }//ButtonListner class closed

}//LoginScreen class closed

