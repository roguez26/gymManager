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
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import mx.fei.gymmanagerapp.logic.member.IMember;
import mx.fei.gymmanagerapp.logic.member.Member;
import mx.fei.gymmanagerapp.logic.member.MemberDAO;

public class MemberManagementController implements Initializable {

    @FXML
    private Button backButton;
    
    @FXML
    private Button registerButton;

    @FXML
    private TableColumn<Member, String> emailTableColumn;

    @FXML
    private TableView<Member> membersTableView;

    @FXML
    private TableColumn<Member, String> maternalSurnameTableColumn;

    @FXML
    private TableColumn<Member, String> nameTableColumn;

    @FXML
    private TableColumn<Member, String> paternalSurnameTableColumn;

    @FXML
    private TableColumn<Member, String> phoneNumberTableColumn;
    
    private final IMember MEMBER_DAO = new MemberDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        paternalSurnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("paternalSurname"));
        maternalSurnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("maternalSurname"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        membersTableView.getItems().addAll(initializeMembersArray());
        
        membersTableView.setRowFactory(tv -> {
            TableRow<Member> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (!row.isEmpty())) {
                    Member rowData = row.getItem();
                    try {
                        handleDoubleClick(rowData);
                    } catch (IOException exception) {
                        Logger.getLogger(MemberManagementController.class.getName()).log(Level.SEVERE, null, exception);
                    }
                }
            });
            return row;
        });
    }    
    
    @FXML
    void backButtonIsPressed(ActionEvent event) throws IOException{
        if(backConfirmation()) {
            MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/main");   
        }
    }
    
    @FXML
    void registerButtonIsPressed(ActionEvent event) throws IOException {
        MainApp.changeView("/mx/fei/gymmanagerapp/gui/views/MemberRegister");
    }    
    
    private boolean backConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Regresar al menu principal", "¿Desea regresar al menu principal?");
        return (response.get() == DialogController.BUTTON_YES);
    }    
    
    private void handleDoubleClick(Member member) throws IOException {
        if(member != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/gymmanagerapp/gui/views/MemberDetails.fxml"));
            MainApp.changeView(fxmlLoader);
            MemberDetailsController memberDetailsController = fxmlLoader.getController();
            memberDetailsController.setMember(member);
        }
    }
    
    private ArrayList<Member> initializeMembersArray() {
        ArrayList<Member> members = new ArrayList<>();
        try {
            members = MEMBER_DAO.getMembers();
        } catch (DAOException exception) {
            java.util.logging.Logger.getLogger(EmployeeManagementController.class.getName()).log(Level.SEVERE, null, exception);
        }
        return members;
    }
    
}
