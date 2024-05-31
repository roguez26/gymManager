package mx.fei.gymmanagerapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import main.MainApp;
import mx.fei.gymmanagerapp.gui.views.DialogController;
import mx.fei.gymmanagerapp.logic.employee.Employee;
import mx.fei.gymmanagerapp.logic.employee.EmployeeDAO;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;

public class EmployeeManagementController implements Initializable {

    @FXML
    private Button backButton;
    
    @FXML
    private Button registerButton;

    @FXML
    private TableColumn<Employee, String> emailTableColumn;

    @FXML
    private TableView<Employee> employeesTableView;

    @FXML
    private TableColumn<Employee, String> maternalSurnameTableColumn;

    @FXML
    private TableColumn<Employee, String> nameTableColumn;

    @FXML
    private TableColumn<Employee, String> paternalSurnameTableColumn;

    @FXML
    private TableColumn<Employee, String> phoneNumberTableColumn;

    @FXML
    private TableColumn<Employee, String> positionTableColumn;
    
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        paternalSurnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("paternalSurname"));
        maternalSurnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("maternalSurname"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        positionTableColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        employeesTableView.getItems().addAll(initializeEmployeesArray());
        
        employeesTableView.setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (!row.isEmpty())) {
                    Employee rowData = row.getItem();
                    try {
                        handleDoubleClick(rowData);
                    } catch (IOException exception) {
                        Logger.getLogger(EmployeeManagementController.class.getName()).log(Level.SEVERE, null, exception);
                    }
                }
            });
            return row;
        });
    }    
    
    @FXML
    void backButtonIsPressed(ActionEvent event) throws IOException{
        if(backConfirmation()) {
            MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/EmployeeMain");   
        }
    }
    
    @FXML
    void registerButtonIsPressed(ActionEvent event) throws IOException {
        MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/EmployeeRegister");
    }    
    
    private boolean backConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Regresar al menu principal", "¿Desea regresar al menu principal?");
        return (response.get() == DialogController.BUTTON_YES);
    }    
    
    private void handleDoubleClick(Employee employee) throws IOException {
        if(employee != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/gymmanagerapp/gui/views/EmployeeDetails.fxml"));
            MainApp.changeView(fxmlLoader);
            EmployeeDetailsController employeeDetailsController = fxmlLoader.getController();
            employeeDetailsController.setEmployee(employee);
        }
    }
    
    private ArrayList<Employee> initializeEmployeesArray() {
        ArrayList<Employee> employees = new ArrayList<>();
        try {
            employees = employeeDAO.getEmployees();
        } catch (DAOException exception) {
            java.util.logging.Logger.getLogger(EmployeeManagementController.class.getName()).log(Level.SEVERE, null, exception);
        }
        return employees;
    }
    
}
