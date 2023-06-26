import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class ListBooking extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;

    public ListBooking() {
        setTitle("List Booking");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID Booking");
        tableModel.addColumn("Nama");
        tableModel.addColumn("Tanggal");
        tableModel.addColumn("Judul Film");
        tableModel.addColumn("Nomor Teater");
        tableModel.addColumn("Nomor Kursi");

        try {
            URL url = new URL("http://localhost:3000/listbooking");
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
                    JSONObject booking = dataArray.getJSONObject(i);
                    int id = booking.getInt("id_bookings");
                    String nama = booking.getString("Nama");
                    String tanggal = booking.getString("Tanggal");
                    String judul = booking.getString("JudulFilm");
                    int noteater = booking.getInt("NoTeater");
                    String nokursi = booking.getString("NoKursi");

                    tableModel.addRow(new Object[]{id, nama, tanggal, judul, noteater, nokursi});
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

        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(255, 102, 102));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);

        closeButton.addActionListener(e -> {
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ListBooking().setVisible(true);
        });
    }
}
