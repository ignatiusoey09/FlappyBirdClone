package ignatiusoey09.flappybirdclone;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PipeBuilderComponent extends Component {
    private class PipeComponent extends Component {
        private Vec2 velocity = new Vec2(-5.0, 0);
        @Override
        public void onUpdate(double tpf) {
            entity.translate(velocity);
        }
    }

    private final double DISTANCE = 100.0; //the distance between pipes
    private final double INTERVAL = 1;
    private double prev = getGameTimer().getNow();

    @Override
    public void onUpdate(double tpf) {
        if (getGameTimer().getNow() - prev >= INTERVAL) {
            buildPair();
            prev = getGameTimer().getNow();
        }
    }

    private void buildPair() {
        double height = FXGL.getAppHeight();
        Rectangle topRect = new Rectangle(60, (height - DISTANCE)/2);
        entityBuilder()
                .type(EntityType.PIPE)
                .at(getAppWidth(), 0)
                .with(new PipeComponent())
                .viewWithBBox(topRect)
                .collidable()
                .buildAndAttach();

        Rectangle botRect = new Rectangle(60, (height - DISTANCE)/2, Paint.valueOf("#eb4034"));
        entityBuilder()
                .type(EntityType.PIPE)
                .at(getAppWidth(), height/2 + DISTANCE)
                .with(new PipeComponent())
                .viewWithBBox(botRect)
                .collidable()
                .buildAndAttach();
    }
}
