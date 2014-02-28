package examples.Lights;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.jemge.box2d.Physics2D;
import com.jemge.box2d.PolygonObject;
import com.jemge.box2d.box2dLight.Light;
import com.jemge.box2d.box2dLight.PointLight;
import com.jemge.box2d.box2dLight.RayHandler;
import com.jemge.core.JAppLWJGL;
import com.jemge.core.JConfig;
import com.jemge.core.JGame;
import com.jemge.core.Jemge;
import com.jemge.input.InputManager;
import com.jemge.resource.TextureResource;

public class MultiColorTest extends JGame {
    private PointLight mouse_light;

    /**
     * Called when the {@link com.jemge.core.JGame} is first created.
     */

    @Override
    public void create() {
        super.create(); //important call, don't forget it!

        Jemge.manager.addResource("background", new TextureResource("assets/test.jpg"));
        Jemge.renderer2D.getBackground().setTexture(Jemge.manager.getTexture("background"));

        Jemge.renderer2D.rayHandler.setAmbientLight(0);
        RayHandler.useDiffuseLight(true);

        mouse_light = new PointLight(Light.Quality.NICE, Color.WHITE, 500, 20, 20);
    }

    /**
     * Called when the {@link com.jemge.core.JGame} should render itself. Update stuff etc.
     */

    @Override
    public void render() {
        super.render();  //never forget to call "super.render"!

        mouse_light.setPosition(InputManager.getInputPosition());
    }

    public static void main(String[] args) {
        JConfig config = new JConfig();
        config.setSize(800, 480);

        new JAppLWJGL(new MultiColorTest(), config);   //finally create the lwjgl application.
    }
}
