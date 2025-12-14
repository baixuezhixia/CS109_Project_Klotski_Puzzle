
package view.game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
public class TriangleButton extends JButton {

    public enum Direction { UP, DOWN, LEFT, RIGHT }

    private Direction direction;
    private Shape triangleShape;

    public TriangleButton(Direction dir) {
        this.direction = dir;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        int w = getWidth();
        int h = getHeight();
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];

        switch (direction) {
            case UP:
                xPoints = new int[]{w / 2, 0, w};
                yPoints = new int[]{0, h, h};
                break;
            case DOWN:
                xPoints = new int[]{0, w / 2, w};
                yPoints = new int[]{0, h, 0};
                break;
            case LEFT:
                xPoints = new int[]{w, 0, w};
                yPoints = new int[]{0, h / 2, h};
                break;
            case RIGHT:
                xPoints = new int[]{0, w, 0};
                yPoints = new int[]{0, h / 2, h};
                break;
        }

        triangleShape = new Polygon(xPoints, yPoints, 3);

        // 填充颜色
        g2.setColor(getModel().isArmed() ? Color.GRAY : getBackground());
        g2.fill(triangleShape);

        // 画边框
        g2.setColor(Color.BLACK);
        g2.draw(triangleShape);

        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        return triangleShape != null && triangleShape.contains(x, y);
    }
}
