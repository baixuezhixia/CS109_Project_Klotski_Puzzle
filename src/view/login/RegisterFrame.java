package view.login;

import model.UserManager;
import view.FrameUtil;
import view.game.GameFrame;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField username;
    private JTextField password;
    private JButton registerBtn;

    public RegisterFrame() {
        this.setTitle("Register");
        this.setLayout(null);
        this.setSize(300, 250);

        FrameUtil.createJLabel(this, new Point(50, 20), 70, 40, "Username:");
        FrameUtil.createJLabel(this, new Point(50, 80), 70, 40, "Password:");
        username = FrameUtil.createJTextField(this, new Point(120, 20), 120, 40);
        password = FrameUtil.createJTextField(this, new Point(120, 80), 120, 40);

        registerBtn = FrameUtil.createButton(this, "Submit", new Point(100, 140), 100, 40);

        registerBtn.addActionListener(e -> {
            String user = username.getText();
            String pass = password.getText();
            if (UserManager.register(user, pass)) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                this.dispose(); // close registration window
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists.");
            }
        });

        this.setLocationRelativeTo(null);
    }
}
