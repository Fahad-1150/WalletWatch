package walletwatch.project;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class signinpageController implements Initializable {

    @FXML
    private TextField inusername;
    @FXML
    private PasswordField inpassword1;
    @FXML
    private PasswordField inpassword2;
    @FXML
    private Button signbt;
    @FXML
    private Button backlogin;
    @FXML
    private Label displaydone;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
               
                conn.close();
            } else {
                displaydone.setText("connection failed.");
            }
        } catch (Exception e) {
            displaydone.setText("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void signinaction(ActionEvent event) {
        String username = inusername.getText().trim();
        String password1 = inpassword1.getText().trim();
        String password2 = inpassword2.getText().trim();

      
        if (username.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            displaydone.setText("Please fill all fields.");
            return;
        }

        
        if (!password1.equals(password2)) {
            displaydone.setText("Passwords do not match.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();

            if (conn == null) {
                displaydone.setText("connection failed.");
                return;
            }

         
            String query = "INSERT INTO user (username, password) VALUES (?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password1);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                
                createUserTables(conn, username);
                displaydone.setText("Account created.");
            } else {
                displaydone.setText("failed.");
            }

            pst.close();
            conn.close();

        } catch (Exception e) {
            displaydone.setText("Error: " + e.getMessage());
        }
    }

   
    private void createUserTables(Connection conn, String username) {
        try {
            String[] tableNames = {
                "education_" + username,
                "living_" + username,
                "transport_" + username,
                "food_" + username,
                "others_" + username,
                "income_" + username
            };

            String[] tableQueries = {
                "CREATE TABLE IF NOT EXISTS " + tableNames[0] + " (id INT AUTO_INCREMENT PRIMARY KEY, expense_category VARCHAR(50), date DATE, amount DOUBLE)",
                "CREATE TABLE IF NOT EXISTS " + tableNames[1] + " (id INT AUTO_INCREMENT PRIMARY KEY, expense_category VARCHAR(50), date DATE, amount DOUBLE)",
                "CREATE TABLE IF NOT EXISTS " + tableNames[2] + " (id INT AUTO_INCREMENT PRIMARY KEY, expense_category VARCHAR(50), date DATE, amount DOUBLE)",
                "CREATE TABLE IF NOT EXISTS " + tableNames[3] + " (id INT AUTO_INCREMENT PRIMARY KEY, expense_category VARCHAR(50), date DATE, amount DOUBLE)",
                "CREATE TABLE IF NOT EXISTS " + tableNames[4] + " (id INT AUTO_INCREMENT PRIMARY KEY, expense_category VARCHAR(50), date DATE, amount DOUBLE)",
                "CREATE TABLE IF NOT EXISTS " + tableNames[5] + " (id INT AUTO_INCREMENT PRIMARY KEY, income_category VARCHAR(50), date DATE, amount DOUBLE)"
            };

            for (String query : tableQueries) {
                PreparedStatement pst = conn.prepareStatement(query);
                pst.executeUpdate();
                pst.close();
            }

        } catch (Exception e) {
            displaydone.setText("Table creation error: " + e.getMessage());
        }
    }

    @FXML
    private void backloginaction(ActionEvent event) throws IOException {
      
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
