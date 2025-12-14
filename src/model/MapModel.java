package model;
public class MapModel {
    int[][] matrix;
    int steps;
    int initialsteps;
    public void setInitialsteps(int initialsteps) {
        this.initialsteps = initialsteps;
    }
    private int[][] initialMatrix;
    public void setMatrix(int[][] matrix) {
        this.matrix=deepCopy(matrix);
    }
    public int getSteps() {
        return steps;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }
    public void setOriginal(int[][] newMap) {
        this.initialMatrix = deepCopy(newMap);  // 深拷贝，避免外部引用干扰
    }
    public void reset() {
        this.matrix = deepCopy(initialMatrix);
        this.steps = initialsteps;
    }

    public MapModel(int[][] matrix) {
        this.initialMatrix = deepCopy(matrix);
        this.matrix = deepCopy(matrix);
        this.steps=0;
        this.initialsteps=0;
    }
    public void stepplus(){
        this.steps++;
    }
    public void stepminus(){
        this.steps--;
    }
    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }

    public int getId(int row, int col) {
        return matrix[row][col];
    }
    public int[][] getMatrix() {
        return matrix;
    }
    public boolean checkInWidthSize(int col) {
        return col >= 0 && col < matrix[0].length;
    }
    public boolean checkInHeightSize(int row) {
        return row >= 0 && row < matrix.length;
    }
    public int[][] copyMatrix() {
        int[][] copy = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i].clone();
        }
        return copy;
    }
    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
}
