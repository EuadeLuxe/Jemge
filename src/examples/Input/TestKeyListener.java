package examples.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.jemge.core.Jemge;
import com.jemge.input.KeyListener;
import com.jemge.input.ListenKeyDown;
import com.jemge.input.ListenKeyUp;

public class TestKeyListener implements KeyListener {

    @ListenKeyDown(key = Input.Keys.A)
    public void clicked(){
        Gdx.app.log("Test App", "Key 'a'!");
    }

    @ListenKeyUp(key = Input.Keys.B)
    public void TheNameOfTheMethodDoesNotMatter(){
        Gdx.app.log("Test App", "Key 'b'!");
    }

    @ListenKeyDown(key = Input.Keys.C)
    public void SelfDestroy(){
        Gdx.app.log("Test App", "No listener anymore");

        Jemge.inputManager.removeKeyListener(this);
    }

}
