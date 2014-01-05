package examples.Screens;

import com.badlogic.gdx.Gdx;
import com.jemge.core.JAppLWJGL;
import com.jemge.core.JConfig;
import com.jemge.core.JGame;

public class ScreenTest extends JGame {
    private final ScreenA screenA = new ScreenA();
    private final ScreenB screenB = new ScreenB();

    enum MODE {
        SCREEN_A, SCREEN_B
    }

    private MODE mode = MODE.SCREEN_A;

    @Override
    public void create() {
        super.create();
        setScreen(screenA);

    }

    @Override
    public void render() {
        super.render();
        if (Gdx.input.justTouched() && mode == MODE.SCREEN_A) {
            setScreen(screenB);
            mode = MODE.SCREEN_B;
        } else if (Gdx.input.justTouched() && mode == MODE.SCREEN_B) {
            setScreen(screenA);
            mode = MODE.SCREEN_A;
        }
    }


    public static void main(String[] args) {
        JConfig config = new JConfig();

        config.setTitle("JScreen Test");

        new JAppLWJGL(new ScreenTest(), config);
    }
}
