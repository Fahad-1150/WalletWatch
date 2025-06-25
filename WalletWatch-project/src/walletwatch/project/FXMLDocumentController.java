package walletwatch.project;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField inname;

    @FXML
    private TextField inpass;

    @FXML
    private Button loginbt;

    @FXML
    private Button signbt;

    @FXML
    private Label display;

    @FXML
    private VBox vbox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection conn = DatabaseConnection.getConnection();
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    @FXML
    private void loginaction(ActionEvent event) throws IOException {
        String username = inname.getText().trim();
        String password = inpass.getText().trim();

        boolean loginSuccess = checkLogin(username, password);

        if (loginSuccess) {
            // Get current stage from the button clicked
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashpage.fxml"));
            Parent root = loader.load();

            dashpageController newController = loader.getController();
            newController.setdata(username);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show(); // optional, stage already showing
        } else {
            display.setText("Invalid Username or Password.");
        }
    }

    @FXML
    private void signaction(ActionEvent event) throws IOException {
        // Get current stage from the button clicked
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource("Signinpage.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show(); // optional
    }

    private boolean checkLogin(String username, String password) {
        boolean isValid = false;

        try {
            Connection conn = DatabaseConnection.getConnection();

            String query = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                isValid = true;
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error!");
        }

        return isValid;
    }
}
