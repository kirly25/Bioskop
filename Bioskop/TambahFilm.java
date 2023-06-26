import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TambahFilm extends JFrame {
    private JLabel judulLabel, hargaLabel, noteaterLabel, jumlahkursiLabel;
    private JTextField judulTextField, hargaTextField, noteaterTextField, jumlahkursiTextField;
    private JButton tambahButton, backButton;

    public TambahFilm() {
        setTitle("Tambah Film");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 250));

        judulLabel = new JLabel("Judul Film");
        hargaLabel = new JLabel("Harga");
        noteaterLabel = new JLabel("Nomor Teater");
        jumlahkursiLabel = new JLabel("Jumlah Kursi");

        judulTextField = new JTextField(20);
        hargaTextField = new JTextField(20);
        noteaterTextField = new JTextField(20);
        jumlahkursiTextField = new JTextField(20);

        tambahButton = new JButton("Tambah ke List");
        backButton = new JButton("Close");

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(judulLabel);
        formPanel.add(judulTextField);
        formPanel.add(hargaLabel);
        formPanel.add(hargaTextField);
        formPanel.add(noteaterLabel);
        formPanel.add(noteaterTextField);
        formPanel.add(jumlahkursiLabel);
        formPanel.add(jumlahkursiTextField);
        formPanel.add(tambahButton);
        formPanel.add(backButton);

        add(formPanel, BorderLayout.CENTER);
        tambahButton.setBackground(new Color(255, 102, 102));
        tambahButton.setForeground(Color.WHITE);
        tambahButton.setFocusPainted(false);
        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String judul = judulTextField.getText();
                String harga = hargaTextField.getText();
                String noteater = noteaterTextField.getText();
                String jumlahkursi = jumlahkursiTextField.getText();

                if (judul.isEmpty() || harga.isEmpty() || noteater.isEmpty() || jumlahkursi.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Masukkan semua data", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    URL url = new URL("http://localhost:3000/tambahfilm");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    String requestBody = String.format("{\"judul\":\"%s\",\"harga\":\"%s\",\"noteater\":\"%s\",\"jumlahkursi\":\"%s\"}", judul, harga, noteater, jumlahkursi);
                    conn.getOutputStream().write(requestBody.getBytes());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    JOptionPane.showMessageDialog(null, response.toString(), "Server Response", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        backButton.setBackground(new Color(255, 102, 102));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TambahFilm form = new TambahFilm();
                form.setVisible(true);
            }
        });
    }
}
