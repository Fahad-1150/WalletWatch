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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class dashpageController implements Initializable {

    @FXML private Label hellouser;
    @FXML private Label expanseshow, incomeshow;
    @FXML private Button educationbt, logoutbt, livingbt, foodbt, addincomebt, trasporbt, othersbt;
    @FXML private VBox vbox, chartvbox, piechartvbox;
    @FXML private PieChart piechart;
    @FXML private ComboBox<String> expansetypecombobox;
    @FXML private TableView<ExpenseRecord> tableshow;
    @FXML private TableColumn<ExpenseRecord, Integer> colid;
    @FXML private TableColumn<ExpenseRecord, String> colcategory;
    @FXML private TableColumn<ExpenseRecord, LocalDate> coldate;
    @FXML private TableColumn<ExpenseRecord, Double> colamount;

    @FXML private LineChart<String, Number> linechart;
    @FXML private NumberAxis numberline;
    @FXML private CategoryAxis catagoryline;

    private String username = "";
    private double income = 0;
    private double expense = 0;

    @FXML private Button chartshow;
    @FXML private VBox comboboxvbox;
    @FXML private TextField shwofromdatetf;
    @FXML
    private TextField quickadddatetf;
    @FXML
    private TextField quickaddcattf;
    @FXML
    private TextField quichaddamonttf;
    @FXML
    private Button quickadd;
    @FXML
    private ComboBox<String> tabletypeforquickadd;
    int showhid=0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        shwofromdatetf.setOnAction(e -> chartshowaction(null));

        expansetypecombobox.setItems(FXCollections.observableArrayList("history",
            "income", "education", "living", "food", "transport", "others"
        ));
        tabletypeforquickadd.setItems(FXCollections.observableArrayList(
            "income", "education", "living", "food", "transport", "others"
        ));

        expansetypecombobox.setOnAction(e -> {
            String selected = expansetypecombobox.getValue();
            if (selected != null) {
                loadTableData(selected);
            }
        });

        colid.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colcategory.setCellValueFactory(data -> data.getValue().categoryProperty());
        coldate.setCellValueFactory(data -> data.getValue().dateProperty());
        colamount.setCellValueFactory(data -> data.getValue().amountProperty().asObject());
    }

    public void setdata(String getusername) {
        username = getusername;
        hellouser.setText( username);

        loadIncomeFromDB(); 
        loadExpensesFromDB(); 

        

        setupPieChart(); 
        showExpensePercentagesSimple(); 
        loadTableData("history");
    }

    private void loadIncomeFromDB() {
        income = 0;
        String table = "income_" + username;
        LocalDate fromDate = getFromDate(); 

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT SUM(amount) AS total FROM " + table;
            if (fromDate != null) sql += " WHERE date >= ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (fromDate != null) ps.setDate(1, Date.valueOf(fromDate));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    income = rs.getDouble("total");
                }
            }
        } catch (Exception e) {
            System.out.println("Income query error: " + e.getMessage());
        }
    }

    private void loadExpensesFromDB() {
        expense = 0;
        String[] tables = {
            "education_" + username,
            "living_" + username,
            "food_" + username,
            "transport_" + username,
            "others_" + username
        };
        LocalDate fromDate = getFromDate(); 

        try (Connection conn = DatabaseConnection.getConnection()) {
            for (String table : tables) {
                try {
                    String sql = "SELECT SUM(amount) AS total FROM " + table;
                    if (fromDate != null) sql += " WHERE date >= ?";

                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        if (fromDate != null) ps.setDate(1, Date.valueOf(fromDate));
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            expense += rs.getDouble("total");
                        }
                    }
                } catch (Exception e) {
                  
                }
            }
        } catch (Exception e) {
            System.out.println("DB connection error for expense: " + e.getMessage());
        }
    }

  
private void setupPieChart() {
    piechart.getData().clear();

    
    if (income > 0) {
        piechart.getData().add(new PieChart.Data("Income", income));
    }

    if (expense > 0) {
        piechart.getData().add(new PieChart.Data("Expense", expense));
    }
}


   private void loadTableData(String category) {
    String table;
    String catColumn;

    if (category.equals("income")) {
        table = "income_" + username;
        catColumn = "income_category";
    } else if (category.equals("histoy")) { 
        table = "history_" + username;
        catColumn = "category";
    } else {
        table = category + "_" + username;
        catColumn = "expense_category";
    }

    ObservableList<ExpenseRecord> data = FXCollections.observableArrayList();
    LocalDate fromDate = getFromDate();

    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT * FROM " + table;
        if (fromDate != null) sql += " WHERE date >= ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (fromDate != null) ps.setDate(1, Date.valueOf(fromDate));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String cat = rs.getString(catColumn);
                    Date d = rs.getDate("date");
                    LocalDate date = (d != null) ? d.toLocalDate() : null;
                    double amount = rs.getDouble("amount");

                    data.add(new ExpenseRecord(id, cat, date, amount));
                }
            }

            tableshow.setItems(data);
        }
    } catch (Exception e) {
        System.out.println("Error loading table data: " + e.getMessage());
    }
}

    private void showExpensePercentagesSimple() {
        if (username == null || username.isEmpty()) return;

        String[] categories = {"education", "living", "food", "trasport", "others"};
        double totalExpense = 0;
        java.util.Map<String, Double> categoryAmounts = new java.util.HashMap<>();
        LocalDate fromDate = getFromDate();

        try (Connection conn = DatabaseConnection.getConnection()) {
            for (String cat : categories) {
                String table = cat + "_" + username;
                String sql = "SELECT SUM(amount) AS total FROM " + table;
                if (fromDate != null) sql += " WHERE date >= ?";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    if (fromDate != null) ps.setDate(1, Date.valueOf(fromDate));
                    ResultSet rs = ps.executeQuery();
                    double sum = 0;
                    if (rs.next()) {
                        sum = rs.getDouble("total");
                    }
                    categoryAmounts.put(cat, sum);
                    totalExpense += sum;
                } catch (SQLException ex) {
                    categoryAmounts.put(cat, 0.0);
                }
            }
        } catch (Exception e) {
            System.out.println("DB error fetching expenses: " + e.getMessage());
            return;
        }

        linechart.getData().clear();
        javafx.scene.chart.XYChart.Series<String, Number> series = new javafx.scene.chart.XYChart.Series<>();
        series.setName("Expense % of Total");

        for (String cat : categories) {
            double amount = categoryAmounts.getOrDefault(cat, 0.0);
            double percent = (totalExpense > 0) ? (amount / totalExpense) * 100 : 0;
            series.getData().add(new javafx.scene.chart.XYChart.Data<>(cat, percent));
        }

        linechart.getData().add(series);
    }

   
    private LocalDate getFromDate() {
        try {
            String input = shwofromdatetf.getText();
            if (input != null && !input.isEmpty()) {
                if (input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    return LocalDate.parse(input);
                } else if (input.matches("\\d{2}-\\d{2}-\\d{4}")) {
                    String[] parts = input.split("-");
                    return LocalDate.of(
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[0])
                    );
                } else {
                    System.out.println("Unsupported date format: " + input);
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid date format in shwofromdatetf: " + e.getMessage());
        }
        return null;
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

@FXML
private void educationexpanse(ActionEvent event) throws IOException {
    openTable("education", event);
}

@FXML
private void foodexpanse(ActionEvent event) throws IOException {
    openTable("food", event);
}

@FXML
private void trasportexpanse(ActionEvent event) throws IOException {
    openTable("transport", event);
}

@FXML
private void othersexpanse(ActionEvent event) throws IOException {
    openTable("others", event);
}

@FXML
private void livingexpanse(ActionEvent event) throws IOException {
    openTable("living", event);
}

@FXML
private void addincomeaction(ActionEvent event) throws IOException {
    openTable("income", event);
}

    @FXML
    private void logoutaction(ActionEvent event) throws IOException {
        Stage stage = (Stage) logoutbt.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
   
private void chartshowaction(ActionEvent event) {
    loadIncomeFromDB(); 
    loadExpensesFromDB();
    
  
   
    
    setupPieChart(); 
    showExpensePercentagesSimple(); 
    
    String selected = expansetypecombobox.getValue();
    if (selected != null && !selected.isEmpty()) {
        loadTableData(selected); 
    }
    
   incomeshow.setText("Income: " + income);
expanseshow.setText("Expense: " + expense);

javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
pause.setOnFinished(e -> {
    incomeshow.setText("Income: xxxx");
    expanseshow.setText("Expense: xxxx");
    showhid = 1; 
});
pause.play();

}

    @FXML
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
    @FXML
    
    
private void quickaddaction(ActionEvent event) {

    String category = quickaddcattf.getText().trim();
    String date = quickadddatetf.getText().trim();
    String amountStr = quichaddamonttf.getText().trim();
    String tableType = tabletypeforquickadd.getValue();

    if (category.isEmpty() || date.isEmpty() || amountStr.isEmpty() || tableType == null) {
        System.out.println("Fill all fields to add.");
        return;
    }

    try {
        double amount = Double.parseDouble(amountStr);

        String mainTable = tableType + "_" + username;
        String mainColumn = tableType.equals("income") ? "income_category" : "expense_category";

       
        String query = "INSERT INTO `" + mainTable + "` (" + mainColumn + ", date, amount) VALUES ('" +
                category + "', '" + date + "', " + amount + ")";
        executeQuery(query);

       
        String historyTable = "history_" + username;
        double historyAmount = tableType.equals("income") ? amount : -amount;

        try (Connection con = DatabaseConnection.getConnection()) {
            String insertHistory = "INSERT INTO " + historyTable + " (expense_category, date, amount) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(insertHistory);
            pst.setString(1, category);
            pst.setString(2, date);
            pst.setDouble(3, historyAmount);
            pst.executeUpdate();
            pst.close();
        }

        System.out.println("Quick add completed.");

        loadTableData("history");

    } catch (NumberFormatException e) {
        System.out.println("Invalid amount input.");
    } catch (Exception e) {
        System.out.println("Quick add error: " + e.getMessage());
    }
}

    
}


