package ignatiusoey09.flappybirdclone;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;

public class PipeBuilderComponent extends Component {
    private boolean built = false;
    private final double DISTANCE = 100.0;
    @Override
    public void onUpdate(double tpf) {
        if (!built) {
            buildPair();
            built = true;
        }
    }

    private void buildPair() {
        double height = FXGL.getAppHeight();
        Rectangle topRect = new Rectangle(60, height/2 - DISTANCE);
        entityBuilder()
                .at(500, 0)
                .viewWithBBox(topRect)
                .collidable()
                .buildAndAttach();

        Rectangle botRect = new Rectangle(60, height/2 - DISTANCE, Paint.valueOf("#eb4034"));
        entityBuilder()
                .at(500, getAppHeight() - DISTANCE)
                .viewWithBBox(botRect)
                .collidable()
                .buildAndAttach();
    }
}
