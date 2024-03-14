package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import logicaMail.Conexion;

/**
 *
 * @author usuario
 */
public class LoginFrame extends JFrame {

    private JPanel loginPanel;
    private JTextField mailField;
    private JPasswordField passwordField;

    public LoginFrame() {
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Login");

        // Agregar componentes y manejar eventos según sea necesario
        createLoginComponents();

        setVisible(true);
    }

    private void createLoginComponents() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado exterior

        JLabel titleLabel = new JLabel("Iniciar Sesión");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loginPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        loginPanel.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        mailField = new JTextField(20);
        loginPanel.add(mailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        loginPanel.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mail = mailField.getText();
                char[] password = passwordField.getPassword();
                
               
                Conexion c = new Conexion();
                switch (c.loginTransport(mail,new String(password))) {
                    case 1:
                        MainFrame m =new MainFrame(c);
                    JOptionPane.showMessageDialog(LoginFrame.this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                        break;
                    case 2:
                    JOptionPane.showMessageDialog(LoginFrame.this, "Incorrect email or password", "Authentication Error", JOptionPane.ERROR_MESSAGE);

                        break;
                    case 3:
                    JOptionPane.showMessageDialog(LoginFrame.this, "Mail provider not found", "Error", JOptionPane.ERROR_MESSAGE);

                        break;
                    case 4:
                    JOptionPane.showMessageDialog(LoginFrame.this, "An error occurred while connecting to the mail server", "Connection Error", JOptionPane.ERROR_MESSAGE);

                        break;
                }
                

                // Limpia los campos después de intentar iniciar sesión
                mailField.setText("");
                passwordField.setText("");
            }

        });
        loginPanel.add(loginButton, gbc);

        add(loginPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Inicializa las capas y la interfaz

                LoginFrame loginFrame = new LoginFrame();
            }
        });
    }

}
