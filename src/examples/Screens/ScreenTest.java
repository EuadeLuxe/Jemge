package examples.Screens;

import com.badlogic.gdx.Gdx;
import com.jemge.core.JAppLWJGL;
import com.jemge.core.JConfig;
import com.jemge.core.JGame;

public class ScreenTest extends JGame {
    enum MODE {
        SCREEN_A, SCREEN_B
    }

    private MODE mode = MODE.SCREEN_A;

    @Override
    public void create() {
        super.create();
        setScreen(new ScreenA());

    }

    @Override
    public void render() {
        super.render();
        if (Gdx.input.justTouched() && mode == MODE.SCREEN_A) {
            setScreen(new ScreenB());
            mode = MODE.SCREEN_B;
        } else if (Gdx.input.justTouched() && mode == MODE.SCREEN_B) {
            setScreen(new ScreenA());
            mode = MODE.SCREEN_A;
        }
        //It's intended that it always produces a new instance. Testing memory usage.
    }


    public static void main(String[] args) {
        JConfig config = new JConfig();

        config.setTitle("JScreen Test");

        new JAppLWJGL(new ScreenTest(), config);
    }
}
