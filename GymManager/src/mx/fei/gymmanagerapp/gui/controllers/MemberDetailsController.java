package mx.fei.gymmanagerapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import main.MainApp;
import mx.fei.gymmanagerapp.gui.views.AlertMessage;
import mx.fei.gymmanagerapp.gui.views.DialogController;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import static mx.fei.gymmanagerapp.logic.implementations.Status.ERROR;
import static mx.fei.gymmanagerapp.logic.implementations.Status.FATAL;
import mx.fei.gymmanagerapp.logic.member.IMember;
import mx.fei.gymmanagerapp.logic.member.Member;
import mx.fei.gymmanagerapp.logic.member.MemberDAO;

public class MemberDetailsController implements Initializable {

    @FXML
    private Button backButton;


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
    private TextField telefonoTextField;
    
    private Member member;
    private final IMember MEMBER_DAO = new MemberDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }     

    @FXML
    void backButtonIsPressed(ActionEvent event) throws IOException {
        if(backConfirmation()) {
            MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/MemberManagement");
        }
    }

    @FXML
    void deleteButtonIsPressed(ActionEvent event) throws IOException {
        int rowsAffected = -1;
        
        if(deleteConfirmation()) {
            try {
                rowsAffected = MEMBER_DAO.deleteMemberById(member.getIdMember());
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
        }
        if (rowsAffected > 0) {
            DialogController.getInformativeConfirmationDialog("Miembro eliminado", "Miembro eliminado con éxito");
            MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/MemberManagement");
        } else {
            DialogController.getInformativeConfirmationDialog("Miembro no eliminado", "El miembro no se pudo eliminar");
        }
    }
    
    private boolean backConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog(
                "Regresar a la ventana gestionar miembros", "¿Desea regresar a la ventana gestionar miembros?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean deleteConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Eliminar Miembro", "¿Desea eliminar al miebro?");
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
    
    public void initializeTextFields(Member member) {
        nombreTextField.setText(member.getName());
        paternalSurnameTextField.setText(member.getPaternalSurname());
        maternalSurnameTextField.setText(member.getMaternalSurname());
        emailTextField.setText(member.getEmail());
        telefonoTextField.setText(member.getPhoneNumber());     
    }
    
    public void initializeTextFields() {
        nombreTextField.setText(member.getName());
        paternalSurnameTextField.setText(member.getPaternalSurname());
        maternalSurnameTextField.setText(member.getMaternalSurname());
        emailTextField.setText(member.getEmail());
        telefonoTextField.setText(member.getPhoneNumber());     
    }  
    
    public void setMember(Member member) {
        this.member = member;
        initializeTextFields(member);
    }
    
    public Member getMember() {
        return member;
    }
}
