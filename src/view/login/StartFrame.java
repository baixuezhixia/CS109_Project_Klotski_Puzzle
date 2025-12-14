package view.login;
//新增类
import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    private LoginFrame loginFrame; // 新增字段
    public StartFrame() {
        // 窗口基础设置
        setTitle("GAME START FRAME");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示
        // 主面板使用渐变背景
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(135, 206, 250); // 浅蓝色
                Color color2 = new Color(70, 130, 180);   // 钢蓝色
                g2d.setPaint(new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10)); // 3行1列，间距10px

        // 按钮样式统一设置
        Font buttonFont = new Font("微软雅黑", Font.BOLD, 18);
        Color buttonColor = new Color(255, 255, 255, 200); // 半透明白色

        // 开始游戏按钮
        JButton startButton = createStyledButton("START GAME", buttonFont, buttonColor);
        startButton.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame(280, 280);
            loginFrame.setVisible(true);
            this.dispose();
        });
        // 设置按钮（功能预留）
        JButton settingsButton = createStyledButton("SETTING", buttonFont, buttonColor);
        settingsButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "设置功能待实现")
        );
        // 退出按钮
        JButton exitButton = createStyledButton("EXIT GAME", buttonFont, buttonColor);
        exitButton.addActionListener(e -> System.exit(0));
        // 将按钮添加到面板
        mainPanel.add(startButton);
        mainPanel.add(settingsButton);
        mainPanel.add(exitButton);
        // 添加边距并显示
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);
    }

    // 创建带有统一样式的按钮
    private JButton createStyledButton(String text, Font font, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        return button;
    }
}
