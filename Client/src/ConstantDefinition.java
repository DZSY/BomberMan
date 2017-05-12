/**
 * Created by positif on 18/12/2016.
 */
public class ConstantDefinition {
    public final static int GAME_WIDTH = 720;
    public final static int GAME_HEIGHT = 480;
    public final static int FRAME_WIDTH = 720;
    public final static int FRAME_HEIGHT = 510;
    public final static int GRID_X_COUNT = 18;
    public final static int GRID_Y_COUNT = 12;
    public final static int GRID_WIDTH  = GAME_WIDTH / GRID_X_COUNT;
    public final static int GRID_HEIGHT = GAME_HEIGHT / GRID_Y_COUNT;
    public final static int ESCAPE_WIDTH = GRID_WIDTH / 4;
    public final static int ESCAPE_HEIGTH = GRID_HEIGHT / 4;
    public final static int STATIC = -1;
    public final static int UP = 0;
    public final static int DOWN = 1;
    public final static int LEFT = 2;
    public final static int RIGHT = 3;
    public final static int MAX_BOMB_NUM = 5;
    public final static int BOMBING_DISTANCE = 5;
    public final static int BOMBING_SUSTAIN = 3;
    public final static int PORT = 12345;

    public final static int CREATER = 1;
    public final static int JOINER = 2;

    public final static int FIGURE_THIS = 1;
    public final static int FIGURE_RIVAL = 2;

    public final static String CLIENT_REGISTER = "CLIENT_REGISTER";
    public final static String CLIENT_LOGIN = "CLIENT_LOGIN";
    public final static String CLIENT_LOGOUT = "CLIENT_LOGOUT";
    public final static String CLIENT_CREATE_FIGHT = "CLIENT_CREATE_FIGHT";
    public final static String CLIENT_JOIN_FIGHT = "CLIENT_JOIN_FIGHT";
    public final static String CLIENT_FIGHT_MOVE = "CLIENT_FIGHT_MOVE";
    public final static String CLIENT_FIGHT_STOP = "CLIENT_FIGHT_STOP";
    public final static String CLIENT_FIGHT_LAY_A_BOMB = "CLIENT_FIGHT_LAY_A_BOMB";
    public final static String CLIENT_FIGHT_DEAD = "CLIENT_FIGHT_DEAD";
    public final static String CLIENT_FIGHT_EXIT = "CLIENT_FIGHT_EXIT";
    public final static String SERVER_REGISTER_FAIL = "SERVER_REGISTER_FAIL";
    public final static String SERVER_REGISTER_SUCCEED = "SERVER_REGISTER_SUCCEED";
    public final static String SERVER_LOGIN_FAIL = "SERVER_LOGIN_FAIL";
    public final static String SERVER_LOGIN_SUCCEED = "SERVER_LOGIN_SUCCEED";
    public final static String SERVER_FIGHT_START = "SERVER_FIGHT_START";
    public final static String SERVER_JOIN_FIGHT_FAIL = "SERVER_JOIN_FIGHT_FAIL";
    public final static String SERVER_FIGHT_MOVE = "SERVER_FIGHT_MOVE";
    public final static String SERVER_FIGHT_STOP = "SERVER_FIGHT_STOP";
    public final static String SERVER_FIGHT_LAY_A_BOMB = "SERVER_FIGHT_LAY_A_BOMB";
    public final static String SERVER_FIGHT_WIN = "SERVER_FIGHT_WIN";
    public final static String SERVER_FIGHT_LOSE = "SERVER_FIGHT_LOSE";
    public final static String SERVER_FIGHT_EXIT = "SERVER_FIGHT_EXIT";

}
