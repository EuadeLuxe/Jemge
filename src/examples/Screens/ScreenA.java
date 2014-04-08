package examples.Screens;

import com.badlogic.gdx.graphics.Color;
import com.jemge.core.JScreen;

public class ScreenA extends JScreen {

	@Override
	public void render(float v) {

	}

	@Override
	public void show() {
		super.show();
		getRenderer2D().getBackground().setColor(Color.RED);
	}

	@Override
	public void hide() {

	}
}
