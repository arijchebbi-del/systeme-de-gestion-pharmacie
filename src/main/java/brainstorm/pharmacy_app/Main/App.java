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

            FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("/FXML/SuppliersControl.fxml"));
            Parent root = loader.load();

            // --- CONFIGURATION DU ZOOM (REDUCTION A 80%) ---
            double scaleFactor = 0.8;
            root.setScaleX(scaleFactor);
            root.setScaleY(scaleFactor);

            // Pour éviter que le contenu ne flotte au milieu avec du vide autour,
            // on utilise un StackPane comme conteneur pour forcer le cadrage.
            StackPane container = new StackPane(root);

            // On calcule la nouvelle taille de la fenêtre basée sur le zoom
            double width = root.getBoundsInLocal().getWidth() * scaleFactor;
            double height = root.getBoundsInLocal().getHeight() * scaleFactor;

            // On crée la scène avec les dimensions réduites
            Scene scene = new Scene(container, width, height);
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
            stage.setTitle("MyPharma - Login");

            // Supprime le décalage en centrant la fenêtre sur l'écran
            stage.show();
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
