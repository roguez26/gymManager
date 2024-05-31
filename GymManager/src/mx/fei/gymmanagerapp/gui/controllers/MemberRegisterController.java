package mx.fei.gymmanagerapp.gui.controllers;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.MainApp;
import mx.fei.gymmanagerapp.gui.views.AlertMessage;
import mx.fei.gymmanagerapp.gui.views.DialogController;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import mx.fei.gymmanagerapp.logic.implementations.Status;
import static mx.fei.gymmanagerapp.logic.implementations.Status.ERROR;
import static mx.fei.gymmanagerapp.logic.implementations.Status.FATAL;
import mx.fei.gymmanagerapp.logic.member.IMember;
import mx.fei.gymmanagerapp.logic.member.Member;
import mx.fei.gymmanagerapp.logic.member.MemberDAO;

public class MemberRegisterController implements Initializable {

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
    private PasswordField passwordPasswordField;

    @FXML
    private ComboBox membershipTypeCombobox;

    private final IMember MEMBER_DAO = new MemberDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        membershipTypeCombobox.setItems(FXCollections.observableArrayList(
                "Anual", "Mensual", "Trimestral"
        ));

    }

    @FXML
    private void saveButtonIsPressed(ActionEvent event) {
        try {
            invokeMemberRegister();
            MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/MemberManagement");
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (IOException exception) {
            Logger.getLogger(MemberRegisterController.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    @FXML
    private void cancelButtonIsPressed(ActionEvent event) throws IOException {
        if (confirmCancelation()) {
            MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/MemberManagement");
        }
    }

    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Desea cancelar el registro?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private void invokeMemberRegister() throws DAOException, IOException {
        Member member = initializeMember();
        if (!fieldsAreEmpty()) {
            if (doPayment()) {
                int idMember = MEMBER_DAO.registerMember(member);
                if (idMember > 0) {
                    DialogController.getInformativeConfirmationDialog("Registrado", "Se ha realizado el registro con éxito.");
                    cleanFields();
                }
            } else {
                DialogController.getInformativeConfirmationDialog("Advertencia", "Es necesario realizar el pago para poder registrar al miembro.");
            }

        } else {
            DialogController.getInformativeConfirmationDialog("Campos vacios", "Asegurese de llenar todos los campos con *");
        }
    }

    private boolean doPayment() {
        boolean response = false;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mx/fei/gymmanagerapp/gui/views/DoPayment.fxml"));
            Parent root = loader.load();

            DoPaymentController doPaymentController = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait(); // Espera hasta que la ventana se cierre

            return doPaymentController.getResponse();
        } catch (IOException exception) {
            Logger.getLogger(MemberRegisterController.class.getName()).log(Level.SEVERE, null, exception);
        }

        return response;
    }

    private void cleanFields() {
        nameTextField.setText("");
        paternalSurnameTextField.setText("");
        maternalSurnameTextField.setText("");
        emailTextField.setText("");

        phoneNumberTextField.setText("");
        passwordPasswordField.setText("");
    }

    private boolean fieldsAreEmpty() {
        boolean emptyFieldsCheck = false;
        if (nameTextField.getText().isEmpty()
                || paternalSurnameTextField.getText().isEmpty()
                || emailTextField.getText().isEmpty()
                || phoneNumberTextField.getText().isEmpty()
                || passwordPasswordField.getText().isEmpty()) {
            emptyFieldsCheck = true;
        }

        return emptyFieldsCheck;
    }

    private Member initializeMember() throws DAOException {
        Member member = new Member();
        member.setName(nameTextField.getText());
        member.setPaternalSurname(paternalSurnameTextField.getText());
        member.setMaternalSurname(maternalSurnameTextField.getText());
        member.setEmail(emailTextField.getText());

        member.setPhoneNumber(phoneNumberTextField.getText());
        member.setPassword(passwordPasswordField.getText());
        return member;
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/EmployeeMain");
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Logger.getLogger(MemberRegisterController.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getDialog(new AlertMessage(exception.getMessage(), Status.WARNING));
    }

}
