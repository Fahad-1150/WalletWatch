package walletwatch.project;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TableController implements Initializable {

    @FXML
    private TableView<ExpenseRecord> tableshow;
    @FXML
    private TableColumn<ExpenseRecord, Integer> colid;
    @FXML
    private TableColumn<ExpenseRecord, String> coldate;
    @FXML
    private TableColumn<ExpenseRecord, String> colcategory;
    @FXML
    private TableColumn<ExpenseRecord, Double> colamount;

    private ObservableList<ExpenseRecord> datalist = FXCollections.observableArrayList();

    @FXML private Label tabletypedisplay;
    @FXML private TextField inid;
    @FXML private TextField indate;
    @FXML private TextField incategory;
    @FXML private TextField inamount;
    @FXML private Button deletebt;
    @FXML private Button addbt;
    @FXML private Button updatebt;
    @FXML private Button homepagebt;
    @FXML private Button reportbt;
    @FXML private TextField indate1;
    @FXML private Label amountshow;
    @FXML private VBox textfiledvbox;
    @FXML private HBox tfhbox;
    @FXML private Button searchbt;

    String username = "";
    String typeoftable = "";
    String tablename = "";

    private String getCategoryColumn() {
        return tablename.startsWith("income_") ? "income_category" : "expense_category";
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colcategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colamount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tableshow.setItems(datalist);
    }

    private void executeQuery(String query) {
        try {
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            System.out.println("Query executed successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    private void showTable(String fromDate) {
        datalist.clear();
        double total = 0;
        String today = LocalDate.now().toString();

        String query = "SELECT * FROM `" + tablename + "` WHERE date >= '" + fromDate + "' AND date <= '" + today + "'";

        try {
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String date = rs.getString("date");
                String category = rs.getString(getCategoryColumn());
                double amount = rs.getDouble("amount");

                LocalDate localDate = LocalDate.parse(date);
                datalist.add(new ExpenseRecord(id, category, localDate, amount));
                total += amount;
            }

            tableshow.setItems(datalist);
            amountshow.setText(total + " TK");

        } catch (SQLException e) {
            System.out.println("Table Load Error: " + e.getMessage());
        }
    }

    @FXML
    private void addaction(ActionEvent event) {
        String category = incategory.getText().trim();
        String date = indate.getText().trim();
        String amount = inamount.getText().trim();

        if (!category.isEmpty() && !date.isEmpty() && !amount.isEmpty()) {
            String query = "INSERT INTO `" + tablename + "` (" + getCategoryColumn() + ", date, amount) VALUES ('" +
                    category + "', '" + date + "', " + amount + ")";
            executeQuery(query);
            showTable(indate1.getText());
        } else {
            System.out.println("Fill all fields to add.");
        }
    }

    @FXML
    private void deleteaction(ActionEvent event) {
        String id = inid.getText().trim();

        if (!id.isEmpty()) {
            String query = "DELETE FROM `" + tablename + "` WHERE id = " + id;
            executeQuery(query);
            showTable(indate1.getText());
        } else {
            System.out.println("Enter ID to delete.");
        }
    }

    @FXML
    private void updateaction(ActionEvent event) {
        String id = inid.getText().trim();
        String category = incategory.getText().trim();
        String date = indate.getText().trim();
        String amount = inamount.getText().trim();

        if (!id.isEmpty() && !category.isEmpty() && !date.isEmpty() && !amount.isEmpty()) {
            String query = "UPDATE `" + tablename + "` SET " + getCategoryColumn() + " = '" + category +
                    "', date = '" + date + "', amount = " + amount + " WHERE id = " + id;
            executeQuery(query);
            showTable(indate1.getText());
        } else {
            System.out.println("Fill all fields to update.");
        }
    }

    void setdata(String usernamein, String tabletype) {
        username = usernamein;
        typeoftable = tabletype;
        tablename = typeoftable + "_" + usernamein;
        tabletypedisplay.setText(tablename);
        showTable("1900-01-01");
    }

    @FXML
    private void selectedbyclicked(MouseEvent event) {
        ExpenseRecord selected = tableshow.getSelectionModel().getSelectedItem();
        if (selected != null) {
            inid.setText(String.valueOf(selected.idProperty().get()));
            incategory.setText(selected.categoryProperty().get());
            indate.setText(selected.dateProperty().get().toString());
            inamount.setText(String.valueOf(selected.amountProperty().get()));
        }
    }

    @FXML
    private void searchaction(ActionEvent event) {
        String fromDate = indate1.getText().trim();
        showTable(fromDate);
    }

    @FXML
    private void backtohomepage(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashpage.fxml"));
        Parent root = loader.load();
        dashpageController newController = loader.getController();
        newController.setdata(username);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void showreport(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("graph.fxml"));
        Parent root = loader.load();
        graphController newController = loader.getController();
        newController.setdata(username);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
