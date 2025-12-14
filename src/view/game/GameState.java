package view.game;

public class GameState {
    private int[][] matrix;
    private Integer selectedRow;
    private Integer selectedCol;
    private int movedBoxRow;
    private int movedBoxCol;

    public int getMovedBoxRow() {
        return movedBoxRow;
    }
    public void setMovedBoxRow(int movedBoxRow) {
        this.movedBoxRow = movedBoxRow;
    }
    public int getMovedBoxCol() {
        return movedBoxCol;
    }
    public void setMovedBoxCol(int movedBoxCol) {
        this.movedBoxCol = movedBoxCol;
    }
    public GameState(int[][] matrix, int selectedRow, int selectedCol,int movedBoxRow,int movedBoxCol) {
        // 深拷贝矩阵
        this.matrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            this.matrix[i] = matrix[i].clone();
        }
        this.selectedRow = selectedRow;
        this.selectedCol = selectedCol;
        this.movedBoxRow=movedBoxRow;
        this.movedBoxCol=movedBoxCol;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }
    public boolean hasSelectedBox() {
        return selectedRow != null && selectedCol != null;
    }
}
