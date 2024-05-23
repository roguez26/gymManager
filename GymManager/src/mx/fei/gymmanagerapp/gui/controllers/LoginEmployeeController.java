package mx.fei.gymmanagerapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.MainApp;
import mx.fei.gymmanagerapp.gui.views.AlertMessage;
import mx.fei.gymmanagerapp.gui.views.DialogController;
import mx.fei.gymmanagerapp.logic.employee.Employee;
import mx.fei.gymmanagerapp.logic.employee.EmployeeDAO;
import mx.fei.gymmanagerapp.logic.employee.IEmployee;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import static mx.fei.gymmanagerapp.logic.implementations.Status.ERROR;
import static mx.fei.gymmanagerapp.logic.implementations.Status.FATAL;

/**
 *
 * @author ivanr
 */
public class LoginEmployeeController implements Initializable {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private Button startSessionButton;
    private static final Logger LOG = Logger.getLogger(EmployeeDAO.class.getName());
    private final IEmployee EMPLOYEE_DAO = new EmployeeDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void startSessionButtonIsPressed(ActionEvent event) {
        Employee employeeForSession = new Employee();
        try {
            employeeForSession.setEmail(emailTextField.getText());
            employeeForSession.setPassword(passwordPasswordField.getText());
            employeeForSession = EMPLOYEE_DAO.authenticateEmployee(emailTextField.getText(), passwordPasswordField.getText());
            if (employeeForSession.getIdEmployee() > 0) {
                DialogController.getInformativeConfirmationDialog("Bienvenido", "Bienvenido " + employeeForSession.getName());
                cleanFields();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/gymmanagerapp/gui/views/EmployeeMain.fxml"));
                MainApp.changeView(fxmlLoader);

            } else {
                DialogController.getInformativeConfirmationDialog("Sin coincidencias", "El correo o contraseña son incorrectos");
            }
        } catch (IllegalArgumentException exception) {
            DialogController.getInvalidDataDialog(exception.getMessage());
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IOException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
        }
    }

    private void cleanFields() {
        emailTextField.setText("");
        passwordPasswordField.setText("");
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/main/MainApp");
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
        }
    }
}
