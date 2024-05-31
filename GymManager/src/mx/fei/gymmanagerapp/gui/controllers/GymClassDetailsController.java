package mx.fei.gymmanagerapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
//import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
//import java.util.logging.Level;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
//import javafx.scene.control.cell.PropertyValueFactory;
import main.MainApp;
import mx.fei.gymmanagerapp.gui.views.AlertMessage;
import mx.fei.gymmanagerapp.gui.views.DialogController;
import mx.fei.gymmanagerapp.logic.gymclass.*;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import static mx.fei.gymmanagerapp.logic.implementations.Status.ERROR;
import static mx.fei.gymmanagerapp.logic.implementations.Status.FATAL;

/**
 * FXML Controller class
 *
 * @author d0uble_d
 */
public class GymClassDetailsController implements Initializable {

    @FXML
    private TextField nameTextField;
    
    @FXML
    private TextField descriptionTextField;
    
    @FXML
    private TextField scheduleTextField;
    
    @FXML
    private TextField daysTextField;
    
    @FXML
    private TextField coachTextField;
    
    @FXML
    private TextField capacityTextField;
    
    //@FXML
    //private TableView<Member> membersTableView;
    
    //@FXML
    //private TableColumn<Member, String> memberTableColumn;
    
    @FXML
    private Button backButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;
    
    private GymClass gymClass = new GymClass();
    private final GymClassDAO GYMCLASS_DAO = new GymClassDAO();
    
    //private final MemberDAO MEMBER_DAO = new MemberDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
    }
    
    @FXML
    void backButtonIsPressed(ActionEvent event) throws IOException {
        MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/GymClassManagement");
    }
    
    @FXML
    void deleteButtonIsPressed(ActionEvent event) throws IOException {
        int rowsAffected = -1;
        
        if(deleteConfirmation()) {
            try {
                rowsAffected = GYMCLASS_DAO.deleteGymClassByIdGymClass(gymClass.getIdGymClass());
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
            if (rowsAffected > 0) {
                DialogController.getInformativeConfirmationDialog("Clase eliminada", "Clase eliminada con éxito");
                MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/GymClassManagement");
            } else {
                DialogController.getInformativeConfirmationDialog("Clase no eliminada", "La clase no se pudo eliminar");
            }
        }
    }
    
    @FXML
    void updateButtonIsPressed(ActionEvent event) throws IOException {
        if(gymClass != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/gymmanagerapp/gui/views/GymClassUpdate.fxml"));
            MainApp.changeView(fxmlLoader);            
            GymClassUpdateController gymClassUpdateController = fxmlLoader.getController();
            gymClassUpdateController.setGymClass(gymClass);
        }
    }
    
    private boolean deleteConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Eliminar clase", "¿Desea eliminar la clase?");
        return (response.get() == DialogController.BUTTON_YES);         
    }
    
    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage (exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR -> MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/EmployeeMain");
                case FATAL -> MainApp.changeView("/main/MainApp");
                
            }
        } catch (IOException ioException) {
            
        }
    }
    
    public void initializeTextFields() {
        nameTextField.setText(gymClass.getName());
        descriptionTextField.setText(gymClass.getDescription());
        scheduleTextField.setText(gymClass.getSchedule());
        daysTextField.setText(gymClass.getDays());
        coachTextField.setText(gymClass.getCoachName());
        capacityTextField.setText(String.valueOf(gymClass.getCapacity()));
    }
    
    //private ArrayList<Member> initializeMembersArray() {
        //ArrayList<Member> members = new ArrayList<>();
        //try {
            //members = MEMBER_DAO.getMembersByIdGymClass(gymClass.getIdGymClass());
        //} catch (DAOException exception) {
            //java.util.logging.Logger.getLogger(GymClassDetailsController.class.getName()).log(Level.SEVERE, null, exception);
        //}
        //return members;
    //}
    
    public void setGymClass(GymClass gymClass) {
        this.gymClass = gymClass;
        initializeTextFields();   
        //membersTableView.getItems().addAll(initializeMembersArray());
        //memberTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }
    
    public GymClass getGymClass() {
        return gymClass;
    }
    
}
