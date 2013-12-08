package examples.Culling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.jemge.core.JAppLWJGL;
import com.jemge.core.JConfig;
import com.jemge.core.JGame;
import com.jemge.j2d.JSprite;
import com.jemge.j2d.Renderer2D;


public class CullingTest extends JGame {
    private FPSLogger logger;

    /**
     * Called when the {@link JGame} is first created.
     */

    @Override
    public void create() {
        super.create(); //important call, don't forget it!

        Texture texture = new Texture(Gdx.files.internal("erdeundgras.jpg")); //just a placeholder
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        logger = new FPSLogger();

        for(int x = 0; x < 100; x++){
            for(int y = 0; y < 100; y++){
                Renderer2D.getRenderer2D().add(new JSprite(texture, x * 64, y * 64, 64, 64).setStatic(true));

            }
        }

    }


    @Override
    public void render() {
        super.render();  //never forget to call "super.render"!

        getCamera().position.add(1f, 0f, 0);

        logger.log();
    }

    public static void main(String[] args){
        JConfig config = new JConfig();
        config.setGL(JConfig.Version.GL_20);
        config.vSyncEnabled = true;

        new JAppLWJGL(new CullingTest(), config);   //finally create the lwjgl application.
    }
}
