/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package walletwatch.project;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author MERAZ IT
 */
public class TableController implements Initializable {

    @FXML
    private TableView<amountset> tableshow;
    @FXML
    private TableColumn<amountset, Integer> colid;
    @FXML
    private TableColumn<amountset, String> coldate;
    @FXML
    private TableColumn<amountset, String> colcategory;
    @FXML
    private TableColumn<amountset, Double> colamount;

    private ObservableList<amountset> datalist = FXCollections.observableArrayList();
    @FXML
    private Label tabletypedisplay;
    @FXML
    private TextField inid;
    @FXML
    private TextField indate;
    @FXML
    private TextField incategory;
    @FXML
    private TextField inamount;
    @FXML
    private Button deletebt;
    @FXML
    private Button addbt;
    @FXML
    private Button updatebt;
    @FXML
    private Button homepagebt;
    @FXML
    private Label totalshow;
    @FXML
    private Button reportbt;
    String username="";
    String typeoftable="";
    String tablename="";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colcategory.setCellValueFactory(new PropertyValueFactory<>("expense_category"));
        colamount.setCellValueFactory(new PropertyValueFactory<>("amount"));

       
        datalist.add(new amountset(1, "Book", "2024-06-01", 500));
        datalist.add(new amountset(2, "pen", "2024-06-05", 300));
        datalist.add(new amountset(3, "notebook", "2024-06-10", 150));
        datalist.add(new amountset(4, "Others", "2024-06-15", 200));

      
        tableshow.setItems(datalist);
    }    

    @FXML
    private void deleteaction(ActionEvent event) {
    }

    @FXML
    private void addaction(ActionEvent event) {
    }

    @FXML
    private void updateaction(ActionEvent event) {
    }

    @FXML
    private void backtohomepage(ActionEvent event) throws IOException {
         Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashpage.fxml"));
            Parent root = loader.load();
            dashpageController newController = loader.getController();
            newController.setdata(username);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
    }

    @FXML
    private void reportaction(ActionEvent event) throws IOException {
        
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("graph.fxml"));
        Parent root = loader.load();
        graphController newController = loader.getController();
        newController.setdata(username);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    void setdata(String usernamein, String tabletype) {
        username=usernamein;
        typeoftable=tabletype;
        tablename=tabletype+'_'+usernamein;
        tabletypedisplay.setText(tablename);
        
        
       
    }
    
}
