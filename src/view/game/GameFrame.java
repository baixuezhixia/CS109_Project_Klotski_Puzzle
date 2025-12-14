package view.game;
import model.Direction;
import controller.GameController;
import model.MapModel;
import utils.MusicPlayer;
import view.FrameUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class GameFrame extends JFrame {

    private MusicPlayer musicPlayer;
    private GameController controller;
    private JButton restartBtn;
    private JButton saveBtn;
    private JButton loadBtn;
    private JButton undoBtn;
    private JLabel stepLabel;
    private GamePanel gamePanel;
    private TriangleButton upBtn,downBtn,leftBtn,rightBtn;
    private String currentUser;
    private Image backgroundImage;
    public GameFrame(int width, int height, MapModel mapModel,String currentUser) {
        this.setTitle("2025 CS109 Project");
        this.setLayout(null);
        this.setSize(width, height);
        this.currentUser = currentUser;
        try {
            URL imageUrl = getClass().getResource("/resources/background.png");
            if (imageUrl != null) {
                backgroundImage = new ImageIcon(imageUrl).getImage();
            }
        } catch (Exception e) {
            System.err.println("加载背景图片失败: " + e.getMessage());
        }

        gamePanel = new GamePanel(mapModel,currentUser);
        gamePanel.setLocation(30, height / 2 - gamePanel.getHeight() / 2);

        JPanel contentPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 绘制背景图片（如果存在）
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPanel.setOpaque(true);
        this.setContentPane(contentPanel);
        contentPanel.add(gamePanel);

        this.controller = new GameController(gamePanel, mapModel,this);
        controller.setGameWinListener(() -> {
            musicPlayer.stop(); // 胜利后停止音乐
        });
        this.restartBtn = FrameUtil.createButton(this, "Restart", new Point(gamePanel.getWidth() + 80, 120), 80, 50);
        this.stepLabel = FrameUtil.createJLabel(this, "Start", new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 70), 180, 50);
        this.undoBtn = FrameUtil.createButton(this, "undo", new Point(gamePanel.getWidth()+250, 250), 80, 50);
        // 原始位置偏移量（用于你布局参考）
        int baseX = gamePanel.getWidth() + 250;
        int baseY = 145;
        this.upBtn = new TriangleButton(TriangleButton.Direction.UP);
        this.upBtn.setBounds(baseX, baseY - 25, 25, 25);
        this.upBtn.setBackground(Color.YELLOW);
        this.add(upBtn);

        this.downBtn = new TriangleButton(TriangleButton.Direction.DOWN);
        this.downBtn.setBounds(baseX, baseY + 25, 25, 25);
        this.downBtn.setBackground(Color.YELLOW);
        this.add(downBtn);

        this.leftBtn = new TriangleButton(TriangleButton.Direction.LEFT);
        this.leftBtn.setBounds(baseX - 25, baseY, 25, 25);
        this.leftBtn.setBackground(Color.YELLOW);
        this.add(leftBtn);

        this.rightBtn = new TriangleButton(TriangleButton.Direction.RIGHT);
        this.rightBtn.setBounds(baseX + 25, baseY, 25, 25);
        this.rightBtn.setBackground(Color.YELLOW);
        this.add(rightBtn);
        gamePanel.setStepLabel(stepLabel);
        this.restartBtn.addActionListener(e -> {
            controller.restartGame();
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.upBtn.addActionListener(e -> {
            gamePanel.doMoveUp();
            gamePanel.requestFocusInWindow();
        });
        this.downBtn.addActionListener(e -> {
            gamePanel.doMoveDown();
            gamePanel.requestFocusInWindow();
        });
        this.leftBtn.addActionListener(e -> {
            gamePanel.doMoveLeft();
            gamePanel.requestFocusInWindow();
        });
        this.rightBtn.addActionListener(e -> {
            gamePanel.doMoveRight();
            gamePanel.requestFocusInWindow();
        });
        this.undoBtn.addActionListener(e->{
            controller.undo();
            gamePanel.requestFocusInWindow();
        });
        if (currentUser != null) {
            this.saveBtn = FrameUtil.createButton(this, "Save", new Point(gamePanel.getWidth() + 80, 260), 80, 50);
            this.loadBtn = FrameUtil.createButton(this, "Load", new Point(gamePanel.getWidth() + 80, 200), 80, 50);
            this.add(saveBtn);
            this.add(loadBtn);

            saveBtn.addActionListener(e -> {
                controller.saveGame(currentUser);
                gamePanel.requestFocusInWindow();
            });

            loadBtn.addActionListener(e -> {
                boolean success = controller.loadGame(currentUser);
                gamePanel.setLocation(30, 450 / 2 - gamePanel.getHeight() / 2);
                if (!success) {
                    JOptionPane.showMessageDialog(this, "No saved game found!");
                }
            });
            Timer autoSaveTimer = new Timer(300_000, e -> {
                    controller.saveGame(currentUser);
            });
            autoSaveTimer.start();// 每5分钟自动保存
        }
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        musicPlayer = new MusicPlayer();
        musicPlayer.playBackgroundMusic("sounds/background.wav");
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                musicPlayer.stop();
            }
        });
    }
    public boolean tryLoadGame(String username) {
        boolean a=controller.loadGame(username);
        gamePanel.setLocation(30, 450 / 2 - gamePanel.getHeight() / 2);
        return a;
    }
    public void restartGame() {
        controller.restartGame();
        gamePanel.requestFocusInWindow();
    }
    public void restartMusic() {
        musicPlayer.stop();
        musicPlayer.playBackgroundMusic("sounds/background.wav");
    }
}
