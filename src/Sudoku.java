import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Sudoku
{
    private RenderWindow window;
    private Vector2f mousePosition;
    private RectangleShape button;
    private boolean pressed = false;
    //Shapes
    private RectangleShape board;
    private RectangleShape resetButton;
    private RectangleShape solveButton;
    private RectangleShape saveButton;
    private RectangleShape loadButton;
    private RectangleShape checkBox;
    private RectangleShape[][] box;
    private RectangleShape logo;

    //Textures
    private Texture boardTexture;
    private Texture boxTextures[];
    private Color greyColor;
    private Texture resetTexture[];
    private Texture solveTexture[];
    private Texture saveTexture[];
    private Texture loadTexture[];
    private Texture checkBoxTexture[];
    private Texture logoTexture;

    //Logic
    private int[][] numbers;
    private int checkedX = -1, checkedY = -1;
    private boolean showCand = false;
    private ArrayList<ArrayList<ArrayList<RectangleShape>>> candBox;
    private ArrayList<ArrayList<ArrayList<Integer>>> candidates;

    //Checked CandBox
    private boolean checkedCand[][][];

    private Texture candTexture[];
    private Texture checkedCandTexture[];

    private Font font;
    private Text text;

    private void initWindow() // inicjowanie okna
    {
        window = new RenderWindow(new VideoMode(1000,700), "Sudoku solver");
        window.setFramerateLimit(60);
    }

    private void initNumbers()
    {
        numbers = new int[9][9];
        for (int i = 0;i<9;i++)
            for(int j = 0;j<9;j++)
                numbers[i][j] = 0;
    }

    private void updateNumbers()
    {
        for (int i = 0;i < 9;i++)
        {
            for (int j = 0;j < 9;j++)
            {
                box[i][j].setTexture(boxTextures[numbers[i][j]]);
            }
        }
    }

    private void initBoard() throws IOException //inicjowanie planszy
    {
        board = new RectangleShape();
        boardTexture = new Texture();

        resetButton = new RectangleShape();
        resetTexture = new Texture[2];
        resetTexture[0] = new Texture();
        resetTexture[1] = new Texture();

        solveButton = new RectangleShape();
        solveTexture = new Texture[2];
        solveTexture[0] = new Texture();
        solveTexture[1] = new Texture();

        saveButton = new RectangleShape();
        saveTexture = new Texture[2];
        saveTexture[0] = new Texture();
        saveTexture[1] = new Texture();

        loadButton = new RectangleShape();
        loadTexture = new Texture[2];
        loadTexture[0] = new Texture();
        loadTexture[1] = new Texture();

        checkBox = new RectangleShape();
        checkBoxTexture = new Texture[2];
        checkBoxTexture[0] = new Texture();
        checkBoxTexture[1] = new Texture();


        board.setPosition(0.f,0.f);
        board.setSize(new Vector2f(700.f, 700.f));
        boardTexture.loadFromFile(Paths.get("Textures/board.png"));
        board.setTexture(boardTexture);

        resetButton.setPosition(750.f, 300.f);
        resetButton.setSize(new Vector2f(200.f, 100.f));
        resetTexture[0].loadFromFile(Paths.get("Textures/reset.png"));
        resetButton.setTexture(resetTexture[0]);

        solveButton.setPosition(750.f, 200.f);
        solveButton.setSize(new Vector2f(200.f, 100.f));
        solveTexture[0].loadFromFile(Paths.get("Textures/solve.png"));
        solveButton.setTexture(solveTexture[0]);

        saveButton.setPosition(750.f, 400.f);
        saveButton.setSize(new Vector2f(200.f, 100.f));
        saveTexture[0].loadFromFile(Paths.get("Textures/save.png"));
        saveButton.setTexture(saveTexture[0]);

        loadButton.setPosition(750.f, 500.f);
        loadButton.setSize(new Vector2f(200.f, 100.f));
        loadTexture[0].loadFromFile(Paths.get("Textures/load.png"));
        loadButton.setTexture(loadTexture[0]);

        checkBox.setPosition(750.f, 625.f);
        checkBox.setSize(new Vector2f(50.f, 50.f));
        checkBoxTexture[0].loadFromFile(Paths.get("Textures/check2.png"));
        checkBoxTexture[1].loadFromFile(Paths.get("Textures/check.png"));
        checkBox.setTexture(checkBoxTexture[0]);

        loadTexture[1].loadFromFile(Paths.get("Textures/load-hover.png"));
        saveTexture[1].loadFromFile(Paths.get("Textures/save-hover.png"));
        solveTexture[1].loadFromFile(Paths.get("Textures/solve-hover.png"));
        resetTexture[1].loadFromFile(Paths.get("Textures/reset-hover.png"));

        font = new Font();
        text = new Text();
        font.loadFromFile(Paths.get("Fonts/arial.ttf"));
        text.setFont(font);
        text.setString("Candidates");
        text.setPosition(new Vector2f(810.f, 626.f));
        text.setCharacterSize(35);
        text.setColor(Color.WHITE);


        logo = new RectangleShape();
        logoTexture = new Texture();
        logo.setPosition(693.f, 30.f);
        logo.setSize(new Vector2f(312.f, 175.5f));
        logoTexture.loadFromFile(Paths.get("Textures/logo.png"));
        logo.setTexture(logoTexture);

    }

    private void initCandidates() throws IOException
    {
        ArrayList<ArrayList<ArrayList<Integer>>> tab1 = new ArrayList<>(9);

        for(int i = 0;i<9;i++)
        {
            tab1.add(new ArrayList<ArrayList<Integer>>(9));
            for (int j = 0; j < 9; j++)
            {
                tab1.get(i).add(new ArrayList<Integer>());
            }
        }

        candidates = tab1;

        checkedCand = new boolean[9][9][10];

        candTexture = new Texture[10];
        for(int i = 0;i<10;i++)
            candTexture[i] = new Texture();

        checkedCandTexture = new Texture[10];
        for(int i = 0;i<10;i++)
            checkedCandTexture[i] = new Texture();

        for (int i = 1;i < 10;i++)
        {
            String t = "Textures/CandText/" + i + ".png";
            candTexture[i].loadFromFile(Paths.get(t));
            t = "Textures/CheckedCand/" + i + ".png";
            checkedCandTexture[i].loadFromFile(Paths.get(t));
        }
    }

    private void drawCandidates()
    {
        for (int i = 0;i < 9;i++)
        {
            for (int j = 0;j < 9;j++)
            {
                for (int k = 0;k < candBox.get(i).get(j).size();k++)
                {
                    window.draw(candBox.get(i).get(j).get(k));
                }
            }
        }
    }

    private void drawBoard() //narysowanie planszy
    {
        window.draw(board);
        window.draw(resetButton);
        window.draw(solveButton);
        window.draw(loadButton);
        window.draw(saveButton);
        window.draw(checkBox);
        window.draw(text);
        window.draw(logo);
    }

    private void initBoxes() throws IOException //inicjowanie pól
    {
        boxTextures = new Texture[10];
        box = new RectangleShape[9][9];

        greyColor = new Color(186, 181, 181);
        for (int i = 0;i < 10;i++)
        {
            String t = "Textures/" + i + ".png";
            boxTextures[i] = new Texture();
            boxTextures[i].loadFromFile(Paths.get(t));
        }

        int y = 14;
        for (int i = 0;i < 9;i++)
        {
            int x = 14;
            for (int j = 0;j < 9;j++)
            {
                box[i][j] = new RectangleShape();
                box[i][j].setFillColor(Color.WHITE);
                box[i][j].setSize(new Vector2f(70.f, 70.f));
                box[i][j].setPosition(x, y);
                x += 74;
                if (j == 2 || j == 5)
                    x += 5;
            }
            y += 74;
            if (i == 2 || i == 5)
                y += 5;
        }
    }

    private void drawBoxes()
    {
        for(int i = 0;i < 9;i++)
            for (int j = 0;j < 9;j++)
                window.draw(box[i][j]);
    }

    public Sudoku()
    {
        initWindow();
        initNumbers();
        try
        {
            initBoard();
            initCandidates();
            initBoxes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean running()
    {
        return window.isOpen();
    }

    private void poolEvent()
    {
        for(Event ev : window.pollEvents())
        {
            switch (ev.type)
            {
                case CLOSED:
                window.close();
                    break;
                case KEY_PRESSED:
                if (ev.asKeyEvent().key == Keyboard.Key.ESCAPE)
                    window.close();
                    break;
                case MOUSE_BUTTON_RELEASED:
                    if(!Mouse.isButtonPressed(Mouse.Button.LEFT))
                        pressed = false;
                    break;
                case KEY_RELEASED:
                    if (!Keyboard.isKeyPressed(Keyboard.Key.TAB) || !Keyboard.isKeyPressed(Keyboard.Key.UP) ||
                            !Keyboard.isKeyPressed(Keyboard.Key.DOWN) || !Keyboard.isKeyPressed(Keyboard.Key.LEFT) ||
                            !Keyboard.isKeyPressed(Keyboard.Key.RIGHT))
                    pressed = false;
                    break;

            }
        }
    }

    private void menu() throws FileNotFoundException //wywoływanie klas i funkcji po naciśnięciu przycisków
    {
        if (solveButton.getGlobalBounds().contains(mousePosition) && Mouse.isButtonPressed(Mouse.Button.LEFT) && !pressed)
        {
            pressed = true;

            Solver solver = new Solver(numbers);
            numbers = solver.returnSolution();
            updateNumbers();
        }

        else if (resetButton.getGlobalBounds().contains(mousePosition) && Mouse.isButtonPressed(Mouse.Button.LEFT))
        {
            for (int i = 0;i < 9;i++)
            {
                for (int j = 0;j < 9;j++)
                {
                    box[i][j].setTexture(boxTextures[0]);
                    numbers[i][j] = 0;
                    for (int k = 1;k < 10;k++)
                        checkedCand[i][j][k] = false;
                }
            }
        }

        else if (loadButton.getGlobalBounds().contains(mousePosition) && Mouse.isButtonPressed(Mouse.Button.LEFT))
        {
            for (int i = 0;i < 9;i++)
                for (int j = 0;j < 9;j++)
                    for (int k = 1;k < 10;k++)
                        checkedCand[i][j][k] = false;

            numbers = FileManager.Load(numbers);
            updateNumbers();
        }

        else if (saveButton.getGlobalBounds().contains(mousePosition) && Mouse.isButtonPressed(Mouse.Button.LEFT))
        {
            FileManager.Save(numbers);
        }

        else if (checkBox.getGlobalBounds().contains(mousePosition) && Mouse.isButtonPressed(Mouse.Button.LEFT) && !pressed)
        {
            pressed = true;
            if (!showCand)
            {
                showCand = true;
                checkBox.setTexture(checkBoxTexture[1]);
            }
            else
            {
                showCand = false;
                checkBox.setTexture(checkBoxTexture[0]);
            }
        }
    }

    private void updateButtons() //zmiana koloru przycisków przy najechaniu kursorem na nie
    {
        if (resetButton.getGlobalBounds().contains(mousePosition))
            resetButton.setTexture(resetTexture[1]);
        else
            resetButton.setTexture(resetTexture[0]);

        if (solveButton.getGlobalBounds().contains(mousePosition))
            solveButton.setTexture(solveTexture[1]);
        else
            solveButton.setTexture(solveTexture[0]);

        if (loadButton.getGlobalBounds().contains(mousePosition))
            loadButton.setTexture(loadTexture[1]);
        else
            loadButton.setTexture(loadTexture[0]);

        if (saveButton.getGlobalBounds().contains(mousePosition))
            saveButton.setTexture(saveTexture[1]);
        else
            saveButton.setTexture(saveTexture[0]);
    }

    private void updateBoxes()
    {
        //zaznaczenie pola w które bêdziemy wpisywaæ cyfrê
        for (int i = 0;i < 9;i++)
        {
            for (int j = 0;j < 9;j++)
            {
                if (box[i][j].getGlobalBounds().contains(mousePosition) && Mouse.isButtonPressed(Mouse.Button.LEFT) && !pressed)
                {
                    //sprawdza czy zosta³ kliknêty kandydat a nie pole
                    boolean candClick = false;
                    for (int k = 0; k < candBox.get(i).get(j).size(); k++)
                        if (candBox.get(i).get(j).get(k).getGlobalBounds().contains(mousePosition))
                        {
                            candClick = true;
                            break;
                        }

                    //jesli nie zosta³ klikniêty kandydat lub pokazywanie kandydatów jest wy³¹czone
                    if (!candClick || !showCand)
                    {
                        pressed = true;
                        if (checkedX == j && checkedY == i)
                        {
                            box[i][j].setFillColor(Color.WHITE);
                            checkedX = -1;
                            checkedY = -1;
                        }
                        else if (checkedX != -1 && checkedY != -1)
                        {
                            box[checkedY][checkedX].setFillColor(Color.WHITE);
                            box[i][j].setFillColor(greyColor);
                            checkedX = j;
                            checkedY = i;
                        }
                        else
                        {
                            box[i][j].setFillColor(greyColor);
                            checkedX = j;
                            checkedY = i;
                        }
                    }
                }
            }
        }

        if ((Keyboard.isKeyPressed(Keyboard.Key.TAB)) && !pressed)
        {
            pressed = true;
            if (checkedX == -1 && checkedY == -1)
            {
                box[0][0].setFillColor(greyColor);
                checkedX = 0;
                checkedY = 0;
            }
            else if (checkedX == 8 && checkedY != 8)
            {
                box[checkedY][checkedX].setFillColor(Color.WHITE);
                checkedX = 0;
                checkedY++;
                box[checkedY][checkedX].setFillColor(greyColor);
            }
            else if (checkedY == 8 && checkedX == 8)
            {
                box[checkedY][checkedX].setFillColor(Color.WHITE);
                checkedX = 0;
                checkedY = 0;
                box[checkedY][checkedX].setFillColor(greyColor);
            }
            else
            {
                box[checkedY][checkedX].setFillColor(Color.WHITE);
                checkedX++;
                box[checkedY][checkedX].setFillColor(greyColor);
            }
        }
        else if ((Keyboard.isKeyPressed(Keyboard.Key.UP)) && !pressed)
        {
            pressed = true;
            if (checkedX == -1 && checkedY == -1)
            {
                box[0][0].setFillColor(greyColor);
                checkedX = 0;
                checkedY = 0;
            }
            else
            {
                box[checkedY][checkedX].setFillColor(Color.WHITE);
                checkedY--;
                if (checkedY < 0)
                    checkedY = 8;
                box[checkedY][checkedX].setFillColor(greyColor);
            }
        }
        else if ((Keyboard.isKeyPressed(Keyboard.Key.DOWN)) && !pressed)
        {
            if (checkedX == -1 && checkedY == -1)
            {
                box[0][0].setFillColor(greyColor);
                checkedX = 0;
                checkedY = 0;
            }
            else
            {
                pressed = true;
                box[checkedY][checkedX].setFillColor(Color.WHITE);
                checkedY++;
                if (checkedY > 8)
                    checkedY = 0;
                box[checkedY][checkedX].setFillColor(greyColor);
            }
        }
        else if ((Keyboard.isKeyPressed(Keyboard.Key.LEFT)) && !pressed)
        {
            if (checkedX == -1 && checkedY == -1)
            {
                box[0][0].setFillColor(greyColor);
                checkedX = 0;
                checkedY = 0;
            }
            else
            {
                pressed = true;
                box[checkedY][checkedX].setFillColor(Color.WHITE);
                checkedX--;
                if (checkedX < 0)
                    checkedX = 8;
                box[checkedY][checkedX].setFillColor(greyColor);
            }
        }
        else if ((Keyboard.isKeyPressed(Keyboard.Key.RIGHT)) && !pressed)
        {
            if (checkedX == -1 && checkedY == -1)
            {
                box[0][0].setFillColor(greyColor);
                checkedX = 0;
                checkedY = 0;
            }
            else
            {
                pressed = true;
                box[checkedY][checkedX].setFillColor(Color.WHITE);
                checkedX++;
                if (checkedX > 8)
                    checkedX = 0;
                box[checkedY][checkedX].setFillColor(greyColor);
            }
        }


        //wypisanie cyfry z klawiatury
        if (checkedX != -1 && checkedY != -1)
        {
            if (Keyboard.isKeyPressed(Keyboard.Key.NUM0))
            {
                box[checkedY][checkedX].setTexture(boxTextures[0]);
                numbers[checkedY][checkedX] = 0;
            }
            else if (Keyboard.isKeyPressed(Keyboard.Key.NUM1))
            {
                box[checkedY][checkedX].setTexture(boxTextures[1]);
                numbers[checkedY][checkedX] = 1;
            }
            else if (Keyboard.isKeyPressed(Keyboard.Key.NUM2))
            {
                box[checkedY][checkedX].setTexture(boxTextures[2]);
                numbers[checkedY][checkedX] = 2;
            }
            else if (Keyboard.isKeyPressed(Keyboard.Key.NUM3))
            {
                box[checkedY][checkedX].setTexture(boxTextures[3]);
                numbers[checkedY][checkedX] = 3;
            }
            else if (Keyboard.isKeyPressed(Keyboard.Key.NUM4))
            {
                box[checkedY][checkedX].setTexture(boxTextures[4]);
                numbers[checkedY][checkedX] = 4;
            }
            else if (Keyboard.isKeyPressed(Keyboard.Key.NUM5))
            {
                box[checkedY][checkedX].setTexture(boxTextures[5]);
                numbers[checkedY][checkedX] = 5;
            }
            else if (Keyboard.isKeyPressed(Keyboard.Key.NUM6))
            {
                box[checkedY][checkedX].setTexture(boxTextures[6]);
                numbers[checkedY][checkedX] = 6;
            }
            else if (Keyboard.isKeyPressed(Keyboard.Key.NUM7))
            {
                box[checkedY][checkedX].setTexture(boxTextures[7]);
                numbers[checkedY][checkedX] = 7;
            }
            else if (Keyboard.isKeyPressed(Keyboard.Key.NUM8))
            {
                box[checkedY][checkedX].setTexture(boxTextures[8]);
                numbers[checkedY][checkedX] = 8;
            }
            else if (Keyboard.isKeyPressed(Keyboard.Key.NUM9))
            {
                box[checkedY][checkedX].setTexture(boxTextures[9]);
                numbers[checkedY][checkedX] = 9;
            }
        }
    }

    private void updateMousePosition() //aktualizacja pozycji myszy wzglêcem okna
    {
        mousePosition = window.mapPixelToCoords(Mouse.getPosition(window));
    }

    void updateCandidates()
    {
        //aktualizowanie pozycji kandydatów
        var solver = new Solver(numbers);
        candidates = solver.returnCandidates();

        ArrayList<ArrayList<ArrayList<RectangleShape>>> tab = new ArrayList<>(9);

        for(int i = 0;i<9;i++)
        {
            tab.add(new ArrayList<ArrayList<RectangleShape>>(9));
            for (int j = 0; j < 9; j++)
            {
                tab.get(i).add(new ArrayList<RectangleShape>());
            }
        }

        for (int i = 0;i < 9;i++)
        {
            for (int j = 0;j < 9;j++)
            {
                if (numbers[i][j] == 0)
                {
                    float x = 0;
                    float y = 0;
                    var chuj = tab.get(i).get(j).size();
                    for (int k = 0;k < candidates.get(i).get(j).size();k++)
                    {
                        var a = new RectangleShape();
                        a.setPosition(box[i][j].getPosition().x + x, box[i][j].getPosition().y + y);
                        a.setSize(new Vector2f(17.5f, 17.5f));
                        a.setTexture(candTexture[candidates.get(i).get(j).get(k)]);
                        tab.get(i).get(j).add(a);

                        if (x == 52.5)
                        {
                            x = 0;
                            y += 17.5;
                        }
                        else
                            x += 17.5;

                    }
                }
            }
        }
        candBox = tab;

        //aktualizowanie zaznaczenia kandydatów
        for (int i = 0;i < 9;i++)
        {
            for (int j = 0;j < 9;j++)
            {
                for (int k = 0;k < candBox.get(i).get(j).size();k++)
                {
                    if (candBox.get(i).get(j).get(k).getGlobalBounds().contains(mousePosition) && Mouse.isButtonPressed(Mouse.Button.LEFT) && !pressed)
                    {
                        pressed = true;
                        if(checkedCand[i][j][candidates.get(i).get(j).get(k)] == true)
                            checkedCand[i][j][candidates.get(i).get(j).get(k)] = false;
                        else
                            checkedCand[i][j][candidates.get(i).get(j).get(k)] = true;
                    }
                }
            }
        }
    }

    private void updateCandColor()
    {
        for (int i = 0;i < 9;i++)
        {
            for (int j = 0;j < 9;j++)
            {
                for (int k = 0;k < candBox.get(i).get(j).size();k++)
                {
                    if (checkedCand[i][j][candidates.get(i).get(j).get(k)] == true)
                        candBox.get(i).get(j).get(k).setTexture(checkedCandTexture[candidates.get(i).get(j).get(k)]);
                    else
                        candBox.get(i).get(j).get(k).setTexture(candTexture[candidates.get(i).get(j).get(k)]);

                    if (i == checkedY && j == checkedX)
                        candBox.get(i).get(j).get(k).setFillColor(greyColor);
                    else
                        candBox.get(i).get(j).get(k).setFillColor(Color.WHITE);
                }
            }
        }
    }

    public void update() throws FileNotFoundException
    {
        running();
        poolEvent();
        updateMousePosition();
        updateBoxes();
        updateButtons();
        updateNumbers();
        updateCandidates();
        updateCandColor();
        menu();
    }

    public void render()
    {
        window.clear();
        drawBoard();
        drawBoxes();
        if(showCand)
            drawCandidates();
        window.display();
    }
}
