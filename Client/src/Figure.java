import java.awt.*;

/**
 * Created by positif on 18/12/2016.
 */
public class Figure {
    public int X;
    public int Y;
    public Integer bombsNum = 0;

    private Color color;
    int gridWidth  = ConstantDefinition.GRID_WIDTH;
    int gridHeight = ConstantDefinition.GRID_HEIGHT;

    public Figure(int playerType) {
        if (playerType == ConstantDefinition.CREATER) {
            color = Color.pink;
            this.X = 0;
            this.Y = 0;
        }
        else {
            color = Color.cyan;
            this.X = 480;
            this.Y = 0;
        }
    }

    public void draw(Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRoundRect(X, Y, gridWidth, gridHeight, 20, 20);
    }

}
