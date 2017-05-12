import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by positif on 18/12/2016.
 */

public class Bombs {
    public int[][] bombsPosition;

    int gridWidth  = ConstantDefinition.GRID_WIDTH;
    int gridHeight = ConstantDefinition.GRID_HEIGHT;

    public Bombs() {
        bombsPosition = new int[ConstantDefinition.GRID_Y_COUNT][ConstantDefinition.GRID_X_COUNT];
    }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.ORANGE);
        for ( int i = 0; i < ConstantDefinition.GRID_Y_COUNT; i++) {
            for ( int j = 0; j < ConstantDefinition.GRID_X_COUNT; j++) {
                if (bombsPosition[i][j] != 0) {
                    graphics.fillOval(
                            j * gridWidth,
                            i * gridHeight,
                            gridWidth,
                            gridHeight);
                }
            }
        }
    }

}
