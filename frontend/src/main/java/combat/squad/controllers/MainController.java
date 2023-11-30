package combat.squad.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label label;

    @FXML
    public void initialize() {
        fetchDataFromBackend();
    }

    private void fetchDataFromBackend() {

        try {
            java.net.URL url = new java.net.URL("http://localhost:8080/user");
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
                java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                label.setText("Dane z backendu: " + response.toString());
            } else {
                label.setText("Błąd podczas pobierania danych z backendu.");
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
