import java.awt.*;
import java.io.PrintWriter;
import java.text.ParseException;
import javax.swing.JPanel;

/**
 * Created by positif on 18/12/2016.
 */
public class GamePanel extends JPanel {
    private Background background;
    private Figure thisfigure;
    private Figure rivalfigure;
    private Bombs bombs;
    private Bombing bombing;
    private PrintWriter out;

    private final static int UP = 0;
    private final static int DOWN = 1;
    private final static int LEFT = 2;
    private final static int RIGHT = 3;


    /** Construct a default GamePanel */
    public GamePanel(int playerType, PrintWriter out) {
        background = new Background();
        thisfigure = new Figure(playerType);
        if (playerType == ConstantDefinition.CREATER)
            rivalfigure = new Figure(ConstantDefinition.JOINER);
        else
            rivalfigure = new Figure(ConstantDefinition.CREATER);
        bombs = new Bombs();
        bombing = new Bombing();
        this.out = out;
    }

    public boolean isVacant(int X, int Y) {
        if (    X >= 0 &&
                X < ConstantDefinition.GRID_X_COUNT &&
                Y >= 0 &&
                Y < ConstantDefinition.GRID_Y_COUNT &&
                !background.isWall[Y][X])
            return true;
        return false;
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        background.draw(graphics);
        // Get the appropriate size for the thisfigure
        bombs.draw(graphics);
        thisfigure.draw(graphics);
        rivalfigure.draw(graphics);
        bombing.draw(graphics);

    }

    public void figureMove(int figureType, int moveType) {
        Figure figure;
        if (figureType == ConstantDefinition.FIGURE_THIS) {
            figure = thisfigure;
        }
        else {
            figure = rivalfigure;
        }
        if (moveType == ConstantDefinition.UP)
            figure.Y -= 3;
        else if (moveType == ConstantDefinition.DOWN)
            figure.Y += 3;
        else if (moveType == ConstantDefinition.LEFT)
            figure.X -= 3;
        else if (moveType == ConstantDefinition.RIGHT)
            figure.X += 3;
    }

    public String getthisPosition() {
        return (" " + thisfigure.X + " " + thisfigure.Y);
    }

    public void setrivalPosition(String X, String Y) {
        try {
            rivalfigure.X = Integer.parseInt(X);
            rivalfigure.Y = Integer.parseInt(Y);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public boolean figureCanMove(int figureType, int moveType) {
        Figure figure;
        if (figureType == ConstantDefinition.FIGURE_THIS) {
            figure = thisfigure;
        }
        else {
            figure = rivalfigure;
        }
        int nextGridX1;
        int nextGridY1;
        int nextGridX2 = figure.X / ConstantDefinition.GRID_WIDTH;
        int nextGridY2 = figure.Y / ConstantDefinition.GRID_HEIGHT;
        if (moveType == UP) {
            if (figure.Y - 3 < 0)
                return false;
            nextGridX1 = (figure.X + ConstantDefinition.GRID_WIDTH) / ConstantDefinition.GRID_WIDTH;
            nextGridY1 = (figure.Y - 3)/ ConstantDefinition.GRID_HEIGHT;
            if (    isVacant(nextGridX1,nextGridY1) &&
                    !isBomb(nextGridX1,nextGridY1) &&

                    isVacant(nextGridX2,nextGridY1) &&
                    !isBomb(nextGridX2,nextGridY1)
                    )
                return true;
        }
        else if (moveType == DOWN) {
            nextGridX1 = (figure.X + ConstantDefinition.GRID_WIDTH) / ConstantDefinition.GRID_WIDTH;
            nextGridY1 = (figure.Y + ConstantDefinition.GRID_HEIGHT + 3) / ConstantDefinition.GRID_HEIGHT;
            if (    isVacant(nextGridX1,nextGridY1) &&
                    !isBomb(nextGridX1,nextGridY1) &&

                    isVacant(nextGridX2,nextGridY1) &&
                    !isBomb(nextGridX2,nextGridY1)
                    )
                return true;
        }
        else if (moveType == LEFT) {
            nextGridX1 = (figure.X - 3 - ConstantDefinition.GRID_WIDTH) / ConstantDefinition.GRID_WIDTH;
            nextGridY1 = (figure.Y + ConstantDefinition.GRID_HEIGHT) / ConstantDefinition.GRID_HEIGHT;
            if (    isVacant(nextGridX1,nextGridY1) &&
                    !isBomb(nextGridX1,nextGridY1) &&

                    isVacant(nextGridX1,nextGridY2) &&
                    !isBomb(nextGridX1,nextGridY2)
                    )
                return true;
        }
        else if (moveType == RIGHT) {
            nextGridX1 = (figure.X + ConstantDefinition.GRID_WIDTH + 3) / ConstantDefinition.GRID_WIDTH ;
            nextGridY1 = (figure.Y + ConstantDefinition.GRID_HEIGHT) / ConstantDefinition.GRID_HEIGHT;

            if (    isVacant(nextGridX1,nextGridY1) &&
                    !isBomb(nextGridX1,nextGridY1) &&

                    isVacant(nextGridX1,nextGridY2) &&
                    !isBomb(nextGridX1,nextGridY2)
                    )
                return true;
        }
        return false;
    }

    public boolean isBomb(int X, int Y) {
        if (bombs.bombsPosition[Y][X] != 0)
            return true;
        return false;
    }

    public boolean isBombing(int X, int Y) {
        if (bombing.bombingPosition[Y / ConstantDefinition.GRID_HEIGHT][X / ConstantDefinition.GRID_WIDTH])
            return true;
        return false;
    }

    public void layABomb(int figureType) {
        Figure figure;
        if (figureType == ConstantDefinition.FIGURE_THIS) {
            figure = thisfigure;
            out.println(ConstantDefinition.CLIENT_FIGHT_LAY_A_BOMB);
        }
        else
            figure = rivalfigure;

        if (figure.bombsNum < ConstantDefinition.MAX_BOMB_NUM) {
            int bombX = (figure.X + ConstantDefinition.GRID_WIDTH / 2) / ConstantDefinition.GRID_WIDTH;
            int bombY = (figure.Y + ConstantDefinition.GRID_HEIGHT / 2) / ConstantDefinition.GRID_HEIGHT;
            if (isBomb(bombX, bombY))
                return;
            bombs.bombsPosition[bombY][bombX] = figureType;
            figure.bombsNum++;
            (new BombThread(bombX, bombY, this)).start();
        }
    }

    public synchronized void removeABomb(int X, int Y) {
        Figure figure;
        if (bombs.bombsPosition[Y][X] == ConstantDefinition.FIGURE_THIS)
            figure = thisfigure;
        else if (bombs.bombsPosition[Y][X] == ConstantDefinition.FIGURE_RIVAL)
            figure = rivalfigure;
        else return;
        if (figure.bombsNum > 0) {
            if (!isBomb(X, Y))
                return;
            bombs.bombsPosition[Y][X] = 0;
            figure.bombsNum--;
        }
    }

    public synchronized void addBombing(int X, int Y) {
        bombing.bombingPosition[Y][X] = true;
    }

    public synchronized void removeBombing(int X, int Y) {
        bombing.bombingPosition[Y][X] = false;
    }

    public synchronized void bombingDetect() {
        if (isBombing(thisfigure.X + ConstantDefinition.ESCAPE_WIDTH, thisfigure.Y + ConstantDefinition.ESCAPE_HEIGTH) ||
                isBombing(thisfigure.X + ConstantDefinition.ESCAPE_WIDTH, thisfigure.Y + ConstantDefinition.GRID_HEIGHT - ConstantDefinition.ESCAPE_HEIGTH) ||
                isBombing(thisfigure.X + ConstantDefinition.GRID_WIDTH - ConstantDefinition.ESCAPE_WIDTH, thisfigure.Y + ConstantDefinition.ESCAPE_HEIGTH) ||
                isBombing(thisfigure.X + ConstantDefinition.GRID_WIDTH - ConstantDefinition.ESCAPE_WIDTH, thisfigure.Y + ConstantDefinition.GRID_HEIGHT - ConstantDefinition.ESCAPE_HEIGTH))
            out.println(ConstantDefinition.CLIENT_FIGHT_DEAD);
    }
}
