package ignatiusoey09.flappybirdclone;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PipeBuilderComponent extends Component {

    /**
     * Handles Pipe entity translation to the left
     */
    private class PipeComponent extends Component {
        private final Vec2 velocity = new Vec2(-5.0, 0);
        @Override
        public void onUpdate(double tpf) {
            entity.translate(velocity);
        }
    }

    private final Random random = new Random();
    private final double HEIGHT = getAppHeight();
    private final double DISTANCE = 140.0; //the distance between pipes

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
        double gapHeight = random.nextDouble(HEIGHT - DISTANCE - 80) + 40;

        Rectangle topRect = new Rectangle(60, gapHeight);
        entityBuilder()
                .type(EntityType.PIPE)
                .at(getAppWidth(), 0)
                .with(new PipeComponent())
                .viewWithBBox(topRect)
                .collidable()
                .buildAndAttach();

        Rectangle botRect = new Rectangle(60, HEIGHT - DISTANCE - gapHeight, Paint.valueOf("#eb4034"));
        entityBuilder()
                .type(EntityType.PIPE)
                .at(getAppWidth(), gapHeight + DISTANCE)
                .with(new PipeComponent())
                .viewWithBBox(botRect)
                .collidable()
                .buildAndAttach();

        Rectangle scoreRect = new Rectangle(60, DISTANCE, Color.TRANSPARENT);
        entityBuilder()
                .type(EntityType.SCOREBOX)
                .at(getAppWidth(), gapHeight)
                .with(new PipeComponent())
                .viewWithBBox(scoreRect)
                .collidable()
                .buildAndAttach();
    }
}
