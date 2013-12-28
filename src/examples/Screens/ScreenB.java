package examples.Screens;

import com.badlogic.gdx.graphics.Color;
import com.jemge.core.JScreen;

public class ScreenB extends JScreen {

    @Override
    public void render(float v) {

    }

    @Override
    public void show(){
        super.show();
        getRenderer2D().getBackground().setColor(Color.BLUE);
    }

    @Override
    public void hide() {

    }
}
