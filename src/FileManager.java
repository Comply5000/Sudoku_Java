import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileManager {

    public static Integer[][] Load() throws FileNotFoundException {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("./Saves"));

        int response = fileChooser.showOpenDialog(null);

        if(response == JFileChooser.APPROVE_OPTION)
        {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            Scanner scanner = new Scanner(file);

            Integer[][] numbers = new Integer[9][9];

            for(int i = 0;i<9;i++)
            {
                String line = scanner.nextLine();
                for(int j = 0;j<9;j++)
                    numbers[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
            }

            return numbers;
        }

        return null;
    }

    public static void Save(Integer[][] numbers) throws FileNotFoundException {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("./Saves"));

        int response = fileChooser.showSaveDialog(null);

        if(response == JFileChooser.APPROVE_OPTION)
        {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

            PrintWriter writer = new PrintWriter(file);

            for(int i = 0;i<9;i++)
            {
                for(int j = 0;j<9;j++)
                {
                    writer.print(numbers[i][j]);
                }
                writer.println();
            }

            writer.close();
        }
    }
}
