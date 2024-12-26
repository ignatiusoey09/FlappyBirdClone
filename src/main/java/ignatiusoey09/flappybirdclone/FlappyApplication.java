package ignatiusoey09.flappybirdclone;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
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

import static com.almasb.fxgl.dsl.FXGL.*;

import ignatiusoey09.flappybirdclone.PipeBuilderComponent.PipeComponent;

public class FlappyApplication extends GameApplication {
    private PlayerComponent playerComponent;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(700);
        settings.setHeight(600);
        settings.setTitle("Flappy Bird");
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
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
        onCollision(EntityType.PLAYER, EntityType.WALL, (player, wall) -> {
            playerComponent.stopVertical();
            player.yProperty().setValue(getAppHeight() - wall.getHeight() - player.getHeight());
        });

        //Handle player collide with pipe
        onCollision(EntityType.PLAYER, EntityType.PIPE, (player, pipe) -> {
            playerComponent.stopVertical();

            player.removeComponent(PipeBuilderComponent.class);
            List<Entity> entityList = getGameWorld().getEntitiesByType(EntityType.PIPE);
            for (Entity e:entityList) {
                e.getComponent(PipeComponent.class).stop();
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
        Rectangle rect = new Rectangle(getAppWidth(), getAppHeight(), Color.WHITE);

        Entity bg = entityBuilder()
                .view(rect)
                .with("rect", rect)
                .buildAndAttach();

        bg.xProperty().bind(getGameScene().getViewport().xProperty());
        bg.yProperty().bind(getGameScene().getViewport().yProperty());
    }

    private void initFloorsCeilings() {
        Rectangle rect1 = new Rectangle(getAppWidth(), 30, Color.DARKGREEN);
        Entity ceiling = entityBuilder()
                .type(EntityType.WALL)
                .at(0, 0)
                .view(rect1)
                .buildAndAttach();

        Rectangle rect2 = new Rectangle(getAppWidth(), 30, Color.DARKGREEN);
        Entity floor = entityBuilder()
                .type(EntityType.WALL)
                .at(0, getAppHeight()-30)
                .viewWithBBox(rect2)
                .collidable()
                .buildAndAttach();
    }

    private void initPlayer() {
        Paint paint = Paint.valueOf("#eb4034");
        Node node = new Rectangle(40, 40, paint);

        playerComponent = new PlayerComponent();
        Entity player = entityBuilder()
                .type(EntityType.PLAYER)
                .at(100, 100)
                .view(node)
                .with(playerComponent)
                .with(new PipeBuilderComponent())
                .bbox(new HitBox(BoundingShape.box(50,50)))
                .collidable()
                .build();


        getGameScene().getViewport().setBounds(0,0,Integer.MAX_VALUE, getAppHeight());
        getGameScene().getViewport().bindToEntity(player, getAppWidth()/3, getAppHeight()/3);
        spawnWithScale(player, Duration.seconds(0.2));
    }

    public void showGameOver() {
        BorderPane window = new BorderPane();
        window.setPrefSize(200, 200);

        Text finalScore = new Text("Final Score:");
        Text score = new Text(getip("score").getValue().toString());
        score.setTextAlignment(TextAlignment.CENTER);
        VBox scoreBox = new VBox(finalScore, score);

        window.setBottom(new Text("Press N to retry"));

        window.setTop(scoreBox);

        getGameScene().addUINode(window);

    }

    public static void main(String[] args) {
        launch(args);
    }
}