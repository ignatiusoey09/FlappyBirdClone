module ignatiusoey09.flappybirdclone {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens ignatiusoey09.flappybirdclone to javafx.fxml;
    exports ignatiusoey09.flappybirdclone;
}