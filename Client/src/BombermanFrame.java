/**
 * Created by positif on 18/12/2016.
 */

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BombermanFrame extends JFrame {

    private GamePanel gamePanel;
    private int thiscurrentState = ConstantDefinition.STATIC;
    private int rivalcurrentState = ConstantDefinition.STATIC;
    private ThisFigureMoveThread thisfigureMoveThread = null;
    private RivalFigureMoveThread rivalFigureMoveThread = null;

    private Object thismoveLock = new Object();
    private Object rivalmoveLock = new Object();

    private PrintWriter out;

    private StartFrame startFrame;
    private ClientReceiveThread clientReceiveThread;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public BombermanFrame(StartFrame startFrame, int playerType, BufferedReader in, PrintWriter out) {

        this.startFrame = startFrame;
        setLayout(new FlowLayout());
        gamePanel = new GamePanel(playerType,out);
        gamePanel.setPreferredSize(
                new Dimension(
                        ConstantDefinition.GAME_WIDTH,
                        ConstantDefinition.GAME_HEIGHT));
        add(gamePanel);
        this.out = out;
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                out.println(ConstantDefinition.CLIENT_FIGHT_EXIT);
            }
        });
        this.requestFocus();

        addEventListener();
        thisfigureMoveThread = new ThisFigureMoveThread(thismoveLock, gamePanel,out);
        rivalFigureMoveThread = new RivalFigureMoveThread(rivalmoveLock, gamePanel);
        clientReceiveThread = new ClientReceiveThread(this, in);
        executorService.execute(thisfigureMoveThread);
        executorService.execute(rivalFigureMoveThread);
        executorService.execute(clientReceiveThread);
    }


    private void addEventListener() {
        this.addKeyListener(new KeyAdapter()//键盘监听按钮
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP :
                        if (thiscurrentState == ConstantDefinition.STATIC)
                            thiscurrentState = ConstantDefinition.UP;
                    case KeyEvent.VK_DOWN :
                        if (thiscurrentState == ConstantDefinition.STATIC)
                            thiscurrentState = ConstantDefinition.DOWN;
                    case KeyEvent.VK_LEFT :
                        if (thiscurrentState == ConstantDefinition.STATIC)
                            thiscurrentState = ConstantDefinition.LEFT;
                    case KeyEvent.VK_RIGHT : {
                        if (thiscurrentState == ConstantDefinition.STATIC) {
                            thiscurrentState = ConstantDefinition.RIGHT;
                        }
                        synchronized (thismoveLock) {
                            thisfigureMoveThread.moveType = thiscurrentState;
                            thismoveLock.notify();
                        }
                        break;
                    }
                    case KeyEvent.VK_SPACE : {
                        gamePanel.layABomb(ConstantDefinition.FIGURE_THIS);
                        gamePanel.repaint();
                        break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_RIGHT : {
                        thisfigureMoveThread.singlemoveStop = true;
                        thiscurrentState = ConstantDefinition.STATIC;
                        break;
                    }
                }
            }
        });
    }

    public void rivalMove(String moveType) {
        switch (moveType) {
            case "0" :
                if (rivalcurrentState == ConstantDefinition.STATIC)
                    rivalcurrentState = ConstantDefinition.UP;
            case "1" :
                if (rivalcurrentState == ConstantDefinition.STATIC)
                    rivalcurrentState = ConstantDefinition.DOWN;
            case "2" :
                if (rivalcurrentState == ConstantDefinition.STATIC)
                    rivalcurrentState = ConstantDefinition.LEFT;
            case "3" : {
                if (rivalcurrentState == ConstantDefinition.STATIC) {
                    rivalcurrentState = ConstantDefinition.RIGHT;
                }
                synchronized (rivalmoveLock) {
                    rivalFigureMoveThread.moveType = rivalcurrentState;
                    rivalmoveLock.notify();
                }
                break;
            }
        }
    }

    public void rivalStop(String X, String Y) {
        rivalFigureMoveThread.singlemoveStop = true;
        rivalcurrentState = ConstantDefinition.STATIC;
        gamePanel.setrivalPosition(X,Y);
        gamePanel.repaint();
    }

    public void layaBomb() {
        gamePanel.layABomb(ConstantDefinition.FIGURE_RIVAL);
        gamePanel.repaint();
    }

    public void win() {
        setVisible(false);
        clientReceiveThread.stop = true;
        out.println();
        startFrame.win();
        dispose();
    }

    public void lose() {
        out.println();
        clientReceiveThread.stop = true;
        setVisible(false);
        startFrame.lose();
        dispose();
    }

    public void rivalExit() {
        out.println();
        clientReceiveThread.stop = true;
        setVisible(false);
        startFrame.rivalExit();
        dispose();
    }


}
