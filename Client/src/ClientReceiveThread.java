import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by positif on 28/12/2016.
 */
public class ClientReceiveThread extends Thread{

    private BombermanFrame bombermanFrame;
    private BufferedReader in;
    public boolean stop = false;
    public ClientReceiveThread(BombermanFrame bombermanFrame, BufferedReader in) {
        this.bombermanFrame = bombermanFrame;
        this.in = in;
    }

    @Override
    public void run() {
        while(!stop) {
            String msg = new String();
            try {
                msg = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Server : " + msg);
            String[] temp = msg.split(" ");

            switch (temp[0]) {
                case ConstantDefinition.SERVER_FIGHT_MOVE : {
                    bombermanFrame.rivalMove(temp[1]);
                    break;
                }
                case ConstantDefinition.SERVER_FIGHT_STOP : {
                    bombermanFrame.rivalStop(temp[1],temp[2]);
                    break;
                }
                case ConstantDefinition.SERVER_FIGHT_LAY_A_BOMB : {
                    bombermanFrame.layaBomb();
                    break;
                }
                case ConstantDefinition.SERVER_FIGHT_WIN : {
                    bombermanFrame.win();
                    break;
                }
                case ConstantDefinition.SERVER_FIGHT_LOSE : {
                    bombermanFrame.lose();
                    break;
                }
                case ConstantDefinition.SERVER_FIGHT_EXIT : {
                    bombermanFrame.rivalExit();
                    break;
                }
            }
        }
    }

}
