//已大改
package view.game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BoxComponent extends JComponent {
    private static final Map<Integer, Image> imageMap = new HashMap<>();
    private static final int[][] SIZE_MAP = {
            {},       // 0号空位
            {1, 1},   // 1x1
            {2, 1},   // 2x1横向（马超）
            {1, 2},   // 1x2竖向（关羽）
            {2, 2},   // 2x2
            {1, 2},   // 1x2竖向（张飞）
            {1, 2},   // 1x2竖向（黄忠）
            {1, 2}    // 1x2竖向（赵云）
    };
    private Image image;
    private int row;
    private int col;
    private boolean isSelected;

    static {
        // 初始化图片资源（路径需要根据实际项目结构调整）
        imageMap.put(1, loadImage("/resources/Xiaobing.png", 1));
        imageMap.put(2, loadImage("/resources/Machao.png", 2));
        imageMap.put(3, loadImage("/resources/Guanyu.png", 3));
        imageMap.put(4, loadImage("/resources/Caocao.png", 4));
        imageMap.put(5, loadImage("/resources/Zhangfei.png", 5));
        imageMap.put(6, loadImage("/resources/Huangzhong.png",6));
        imageMap.put(7, loadImage("/resources/Zhaoyun.png",7));
    }
    public BoxComponent(int type, int row, int col) {
        this.image = imageMap.get(type);
        this.row = row;
        this.col = col;
        isSelected = false;
        int width = SIZE_MAP[type][0] * 50;
        int height = SIZE_MAP[type][1] * 50;
        setSize(width, height);

        // 启用高质量渲染
        setOpaque(false);
        setDoubleBuffered(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

        Border border = isSelected ?
                BorderFactory.createLineBorder(Color.RED, 3) :
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
        this.setBorder(border);
    }
    private static Image loadImage(String path, int type) {
        // 使用类加载器获取资源URL
        URL imgUrl = BoxComponent.class.getClassLoader().getResource(path.substring(1));
        if (imgUrl == null) {
            throw new RuntimeException("图片资源未找到: " + path);
        }
        ImageIcon icon = new ImageIcon(imgUrl);

        int baseSize = 50; // 基础网格尺寸
        int targetWidth = baseSize * SIZE_MAP[type][0];
        int targetHeight = baseSize * SIZE_MAP[type][1];
        return icon.getImage().getScaledInstance(
                targetWidth,
                targetHeight,
                Image.SCALE_SMOOTH
        );
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
        this.repaint();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

}
