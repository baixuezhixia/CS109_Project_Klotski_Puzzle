package view.login;
import model.MapModel;
import view.game.GameFrame;

import javax.swing.*;
import java.awt.*;

public class LevelSelectFrame extends JFrame {
    private String currentUser;

    public LevelSelectFrame(String user) {
        this.currentUser = user;

        System.out.println("收到的用户是"+currentUser);
        this.setTitle("Select Level");
        this.setSize(400, 300);
        this.setLayout(new GridLayout(3, 1, 10, 10));

        JButton level1 = new JButton("Level 1");
        JButton level2 = new JButton("Level 2");
        JButton continueBtn = new JButton("继续游戏");

        level1.addActionListener(e -> launchGame(getLevel1Map(),false));
        level2.addActionListener(e -> launchGame(getLevel2Map(),false));
        continueBtn.addActionListener(e -> {
            int[][] dummy = {
                    {0,0,1,1},
                    {3,0,4,4},
                    {3,0,4,4},
            };
            launchGame(dummy, true);
        });

        this.add(level1);
        this.add(level2);
        if(user!=null){this.add(continueBtn);};

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private int[][] getLevel1Map() {
        return new int[][]{
                {2,2,2,2},
                {0,0,2,2},
                {2,2,2,2},
                {1,1,4,4},
                {1,1,4,4}
        };
    }

    private int[][] getLevel2Map() {
        return new int[][]{
                {5,4,4,6},
                {5,4,4,6},
                {3,2,2,7},
                {3,1,1,7},
                {1,0,0,1}
        };
    }
    private void launchGame(int[][] map, boolean tryLoad) {
        MapModel model = new MapModel(map);
        GameFrame frame = new GameFrame(600, 450, model,currentUser);
        if (tryLoad) {
            boolean success = frame.tryLoadGame(currentUser);
            if (!success) {
                JOptionPane.showMessageDialog(this, "无存档，将开启新游戏！");
                frame.restartGame();
            }
        } else {
            frame.restartGame(); // 确保从头开始
        }
        frame.setVisible(true);
        this.dispose();
    }
}
