package ignatiusoey09.flappybirdclone;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PipeBuilderComponent extends Component {

    /**
     * Handles Pipe entity translation to the left
     */
    public class PipeComponent extends Component {
        private final Vec2 velocity = new Vec2(-5.0, 0);
        @Override
        public void onUpdate(double tpf) {
            entity.translate(velocity);
        }

        public void stop() {
            velocity.setZero();
        }
    }

    private final Random random = new Random();
    private final double HEIGHT = getAppHeight();
    private final double PIPE_DISTANCE = 140.0; //the distance between pipes

    private final double BUILD_INTERVAL = 1.5; //time interval between pipe spawns
    private double prevPipeSpawn = getGameTimer().getNow();

    @Override
    public void onUpdate(double tpf) {
        if (getGameTimer().getNow() - prevPipeSpawn >= BUILD_INTERVAL) {
            buildPair();
            prevPipeSpawn = getGameTimer().getNow();
        }
    }

    private void buildPair() {
        //generate random gap height, with buffer for floor and ceiling
        double gapHeight = random.nextDouble(HEIGHT - PIPE_DISTANCE - 80) + 40;

        //retrieve pipe texture and extend by 200px
        Texture pipeTexture = texture("pipe.png");
        Texture pipeExtension = pipeTexture.subTexture(new Rectangle2D(0, 30,52, 230)); //200px sample of pipe, excluding head
        pipeTexture = pipeTexture.superTexture(pipeExtension, VerticalDirection.DOWN);

        //flip pipe texture for top pipes
        Texture pipeFlipped = pipeTexture.copy();
        pipeFlipped.setRotate(180.0);

        HitBox topHitbox = new HitBox(BoundingShape.box(52, 520));
        HitBox bottomHitbox = new HitBox(BoundingShape.box(52,520));

        entityBuilder()
                .type(EntityType.PIPE)
                .at(getAppWidth(), -520 + gapHeight)
                .with(new PipeComponent())
                .view(pipeFlipped)
                .bbox(topHitbox)
                .collidable()
                .buildAndAttach();

        entityBuilder()
                .type(EntityType.PIPE)
                .with(new PipeComponent())
                .at(getAppWidth(), gapHeight + PIPE_DISTANCE)
                .view(pipeTexture)
                .bbox(bottomHitbox)
                .collidable()
                .buildAndAttach();

        Rectangle scoreRect = new Rectangle(60, PIPE_DISTANCE, Color.TRANSPARENT);
        entityBuilder()
                .type(EntityType.SCOREBOX)
                .at(getAppWidth(), gapHeight)
                .with(new PipeComponent())
                .viewWithBBox(scoreRect)
                .collidable()
                .buildAndAttach();
    }
}
