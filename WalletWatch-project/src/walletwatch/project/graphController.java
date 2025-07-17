package walletwatch.project;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class graphController implements Initializable {

    private String username = "";

    @FXML private Label diseducationcost;
    @FXML private Label disfoodcost;
    @FXML private Label discostrasport;
    @FXML private Label discostliving;
    @FXML private Label discostothers;
    @FXML private Button foodbt;
    @FXML private Button livingbt;
    @FXML private Button othersbt;
    @FXML private Button educationbt;
    @FXML private Button transportbt;
    @FXML private Button backtohome;
    @FXML private Label distotalcost;
    @FXML private TextField fromdate;
    @FXML private TextField todate;
    @FXML private Button searchbt;
    @FXML private Button print;
    @FXML
    private VBox costshowvbox;
    @FXML
    private HBox searchhbox;
    @FXML
    private HBox searchbthbox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        fromdate.setText("2000-01-01");
        todate.setText(LocalDate.now().toString());

        searchbt.setOnAction(event -> searchAction(event));

        
        searchAction(null);
    }

    void setdata(String usernameg) {
        username = usernameg;
    }

    private void openTable(String category, ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("table.fxml"));
        Parent root = loader.load();
        TableController controller = loader.getController();
        controller.setdata(username, category);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML private void showeducationaction(ActionEvent event) throws IOException { openTable("education", event); }
    @FXML private void foodshowaction(ActionEvent event) throws IOException { openTable("food", event); }
    @FXML private void trasportshowaction(ActionEvent event) throws IOException { openTable("transport", event); }
    @FXML private void livingshowaction(ActionEvent event) throws IOException { openTable("living", event); }
    @FXML private void othersshowaction(ActionEvent event) throws IOException { openTable("others", event); }

    @FXML private void backtohomeaction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashpage.fxml"));
        Parent root = loader.load();
        dashpageController newController = loader.getController();
        newController.setdata(username);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML private void printaction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("print.fxml"));
        Parent root = loader.load();
        PrintController newController = loader.getController();
        newController.setdata(username, todate, fromdate);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void searchAction(ActionEvent event) {
        String from = fromdate.getText();
        String to = todate.getText();

        if (from.isEmpty() || to.isEmpty()) {
            System.out.println("Please enter both from and to dates.");
            return;
        }

        try {
            Date fromDate = Date.valueOf(from);
            Date toDate = Date.valueOf(to);

            String[] categories = {"education", "food", "transport", "living", "others"};
            Label[] costLabels = {diseducationcost, disfoodcost, discostrasport, discostliving, discostothers};

            double totalCost = 0;
            double[] sums = new double[categories.length];

            try (Connection conn = DatabaseConnection.getConnection()) {
                if (conn == null) {
                    System.out.println("Could not connect to database.");
                    return;
                }

                for (int i = 0; i < categories.length; i++) {
                    String cat = categories[i];
                    String tableName = cat + "_" + username;
                    double sum = getSumAmountForCategory(conn, tableName, fromDate, toDate);
                    sums[i] = sum;
                    totalCost += sum;
                }
            }

            for (int i = 0; i < categories.length; i++) {
                costLabels[i].setText(String.format("%.2f", sums[i]));
            }

            distotalcost.setText(String.format("Total Expense: %.2f", totalCost));

        } catch (IllegalArgumentException e) {
            System.out.println("Please enter dates in the format YYYY-MM-DD.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while fetching data.");
        }
    }

    private double getSumAmountForCategory(Connection conn, String tableName, Date fromDate, Date toDate) {
        String sql = "SELECT SUM(amount) AS total FROM " + tableName + " WHERE date BETWEEN ? AND ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setDate(1, fromDate);
            pst.setDate(2, toDate);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (Exception e) {
            System.out.println("Error fetching sum for table " + tableName + ": " + e.getMessage());
        }
        return 0;
    }
}
