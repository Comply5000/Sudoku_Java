import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Sudoku sudoku = new Sudoku();
        while (sudoku.running())
        {
            sudoku.update();
            sudoku.render();
        }
    }
}