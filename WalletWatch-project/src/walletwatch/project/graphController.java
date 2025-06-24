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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class graphController implements Initializable {

    @FXML
    private BarChart<String, Number> barchart;
    @FXML
    private NumberAxis bary;
    @FXML
    private CategoryAxis barx;
    String username ="";
    @FXML
    private Label diseducationcost;
    @FXML
    private Label disfoodcost;
    @FXML
    private Label discostrasport;
    @FXML
    private Label discostliving;
    @FXML
    private Label discostothers;
    @FXML
    private Button showeducation;
    @FXML
    private Button foodbt;
    @FXML
    private Button trasportbt;
    @FXML
    private Button livingbt;
    @FXML
    private Button othersbt;
    @FXML
    private Button backtohome;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
        bary.setAutoRanging(false);
        bary.setLowerBound(0);
        bary.setUpperBound(100);
        bary.setTickUnit(10);

        
        barx.setCategories(javafx.collections.FXCollections.observableArrayList(
                "Education", "Food", "Trasport", "Living", "others"
        ));

       
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        

       
        series.getData().add(new XYChart.Data<>("Education", 20));
        series.getData().add(new XYChart.Data<>("Food", 40));
        series.getData().add(new XYChart.Data<>("Trasport", 60));
        series.getData().add(new XYChart.Data<>("Living", 80));
        series.getData().add(new XYChart.Data<>("others", 50));

        
        barchart.getData().add(series);
    }

    void setdata(String usernameg) {
        
        username=usernameg;
        
    }

    @FXML
    private void showeducationaction(ActionEvent event) throws IOException {
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
    private void foodshowaction(ActionEvent event) throws IOException {
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
    private void trasportshowaction(ActionEvent event) throws IOException {
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
    private void livingshowaction(ActionEvent event) throws IOException {
        
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
    private void othersshowaction(ActionEvent event) throws IOException {
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
    private void backtohomeaction(ActionEvent event) throws IOException {
         Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashpage.fxml"));
            Parent root = loader.load();
            dashpageController newController = loader.getController();
            newController.setdata(username);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
    }
}
