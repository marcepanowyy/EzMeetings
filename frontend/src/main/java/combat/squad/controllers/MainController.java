package combat.squad.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;

import combat.squad.model.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class MainController {

    @FXML
    private Label label;

    @FXML
    private ListView<Event> eventsListView;

    @FXML
    private Label eventDetailsLabel;

    @FXML
    public void initialize() {
        listAllUserEvents();
    }
    private void showEventDetails(Event event) {
        String details = "Event Details:\n" +
                "Name: " + event.getName() + "\n" +
                "Description: " + event.getDescription() + "\n" +
                "Final Date: " + event.getFinalDate() + "\n" +
                "Location: " + event.getLocation() + "\n" +
                "Creator ID: " + event.getCreatorId() + "\n" +
                "Event ID: " + event.getId();

        eventDetailsLabel.setText(details);
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

    private void listAllUserEvents() {

        try {
            java.net.URL url = new java.net.URL("http://localhost:8080/user/1/events");
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
                System.out.println("Raw JSON Response: " + response.toString());

                ObjectMapper objectMapper = new ObjectMapper();
                Event[] events = objectMapper.readValue(response.toString(), Event[].class);

                eventsListView.setItems(javafx.collections.FXCollections.observableArrayList(events));
                eventsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        showEventDetails(newValue);
                    }
                });

            }else {
                System.out.println("Błąd podczas pobierania danych z backendu.");
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


    public void refreshEvents(ActionEvent actionEvent) {
        listAllUserEvents();
    }
}
