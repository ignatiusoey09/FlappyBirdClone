package ignatiusoey09.flappybirdclone;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;

public class PlayerComponent extends Component {
    private Vec2 acceleration = new Vec2(0, 3);
    private boolean isFalling = true;


    @Override
    public void onUpdate(double tpf) {
        if (isFalling) {
            acceleration.y += tpf * 14;
        }

        if (acceleration.y < -5) {
            acceleration.y = -5;
        }
        entity.translate(acceleration);
        isFalling = true;
    }

    public void jump() {
        acceleration.addLocal(0, -10);
    }

    public void stopVertical() {
        isFalling = false;
        acceleration.setZero();
    }
}
