package ignatiusoey09.flappybirdclone;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FlappyApplication extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(700);
        settings.setHeight(600);
        settings.setTitle("Flappy Bird");
    }

    @Override
    public void initUi() {
        
    }

    @Override
    public void initGame() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}