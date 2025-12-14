package model;//新增类

import java.io.Serializable;

public class SaveState implements Serializable {//序列化以存储状态
    private final int[][] matrix;
    private final int steps;
    private final String username;

    public SaveState(int[][] matrix, int steps, String username) {
        this.matrix = matrix;
        this.steps = steps;
        this.username = username;
    }

    public int[][] getMatrix() { return matrix; }
    public int getSteps() { return steps; }
    public String getUsername() { return username; }
}