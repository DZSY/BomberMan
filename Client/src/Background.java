import java.awt.*;

/**
 * Created by positif on 18/12/2016.
 */
public class Background {
    boolean[][] isWall = {
            {false, false, false, false, false, false, false, true , true , true , true , false, false, false, false, false, true , true },
            {false, false, false, false, false, false, false, true , true , true , true , false, false, false, false, false, true , true },
            {false, false, false, false, false, false, false, true , true , true , true , false, false, false, false, false, true , true },
            {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true },
            {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true , true },
            {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true },
            {true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
            {true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
            {true , true , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
            {true , true , false, false, false, false, false, true , true , true , true , false, false, false, false, false, false, false},
            {true , true , false, false, false, false, false, true , true , true , true , false, false, false, false, false, false, false},
            {true , true , false, false, false, false, false, true , true , true , true , false, false, false, false, false, false, false}
    };

    int width = ConstantDefinition.GAME_WIDTH;
    int height = ConstantDefinition.GAME_HEIGHT;

    int gridWidth  = ConstantDefinition.GRID_WIDTH;
    int gridHeight = ConstantDefinition.GRID_HEIGHT;

    public Background() {

    }

    public void draw(Graphics graphics) {
        //draw the background
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        //draw the walls

        graphics.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < ConstantDefinition.GRID_Y_COUNT; i++) {
            for (int j = 0; j < ConstantDefinition.GRID_X_COUNT; j++) {
                if (isWall[i][j]) {
                    graphics.fillRect(
                            j * gridHeight,
                            i * gridWidth,
                            gridWidth,
                            gridHeight
                    );
                }
            }
        }
    }
}
