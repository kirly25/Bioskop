import javax.swing.*;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Admin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Admin() {
        setTitle("    Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(255, 102, 102));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    JSONObject loginData = new JSONObject();
                    loginData.put("username", username);
                    loginData.put("password", password);

                    URL url = new URL("http://localhost:3000/admin");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    OutputStream os = connection.getOutputStream();
                    os.write(loginData.toString().getBytes());
                    os.flush();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                        String response = responseBuilder.toString();
                        System.out.println("Server Response: " + response);

                        // Show a message dialog based on the response
                        if (response.equals("Login Berhasil")) {
                            JOptionPane.showMessageDialog(null, "Login berhasil!");
                            mainMenu();
                        } else {
                            JOptionPane.showMessageDialog(null, "Username atau password salah. Coba lagi.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to connect to the JSON server. Response code: " + responseCode);
                    }

                    connection.disconnect();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred while connecting to the JSON server.");
                    ex.printStackTrace();
                }
            }
        });
    }

    private void mainMenu() {
        dispose();
        JFrame mainMenuFrame = new JFrame("Main Menu");
        mainMenuFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainMenuFrame.setSize(300, 400);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setLayout(new GridBagLayout());
        mainMenuFrame.setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JButton listFilmButton = createMenuButton("List Film");
        JButton tambahFilmButton = createMenuButton("Tambah Film");
        JButton editFilmButton = createMenuButton("Edit Film");
        JButton listBookingButton = createMenuButton("List Pesanan");
        JButton logoutButton = createMenuButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.add(listFilmButton, gbc);
        gbc.gridy++;
        buttonPanel.add(tambahFilmButton, gbc);
        gbc.gridy++;
        buttonPanel.add(editFilmButton, gbc);
        gbc.gridy++;
        buttonPanel.add(listBookingButton, gbc);
        gbc.gridy++;
        buttonPanel.add(logoutButton, gbc);

        mainMenuFrame.add(buttonPanel, gbc);

        listFilmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListFilm listfilm = new ListFilm();
                listfilm.setVisible(true);
            }
        });

        tambahFilmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TambahFilm tambahfilm = new TambahFilm();
                tambahfilm.setVisible(true);
            }
        });

        editFilmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditFilm editfilm = new EditFilm();
                editfilm.setVisible(true);
            }
        });

        listBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListBooking listbooking = new ListBooking();
                listbooking.setVisible(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainMenuFrame.dispose();
                new Admin().setVisible(true);
            }
        });

        mainMenuFrame.setVisible(true);
    }

        private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 102, 102));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Admin admin = new Admin();
                admin.setVisible(true);
                admin.getContentPane().setBackground(Color.WHITE);
            }
        });
    }
}