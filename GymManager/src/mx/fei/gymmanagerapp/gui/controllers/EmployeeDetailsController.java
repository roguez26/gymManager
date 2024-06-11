package mx.fei.gymmanagerapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.MainApp;
import mx.fei.gymmanagerapp.gui.views.AlertMessage;
import mx.fei.gymmanagerapp.gui.views.DialogController;
import mx.fei.gymmanagerapp.logic.employee.Employee;
import mx.fei.gymmanagerapp.logic.employee.EmployeeDAO;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import static mx.fei.gymmanagerapp.logic.implementations.Status.ERROR;
import static mx.fei.gymmanagerapp.logic.implementations.Status.FATAL;

public class EmployeeDetailsController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField maternalSurnameTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField paternalSurnameTextField;
    
    @FXML
    private ComboBox<String> positionComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private TextField telefonoTextField;

    @FXML
    private Button updateButton;
    
    private Employee employee;
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }     

    @FXML
    void backButtonIsPressed(ActionEvent event) throws IOException {
        if(backConfirmation()) {
            MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/EmployeeManagement");
        }
    }

    @FXML
    void cancelButtonIsPressed(ActionEvent event) {
        if(cancelConfirmation()) {
            changeComponentsEditabilityFalse();
            initializeTextFields();
        }
    }

    @FXML
    void deleteButtonIsPressed(ActionEvent event) throws IOException {
        int rowsAffected = -1;
        
        if(deleteConfirmation()) {
            try {
                if (!employeeDAO.trainerIsInAtLeastOneClass(employee)) {
                    rowsAffected = employeeDAO.deleteEmployeeById(employee.getIdEmployee());
                }
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
        }
        if (rowsAffected > 0) {
            DialogController.getInformativeConfirmationDialog("Empleado eliminado", "Empleado eliminado con éxito");
            MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/EmployeeManagement");
        } 
    }

    @FXML
    void saveButtonIsPressed(ActionEvent event) throws IOException {
        int rowsAffected = -1;
        
        if (saveConfirmation()) {
            try {
               rowsAffected = employeeDAO.updateEmployee(initializeEmployee());
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
        }
        if (rowsAffected > 0) {
            DialogController.getInformativeConfirmationDialog("Informacion actualizada", "Los cambios fueron realizados con exito");
            MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/EmployeeManagement");
        }          
    }

    @FXML
    void updateButtonIsPressed(ActionEvent event) {
        changeComponentsEditabilityTrue();
    }
    
    private boolean wasUpdatedConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Informacion actualizada", "Los cambios fueron realizados con exito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }    
    
    private boolean backConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog(
                "Regresar a la ventana gestionar empleados", "¿Desea regresar a la ventana gestionar empleados?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean deleteConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Eliminar empleado", "¿Desea eliminar al empleado?");
        return (response.get() == DialogController.BUTTON_YES);         
    }    
    
    private boolean cancelConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Cancelar edicion", "¿Desea deshacer los cambios y regresar?");
        return (response.get() == DialogController.BUTTON_YES);        
    }   
    
    private boolean saveConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cambios", "¿Desea realizar los cambios?");
        return (response.get() == DialogController.BUTTON_YES); 
    }
    
    private void changeComponentsEditabilityTrue() {
        updateButton.setVisible(false);
        deleteButton.setVisible(false);
        backButton.setVisible(false);
        cancelButton.setVisible(true);
        cancelButton.setDisable(false);
        saveButton.setVisible(true);
        saveButton.setDisable(false);
        
        nombreTextField.setEditable(true);
        paternalSurnameTextField.setEditable(true);
        maternalSurnameTextField.setEditable(true);
        emailTextField.setEditable(true);
        telefonoTextField.setEditable(true);
        positionComboBox.setDisable(false);
    }    
    
    private void changeComponentsEditabilityFalse() {
        updateButton.setVisible(true);
        deleteButton.setVisible(true);
        backButton.setVisible(true);
        cancelButton.setVisible(false);
        cancelButton.setDisable(true);
        saveButton.setVisible(false);
        saveButton.setDisable(true);
        
        nombreTextField.setEditable(false);
        paternalSurnameTextField.setEditable(false);
        maternalSurnameTextField.setEditable(false);
        emailTextField.setEditable(false);
        telefonoTextField.setEditable(false);
        positionComboBox.setDisable(true);
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
    
    private Employee initializeEmployee() {
        Employee newEmployeeInformation = new Employee();
        
        newEmployeeInformation.setIdEmployee(employee.getIdEmployee());
        newEmployeeInformation.setName(nombreTextField.getText());
        newEmployeeInformation.setPaternalSurname(paternalSurnameTextField.getText());
        newEmployeeInformation.setMaternalSurname(maternalSurnameTextField.getText());
        newEmployeeInformation.setEmail(emailTextField.getText());
        newEmployeeInformation.setPhoneNumber(telefonoTextField.getText());  
        newEmployeeInformation.setPosition(positionComboBox.getValue());
        return newEmployeeInformation;        
    }
    
    public void initializeTextFields(Employee employee) {
        nombreTextField.setText(employee.getName());
        paternalSurnameTextField.setText(employee.getPaternalSurname());
        maternalSurnameTextField.setText(employee.getMaternalSurname());
        emailTextField.setText(employee.getEmail());
        telefonoTextField.setText(employee.getPhoneNumber());     
        positionComboBox.setValue(employee.getPosition());
    }
    
    public void initializeTextFields() {
        nombreTextField.setText(employee.getName());
        paternalSurnameTextField.setText(employee.getPaternalSurname());
        maternalSurnameTextField.setText(employee.getMaternalSurname());
        emailTextField.setText(employee.getEmail());
        telefonoTextField.setText(employee.getPhoneNumber());     
        positionComboBox.setValue(employee.getPosition());
    }  
    
    private void initializePositionsComboBox(Employee employee) {
        ArrayList<String> positions = new ArrayList<>();
        positions.add("Entrenador");
        positions.add("Gerente");
        positions.add("Recepcionista");
        positionComboBox.setItems(FXCollections.observableArrayList(positions));
        positionComboBox.setValue(employee.getPosition());
    }    
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
        initializeTextFields(employee);
        initializePositionsComboBox(employee);
    }
    
    public Employee getEmployee() {
        return employee;
    }
}
