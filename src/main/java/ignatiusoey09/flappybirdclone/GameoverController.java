package ignatiusoey09.flappybirdclone;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.UIController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameoverController implements UIController {

    @FXML
    private HBox score;

    @FXML
    private Text gameoverTitle;

    @Override
    public void init() {

        gameoverTitle.textProperty().setValue("Game Over");
        Font font = FXGL.getAssetLoader().loadFont("PixelFont.ttf").newFont(50);
        gameoverTitle.setFont(font);
        gameoverTitle.setFill(Color.WHITE);

        score.setAlignment(Pos.CENTER);
    }

    public HBox getScore() {
        return this.score;
    }
}
