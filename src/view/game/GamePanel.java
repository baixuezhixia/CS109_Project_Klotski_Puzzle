package view.game;

import controller.GameController;
import model.Direction;
import model.MapModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * It is the subclass of ListenerPanel, so that it should implement those four methods: do move left, up, down ,right.
 * The class contains a grids, which is the corresponding GUI view of the matrix variable in MapMatrix.
 */
public class GamePanel extends ListenerPanel {
    private List<BoxComponent> boxes;
    private MapModel model;
    private GameController controller;
    private JLabel stepLabel;
    private final int GRID_SIZE = 50;
    private BoxComponent selectedBox;
    private String username;
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    public GamePanel(MapModel model,String username) {
        boxes = new ArrayList<>();
        this.username=username;
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setSize(model.getWidth() * GRID_SIZE + 4, model.getHeight() * GRID_SIZE + 4);
        this.model = model;
        this.selectedBox = null;
        initialGame();
    }

    public void initialGame() {
        //copy a map
        int[][] map = new int[model.getHeight()][model.getWidth()];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = model.getId(i, j);
            }
        }
        System.out.println("initial with matrix"+Arrays.deepToString(map));
        //build Component
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                BoxComponent box = null;
                if (map[i][j] == 1) {
                    box = new BoxComponent(1, i, j);
                    box.setSize(GRID_SIZE, GRID_SIZE);
                    map[i][j] = 0;
                } else if (map[i][j] == 2) {
                    box = new BoxComponent(2, i, j);
                    box.setLocation(
                            j * GRID_SIZE + 2,
                            i * GRID_SIZE + 2
                    );
                    map[i][j + 1] = 0; // 标记右侧格子为已占用
                } else if (map[i][j] == 3) {
                    box = new BoxComponent(3, i, j);
                    box.setLocation(
                            j * GRID_SIZE + 2,
                            i * GRID_SIZE + 2
                    );
                    map[i + 1][j] = 0; // 标记下方格子为已占用
                } else if (map[i][j] == 4) {
                    box = new BoxComponent(4, i, j);
                    box.setSize(GRID_SIZE * 2, GRID_SIZE * 2);
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                    map[i][j + 1] = 0;
                    map[i + 1][j + 1] = 0;
                }
                else if (map[i][j] == 5 || map[i][j] == 6 || map[i][j] == 7) {
                    box = new BoxComponent(map[i][j], i, j); // 使用实际类型值
                    box.setLocation(
                            j * GRID_SIZE + 2,
                            i * GRID_SIZE + 2
                    );
                    map[i + 1][j] = 0; // 标记下方格子为已占用
                }
                if (box != null) {
                    box.setLocation(j * GRID_SIZE + 2, i * GRID_SIZE + 2);
                    boxes.add(box);
                    this.add(box);
                }
            }
        }
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
        this.setBorder(border);
    }

    @Override
    public void doMouseClick(Point point) {
        Component component = this.getComponentAt(point);
        if (component instanceof BoxComponent clickedComponent) {
            if (selectedBox == null) {
                selectedBox = clickedComponent;
                selectedBox.setSelected(true);
            } else if (selectedBox != clickedComponent) {
                selectedBox.setSelected(false);
                clickedComponent.setSelected(true);
                selectedBox = clickedComponent;
            } else {
                clickedComponent.setSelected(false);
                selectedBox = null;
            }
        }
    }

    @Override
    public void doMoveRight() {
        System.out.println("Click VK_RIGHT");
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.RIGHT)) {
                afterMove();
            }
        }
    }

    @Override
    public void doMoveLeft() {
        System.out.println("Click VK_LEFT");
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.LEFT)) {
                afterMove();
            }
        }
    }

    @Override
    public void doMoveUp() {
        System.out.println("Click VK_Up");
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.UP)) {
                afterMove();
            }
        }
    }

    @Override
    public void doMoveDown() {
        System.out.println("Click VK_DOWN");
        if (selectedBox != null) {
            System.out.println("Click VK_DOWN1");
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.DOWN)) {
                System.out.println("Click VK_DOWN2");
                afterMove();
            }
        }
    }

    public void afterMove() {
        model.stepplus();
        this.stepLabel.setText(String.format("Step: %d", model.getSteps()));
        controller.checkAfterMove();//新增
    }
    public void resetMove(){
        this.stepLabel.setText(String.format("Step: %d", model.getSteps()));
    }

    public void undoMove() {
        model.stepminus();
        this.stepLabel.setText(String.format("Step: %d", model.getSteps()));
    }

    public void setStepLabel(JLabel stepLabel) {
        this.stepLabel = stepLabel;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public BoxComponent getSelectedBox() {
        return selectedBox;
    }

    public int getGRID_SIZE() {
        return GRID_SIZE;
    }

    // 新增重置方法
    public void resetGame() {
        // 清除所有现有方块
        for (BoxComponent box : boxes) {
            this.remove(box);
        }
        boxes.clear();
        selectedBox = null;
        initialGame();
        this.repaint();
    }

    public void resetGameWithSteps(int steps) {
        for (BoxComponent box : boxes) {
            this.remove(box);
        }
        boxes.clear();
        selectedBox = null;
        model.setSteps(steps); // 设置新步数
        stepLabel.setText("Step: " + steps);
        this.setSize(model.getWidth() * GRID_SIZE + 4, model.getHeight() * GRID_SIZE + 4);
        this.revalidate();
        System.out.println("resetmatrix"+Arrays.deepToString(model.getMatrix()));
        initialGame();
        this.repaint();
    }
    public BoxComponent getBoxAt(int row, int col) {
        for (Component c : this.getComponents()) {
            if (c instanceof BoxComponent) {
                BoxComponent box = (BoxComponent) c;
                if (box.getRow() == row && box.getCol() == col) {
                    return box;
                }
            }
        }
        return null;
    }

}
