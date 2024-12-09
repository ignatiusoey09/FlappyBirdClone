package ignatiusoey09.flappybirdclone;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;

public class PlayerComponent extends Component {
    private Vec2 acceleration = new Vec2(0, 3);
    private boolean isFalling = true;


    @Override
    public void onUpdate(double tpf) {
        if (isFalling) {
            acceleration.y += tpf * 11;
        }

        if (acceleration.y < -5) {
            acceleration.y = -5;
        }
        entity.translate(acceleration);
        isFalling = true;
    }

    public void jump() {
        acceleration.y = 0;
        acceleration.addLocal(0, -5);
    }

    public void stopVertical() {
        isFalling = false;
        acceleration.setZero();
    }
}
