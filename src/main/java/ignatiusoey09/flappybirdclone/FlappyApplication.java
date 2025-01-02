package ignatiusoey09.flappybirdclone;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.UI;
import javafx.geometry.HorizontalDirection;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.almasb.fxgl.dsl.FXGL.*;

import ignatiusoey09.flappybirdclone.PipeBuilderComponent.PipeComponent;

public class FlappyApplication extends GameApplication {
    private PlayerComponent playerComponent;
    private ScoreDisplay scoreDisplay;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(512);
        settings.setHeight(512);
        settings.setTitle("Flappy Bird");
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("gameOver", false);
    }

    @Override
    protected void initUI() {
        scoreDisplay = new ScoreDisplay();
        FXGL.onIntChange("score", (score) -> {
            scoreDisplay.updateScore(score);
            centerDisplay(scoreDisplay);
        });
        FXGL.addUINode(scoreDisplay);
        centerDisplay(scoreDisplay);
    }

    private void centerDisplay(HBox node) {
        FXGL.runOnce(() -> {
            node.setTranslateX((FXGL.getAppWidth() - node.getLayoutX())/2);
            node.setTranslateY(FXGL.getAppHeight()/5.0);
        }, Duration.millis(1));
    }

    @Override
    protected void initInput() {
        Input input = getInput();
        input.addAction(new UserAction("jump") {
            @Override
            protected void onActionBegin() {
                playerComponent.jump();
            }
        }, KeyCode.SPACE);

        input.addAction(new UserAction("restart") {
            @Override
            protected void onActionBegin() {
                getGameController().startNewGame();
            }
        }, KeyCode.N);
    }

    @Override
    protected void initPhysics() {
        //Handle player collide with pipe or walls
        onCollision(EntityType.PLAYER, EntityType.PIPE, (player, pipe) -> {
            playerComponent.stopVertical();

            player.removeComponent(PipeBuilderComponent.class);
            player.removeComponent(PlayerComponent.class);

            List<Entity> entityList = getGameWorld().getEntitiesByType(EntityType.PIPE);
            for (Entity e:entityList) {
                Optional<PipeComponent> opt = e.getComponentOptional(PipeComponent.class);
                opt.ifPresent(pipeComponent -> pipeComponent.stop());
            }
            getGameScene().removeUINode(scoreDisplay);
            showGameOver();
        });

        onCollisionEnd(EntityType.PLAYER, EntityType.SCOREBOX, (player, box) -> {
            inc("score", 1);
        });
    }

    @Override
    protected void initGame() {
        initBackground();
        initFloorsCeilings();
        initPlayer();
    }

    private void initBackground() {
        Texture backgroundTexture = texture("background.png");
        Texture backgroundCopy = backgroundTexture.copy();
        Texture backgroundExtended = backgroundTexture.superTexture(backgroundCopy,
                HorizontalDirection.RIGHT);

        Entity bg = entityBuilder()
                .view(backgroundExtended)
                .buildAndAttach();

        bg.xProperty().bind(getGameScene().getViewport().xProperty());
        bg.yProperty().bind(getGameScene().getViewport().yProperty());
    }

    private void initFloorsCeilings() {
        Rectangle rect1 = new Rectangle(getAppWidth(), 30, Color.DARKGREEN);
        Entity ceiling = entityBuilder()
                .type(EntityType.PIPE)
                .at(0, 0)
                .viewWithBBox(rect1)
                .collidable()
                .buildAndAttach();

        Rectangle rect2 = new Rectangle(getAppWidth(), 30, Color.DARKGREEN);
        Entity floor = entityBuilder()
                .type(EntityType.PIPE)
                .at(0, getAppHeight()-30)
                .viewWithBBox(rect2)
                .collidable()
                .buildAndAttach();
    }

    private void initPlayer() {
        Texture birdTexture = texture("bird.png");

        playerComponent = new PlayerComponent();
        Entity player = entityBuilder()
                .type(EntityType.PLAYER)
                .at(100, 100)
                .view(birdTexture)
                .with(playerComponent)
                .with(new PipeBuilderComponent())
                .bbox(new HitBox(BoundingShape.box(30,30)))
                .collidable()
                .build();


        getGameScene().getViewport().setBounds(0,0,Integer.MAX_VALUE, getAppHeight());
        getGameScene().getViewport().bindToEntity(player, getAppWidth()/3, getAppHeight()/3);
        spawnWithScale(player, Duration.seconds(0.2));
    }

    public void showGameOver() {
        boolean isGameOver = getbp("gameOver").get();
        if (!isGameOver) {
            int score = getip("score").get();

            //instantiate ui controller
            GameoverController gameover = new GameoverController();
            //load fxml asset
            UI gameoverUI = getAssetLoader().loadUI("gameoverScreen.fxml", gameover);
            //inject fxml fields
            gameover.getScore().getChildren().clear();
            gameover.getScore().getChildren().addAll(scoreDisplay.getChildren());

            getGameScene().addUI(gameoverUI);
            getbp("gameOver").setValue(true);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}