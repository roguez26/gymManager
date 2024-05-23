package mx.fei.gymmanagerapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import main.MainApp;

/**
 *
 * @author ivanr
 */
public class EmployeeMainController implements Initializable {

    @FXML
    private Button classesButton;

    @FXML
    private Button closeSessionButton;

    @FXML
    private Button employeesButton;

    @FXML
    private Button membersButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void classesButtonIsPressed(ActionEvent event) throws IOException {
        MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/GymClassManagement");
    }

    @FXML
    void closeSessionButtonIsPressed(ActionEvent event) throws IOException {
        MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/LoginEmployee");
    }

    @FXML
    void employeesButtonIsPressed(ActionEvent event) throws IOException {
        MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/EmployeeManagement");
    }

    @FXML
    void membersButtonIsPressed(ActionEvent event) throws IOException {
        MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/MemberManagement");
    }

}
