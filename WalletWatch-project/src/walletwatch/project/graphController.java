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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private Button foodbt;
    @FXML
    private Button livingbt;
    @FXML
    private Button othersbt;
    @FXML
    private Button backtohome;
    @FXML
    private VBox costshowvbox;
    @FXML
    private Label distotalcost;
    @FXML
    private VBox showbtvbox;
    @FXML
    private Button educationbt;
    @FXML
    private Button transportbt;
    @FXML
    private VBox graphvbox;
    @FXML
    private HBox searchhbox;
    @FXML
    private TextField fromdate;
    @FXML
    private TextField todate;
    @FXML
    private HBox searchbthbox;
    @FXML
    private Button searchbt;
    @FXML
    private Button othersbt1;

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
    private void showeducationaction(ActionEvent event) throws IOException {
        openTable("education", event);
    }

    @FXML
    private void foodshowaction(ActionEvent event) throws IOException {
      openTable("food", event);
        
    }

    @FXML
    private void trasportshowaction(ActionEvent event) throws IOException {
        openTable("transport", event);
    }

    @FXML
    private void livingshowaction(ActionEvent event) throws IOException {
        
        openTable("living", event);
    }

    @FXML
    private void othersshowaction(ActionEvent event) throws IOException {
        openTable("others", event);
        
    }

    @FXML
    private void backtohomeaction(ActionEvent event) throws IOException {
         Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashpage.fxml"));
            Parent root = loader.load();
            dashpageController newController = loader.getController();
            newController.setdata(username);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
    }
}
