import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class StartFrame extends JFrame implements ActionListener {

    JLabel unameLabel;
    JLabel passwordLabel;
    JLabel HOSTLabel;

    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    private JTextField unameText;
    private JPasswordField passwordText;
    private JTextField HOSTText;

    private JButton loginButton;
    private JButton registerButton;

    private JButton createfightButton;
    private JButton joinfightButton;
    private JButton logoutButton;

    private PairClient pairClient;

    private String rivalName;

    public StartFrame() {
        super("BombberMan");
        unameText = new JTextField(15);
        passwordText = new JPasswordField(15);
        HOSTText = new JTextField(15);

        unameText.getDocument().addDocumentListener(new OnTextValueChanged());
        passwordText.getDocument().addDocumentListener(new OnTextValueChanged());
        HOSTText.getDocument().addDocumentListener(new OnTextValueChanged());

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        loginButton.setEnabled(false);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        registerButton.setEnabled(false);

        createfightButton = new JButton("Create Fight");
        createfightButton.addActionListener(this);

        joinfightButton = new JButton("Join Fight");
        joinfightButton.addActionListener(this);

        logoutButton = new JButton("Log out");
        logoutButton.addActionListener(this);

        unameLabel = new JLabel("User Name");
        passwordLabel = new JLabel("Password");
        HOSTLabel = new JLabel("Server IP");
        add(unameLabel);
        add(unameText);
        add(passwordLabel);
        add(passwordText);
        add(HOSTLabel);
        add(HOSTText);
        add(loginButton);
        add(registerButton);
        add(createfightButton);
        add(joinfightButton);
        add(logoutButton);

        gridBagLayout.setConstraints(unameLabel, gridBagConstraints);
        gridBagLayout.setConstraints(passwordLabel, gridBagConstraints);
        gridBagLayout.setConstraints(HOSTLabel, gridBagConstraints);
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridwidth = 0;
        gridBagLayout.setConstraints(unameText, gridBagConstraints);
        gridBagLayout.setConstraints(passwordText, gridBagConstraints);
        gridBagLayout.setConstraints(HOSTText, gridBagConstraints);
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(loginButton, gridBagConstraints);
        gridBagLayout.setConstraints(registerButton, gridBagConstraints);
        gridBagConstraints.gridwidth = 0;
        gridBagLayout.setConstraints(createfightButton, gridBagConstraints);
        gridBagLayout.setConstraints(joinfightButton, gridBagConstraints);
        gridBagLayout.setConstraints(logoutButton, gridBagConstraints);
        beforeloginLayout();
        setTitle("Bomberman");
        setLayout(gridBagLayout);
        requestFocus();

        // 调整窗体布局大小
        this.pack();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == loginButton || event.getSource() == registerButton) {
            if (HOSTText.getText().indexOf(' ') != -1 || (new String(passwordText.getPassword())).indexOf(' ') != -1) {
                JOptionPane.showMessageDialog(null, "The blank space isn't supported!", "From Bomberman", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            pairClient = new PairClient(HOSTText.getText());
            if (!pairClient.connect()) {
                JOptionPane.showMessageDialog(null, "Can't connect to the server!", "From Bomberman", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            //judge to login or register
            if (event.getSource() == loginButton) {
                if (!pairClient.login(unameText.getText(),new String(passwordText.getPassword()))) {
                    JOptionPane.showMessageDialog(null, "The username doesn't match the password, \nor the user is already online :(", "From Bomberman", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
            } else if (event.getSource() == registerButton) {
                if (!pairClient.register(unameText.getText(),new String(passwordText.getPassword()))) {
                    JOptionPane.showMessageDialog(null, "The username is already registered, \nor the user is already online :(", "From Bomberman", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
            }
            afterloginLayout();
        }
        else if (event.getSource() == createfightButton) {
            rivalName = pairClient.createFight();
            if (rivalName == null) {
                JOptionPane.showMessageDialog(null, "Can't create a fight!", "From Bomberman", JOptionPane.PLAIN_MESSAGE);
            }
            else {
                this.setVisible(false);
                BombermanFrame bombermanFrame = new BombermanFrame(this,ConstantDefinition.CREATER,pairClient.in,pairClient.out);
                bombermanFrame.setLayout(new FlowLayout());
                bombermanFrame.setTitle("Bomberman with " + rivalName);
                bombermanFrame.setSize(
                        ConstantDefinition.FRAME_WIDTH,
                        ConstantDefinition.FRAME_HEIGHT);
                bombermanFrame.setLocationRelativeTo(null); // Center the frame
                bombermanFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                bombermanFrame.setVisible(true);
            }
        }
        else if (event.getSource() == joinfightButton) {
            rivalName = pairClient.joinFight();
            if (rivalName == null) {
                JOptionPane.showMessageDialog(null, "Can't join others", "From Bomberman", JOptionPane.PLAIN_MESSAGE);
            }
            else {
                this.setVisible(false);
                BombermanFrame bombermanFrame = new BombermanFrame(this,ConstantDefinition.JOINER,pairClient.in,pairClient.out);
                bombermanFrame.setLayout(new FlowLayout());
                bombermanFrame.setTitle("Bomberman with " + rivalName);
                bombermanFrame.setSize(
                        ConstantDefinition.FRAME_WIDTH,
                        ConstantDefinition.FRAME_HEIGHT);
                bombermanFrame.setLocationRelativeTo(null); // Center the frame
                bombermanFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                bombermanFrame.setVisible(true);
            }
        }
        else if (event.getSource() == logoutButton) {
            pairClient.logout();
            pairClient.close();
            pairClient = null;
            beforeloginLayout();
        }
    }

    public void win() {
        this.setVisible(true);
        JOptionPane.showMessageDialog(null, "You win!", ":)", JOptionPane.PLAIN_MESSAGE);
    }

    public void lose() {
        this.setVisible(true);
        JOptionPane.showMessageDialog(null, "You lose!", ":(", JOptionPane.PLAIN_MESSAGE);
    }

    public void rivalExit() {
        JOptionPane.showMessageDialog(null, "Sorry, " + rivalName + " exits.", ":(", JOptionPane.PLAIN_MESSAGE);
        this.setVisible(true);
    }

    private void beforeloginLayout() {
        unameLabel.setVisible(true);
        unameText.setVisible(true);
        passwordLabel.setVisible(true);
        passwordText.setVisible(true);
        HOSTLabel.setVisible(true);
        HOSTText.setVisible(true);
        loginButton.setVisible(true);
        registerButton.setVisible(true);

        createfightButton.setVisible(false);
        joinfightButton.setVisible(false);
        logoutButton.setVisible(false);

    }

    private void afterloginLayout() {
        unameLabel.setVisible(false);
        unameText.setVisible(false);
        passwordLabel.setVisible(false);
        passwordText.setVisible(false);
        HOSTLabel.setVisible(false);
        HOSTText.setVisible(false);
        loginButton.setVisible(false);
        registerButton.setVisible(false);

        createfightButton.setVisible(true);
        joinfightButton.setVisible(true);
        logoutButton.setVisible(true);
    }

    class OnTextValueChanged implements DocumentListener {
        public void changedUpdate(DocumentEvent e) {
        }

        public void insertUpdate(DocumentEvent e) {
            if (!unameText.getText().equals("") && !passwordText.getText().equals("") && !HOSTText.getText().equals("")) {
                loginButton.setEnabled(true);
                registerButton.setEnabled(true);
            }
        }

        public void removeUpdate(DocumentEvent e) {
            if (unameText.getText().equals("") || passwordText.getText().equals("") || HOSTText.getText().equals("")) {
                loginButton.setEnabled(false);
                registerButton.setEnabled(false);
            }
        }
    }

}