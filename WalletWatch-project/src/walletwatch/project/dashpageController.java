package walletwatch.project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class dashpageController implements Initializable {

    @FXML
    private Label hellouser;
    String username = "";
    
    @FXML
    private Button chartshow;
    @FXML
    private PieChart incomevsexpansepiechart;
    @FXML
    private Label expanseshow;
    @FXML
    private Label incomeshow;
    @FXML
    private Button addincomebt;
    @FXML
    private Button educationbt;
    @FXML
    private Button foodbt;
    @FXML
    private Button trasporbt;
    @FXML
    private Button othersbt;
    @FXML
    private Button livingbt;
    String tabletype="";
     double income = 10000;
     double expense = 6000;
    @FXML
    private Button logoutbt;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadPieChart();
    }

    void setdata(String getusername) {
        username = getusername;
        hellouser.setText("Hello " + username);
        expanseshow.setText("Expanse: "+expense);
        incomeshow.setText("Income:"+income);
        
    }

    private void loadPieChart() {
       
       

        PieChart.Data incomeData = new PieChart.Data("Income", income);
        PieChart.Data expenseData = new PieChart.Data("Expense", expense);

        incomevsexpansepiechart.getData().addAll(incomeData, expenseData);
     
    }

    @FXML
    private void chartshowaction(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("graph.fxml"));
        Parent root = loader.load();
        graphController newController = loader.getController();
        newController.setdata(username);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void addincomeaction(ActionEvent event) throws IOException {
        
         Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("table.fxml"));
            Parent root = loader.load();
            TableController newController = loader.getController();
            newController.setdata(username,"income");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
        
    }

    @FXML
    private void educationexpanse(ActionEvent event) throws IOException {
         Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("table.fxml"));
            Parent root = loader.load();
            TableController newController = loader.getController();
            newController.setdata(username,"education");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
            
    }

    @FXML
    private void foodexpanse(ActionEvent event) throws IOException {
        
         Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("table.fxml"));
            Parent root = loader.load();
            TableController newController = loader.getController();
            newController.setdata(username,"food");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    }

    @FXML
    private void trasportexpanse(ActionEvent event) throws IOException {
         Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("table.fxml"));
            Parent root = loader.load();
            TableController newController = loader.getController();
            newController.setdata(username,"trasport");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    }

    @FXML
    private void othersexpanse(ActionEvent event) throws IOException {
         Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("table.fxml"));
            Parent root = loader.load();
            TableController newController = loader.getController();
            newController.setdata(username,"others");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
    }

    @FXML
    private void livingexpanse(ActionEvent event) throws IOException {
         Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("table.fxml"));
            Parent root = loader.load();
            TableController newController = loader.getController();
            newController.setdata(username,"living");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    }

    @FXML
    private void logoutaction(ActionEvent event) throws IOException {
        
      Stage stage=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        
    }


    
}
