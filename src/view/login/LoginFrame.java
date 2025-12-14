package view.login;
import model.UserManager;
import view.FrameUtil;
import view.game.GameFrame;

import javax.swing.*;
import java.awt.*;
public class LoginFrame extends JFrame {
    private JTextField username;
    private JTextField password;
    private JButton submitBtn;
    private JButton resetBtn;
    private JButton registerBtn;
    private JButton guestBtn;
    private GameFrame gameFrame;
    private String currentUser;

    public LoginFrame(int width, int height) {
        this.setTitle("Login Frame");
        this.setLayout(null);
        this.setSize(width, height);
        JLabel userLabel = FrameUtil.createJLabel(this, new Point(50, 20), 70, 40, "username:");
        JLabel passLabel = FrameUtil.createJLabel(this, new Point(50, 80), 70, 40, "password:");
        username = FrameUtil.createJTextField(this, new Point(120, 20), 120, 40);
        password = FrameUtil.createJTextField(this, new Point(120, 80), 120, 40);
        registerBtn = FrameUtil.createButton(this, "Register", new Point(40, 200), 100, 40);
        submitBtn = FrameUtil.createButton(this, "Login", new Point(40, 140), 100, 40);
        resetBtn = FrameUtil.createButton(this, "Reset", new Point(160, 140), 100, 40);
        guestBtn = FrameUtil.createButton(this, "Guestmode", new Point(160, 200), 100, 40);
        submitBtn.addActionListener(e -> {
            String user = username.getText();
            String pass = password.getText();
            if (UserManager.login(user, pass)) {
                JOptionPane.showMessageDialog(this, "登录成功！");
                currentUser = user;
                LevelSelectFrame levelSelectFrame = new LevelSelectFrame(currentUser);
                levelSelectFrame.setVisible(true);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "用户名或密码错误！");
            }
        });
        registerBtn.addActionListener(e->{
            new RegisterFrame().setVisible(true);
        });
        guestBtn.addActionListener(e->{
            LevelSelectFrame levelSelectFrame = new LevelSelectFrame(null); // null表示游客
            levelSelectFrame.setVisible(true);
            this.setVisible(false);
        });
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
