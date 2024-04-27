package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Clase que representa la ventana de inicio de sesión.
 */
public class LoginFrame extends JFrame {

    private JPanel loginPanel;
    private JTextField mailField;
    private JPasswordField passwordField;

    /**
     * Constructor de la clase.
     */
    public LoginFrame() {
        initializeUI();
    }

    /**
     * Inicializa la interfaz de usuario.
     */
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

    /**
     * Crea los componentes de inicio de sesión y los agrega al panel.
     */
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
                if (true) {
                    MainFrame m;
                    try {
                        m = new MainFrame(mail, new String(password));
                        m.setVisible(true);

                        JOptionPane.showMessageDialog(LoginFrame.this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    } catch (MessagingException ex) {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Login ERROR", "Error", JOptionPane.ERROR_MESSAGE);
                        //Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //System.out.println(mail + " // " + new String(password));

                    dispose();

                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Incorrect email or password", "Authentication Error", JOptionPane.ERROR_MESSAGE);

                }

                // Limpia los campos después de intentar iniciar sesión
                mailField.setText("");
                passwordField.setText("");
            }
        });
        loginPanel.add(loginButton, gbc);

        add(loginPanel);
    }

    /**
     * Método principal para iniciar la aplicación.
     */
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
