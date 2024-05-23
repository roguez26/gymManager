package main;

import java.io.IOException;
import java.util.function.Consumer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

public class MainApp extends Application {

    private static Scene scene;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/mx/fei/gymmanagerapp/gui/views/LoginMember"));
        stage.setScene(scene);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        scene.setFill(Color.BLUE);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void changeView(FXMLLoader loader) throws IOException {
        Stage currentStage = (Stage) scene.getWindow();
        scene.setRoot(loader.load());
    }

    public static void changeView(String URL) throws IOException {
        Stage currentStage = (Stage) scene.getWindow();
        MainApp.setRoot(URL);
    }
    
    public static void changeView(String fxml, Consumer<Object> controllerSetup) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        Scene scenem = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scenem);
        stage.initModality(Modality.APPLICATION_MODAL);

        if (controllerSetup != null) {
            controllerSetup.accept(fxmlLoader.getController());
        }

        stage.setOnCloseRequest(event -> {
            Stage mainStage = (Stage) scenem.getWindow();
            mainStage.show();
        });

        stage.showAndWait();
    }

    private static Parent loadFXML(String FXML) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(FXML + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
