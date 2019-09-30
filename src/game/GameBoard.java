package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameBoard extends JFrame {
    static int dimension = 3;           //размерность
    static int cellSize = 150;          //размерность одной клетки
    private char[][] gameField;         //матрица игры
    private GameButton[] gameButtons;   //массив кнопок

    private Game game;                  //ссылка на игру

    static  char nullSymbol = '\u0000'; //null символ

    public GameBoard(Game currentGame){
        this.game = currentGame;
        initField();
    }

    //Метод инициализации и отрисовки игрового поля
    private void initField(){
        //основные настройки игры
        setBounds(cellSize*dimension, cellSize*dimension, 400, 300);
        setTitle("Крестики-нолики");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(); //панель управления игрой
        JButton newGameButton = new JButton("Новая игра");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyField(); //очистка поля
            }
        });

        //Стандартные настройки
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(newGameButton);
        controlPanel.setSize(cellSize*dimension, 150);

        JPanel gameFieldPanel = new JPanel(); //панель самой игры
        gameFieldPanel.setLayout(new GridLayout(dimension,dimension));
        gameFieldPanel.setSize(cellSize*dimension, cellSize*dimension);

        gameField = new char[dimension][dimension];
        gameButtons = new GameButton[dimension*dimension];

        //Инициализируем игровое поле - заполняем кнопками
        for(int i=0; i<(dimension*dimension); i++){
            GameButton fieldButton = new GameButton(i, this);
            gameFieldPanel.add(fieldButton);
            gameButtons[i] = fieldButton;
        }

        //Добавление панелей
        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(gameFieldPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    void emptyField(){
        for(int i=0; i<(dimension*dimension); i++){
            gameButtons[i].setText("");             //очистка кнопки
            //очистка символа
            int x=i/GameBoard.dimension;
            int y=i%GameBoard.dimension;
            gameField[x][y] = nullSymbol;
        }
    }

    Game getGame(){
        return game;
    }

    //Метод проверки доступности клетки для хода
    boolean isTurnable(int x, int y){
        boolean result = false;

        if(gameField[y][x] == nullSymbol)
            result = true;
        return result;
    }

    //Обновление матрицы игры после хода
    void updateGameField (int x, int y){
        gameField[y][x] = game.getCurrentPlayer().getPlayerSign();
    }

    //Проверка попеды по столбцам и линиям
    boolean checkWin(){
        boolean result = false;
        char playerSymbol = getGame().getCurrentPlayer().getPlayerSign();
        if(checkWinDiagonals(playerSymbol) || checkWinLines(playerSymbol)){
            result = true;
        }
        return result;
    }
    //Метод проверки заполненности поля
    boolean isFull(){
        boolean result = true;
        for(int i=0; i<dimension; i++){
            for(int j=0; j<dimension; j++){
                if(gameField[i][j] == nullSymbol) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    //Проверка победы по вертикалям и горизонталям
    private boolean checkWinLines(char playerSymbol){
        boolean cols, rows, result;
        result = false;
        for(int col=0; col<dimension; col++){
            cols = true;
            rows = true;
            for(int row=0; row<dimension; row++){
                cols &= (gameField[col][row] == playerSymbol);
                rows &= (gameField[row][col] == playerSymbol);
            }
            //Условие остановки проверки, после верного столбца/строки
            if (cols||rows){
                result = true;
                break;
            }
            if(result){
                break;
            }
        }
        return result;
    }




    //Проверка победы по диагоналям
    private boolean checkWinDiagonals(char playerSymbol){
        boolean diagonal1, diagonal2, result;
        result = false;
        diagonal1 = true;
        diagonal2 = true;
        for(int k=0; k<dimension; k++) {
                diagonal1 &= (gameField[k][k] == playerSymbol);
                diagonal2 &= (gameField[k][dimension-1-k] == playerSymbol);
        }
        if (diagonal1||diagonal2){
                result = true;
        }
        return result;
    }

    //Рейтинг в зависимости от наличия ячейки с таким же символом
    int checkSymbol(char playerSymbol, int col, int row){
        int xreit=0;
            if ((col!=0)&&(row!=0)&&(gameField[col-1][row-1]==playerSymbol)) xreit ++;
            if ((col!=0)&&(gameField[col-1][row]==playerSymbol)) xreit ++;
            if ((col!=0)&&(row!=dimension-1)&&(gameField[col-1][row+1]==playerSymbol)) xreit ++;
            if ((row!=0)&&(gameField[col][row-1]==playerSymbol)) xreit ++;
            if ((row!=dimension-1)&&(gameField[col][row+1]==playerSymbol)) xreit ++;
            if ((col!=dimension-1)&&(row!=0)&&(gameField[col+1][row-1]==playerSymbol)) xreit ++;
            if ((col!=dimension-1)&&(gameField[col+1][row]==playerSymbol)) xreit ++;
            if ((col!=dimension-1)&&(row!=dimension-1)&&(gameField[col+1][row+1]==playerSymbol)) xreit ++;
        return xreit;
    }



    public GameButton getButton(int buttonIndex){
        return gameButtons[buttonIndex];
    }
}
