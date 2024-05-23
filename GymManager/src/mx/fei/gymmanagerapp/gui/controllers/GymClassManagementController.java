package mx.fei.gymmanagerapp.gui.controllers;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import main.MainApp;
import mx.fei.gymmanagerapp.logic.gymclass.*;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;


/**
 * FXML Controller class
 *
 * @author d0ubl3_d
 */
public class GymClassManagementController implements Initializable {
    
    @FXML
    private TableView<GymClass> gymClassesTableView;

    @FXML
    private TableColumn<GymClass, String> nameTableColumn;

    @FXML
    private TableColumn<GymClass, String> descriptionTableColumn;

    @FXML
    private TableColumn<GymClass, String> scheduleTableColumn;

    @FXML
    private TableColumn<GymClass, String> daysTableColumn;

    @FXML
    private TableColumn<GymClass, Integer> capacityTableColumn;

    @FXML
    private TableColumn<GymClass, String> coachTableColumn;
    
    @FXML
    private Button backButton;
    
    @FXML
    private Button registerButton;
    
    private final GymClassDAO GYMCLASS_DAO = new GymClassDAO();    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gymClassesTableView.getItems().addAll(initializeGymClassesArray());
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        scheduleTableColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        daysTableColumn.setCellValueFactory(new PropertyValueFactory<>("days"));
        capacityTableColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        coachTableColumn.setCellValueFactory(new PropertyValueFactory<>("coach"));
        
        gymClassesTableView.setRowFactory(tv -> {
            TableRow<GymClass> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (!row.isEmpty())) {
                    GymClass rowData = row.getItem();
                    try {
                        handleDoubleClick(rowData);
                    } catch (IOException exception) {
                        Logger.getLogger(GymClassManagementController.class.getName()).log(Level.SEVERE, null, exception);
                    }
                }
            });
            return row;
        });
    }    
    
    @FXML
    void backButtonIsPressed(ActionEvent event) throws IOException{        
        MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/main");           
    }
    
    @FXML
    void registerButtonIsPressed(ActionEvent event) throws IOException {
        MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/GymClassRegister");
    }    
        
    private void handleDoubleClick(GymClass gymClass) throws IOException {
        if(gymClass != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/gymmanagerapp/gui/views/GymClassDetails.fxml"));
            MainApp.changeView(fxmlLoader);
            GymClassDetailsController gymClassDetailsController = fxmlLoader.getController();
            gymClassDetailsController.setGymClass(gymClass);
        }
    }
    
    private ArrayList<GymClass> initializeGymClassesArray() {
        ArrayList<GymClass> gymClasses = new ArrayList<>();
        try {
            gymClasses = GYMCLASS_DAO.getGymClasses();
        } catch (DAOException exception) {
            java.util.logging.Logger.getLogger(GymClassManagementController.class.getName()).log(Level.SEVERE, null, exception);
        }
        return gymClasses;
    }
    
}
