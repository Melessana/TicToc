package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameActionListener implements ActionListener {
    private int row;
    private int cell;
    private GameButton button;

    public GameActionListener(int row, int cell, GameButton gButton){
        this.row = row;
        this.cell = cell;
        this.button = gButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GameBoard board = button.getBoard();

        //Проверка возможности хода в выбранной кнопке
        if(board.isTurnable(row, cell)){
            //Логика простановки символа игрока
            updateByPlayersData(board);
            //Проверка заполненности поля
            if(board.isFull()){
                board.getGame().showMessage("Ничья!");
                board.emptyField();
            }
            else{
                updateByAiData(board);
                if(board.isFull()){
                    board.getGame().showMessage("Ничья!");
                    board.emptyField();
                }
            }
        }
        else{
            board.getGame().showMessage("Некорректный ход!");
        }
    }

    //Ход человека
    private void updateByPlayersData(GameBoard board){
        //Обновление матрицы игры
        board.updateGameField(row, cell);
        //Обновление содержимого кнопки
        button.setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));
        //Проверка состояния победы
        if(board.checkWin()){
            button.getBoard().getGame().showMessage("Вы выиграли!");
            board.emptyField();
        }
        else{
            board.getGame().passTurn();
        }
    }

    private void updateByAiData(GameBoard board){
        //генерация координат хода компьютера
        int reit; //Рейтинг ячейки
        int x = -1;
        int y = -1;
        int maxReit = -1;
        int SIZE = GameBoard.dimension;

//        Random rnd = new Random();
//
//        do {
//            x = rnd.nextInt(GameBoard.dimension);
//            y = rnd.nextInt(GameBoard.dimension);
//        }
//        while(!board.isTurnable(x,y));


        for(int i=0; i<SIZE; i++){
            for(int j=0; j<SIZE; j++){
                if (board.isTurnable(j,i)) {
                    reit = 0;
                    //Присваивание рейтинга ячейки, в зависимости от её местоположения
                    if ((i==j) || (i+j==SIZE-1)){
                        reit = reit + 1;
                        if ((i==SIZE/2)&&(j==SIZE/2)){
                            reit = reit + 1;
                        }
                    }
                    else {
                        reit = 1;
                    }
                    //Увеличение рейтинга, в зависимости от наличия рядом символов
                    reit = reit + board.checkSymbol(board.getGame().getCurrentPlayer().getPlayerSign(),i,j);

                    //Проверка максимального рейтинга, с последущим запоминанием координат
                    if (reit>maxReit) {
                        x = j;
                        y = i;
                        maxReit=reit;
                    }
                }
            }
        }

        //Обновление матрицы игры
        board.updateGameField(x, y);
        //Обновление содержимого кнопки
        int cellIndex = GameBoard.dimension * x + y;
        board.getButton(cellIndex).setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));

        //Проверка победы
        if(board.checkWin()){
            button.getBoard().getGame().showMessage("Компьютер выиграл!");
            board.emptyField();
        }
        else{
            //Передать ход
            board.getGame().passTurn();
        }
    }
}
