package mx.fei.gymmanagerapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.MainApp;
import mx.fei.gymmanagerapp.gui.views.AlertMessage;
import mx.fei.gymmanagerapp.gui.views.DialogController;
import mx.fei.gymmanagerapp.logic.employee.Employee;
import mx.fei.gymmanagerapp.logic.employee.EmployeeDAO;
import mx.fei.gymmanagerapp.logic.gymclass.*;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import mx.fei.gymmanagerapp.logic.implementations.Status;
import static mx.fei.gymmanagerapp.logic.implementations.Status.ERROR;
import static mx.fei.gymmanagerapp.logic.implementations.Status.FATAL;

/**
 * FXML Controller class
 *
 * @author edgar
 */

public class GymClassUpdateController implements Initializable {
    
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
    private GymClass gymClass = new GymClass();
    
    private final EmployeeDAO EMPLOYEE_DAO = new EmployeeDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @FXML
    private void cancelButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == cancelButton) {
            if (confirmCancelation()) {
                if(gymClass != null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/gymmanagerapp/gui/views/GymClassDetails.fxml"));
                    MainApp.changeView(fxmlLoader);
                    GymClassDetailsController gymClassDetailsController = fxmlLoader.getController();
                    gymClassDetailsController.setGymClass(gymClass);
                }
            }
        }
    }
    
    @FXML
    private void saveButtonIsPressed(ActionEvent event) {
        if (event.getSource() == saveButton) {
            try {
                if (!fieldsAreEmpty()) {
                    updateGymClass();
                    if (saveConfirmation()) {
                        int idGymClass = GYMCLASS_DAO.updateGymClass(gymClass);
                        if (idGymClass > 0) {
                            DialogController.getInformativeConfirmationDialog("Información actualizada", "Los cambios fueron realizados con éxito.");
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/gymmanagerapp/gui/views/GymClassDetails.fxml"));
                        MainApp.changeView(fxmlLoader);
                        GymClassDetailsController gymClassDetailsController = fxmlLoader.getController();
                        gymClassDetailsController.setGymClass(gymClass);
                    }
                } else {
                    DialogController.getInformativeConfirmationDialog("Campos vacios", "Asegúrese de llenar todos los campos");
                }
            } catch (DAOException exception) {
                handleDAOException(exception);
            } catch (IllegalArgumentException exception) {
                handleValidationException(exception);
            } catch (IOException exception) {
                Logger.getLogger(GymClassRegisterController.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }
    
    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelación", "¿Está seguro de que desea cancelar?");
        return (response.get() == DialogController.BUTTON_YES);
    }
    
    private boolean saveConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cambios", "¿Desea realizar los cambios?");
        return (response.get() == DialogController.BUTTON_YES);
    }
    
    private void updateGymClass() {
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
        gymClass.setCoach(coachComboBox.getValue());
        gymClass.setCapacity(capacityComboBox.getValue());
    }
    
    public void initializeTextFields() {
        nameTextField.setText(gymClass.getName());
        descriptionTextField.setText(gymClass.getDescription());
    }
            
    private void initializeHoursArrayForComboBox() {
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String hour = String.format("%02d", i);
            hours.add(hour);
        }
        String Schedule = gymClass.getSchedule();
        String[] getHours = Schedule.split("-");
        String startHour = getHours[0].split(":")[0];
        String endHour = getHours[1].split(":")[0];
        startHourComboBox.setItems(FXCollections.observableArrayList(hours));
        startHourComboBox.setValue(startHour);
        endHourComboBox.setItems(FXCollections.observableArrayList(hours));
        endHourComboBox.setValue(endHour);
    }
            
    private void initializeMinutesArrayForComboBox() {
        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            String minute = String.format("%02d", i);
            minutes.add(minute);
        }
        String schedule = gymClass.getSchedule();
        String[] getMinutes = schedule.split("-");
        String startMinute = getMinutes[0].split(":")[1];
        String endMinute = getMinutes[1].split(":")[1];
        startMinutesComboBox.setItems(FXCollections.observableArrayList(minutes));
        startMinutesComboBox.setValue(startMinute);
        endMinutesComboBox.setItems(FXCollections.observableArrayList(minutes));
        endMinutesComboBox.setValue(endMinute);
    }
    
    private void initializeCheckBoxes() {
        mondayCheckBox.setSelected(false);
        tuesdayCheckBox.setSelected(false);
        wednesdayCheckBox.setSelected(false);
        thursdayCheckBox.setSelected(false);
        fridayCheckBox.setSelected(false);
        saturdayCheckBox.setSelected(false);
        sundayCheckBox.setSelected(false);
        
        String days = gymClass.getDays();
        if (days != null && !days.isEmpty()) {
            if (days.contains("Lunes")) {
                mondayCheckBox.setSelected(true);
            }
            if (days.contains("Martes")) {
                tuesdayCheckBox.setSelected(true);
            }
            if (days.contains("Miércoles")) {
                wednesdayCheckBox.setSelected(true);
            }
            if (days.contains("Jueves")) {
                thursdayCheckBox.setSelected(true);
            }
            if (days.contains("Viernes")) {
                fridayCheckBox.setSelected(true);
            }
            if (days.contains("Sábado")) {
                saturdayCheckBox.setSelected(true);
            }
            if (days.contains("Domingo")) {
                sundayCheckBox.setSelected(true);
            }
        }
    }
    
    private void initializeCoachesArrayForComboBox() {
        ArrayList<Employee> employees = new ArrayList<>();
        try {           
            employees = EMPLOYEE_DAO.getEmployeesByPosition();
        } catch (DAOException exception) {
            Logger.getLogger(GymClassRegisterController.class.getName()).log(Level.SEVERE, null, exception);
        }
        coachComboBox.setItems(FXCollections.observableArrayList(employees));
        coachComboBox.setValue(gymClass.getCoach());
    }
    
    private void initializeCapacityArrayForComboBox() {
        ArrayList<Integer> capacity = new ArrayList<>();
        
        for (int i = 1; i <= 50; i++) {
            capacity.add(i);
        }
        capacityComboBox.setItems(FXCollections.observableArrayList(capacity));
        capacityComboBox.setValue(gymClass.getCapacity());
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
        
    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR -> MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/EmployeeMain");
                case FATAL -> MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Logger.getLogger(GymClassRegisterController.class.getName()).log(Level.SEVERE, null, ioException);
        }
    }
    
    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getDialog(new AlertMessage(exception.getMessage(), Status.WARNING));
    }
    
    public void setGymClass(GymClass gymClass) {
        this.gymClass = gymClass;
        initializeTextFields();
        initializeHoursArrayForComboBox();
        initializeMinutesArrayForComboBox();
        initializeCheckBoxes();
        initializeCoachesArrayForComboBox();
        initializeCapacityArrayForComboBox();
    }
    
    public GymClass getGymClass() {
        return gymClass;
    }
}
 
