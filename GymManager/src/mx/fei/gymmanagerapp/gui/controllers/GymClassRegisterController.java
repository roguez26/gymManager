package mx.fei.gymmanagerapp.gui.controllers;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.MainApp;
import mx.fei.gymmanagerapp.gui.views.AlertMessage;
import mx.fei.gymmanagerapp.gui.views.DialogController;
import mx.fei.gymmanagerapp.logic.employee.Employee;
import mx.fei.gymmanagerapp.logic.gymclass.*;
import mx.fei.gymmanagerapp.logic.employee.EmployeeDAO;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import mx.fei.gymmanagerapp.logic.implementations.Status;
import static mx.fei.gymmanagerapp.logic.implementations.Status.ERROR;
import static mx.fei.gymmanagerapp.logic.implementations.Status.FATAL;

/**
 * FXML Controller class
 *
 * @author d0uble_d
 */
public class GymClassRegisterController implements Initializable {
    
    @FXML
    private TextField nameTextField;
    
    @FXML
    private TextField descriptionTextField;
    
    @FXML
    private ComboBox<String> startHourComboBox;
    
    @FXML
    private ComboBox<String> startMinutesComboBox;
    
    @FXML
    private ComboBox<String> endHourComboBox;
    
    @FXML
    private ComboBox<String> endMinutesComboBox;
    
    @FXML
    private CheckBox mondayCheckBox;
    
    @FXML
    private CheckBox tuesdayCheckBox;
    
    @FXML
    private CheckBox wednesdayCheckBox;
    
    @FXML
    private CheckBox thursdayCheckBox;
    
    @FXML
    private CheckBox fridayCheckBox;
    
    @FXML
    private CheckBox saturdayCheckBox;
    
    @FXML
    private CheckBox sundayCheckBox;
    
    @FXML
    private ComboBox<Employee> coachComboBox;
    
    @FXML
    private ComboBox<Integer> capacityComboBox;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button saveButton;
    
    private final GymClassDAO GYMCLASS_DAO = new GymClassDAO();
    private final EmployeeDAO EMPLOYEE_DAO = new EmployeeDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startHourComboBox.setItems(FXCollections.observableArrayList(initializeHoursArrayForComboBox()));        
        startMinutesComboBox.setItems(FXCollections.observableArrayList(initializeMinutesArrayForComboBox()));        
        endHourComboBox.setItems(FXCollections.observableArrayList(initializeHoursArrayForComboBox()));
        endMinutesComboBox.setItems(FXCollections.observableArrayList(initializeMinutesArrayForComboBox()));
        coachComboBox.setItems(FXCollections.observableArrayList(initializeCoachesArrayForComboBox()));
        capacityComboBox.setItems(FXCollections.observableArrayList(initializeCapacityArrayForComboBox()));
    }
    
    @FXML
    private void saveButtonIsPressed(ActionEvent event) {
        if (event.getSource() == saveButton) {
            try {
                invokeGymClassRegister();  
                MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/GymClassManagement");
            } catch (DAOException exception) {
                handleDAOException(exception);
            } catch (IllegalArgumentException exception) {
                handleValidationException(exception);
            } catch (IOException exception) {
                Logger.getLogger(GymClassRegisterController.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }
    
    @FXML
    private void cancelButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == cancelButton) {
            if (confirmCancelation()) {
                MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/GymClassManagement");
            }         
        }
    }
    
    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Está seguro de que desea cancelar?");
        return (response.get() == DialogController.BUTTON_YES);
    }
    
    private void invokeGymClassRegister() throws DAOException, IOException {        
        GymClass gymClass = initializeGymClass();
        if (!fieldsAreEmpty()) {            
            int idGymClass = GYMCLASS_DAO.registerGymClass(gymClass);
            if(idGymClass > 0) {
                DialogController.getInformativeConfirmationDialog
                    ("Registrado","Se ha realizado el registro con éxito.");
                cleanFields();
            }
        } else {
            // FIX ME: CHECK THIS
            DialogController.getInformativeConfirmationDialog(
                    "Campos vacios","Asegurese de llenar todos los campos");
        }        
    }
    
    private void cleanFields() {
        nameTextField.setText("");
        descriptionTextField.setText("");
        startHourComboBox.setValue(null);
        startMinutesComboBox.setValue(null);
        endHourComboBox.setValue(null);
        endMinutesComboBox.setValue(null);
        
        mondayCheckBox.setSelected(false);
        tuesdayCheckBox.setSelected(false);
        wednesdayCheckBox.setSelected(false);
        thursdayCheckBox.setSelected(false);
        fridayCheckBox.setSelected(false);
        saturdayCheckBox.setSelected(false);
        sundayCheckBox.setSelected(false);
        
        coachComboBox.setValue(null);
        capacityComboBox.setValue(null);
                
    }
    
    private boolean fieldsAreEmpty() {
        return nameTextField.getText().isEmpty() ||                                
                descriptionTextField.getText().isEmpty() ||
                startHourComboBox.getValue() == null ||
                startMinutesComboBox.getValue() == null ||
                endHourComboBox.getValue() == null ||
                endMinutesComboBox.getValue() == null ||
                
                (!mondayCheckBox.isSelected() &&
                !tuesdayCheckBox.isSelected() &&
                !wednesdayCheckBox.isSelected() &&
                !thursdayCheckBox.isSelected() &&
                !fridayCheckBox.isSelected() &&
                !saturdayCheckBox.isSelected() &&
                !sundayCheckBox.isSelected()) ||
                
                coachComboBox.getValue() == null ||
                capacityComboBox.getValue() == null;                                        
    }
    
    private GymClass initializeGymClass() throws DAOException {
        GymClass gymClass = new GymClass();
        StringBuilder days = new StringBuilder();
        gymClass.setName(nameTextField.getText());
        gymClass.setDescription(descriptionTextField.getText());
        gymClass.setSchedule(startHourComboBox.getValue() + ":" + startMinutesComboBox.getValue() + "-"
        + endHourComboBox.getValue() + ":" + endMinutesComboBox.getValue());                
        if (mondayCheckBox.isSelected()) {
            days.append("Lunes");
        }
        if (tuesdayCheckBox.isSelected()) {
            if (days.length() > 0) {
                days.append(", ");
            }
            days.append("Martes");
        }
        if (wednesdayCheckBox.isSelected()) {
            if (days.length() > 0) {
                days.append(", ");
            }
            days.append("Miércoles");
        }
        if (thursdayCheckBox.isSelected()) {
            if (days.length() > 0) {
                days.append(", ");
            }
            days.append("Jueves");
        }
        if (fridayCheckBox.isSelected()) {
            if (days.length() > 0) {
                days.append(", ");
            }
            days.append("Viernes");
        }
        if (saturdayCheckBox.isSelected()) {
            if (days.length() > 0) {
                days.append(", ");
            }
            days.append("Sábado");
        }
        if (sundayCheckBox.isSelected()) {
            if (days.length() > 0) {
                days.append(", ");
            }
            days.append("Domingo");
        }
        gymClass.setDays(days.toString());
        gymClass.setCoach((Employee) coachComboBox.getValue());
        if (capacityComboBox.getValue() != null) {            
            gymClass.setCapacity((int) capacityComboBox.getValue());
        } else {
            gymClass.setCapacity(0);
        }        
        return gymClass;
    } 
    
    private ArrayList<String> initializeHoursArrayForComboBox() {
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {            
            String hour = String.format("%02d", i);
            hours.add(hour);
        }
        return hours;
    }
    
    private ArrayList<String> initializeMinutesArrayForComboBox() {
        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {            
            String minute = String.format("%02d", i);
            minutes.add(minute);
        }
        return minutes;
    }
    
    private ArrayList<Employee> initializeCoachesArrayForComboBox() {
        ArrayList<Employee> employees = new ArrayList<>();
        try {            
            employees = EMPLOYEE_DAO.getEmployeesByPosition();
        } catch (DAOException exception) {
            Logger.getLogger(GymClassRegisterController.class.getName()).log(Level.SEVERE, null, exception);
        }
        return employees;
    }
    
    private ArrayList<Integer> initializeCapacityArrayForComboBox() {
        ArrayList<Integer> capacity = new ArrayList<>();
        
        for (int i = 1; i <= 50; i++) {
            capacity.add(i);
        }
        
        return capacity;
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
    
    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getDialog(new AlertMessage( exception.getMessage(), Status.WARNING));
    } 
}
