package examples.Sound;

import com.badlogic.gdx.Gdx;
import com.jemge.core.JAppLWJGL;
import com.jemge.core.JConfig;
import com.jemge.core.JGame;
import com.jemge.core.Jemge;
import com.jemge.input.InputManager;
import com.jemge.resource.AudioResource;

public class SoundExample extends JGame {
    /**
     * Called when the {@link JGame} is first created.
     */

    @Override
    public void create() {
        super.create(); //important call, don't forget it!

        Jemge.manager.addResource("ping", new AudioResource("assets/test_sound.mp3"));
    }

    /**
     * Called when the {@link JGame} should render itself. Update stuff etc.
     */

    @Override
    public void render() {
        super.render();  //never forget to call "super.render"!

        if (Gdx.input.justTouched()) { //left click / touch screen?
            Jemge.audio.playSoundAtPosition(Jemge.manager.getSound("ping"), InputManager.getInputPosition()); // and create a new box
        }
    }

    public static void main(String[] args) {
        JConfig config = new JConfig();

        new JAppLWJGL(new SoundExample(), config);   //finally create the lwjgl application.
    }
}
