package examples.J2DTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.jemge.box2d.Physics2D;
import com.jemge.box2d.PolygonObject;
import com.jemge.core.*;
import com.jemge.input.InputManager;
import com.jemge.resource.TextureResource;

public class RenderToTexture extends JGame{

    /**
     * Called when the {@link JGame} is first created.
     */

    @Override
    public void create() {
        super.create(); // important call, don't forget it!

        new PolygonObject(0, 0, getCamera().viewportWidth, 30,
                BodyDef.BodyType.StaticBody); // ground platform

        Jemge.manager.addResource("background", new TextureResource(
                "assets/erdeundgras.jpg")); // Just an example texture
        Jemge.renderer2D.getBackground().setTexture(
                Jemge.manager.getTexture("background"));
       Jemge.renderer2D.getBackground().setTexture(
                Jemge.renderer2D.renderFrameBuffer().getColorBufferTexture()
        );
    }

    /**
     * Called when the {@link JGame} should render itself. Update stuff etc.
     */

    @Override
    public void render() {
        super.render(); // never forget to call "super.render"!

        if (Gdx.input.justTouched()) { // left click / touch screen?
            new PolygonObject(InputManager.getInputPosition().x,
                    InputManager.getInputPosition().y, 30, 30,
                    BodyDef.BodyType.DynamicBody); // and create a new box
        }
    }

    public static void main(String[] args) {
        JConfig config = new JConfig();
        EngineConfiguration.debugPhysic = true;

        new JAppLWJGL(new RenderToTexture(), config); // finally create the lwjgl
        // application.
    }
}
