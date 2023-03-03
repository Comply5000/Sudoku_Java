import java.io.FileNotFoundException;
import java.util.ArrayList;

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