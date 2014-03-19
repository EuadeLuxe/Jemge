package examples.Box2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.jemge.box2d.Physics2D;
import com.jemge.box2d.PolygonObject;
import com.jemge.core.JAppLWJGL;
import com.jemge.core.JConfig;
import com.jemge.core.JGame;
import com.jemge.core.Jemge;
import com.jemge.input.InputManager;
import com.jemge.resource.TextureResource;

/**
 * Box2d example:
 * In the bottom of the screen is a platform. You can use a left-click to create boxes that fall physically correct.
 */

public class Box2dTest extends JGame {
    private Box2DDebugRenderer debugRenderer;

    /**
     * Called when the {@link JGame} is first created.
     */

    @Override
    public void create() {
        super.create(); //important call, don't forget it!
        this.debugRenderer = new Box2DDebugRenderer();

        new PolygonObject(0, 0, getCamera().viewportWidth, 30, BodyDef.BodyType.StaticBody); // ground platform

        Jemge.manager.addResource("background", new TextureResource("erdeundgras.jpg")); //Just an example texture
        Jemge.renderer2D.getBackground().setTexture(Jemge.manager.getTexture("background"));
    }

    /**
     * Called when the {@link JGame} should render itself. Update stuff etc.
     */

    @Override
    public void render() {
        super.render();  //never forget to call "super.render"!

        this.debugRenderer.render(Physics2D.getMainWorld(), getCamera().combined);  //Render the box2d world

        if (Gdx.input.justTouched()) { //left click / touch screen?
            new PolygonObject(InputManager.getInputPosition().x, InputManager.getInputPosition().y, 30, 30, BodyDef.BodyType.DynamicBody); // and create a new box
        }
    }

    public static void main(String[] args) {
        JConfig config = new JConfig();

        new JAppLWJGL(new Box2dTest(), config);   //finally create the lwjgl application.
    }
}
