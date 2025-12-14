package controller;

import model.Direction;
import model.MapModel;
import model.SaveState;
import model.UserManager;
import view.game.BoxComponent;
import view.game.GameFrame;
import view.game.GamePanel;
import view.game.GameState;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Stack;


public class GameController {
    private final GamePanel view;
    private final MapModel model;
    private final GameFrame frame;
    private Stack<GameState> history = new Stack<>();

    public GameController(GamePanel view, MapModel model,GameFrame frame) {
        this.view = view;
        this.model = model;
        this.frame=frame;
        view.setController(this);
    }


    public void restartGame() {
        model.reset();        // 重置模型
        view.resetGame();
        view.resetMove();
        System.out.println("Game restarted!");
        frame.restartMusic();
        history.clear();
    }


    public boolean doMove(int row, int col, Direction direction) {
        int type = model.getId(row, col);
        int nextRow = row + direction.getRow();
        int nextCol = col + direction.getCol();
        System.out.printf("Trying to move type %d from (%d,%d) to (%d,%d)\n", type, row, col, nextRow, nextCol);
        System.out.println(Arrays.deepToString(model.getMatrix()));
        switch (type) {
            case 1:
                // 1x1 方块
                if (model.checkInHeightSize(nextRow) && model.checkInWidthSize(nextCol)) {
                    if (model.getId(nextRow, nextCol) == 0) {
                        saveCurrentState(nextRow,nextCol);
                        model.getMatrix()[row][col] = 0;
                        model.getMatrix()[nextRow][nextCol] = 1;
                        BoxComponent box = view.getSelectedBox();
                        box.setRow(nextRow);
                        box.setCol(nextCol);
                        box.setLocation(nextCol * view.getGRID_SIZE() + 2, nextRow * view.getGRID_SIZE() + 2);
                        box.repaint();
                        return true;
                    }
                }
                break;
            case 2:
                // 2x1 横向长方块
                // 先判断 nextRow, nextCol, nextCol+1 是否在边界
                if (model.checkInHeightSize(nextRow) && model.checkInWidthSize(nextCol) && model.checkInWidthSize(nextCol + 1)) {
                    boolean canMove = false;
                    if (direction == Direction.LEFT) {
                        canMove = model.getId(nextRow, nextCol) == 0;
                    } else if (direction == Direction.RIGHT) {
                        canMove = model.getId(nextRow, nextCol + 1) == 0;
                    } else if (direction == Direction.UP || direction == Direction.DOWN) {
                        // 向上或向下时，需要竖着的两个格子都空
                        canMove = model.getId(nextRow, nextCol) == 0 && model.getId(nextRow, nextCol + 1) == 0;
                    }

                    if (canMove) {

                        saveCurrentState(nextRow,nextCol);
                        // 清空旧位置
                        model.getMatrix()[row][col] = 0;
                        model.getMatrix()[row][col + 1] = 0;

                        // 设置新位置
                        model.getMatrix()[nextRow][nextCol] = 2;
                        model.getMatrix()[nextRow][nextCol + 1] = 2;

                        // 更新视图
                        BoxComponent box = view.getSelectedBox();
                        box.setRow(nextRow);
                        box.setCol(nextCol);
                        box.setLocation(nextCol * view.getGRID_SIZE() + 2, nextRow * view.getGRID_SIZE() + 2);
                        box.repaint();
                        return true;
                    }
                }
                break;
            case 3:  // 关羽
            case 5:  // 张飞
            case 6:  // 黄忠
            case 7:  // 赵云
                // 1x2 竖向长方块
                // 先判断 nextRow, nextRow+1, nextCol 是否在边界
                if (model.checkInHeightSize(nextRow) && model.checkInHeightSize(nextRow + 1) && model.checkInWidthSize(nextCol)) {
                    boolean canMove = false;

                    if (direction == Direction.UP) {
                        // 向上：检查目标顶部格子
                        canMove = model.getId(nextRow, nextCol) == 0;
                    } else if (direction == Direction.DOWN) {
                        // 向下：检查一个目标格子
                        canMove = model.getId(nextRow + 1, nextCol) == 0;
                    } else if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                        // 左右侧移时：上下两格都要为空
                        canMove = model.getId(nextRow, nextCol) == 0 && model.getId(nextRow + 1, nextCol) == 0;
                    }
                    if (canMove) {
                        saveCurrentState(nextRow,nextCol);
                        // 清空旧位置
                        model.getMatrix()[row][col] = 0;
                        model.getMatrix()[row + 1][col] = 0;
                        // 设置新位置
                        model.getMatrix()[nextRow][nextCol] = type;
                        model.getMatrix()[nextRow + 1][nextCol] = type;
                        // 更新视图
                        BoxComponent box = view.getSelectedBox();
                        box.setRow(nextRow);
                        box.setCol(nextCol);
                        box.setLocation(nextCol * view.getGRID_SIZE() + 2, nextRow * view.getGRID_SIZE() + 2);
                        box.repaint();
                        return true;
                    }
                }
                break;
            case 4:
                // 2x2 大方块
                // 判断 nextRow, nextRow+1 和 nextCol, nextCol+1 都在边界内且为空
                if (model.checkInHeightSize(nextRow) && model.checkInHeightSize(nextRow + 1) &&
                        model.checkInWidthSize(nextCol) && model.checkInWidthSize(nextCol + 1)) {

                    boolean canMove = false;

                    if (direction == Direction.UP) {
                        // 向上：检查 (nextRow,nextCol) 和 (nextRow,nextCol+1)
                        canMove = model.getId(nextRow, nextCol) == 0 &&
                                model.getId(nextRow, nextCol + 1) == 0;
                    } else if (direction == Direction.DOWN) {
                        // 向下：检查 (nextRow+1,nextCol) 和 (nextRow+1,nextCol+1)
                        canMove = model.getId(nextRow + 1, nextCol) == 0 &&
                                model.getId(nextRow + 1, nextCol + 1) == 0;
                    } else if (direction == Direction.LEFT) {
                        // 向左：检查 (nextRow,nextCol) 和 (nextRow+1,nextCol)
                        canMove = model.getId(nextRow, nextCol) == 0 &&
                                model.getId(nextRow + 1, nextCol) == 0;
                    } else if (direction == Direction.RIGHT) {
                        // 向右：检查 (nextRow,nextCol+1) 和 (nextRow+1,nextCol+1)
                        canMove = model.getId(nextRow, nextCol + 1) == 0 &&
                                model.getId(nextRow + 1, nextCol + 1) == 0;
                    }

                    if (canMove) {
                        saveCurrentState(nextRow,nextCol);
                        // 清空旧位置
                        model.getMatrix()[row][col] = 0;
                        model.getMatrix()[row][col + 1] = 0;
                        model.getMatrix()[row + 1][col] = 0;
                        model.getMatrix()[row + 1][col + 1] = 0;
                        // 设置新位置
                        model.getMatrix()[nextRow][nextCol] = 4;
                        model.getMatrix()[nextRow][nextCol + 1] = 4;
                        model.getMatrix()[nextRow + 1][nextCol] = 4;
                        model.getMatrix()[nextRow + 1][nextCol + 1] = 4;
                        // 更新视图
                        BoxComponent box = view.getSelectedBox();
                        box.setRow(nextRow);
                        box.setCol(nextCol);
                        box.setLocation(nextCol * view.getGRID_SIZE() + 2, nextRow * view.getGRID_SIZE() + 2);
                        box.repaint();
                        return true;
                    }
                }
                break;
        }
        System.out.println("Move failed");
        return false;
    }

    public void undo() {
        if (!history.isEmpty()) {
            System.out.println("trying to undo");
            GameState prev = history.pop();
            model.setMatrix(prev.getMatrix()); // 恢复矩阵
            BoxComponent box = view.getBoxAt(prev.getMovedBoxRow(), prev.getMovedBoxCol());
            box.setRow(prev.getSelectedRow());
            box.setCol(prev.getSelectedCol());
            box.setLocation(prev.getSelectedCol() * view.getGRID_SIZE() + 2,
                    prev.getSelectedRow() * view.getGRID_SIZE() + 2);
            view.undoMove();
            view.repaint();
            // 刷新界面
        }else{
                System.out.println("Undo history is empty!");
        }
    }

    // 新增胜利检测方法
    public void checkAfterMove() {
        if (checkWinCondition()) {
            showWinDialog(model.getSteps());
        }
    }

    // GameController.java 新增方法
    public boolean checkWinCondition() {
        int[][] matrix = model.getMatrix();
        // 检查目标区域（第3行第1和2列）是否被4号方块覆盖
        return matrix[matrix.length-1][1] == 4 && matrix[matrix.length-1][2] == 4;
    }
    //新增
    private void showWinDialog(int steps) {
        if (gameWinListener != null) {
            gameWinListener.onGameWin(); // 通知 GameFrame 停止音乐
        }
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(view), "VICTORY", false);
        dialog.setSize(350, 220);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(),BoxLayout.Y_AXIS));

        JLabel label = new JLabel("<html><center>YOU WON<br>STEPS：" + steps + "</center></html>");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton restart = new JButton("NEW GAME");
        JButton backToMenu = new JButton("BACK TO MENU");
        JButton exit = new JButton("EXIT");

        restart.addActionListener(e -> {
            restartGame();
            dialog.dispose();
        });
        exit.addActionListener(e -> System.exit(0));
        backToMenu.addActionListener(e -> {
            // 获取当前窗口并关闭
            Window topWindow = SwingUtilities.getWindowAncestor(view);
            if (topWindow != null) {
                topWindow.dispose();
            }
            // 回到关卡选择
            String username = view.getUsername();
            new view.login.LevelSelectFrame(username).setVisible(true);
            dialog.dispose();
        });
        btnPanel.add(restart);
        btnPanel.add(exit);
        btnPanel.add(backToMenu);

        dialog.add(label, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(view);
        dialog.setVisible(true);
    }
//新增saveGame方法
    public void saveGame(String username) {
        try (ObjectOutputStream oos = new ObjectOutputStream(//ObjectOutputStream：将对象转化为字节流，写入文件。
                new FileOutputStream(UserManager.getSavePath(username)))
        ) {//try：自动管理文件，结束后自动关闭，防泄漏
            int[][] matrix = model.copyMatrix();
            int steps = model.getSteps();
            System.out.println(Arrays.deepToString(matrix));
            oos.writeObject(new SaveState(matrix, steps, username));//封装当前游戏状态
            JOptionPane.showMessageDialog(null, "Saved successfully!");
        } catch (IOException e) {//获取可能的异常，弹出对话框
            JOptionPane.showMessageDialog(null, "Save failed:" + e.getMessage());
        }
    }

    public boolean loadGame(String username) {
        File saveFile = new File(UserManager.getSavePath(username));
        if (!saveFile.exists()) return false;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(saveFile))
        ) {
            SaveState state = (SaveState) ois.readObject();
            // 验证用户匹配
            if (!state.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(null, "存档用户不匹配！");
                return false;
            }
            System.out.println("loadgame to"+Arrays.deepToString(state.getMatrix()));
            System.out.println("Saved matrix rows: " + state.getMatrix().length + ", cols: " + state.getMatrix()[0].length);
            model.setMatrix(state.getMatrix());
            view.resetGameWithSteps(state.getSteps());
            model.setOriginal(state.getMatrix());
            model.setInitialsteps(state.getSteps());
            JOptionPane.showMessageDialog(null, "Load successfully");
            view.requestFocusInWindow();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "File is broken, failed to load");
            return false;
        }
    }
    private void saveCurrentState(int a,int b) {
        int[][] snapshot = model.copyMatrix();
        BoxComponent selectedBox = view.getSelectedBox();
        Integer selectedRow = selectedBox != null ? selectedBox.getRow() : null;
        Integer selectedCol = selectedBox != null ? selectedBox.getCol() : null;
        history.push(new GameState(snapshot, selectedRow, selectedCol,a,b));
    }
    public interface GameWinListener {
        void onGameWin();
    }

    private GameWinListener gameWinListener;

    public void setGameWinListener(GameWinListener listener) {
        this.gameWinListener = listener;
    }
}
