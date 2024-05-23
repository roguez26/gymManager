package mx.fei.gymmanagerapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author ivanr
 */
public class DoPaymentController implements Initializable {

    @FXML
    private Button paymentIsDoneButton;

    @FXML
    private Button paymentIsNotDoneButton;
    private boolean response;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void paymentIsDoneButtonIsPressed(ActionEvent event) {
        response = true;
        closeWindow();
    }

    @FXML
    void paymentIsNotDoneButtonIsPressed(ActionEvent event) {
        response = false;
        closeWindow();
    }
    
    public void setResponse(boolean response) {
        this.response = response;
    }
    
    public boolean getResponse() {
        return response;
    }
    
    private void closeWindow() {
        Stage stage = (Stage) paymentIsNotDoneButton.getScene().getWindow();
        stage.close();
    }

}
