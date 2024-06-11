package mx.fei.gymmanagerapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import mx.fei.gymmanagerapp.gui.views.DialogController;
import mx.fei.gymmanagerapp.logic.member.IMember;
import mx.fei.gymmanagerapp.logic.member.Member;
import mx.fei.gymmanagerapp.logic.member.MemberDAO;

/**
 *
 * @author ivanr
 */
public class DoPaymentController implements Initializable {

    @FXML
    private Button payLaterButton;
    
    @FXML
    private Button cancelButton;

    @FXML
    private Button payNowButton;
    private boolean response;
    private String status;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    public String getStatus() {
        return status;
    }

    @FXML
    void payNowButtonIsPressed(ActionEvent event) {
        DialogController.getInformativeConfirmationDialog("Aviso", "El pago ha sido realizado");
        response = true;
        status = "Pagado";
        closeWindow();
    }

    @FXML
    void payLaterButtonIsPressed(ActionEvent event) {
        DialogController.getInformativeConfirmationDialog("Aviso", "Recordar al miembro que tiene 3 días a partir de hoy para realizar el pago");
        response = true;
        status = "Pendiente";
        closeWindow();
    }
    
    @FXML
    void cancelButtonIsPressed(ActionEvent event) {
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
        Stage stage = (Stage) payLaterButton.getScene().getWindow();
        stage.close();
    }

}
