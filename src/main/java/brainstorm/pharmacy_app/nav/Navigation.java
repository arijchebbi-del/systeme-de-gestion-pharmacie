package brainstorm.pharmacy_app.nav;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigation {
    public static void navTo(String path,Node source){
        try {
            // Chargement de la scène Dashboard
            FXMLLoader loader = new FXMLLoader(Navigation.class.getResource(path));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
