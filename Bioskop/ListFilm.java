import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListFilm extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;

    public ListFilm() {
        setTitle("List Film");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID Film");
        tableModel.addColumn("Judul Film");
        tableModel.addColumn("Harga");
        tableModel.addColumn("Nomor Teater");
        tableModel.addColumn("Jumlah Kursi");

        JButton backButton = new JButton("Close");
        backButton.setBackground(new Color(255, 102, 102));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dispose();
        });

        try {
            URL url = new URL("http://localhost:3000/film");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

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

                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("response");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject film = dataArray.getJSONObject(i);
                    int id = film.getInt("idfilm");
                    String Judul = film.getString("JudulFilm");
                    int Harga = film.getInt("HargaFilm");
                    int NoTeater = film.getInt("NoTeater");
                    int JumlahKursi = film.getInt("JumlahKursi");

                    tableModel.addRow(new Object[]{id, Judul, Harga, NoTeater, JumlahKursi});
                }

            } else {
                System.out.println("Failed to connect to the server. Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception ex) {
            System.out.println("Error occurred while connecting to the server.");
            ex.printStackTrace();
        }

        table = new JTable(tableModel);
        table.getTableHeader().setPreferredSize(new Dimension(0, 30));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        scrollPane = new JScrollPane(table);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ListFilm listFilm = new ListFilm();
                listFilm.setVisible(true);
                listFilm.getContentPane().setBackground(Color.WHITE);
            }
        });
    }
}
