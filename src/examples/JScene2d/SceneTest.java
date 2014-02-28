package examples.JScene2d;

import com.badlogic.gdx.graphics.FPSLogger;
import com.jemge.core.JAppLWJGL;
import com.jemge.core.JConfig;
import com.jemge.core.JGame;
import com.jemge.core.Jemge;
import com.jemge.j2d.jscene.JSpriteActor;
import com.jemge.resource.TextureResource;

public class SceneTest extends JGame {
    private FPSLogger logger;
    private JSpriteActor actor;

    /**
     * Called when the {@link JGame} is first created.
     */

    @Override
    public void create() {
        super.create(); //important call, don't forget it!

        Jemge.manager.addResource("tile", new TextureResource("erdeundgras.jpg"));

        logger = new FPSLogger();
        actor = (JSpriteActor) Jemge.renderer2D.add(new JSpriteActor(Jemge.manager.getTexture("tile"), 64, 64, 64, 64).setStatic(true));


    }


    @Override
    public void render() {
        super.render();  //never forget to call "super.render"!



        logger.log();
    }

    public static void main(String[] args) {
        JConfig config = new JConfig();

        new JAppLWJGL(new SceneTest(), config);   //finally create the lwjgl application.
    }
}
