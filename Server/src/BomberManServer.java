import org.sqlite.SQLiteException;

import java.io.*;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.Socket;
import java.sql.*;

import static java.lang.Thread.sleep;


public class BomberManServer {
    private static final int PORT = 12345;
    private ServerSocket server = null;
    private ExecutorService executorService = null;

    private Map unameSocketMap = new HashMap();
    Queue<String> waitingQueue = new LinkedList<String>();

    private final static String CLIENT_REGISTER = "CLIENT_REGISTER";
    private final static String CLIENT_LOGIN = "CLIENT_LOGIN";
    private final static String CLIENT_LOGOUT = "CLIENT_LOGOUT";
    private final static String CLIENT_CREATE_FIGHT = "CLIENT_CREATE_FIGHT";
    private final static String CLIENT_JOIN_FIGHT = "CLIENT_JOIN_FIGHT";
    private final static String CLIENT_FIGHT_MOVE = "CLIENT_FIGHT_MOVE";
    private final static String CLIENT_FIGHT_STOP = "CLIENT_FIGHT_STOP";
    private final static String CLIENT_FIGHT_LAY_A_BOMB = "CLIENT_FIGHT_LAY_A_BOMB";
    private final static String CLIENT_FIGHT_DEAD = "CLIENT_FIGHT_DEAD";
    private final static String CLIENT_FIGHT_EXIT = "CLIENT_FIGHT_EXIT";

    private final static String SERVER_REGISTER_FAIL = "SERVER_REGISTER_FAIL";
    private final static String SERVER_REGISTER_SUCCEED = "SERVER_REGISTER_SUCCEED";
    private final static String SERVER_LOGIN_FAIL = "SERVER_LOGIN_FAIL";
    private final static String SERVER_LOGIN_SUCCEED = "SERVER_LOGIN_SUCCEED";
    private final static String SERVER_FIGHT_START = "SERVER_FIGHT_START";
    private final static String SERVER_JOIN_FIGHT_FAIL = "SERVER_JOIN_FIGHT_FAIL";
    private final static String SERVER_FIGHT_MOVE = "SERVER_FIGHT_MOVE";
    private final static String SERVER_FIGHT_STOP = "SERVER_FIGHT_STOP";
    private final static String SERVER_FIGHT_LAY_A_BOMB = "SERVER_FIGHT_LAY_A_BOMB";
    private final static String SERVER_FIGHT_WIN = "SERVER_FIGHT_WIN";
    private final static String SERVER_FIGHT_LOSE = "SERVER_FIGHT_LOSE";
    private final static String SERVER_FIGHT_EXIT = "SERVER_FIGHT_EXIT";

    private Connection con;
    private Statement stmt;
    private Integer currentFight = 0;

    public static void main(String[] args) {
        new BomberManServer();
    }

    public BomberManServer()
    {
        try
        {
            server = new ServerSocket(PORT);
            InetAddress address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();
            executorService = Executors.newCachedThreadPool();
            System.out.println("BomberMan Server is running on " + ip + ":" + PORT + "...\n");
        }catch( Exception e ) {
            e.printStackTrace();
        }
        databaseInitial();
        acceptClient();
    }

    private void databaseInitial() {
        con = null;
        stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:bomberman.db");
            stmt = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String sql = "CREATE TABLE USERS("
                    + "username CHAR(1000),"
                    + "password CHAR(1000))";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
        }
        try {
            stmt.close();
            con.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void acceptClient() {
        try {
            while(true)
            {
                //accept a new client
                Socket newClient = server.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(newClient.getInputStream()));
                String msg;
                while ((msg = in.readLine()) == null);

                System.out.println("New Client : " + msg);

                String temp[] = msg.split(" ");

                PrintWriter pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(newClient.getOutputStream(), "UTF-8")), true);

                try {
                    if (CLIENT_REGISTER.equals(temp[0])) {
                        connect();
                        String sql;
                        ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS where "
                                + "username = '" + temp[1] + "';");
                        //the username already exists.
                        if ( rs.next() )
                            pout.println(SERVER_REGISTER_FAIL);
                        else {
                            if(unameSocketMap.containsKey(temp[1])) {
                                pout.println(SERVER_REGISTER_FAIL);
                            }
                            else {
                                sql = "INSERT INTO USERS (username,password) VALUES ('"
                                        + temp[1] + "','"
                                        + temp[2] + "');";
                                stmt.executeUpdate(sql);
                                unameSocketMap.put(temp[1],newClient);
                                ready(temp[1]);
                                pout.println(SERVER_REGISTER_SUCCEED);
                            }
                        }
                        rs.close();
                        dispose();
                    }
                    else if (CLIENT_LOGIN.equals(temp[0])) {
                        connect();
                        String sql;
                        ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS where "
                                + "username = '" + temp[1] + "' AND "
                                + "password = '" + temp[2] + "';");
                        //the username correspond to the password.
                        if ( rs.next() ) {
                            if(unameSocketMap.containsKey(temp[1])) {
                                pout.println(SERVER_LOGIN_FAIL);
                            }
                            else {
                                unameSocketMap.put(temp[1],newClient);
                                ready(temp[1]);
                                pout.println(SERVER_LOGIN_SUCCEED);
                            }
                        }
                        else
                            pout.println(SERVER_LOGIN_FAIL);
                        rs.close();
                        dispose();
                    }
                    else {
                        pout.println("NO");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    private void ready(String username) {
        executorService.execute(new BomberManClient( username ));
    }

    private void connect() {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:bomberman.db");
            stmt = con.createStatement();
        }
        catch(SQLException e) {}
    }

    private void dispose() {
        try {
            stmt.close();
            con.close();
        }
        catch(SQLException e) {}
    }

    class BomberManClient implements Runnable
    {
        private Socket socket;
        private String username;
        private BufferedReader input = null;
        private PrintWriter output;

        public BomberManClient(String username) {
            this.username = username;
            socket = (Socket) unameSocketMap.get(username);
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream(),
                                        "UTF-8")),
                        true);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String msg = input.readLine();
                    if (msg == null) {
                        unameSocketMap.remove(username);
                        socket.close();
                        break;
                    }
                    System.out.println("REQUEST FIGHT OR LOGOUT : " + msg);
                    String temp[] = msg.split(" ");
                    if (CLIENT_CREATE_FIGHT.equals(temp[0])) {
                        waitingQueue.offer(username);
                        break;
                    }
                    else if (CLIENT_JOIN_FIGHT.equals(temp[0])) {
                        synchronized (waitingQueue) {
                            if(waitingQueue.isEmpty()) {
                                output.println(SERVER_JOIN_FIGHT_FAIL);
                            }
                            else {
                                synchronized (currentFight) {
                                    currentFight++;
                                }
                                executorService.execute(new Fight(waitingQueue.poll(), username));
                                break;
                            }
                        }
                    } else if (CLIENT_LOGOUT.equals(temp[0])) {
                        unameSocketMap.remove(username);
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Fight implements Runnable
    {
        private Socket socket1;
        private Socket socket2;
        private BufferedReader input1 = null;
        private BufferedReader input2 = null;
        private PrintWriter output1;
        private PrintWriter output2;
        private String msg = "";

        private String player1;
        private String player2;


        private Thread handle1;
        private Thread handle2;

        private boolean stop = false;

        public Fight(String player1, String player2) {
            this.player1 = player1;
            this.player2 = player2;
            socket1 = (Socket) unameSocketMap.get(player1);
            socket2 = (Socket) unameSocketMap.get(player2);
            try {
                input1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                input2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
                output1 = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket1.getOutputStream(),
                                        "UTF-8")),
                        true);
                output2 = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket2.getOutputStream(),
                                        "UTF-8")),
                        true);
            }catch(IOException e) {
                e.printStackTrace();
            }

            handle1 = new Thread() {
                public void run() {
                    do {
                        String msg1;
                        try {
                            msg1 = input1.readLine();
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                        if (msg1 != null)
                        {
                            System.out.println(player1 + " : " + msg1);
                            String temp[] = msg1.split(" ");
                            if ( CLIENT_FIGHT_MOVE.equals(temp[0]) ) {
                                sendmsg(2,SERVER_FIGHT_MOVE + " " + temp[1]);
                            }
                            else if ( CLIENT_FIGHT_STOP.equals(temp[0]) ) {
                                sendmsg(2,SERVER_FIGHT_STOP + " " + temp[1] + " " + temp[2]);
                            }
                            else if ( CLIENT_FIGHT_LAY_A_BOMB.equals(temp[0]) ) {
                                sendmsg(2,SERVER_FIGHT_LAY_A_BOMB);
                            }
                            else if (CLIENT_FIGHT_DEAD.equals(temp[0])) {
                                sendmsg(2,SERVER_FIGHT_WIN);
                                sendmsg(1,SERVER_FIGHT_LOSE);
                                fightEnd();
                                break;
                            }
                            else if ( CLIENT_FIGHT_EXIT.equals(temp[0])) {
                                sendmsg(2, SERVER_FIGHT_EXIT);
                                fightEnd();
                                break;
                            }
                        }
                        System.out.println(player1 + " : " + msg1);
                    } while(!stop);
                    System.out.println("HANDLE1 STOP");
                }
            };
            handle2 = new Thread() {
                public void run() {
                    do {
                        String msg2;
                        try {
                            msg2 = input2.readLine();
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                        if (msg2 != null) {
                            String temp[] = msg2.split(" ");
                            if (CLIENT_FIGHT_MOVE.equals(temp[0])) {
                                sendmsg(1, SERVER_FIGHT_MOVE + " " + temp[1]);
                            } else if (CLIENT_FIGHT_STOP.equals(temp[0])) {
                                sendmsg(1,SERVER_FIGHT_STOP + " " + temp[1] + " " + temp[2]);
                            } else if (CLIENT_FIGHT_LAY_A_BOMB.equals(temp[0])) {
                                sendmsg(1, SERVER_FIGHT_LAY_A_BOMB);
                            } else if (CLIENT_FIGHT_DEAD.equals(temp[0])) {
                                sendmsg(1, SERVER_FIGHT_WIN);
                                sendmsg(2, SERVER_FIGHT_LOSE);
                                fightEnd();
                                break;
                            } else if (CLIENT_FIGHT_EXIT.equals(temp[0])) {
                                sendmsg(1, SERVER_FIGHT_EXIT);
                                fightEnd();
                                break;
                            }
                            System.out.println(player2 + " : " + msg2);
                        }
                    } while(!stop);
                    System.out.println("HANDLE2 STOP");
                }
            };
        }

        @Override
        public void run() {
            try{
                sleep(500 );
                sendmsg(1,SERVER_FIGHT_START + " " + player2);
                sendmsg(2,SERVER_FIGHT_START + " " + player1);
                handle1.start();
                handle2.start();
                synchronized (currentFight) {
                    currentFight--;
                }
            } catch( InterruptedException e ) {
                e.printStackTrace();
            }
        }

        private void fightEnd() {
            stop = true;
            ready(player1);
            ready(player2);
        }

        public void sendmsg(int who, String msg) {
            System.out.println("Server : " + msg);
            if (who == 0) {
                output2.println(msg);
                output1.println(msg);
            }
            else if (who == 1) {
                output1.println(msg);
            }
            else if (who == 2) {
                output2.println(msg);
            }
        }
    }
}