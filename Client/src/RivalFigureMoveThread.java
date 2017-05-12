

/**
 * Created by positif on 18/12/2016.
 */
public class RivalFigureMoveThread extends Thread{

    private GamePanel gamePanel;

    public int moveType = -1;

    public Boolean stop = false;
    public Boolean singlemoveStop = false;

    private Object moveLock;

    public RivalFigureMoveThread(Object moveLock, GamePanel gamePanel) {
        this.moveLock = moveLock;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        while ( !stop) {
            try {
                synchronized (moveLock) {
                    moveLock.wait();
                }
                singlemoveStop = false;
                while ( !singlemoveStop) {
                    if ( !gamePanel.figureCanMove(ConstantDefinition.FIGURE_RIVAL, moveType) )
                        break;
                    sleep(10);
                    gamePanel.figureMove(ConstantDefinition.FIGURE_RIVAL, moveType);
                    gamePanel.repaint();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
