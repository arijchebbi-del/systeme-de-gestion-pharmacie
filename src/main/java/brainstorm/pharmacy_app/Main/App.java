package brainstorm.pharmacy_app.Main;

import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import io.github.palexdev.materialfx.MFXResourcesLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            CSSFX.start();
            UserAgentBuilder.builder()
                    .themes(JavaFXThemes.MODENA)
                    .themes(MaterialFXStylesheets.forAssemble(true))
                    .build()
                    .setGlobal();

            FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("/FXML/Login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            double scaleX = 0.9;
            double scaleY = 0.9;
            scene.getRoot().setScaleX(scaleX);
            scene.getRoot().setScaleY(scaleY);

            stage.setScene(scene);
            stage.setTitle("MyPharma");

            // Supprime le décalage en centrant la fenêtre sur l'écran
            stage.show();
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
