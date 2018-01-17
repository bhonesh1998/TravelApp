
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class NewUser extends JInternalFrame {

    private JLabel lblUsername,  lblPassword,  lblConfirmMsg,  lblName,  lblCategory;
    private JPasswordField txtPassword,  txtCPassword;
    private JTextField txtUsername,  txtName;
    private JButton btnSave,  btnCancel;
    private JComboBox cmbCategory;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    public NewUser() {
        super("Adding New User", false, true, false, true);
        this.setSize(350, 270);
        this.setLocation((screen.width - 500) / 2, ((screen.height - 350) / 2));
        this.setLayout(null);

        lblName = new JLabel("Name");
        lblCategory = new JLabel("Category");
        lblUsername = new JLabel("Username");
        lblPassword = new JLabel("Password");
        lblConfirmMsg = new JLabel("Re-enter Password");
        txtName = new JTextField();
        cmbCategory = new JComboBox();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtCPassword = new JPasswordField();
        btnSave = new JButton("Save", new ImageIcon(ClassLoader.getSystemResource("images/save.png")));
        btnCancel = new JButton("Cancel", new ImageIcon(ClassLoader.getSystemResource("images/cancel.png")));

        cmbCategory.addItem("Manager");
        cmbCategory.addItem("Booking Clerk");
        cmbCategory.addItem("Supervisor");

        lblName.setBounds(30, 20, 150, 25);
        this.add(lblName);
        txtName.setBounds(150, 20, 150, 25);
        this.add(txtName);
        lblCategory.setBounds(30, 50, 100, 25);
        this.add(lblCategory);
        cmbCategory.setBounds(150, 50, 150, 25);
        this.add(cmbCategory);
        lblUsername.setBounds(30, 80, 100, 25);
        this.add(lblUsername);
        txtUsername.setBounds(150, 80, 150, 25);
        this.add(txtUsername);
        lblPassword.setBounds(30, 110, 100, 25);
        this.add(lblPassword);
        txtPassword.setBounds(150, 110, 150, 25);
        this.add(txtPassword);
        lblConfirmMsg.setBounds(30, 140, 110, 25);
        this.add(lblConfirmMsg);
        txtCPassword.setBounds(150, 140, 150, 25);
        this.add(txtCPassword);
        btnSave.setBounds(60, 180, 100, 25);
        this.add(btnSave);
        btnCancel.setBounds(180, 180, 100, 25);
        this.add(btnCancel);

        txtName.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isLetter(c) || (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Invalid Character", "ERROR", JOptionPane.ERROR_MESSAGE);
                    e.consume();
                }
            }
        });
        txtUsername.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isLetter(c) || (c == KeyEvent.VK_BACK_SPACE) || (Character.isDigit(c)) || (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Invalid Character", "ERROR", JOptionPane.ERROR_MESSAGE);
                    e.consume();
                }
            }
        });
        btnSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (txtName.getText() == null || txtName.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Name", "Missing fields", JOptionPane.DEFAULT_OPTION);
                    txtName.requestFocus();
                    return;
                }
                if (txtUsername.getText() == null || txtUsername.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Username", "Missing fields", JOptionPane.DEFAULT_OPTION);
                    txtUsername.requestFocus();
                    return;
                }
                if (txtPassword.getText() == null || txtPassword.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Password", "Missing fields", JOptionPane.DEFAULT_OPTION);
                    txtPassword.requestFocus();
                    return;
                }
                if (txtCPassword.getText() == null || txtCPassword.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Confirm your password", "Missing fields", JOptionPane.DEFAULT_OPTION);
                    txtCPassword.requestFocus();
                    return;
                }
                if (!txtPassword.getText().equals(txtCPassword.getText())) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match.", "ERROR", JOptionPane.DEFAULT_OPTION);
                    txtCPassword.requestFocus();
                    return;
                }
                try {
                    Statement stmt = DBConnection.getDBConnection().createStatement();
                    String sql = "INSERT INTO users (Name,Category,username, password) VALUES ('" +
                            txtName.getText() + "', '" + cmbCategory.getSelectedItem() + "', '" +
                            txtUsername.getText() + "', '" + txtPassword.getText() + "')";
                    int result = stmt.executeUpdate(sql);
                    if (result > 0) {
                        JOptionPane.showMessageDialog(null, "User details is succesfully added", "SUCCESS", JOptionPane.DEFAULT_OPTION);
                        dispose();
                    }//if closed
                } catch (Exception in) {
                    JOptionPane.showMessageDialog(null, "Error on database updation", "Updation failed", JOptionPane.ERROR_MESSAGE);
                }//try catch closed
            }
        });

        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }//constructor closed
}//class closed

