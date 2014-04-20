package examples.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.jemge.core.Jemge;
import com.jemge.input.*;

public class TestKeyListener implements IKeyListener {
	@ListenWhilePressed(key = Input.Keys.A)
	public void clicked() {
		Gdx.app.log("Test App", "Key 'a'!");
	}

	@ListenKeyUp(key = Input.Keys.B)
	public void TheNameOfTheMethodDoesNotMatter() {
		Gdx.app.log("Test App", "Key 'b'!");
	}

	@ListenKeyDown(key = Input.Keys.C)
	public void SelfDestroy() {
		Gdx.app.log("Test App", "No listener anymore");

		Jemge.inputManager.removeKeyListener(this);
	}

    @ListenTouchDown
    public void Clicked(){
        Gdx.app.log("Test App", "Mouse click");
    }
}