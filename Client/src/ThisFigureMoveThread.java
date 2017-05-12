import java.io.PrintWriter;

/**
 * Created by positif on 18/12/2016.
 */
public class ThisFigureMoveThread extends Thread{

    private GamePanel gamePanel;

    public int moveType = -1;

    public Boolean stop = false;
    public Boolean singlemoveStop = false;

    private Object moveLock;

    private PrintWriter out;

    public ThisFigureMoveThread(Object moveLock, GamePanel gamePanel, PrintWriter out) {
        this.moveLock = moveLock;
        this.gamePanel = gamePanel;
        this.out = out;
    }

    @Override
    public void run() {
        while ( !stop) {
            try {
                synchronized (moveLock) {
                    moveLock.wait();
                }
                out.println(ConstantDefinition.CLIENT_FIGHT_MOVE + " " + moveType);
                singlemoveStop = false;
                while ( !singlemoveStop) {
                    if ( !gamePanel.figureCanMove(ConstantDefinition.FIGURE_THIS, moveType) )
                        break;
                    sleep(10);
                    gamePanel.figureMove(ConstantDefinition.FIGURE_THIS, moveType);
                    gamePanel.repaint();
                }
                out.println(ConstantDefinition.CLIENT_FIGHT_STOP + gamePanel.getthisPosition());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
