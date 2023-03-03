public class Main {
    public static void main(String[] args)
    {
        Sudoku sudoku = new Sudoku();
        while (sudoku.running())
        {
            sudoku.update();
            sudoku.render();
        }
    }
}