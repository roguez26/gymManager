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
import javafx.scene.control.PasswordField;
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
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button showButton;
    @FXML
    private PasswordField passwordPasswordField;

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
    
    @FXML
    private Button editButton;
    
    private String passwordForShow;
    
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
    
    @FXML
    void cancelButtonIsPressed(ActionEvent event) {
        if (cancelConfirmation()) {
            changeComponentsEditabilityFalse();
            initializeTextFields();
        }
    }
    
    private boolean cancelConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Cancelar edicion", "¿Desea deshacer los cambios y regresar?");
        return (response.get() == DialogController.BUTTON_YES);
    }
    
    @FXML
    void editButtonIsPressed(ActionEvent event) {
        changeComponentsEditabilityTrue();
    }
    
    @FXML
    void saveButtonIsPressed(ActionEvent event) throws IOException {
        int rowsAffected = -1;

        if (saveConfirmation()) {
            try {
                MEMBER_DAO.checkDataDuplication(initializeMember());
                rowsAffected = MEMBER_DAO.updateMember(initializeMember());
                changeComponentsEditabilityFalse();
            } catch (DAOException exception) {
                handleDAOException(exception);
            } catch (IllegalArgumentException exception) {
                DialogController.getInformativeConfirmationDialog("Datos invalidos", exception.getMessage());
            }
        }
        if (rowsAffected > 0) {
            DialogController.getInformativeConfirmationDialog("Informacion actualizada", "Los cambios fueron realizados con exito");
        }
    }
    
    private boolean saveConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cambios", "¿Desea realizar los cambios?");
        return (response.get() == DialogController.BUTTON_YES);
    }
    
    private Member initializeMember() {
        Member newMemberInformation = new Member();

        newMemberInformation.setIdMember(member.getIdMember());
        newMemberInformation.setName(nombreTextField.getText());
        newMemberInformation.setPaternalSurname(paternalSurnameTextField.getText());
        newMemberInformation.setMaternalSurname(maternalSurnameTextField.getText());
        newMemberInformation.setEmail(emailTextField.getText());
        newMemberInformation.setPhoneNumber(telefonoTextField.getText());
        if (!"contraseña".equals(passwordPasswordField.getText())) {
            newMemberInformation.setPassword(passwordPasswordField.getText());
        } else {
            newMemberInformation.encryptPassword(passwordPasswordField.getText());
        }

        return newMemberInformation;
    }
    
    private void changeComponentsEditabilityTrue() {
        editButton.setVisible(false);
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
        showButton.setVisible(true);
        passwordPasswordField.setEditable(true);
        deleteButton.setVisible(false);
    }
    
    private void changeComponentsEditabilityFalse() {
        editButton.setVisible(true);
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
        showButton.setVisible(false);
        passwordPasswordField.setEditable(false);
        passwordPasswordField.setText("contraseña");
        deleteButton.setVisible(true);
    }
    
     @FXML
    void showButtonIsPressed() {
        if (passwordPasswordField != null) {
            passwordForShow = passwordPasswordField.getText();
            passwordPasswordField.clear();
            passwordPasswordField.setPromptText(passwordForShow);
        }
    }

    @FXML
    void showButtonIsReleased() {
        passwordPasswordField.setText(passwordForShow);
        passwordPasswordField.setPromptText(null);
    }
}
