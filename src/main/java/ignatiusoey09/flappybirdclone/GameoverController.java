package ignatiusoey09.flappybirdclone;

import com.almasb.fxgl.ui.UIController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class GameoverController implements UIController {

    @FXML
    private Text score;

    @Override
    public void init() {
    }

    public Text getScore() {
        return score;
    }
}
