import java.net.*;
import java.io.*;

public class PairClient {
    private Socket socket;
    private String HOST;
    public BufferedReader in;
    public PrintWriter out;

    public PairClient(String HOST) {
        this.HOST = HOST;
    }

    public boolean connect() {
        try {
            socket = new Socket(HOST,ConstantDefinition.PORT);
        } catch(IOException e) {
            return false;
        }
        try {
            in =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));
            out =
                    new PrintWriter(
                            new BufferedWriter(
                                    new OutputStreamWriter(
                                            socket.getOutputStream())), true);
        } catch(IOException e) {
            try {
                socket.close();
            } catch(IOException e2) {
                return false;
            }
        }
        return true;
    }

    public boolean login(String username, String password) {
        out.println(ConstantDefinition.CLIENT_LOGIN + " " + username + " " + password);
        try {
            String msg = in.readLine();
            if (ConstantDefinition.SERVER_LOGIN_SUCCEED.equals(msg.split(" ")[0])) {
                System.out.println(msg);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String username, String password) {
        out.println(ConstantDefinition.CLIENT_REGISTER + " " + username + " " + password);
        try {
            String msg = in.readLine();
            if (ConstantDefinition.SERVER_REGISTER_SUCCEED.equals(msg.split(" ")[0])){
                System.out.println(msg);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String createFight() {
        out.println(ConstantDefinition.CLIENT_CREATE_FIGHT);
        try {
            String msg = in.readLine();
            String[] temp = msg.split(" ");
            if (ConstantDefinition.SERVER_FIGHT_START.equals(temp[0])){
                System.out.println(msg);
                return temp[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String joinFight() {
        out.println(ConstantDefinition.CLIENT_JOIN_FIGHT);
        try {
            String msg = in.readLine();
            String[] temp = msg.split(" ");
            if (ConstantDefinition.SERVER_FIGHT_START.equals(temp[0])){
                System.out.println(msg);
                return temp[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void logout() {
        out.println(ConstantDefinition.CLIENT_LOGOUT);
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}