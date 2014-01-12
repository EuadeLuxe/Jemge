package examples.Lights;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.jemge.box2d.Physics2D;
import com.jemge.box2d.PolygonObject;
import com.jemge.box2d.box2dLight.Light;
import com.jemge.box2d.box2dLight.PointLight;
import com.jemge.core.JAppLWJGL;
import com.jemge.core.JConfig;
import com.jemge.core.JGame;
import com.jemge.input.InputManager;

/**
 * Box2d example:
 * In the bottom of the screen is a platform. You can use a left-click to create boxes that fall physically correct.
 */

public class LightsTest extends JGame {
    private Box2DDebugRenderer debugRenderer;
    private PointLight mouse_light;

    /**
     * Called when the {@link com.jemge.core.JGame} is first created.
     */

    @Override
    public void create() {
        super.create(); //important call, don't forget it!
        debugRenderer = new Box2DDebugRenderer();

        new PolygonObject(0, 0, getCamera().viewportWidth, 30, BodyDef.BodyType.StaticBody); // ground platform

        mouse_light = new PointLight(Light.Quality.NICE, Color.WHITE, 500, 20, 20);
    }

    /**
     * Called when the {@link com.jemge.core.JGame} should render itself. Update stuff etc.
     */

    @Override
    public void render() {
        super.render();  //never forget to call "super.render"!

        debugRenderer.render(Physics2D.getMainWorld(), getCamera().combined);  //Render the box2d world

        if (Gdx.input.justTouched()) { //left click / touch screen?

           new PolygonObject(InputManager.getInputPosition().x, InputManager.getInputPosition()
                    .y, 30, 30, BodyDef.BodyType.DynamicBody); // and create a new box
        }

        mouse_light.setPosition(InputManager.getInputPosition());
    }

    public static void main(String[] args) {
        JConfig config = new JConfig();

        new JAppLWJGL(new LightsTest(), config);   //finally create the lwjgl application.
    }
}
