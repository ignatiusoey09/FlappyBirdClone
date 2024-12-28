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
import javafx.geometry.HorizontalDirection;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.almasb.fxgl.dsl.FXGL.*;

import ignatiusoey09.flappybirdclone.PipeBuilderComponent.PipeComponent;

public class FlappyApplication extends GameApplication {
    private PlayerComponent playerComponent;

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
        Text uiScore = new Text("");
        uiScore.textProperty().bind(getip("score").asString());
        FXGL.addUINode(uiScore, getAppWidth()/2.0, getAppHeight()/5.0);
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
            BorderPane window = new BorderPane();
            window.setPrefSize(200, 200);

            Text finalScore = new Text("Final Score:");
            Text score = new Text(getip("score").getValue().toString());
            score.setTextAlignment(TextAlignment.CENTER);
            VBox scoreBox = new VBox(finalScore, score);

            window.setBottom(new Text("Press N to retry"));

            window.setTop(scoreBox);

            getGameScene().addUINode(window);
            getbp("gameOver").setValue(true);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}