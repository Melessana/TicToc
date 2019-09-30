package game;

import javax.swing.*;
import java.awt.*;

public class GameButton extends JButton {
    private int buttonIndex;
    private GameBoard board;


    public GameButton(int gameButtonIndex, GameBoard currentGameBoard){
        buttonIndex = gameButtonIndex;
        board = currentGameBoard;



        int rowNum = buttonIndex / GameBoard.dimension;         //строка кнопки
        int cellNum = buttonIndex % GameBoard.dimension;        //столбец кнопки

        setSize(GameBoard.cellSize - 5, GameBoard.cellSize - 5);
        addActionListener(new GameActionListener(rowNum, cellNum, this));
        this.setFont(new Font("Arial", Font.BOLD, 20));
    }

    public GameBoard getBoard(){
        return board;
    }
}
