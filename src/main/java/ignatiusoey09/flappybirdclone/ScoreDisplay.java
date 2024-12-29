package ignatiusoey09.flappybirdclone;

import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;

public class ScoreDisplay extends HBox {
    private Texture[] numberTextures;

    public ScoreDisplay() {
        loadNumbers();
        this.getChildren().add(numberTextures[0]); //init texture with 0
    }

    private void loadNumbers() {
        String format = "numbers/%d.png";
        Texture[] numbers = new Texture[10];
        for (int i = 0; i < 10; i++) {
            String url = String.format(format, i);
            Texture number = getAssetLoader().loadTexture(url);
            numbers[i] = number;
        }

        numberTextures = numbers;
    }

    public void updateScore(int score) {
        this.getChildren().clear();

        String scoreStr = String.valueOf(score);
        for (int i = 0; i < scoreStr.length(); i++) {
            int digit = Character.getNumericValue(scoreStr.charAt(i));
            this.getChildren().add(numberTextures[digit].copy());
        }
        this.alignmentProperty().setValue(Pos.CENTER);
    }
}