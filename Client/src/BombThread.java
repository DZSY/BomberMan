import com.sun.tools.javac.util.Pair;

import java.util.List;

/**
 * Created by positif on 18/12/2016.
 */
public class
BombThread extends Thread{

    private int bombX;
    private int bombY;
    private GamePanel gamePanel;
    int BOMB_DISTANCE = ConstantDefinition.BOMBING_DISTANCE;
    boolean[][] bombingPosition;


    public BombThread(int bombX, int bombY, GamePanel gamePanel) {
        this.bombX = bombX;
        this.bombY = bombY;
        this.gamePanel = gamePanel;
        bombingPosition = new boolean[ConstantDefinition.GRID_Y_COUNT][ConstantDefinition.GRID_X_COUNT];
    }

    @Override
    public void run() {
        try {
            //wait for bombing
            sleep(3000);
            if (gamePanel.isBomb(bombX, bombY)) {
                gamePanel.removeABomb(bombX, bombY);
                findNeighbor(bombX, bombY);
            }
            //add bombing
            for (int i = 0; i < ConstantDefinition.GRID_Y_COUNT; i++)
                for ( int j = 0; j < ConstantDefinition.GRID_X_COUNT; j++)
                    if (bombingPosition[i][j])
                        gamePanel.addBombing(j, i);
            //sustain
            for (int i = 0; i < ConstantDefinition.BOMBING_SUSTAIN; i++) {
                gamePanel.repaint();
                sleep(20);
            }
            gamePanel.bombingDetect();
            //remove bombing
            for (int i = 0; i < ConstantDefinition.GRID_Y_COUNT; i++)
                for ( int j = 0; j < ConstantDefinition.GRID_X_COUNT; j++)
                    if (bombingPosition[i][j])
                        gamePanel.removeBombing(j, i);
            sleep(20);
            gamePanel.repaint();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //find the other neighbor bombs
    void findNeighbor(int X, int Y) {
        int i = X;
        while( gamePanel.isVacant(i,Y) && i > X - BOMB_DISTANCE) i--;
        i++;
        while ( i < X + BOMB_DISTANCE && gamePanel.isVacant(i, Y)) {
            if (gamePanel.isBomb(i, Y)) {
                gamePanel.removeABomb(i, Y);
                findNeighbor(i, Y);
            }
            bombingPosition[Y][i] = true;
            i++;
        }

        int j = Y;
        while( gamePanel.isVacant(X,j) && j > Y - BOMB_DISTANCE) j--;
        j++;
        while ( j < Y + BOMB_DISTANCE && gamePanel.isVacant(X, j)) {
            if (gamePanel.isBomb(X, j)) {
                gamePanel.removeABomb(X, j);
                findNeighbor(X, j);
            }
            bombingPosition[j][X] = true;
            j++;
        }

    }
}
