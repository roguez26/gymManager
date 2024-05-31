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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.MainApp;
import mx.fei.gymmanagerapp.gui.views.AlertMessage;
import mx.fei.gymmanagerapp.gui.views.DialogController;
import mx.fei.gymmanagerapp.logic.employee.Employee;
import mx.fei.gymmanagerapp.logic.employee.EmployeeDAO;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import mx.fei.gymmanagerapp.logic.implementations.Status;

public class EmployeeRegisterController implements Initializable {

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField maternalSurnameTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField paternalSurnameTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private ComboBox<String> positionsComboBox;
    
    @FXML
    private TextField passwordTextField;
    
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        positionsComboBox.setItems(FXCollections.observableArrayList(initializePositionsArrayForComboBox()));
    }    

    @FXML
    private void saveButtonIsPressed(ActionEvent event) {
        try {
          invokeEmployeeRegister();  
          MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/EmployeeManagement");
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (IOException exception) {
            Logger.getLogger(EmployeeManagementController.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    @FXML
    private void cancelButtonIsPressed(ActionEvent event) throws IOException {
        if(confirmCancelation()) {
            MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/EmployeeManagement");
        }
    }

    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Desea cancelar el registro?");
        return (response.get() == DialogController.BUTTON_YES);
    }    
    
    private void invokeEmployeeRegister() throws DAOException, IOException {
        Employee employee = initializeEmployee();
        if (!fieldsAreEmpty()) {
            int idEmployee = employeeDAO.registerEmployee(employee);
            if(idEmployee > 0) {
                DialogController.getInformativeConfirmationDialog
                    ("Registrado","Se ha realizado el registro con éxito.");
                cleanFields();
            }
        } else {
            DialogController.getInformativeConfirmationDialog(
                    "Campos vacios","Asegurese de llenar todos los campos con *");
        }        
    }
    
    private void cleanFields() {
        nameTextField.setText("");
        paternalSurnameTextField.setText("");
        maternalSurnameTextField.setText("");
        emailTextField.setText("");
        positionsComboBox.setValue("");
        phoneNumberTextField.setText("");    
        passwordTextField.setText("");
    }

    
    private boolean fieldsAreEmpty() {
        boolean emptyFieldsCheck = false;
        if (nameTextField.getText().isEmpty() || 
                paternalSurnameTextField.getText().isEmpty() ||
                emailTextField.getText().isEmpty() ||
                positionsComboBox.getValue() == null ||
                phoneNumberTextField.getText().isEmpty() ||
                passwordTextField.getText().isEmpty()) {
            emptyFieldsCheck = true;
        }

        return emptyFieldsCheck;
    }    
    
    private Employee initializeEmployee() throws DAOException{
        Employee employee = new Employee();
        employee.setName(nameTextField.getText());
        employee.setPaternalSurname(paternalSurnameTextField.getText());
        employee.setMaternalSurname(maternalSurnameTextField.getText());
        employee.setEmail(emailTextField.getText());
        employee.setPosition(positionsComboBox.getValue());
        employee.setPhoneNumber(phoneNumberTextField.getText());
        employee.setPassword(passwordTextField.getText());
        return employee;
    }    
    
    private ArrayList<String> initializePositionsArrayForComboBox() {
        ArrayList<String> positions = new ArrayList<>();
        positions.add("Entrenador");
        positions.add("Gerente");
        positions.add("Recepcionista");
        return positions;
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