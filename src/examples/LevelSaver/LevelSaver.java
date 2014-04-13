package examples.LevelSaver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.jemge.box2d.CircleObject;
import com.jemge.box2d.PhysicObject;
import com.jemge.box2d.Physics2D;
import com.jemge.box2d.PolygonObject;
import com.jemge.core.JAppLWJGL;
import com.jemge.core.JConfig;
import com.jemge.core.JGame;
import com.jemge.input.InputManager;

/**
 * LevelSaver example: In the bottom of the screen is a platform. You can use a
 * left-click to create boxes that fall physically correct. ...
 */

public class LevelSaver extends JGame {
    private Box2DDebugRenderer debugRenderer;

    private static final String levelname = "SaveTest";
    private ArrayList<PhysicObject> physicObjects;
    private int numberofPhysicObjects = 0;

    // TODO make level resetable (if you want to reset, you must delete the
    // .save file)

    /**
     * Called when the {@link JGame} is first created.
     */

    @Override
    public void create() {
        super.create(); // important call, don't forget it!
        this.debugRenderer = new Box2DDebugRenderer();

        new PolygonObject(0, 0, getCamera().viewportWidth, 30,
                BodyDef.BodyType.StaticBody); // ground platform

        this.physicObjects = new ArrayList<PhysicObject>();

        try {
            FileInputStream fis = new FileInputStream(levelname + ".level");
            try {
                ObjectInputStream ois = new ObjectInputStream(fis);
                int max = ois.readInt();
                while (numberofPhysicObjects < max) {
                    PhysicObject tempObejct = (PhysicObject) ois.readObject();
                    if (tempObejct instanceof PolygonObject) {
                        PolygonObject polygonObject = (PolygonObject) tempObejct;
                        tempObejct = new PolygonObject(
                                polygonObject.getPosition().x,
                                polygonObject.getPosition().y,
                                polygonObject.getWidth(),
                                polygonObject.getHeight(),
                                BodyDef.BodyType.DynamicBody);
                    } else if (tempObejct instanceof CircleObject) {
                        CircleObject circleObject = (CircleObject) tempObejct;
                        tempObejct = new CircleObject(
                                circleObject.getPosition().x,
                                circleObject.getPosition().y,
                                circleObject.getRadius(),
                                BodyDef.BodyType.DynamicBody);
                    }
                    this.physicObjects.add(tempObejct);
                    this.numberofPhysicObjects++;
                }
                ois.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // Level-Datei existiert noch nicht.
        }
    }

    /**
     * Called when the {@link JGame} should render itself. Update stuff etc.
     */

    @Override
    public void render() {
        super.render(); // never forget to call "super.render"!

        this.debugRenderer.render(Physics2D.getMainWorld(),
                getCamera().combined); // Render the box2d world

        if (Gdx.input.justTouched()) { // left click / touch screen?
            PolygonObject tempObejct = new PolygonObject(
                    InputManager.getInputPosition().x,
                    InputManager.getInputPosition().y, 30, 30,
                    BodyDef.BodyType.DynamicBody); // and create a new box
            this.physicObjects.add(tempObejct);
            this.numberofPhysicObjects++;
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        // savePosition important!
        for (PhysicObject physicObject : physicObjects) {
            if (physicObject instanceof PolygonObject) {
                ((PolygonObject) physicObject).savePosition();
            }
            if (physicObject instanceof CircleObject) {
                ((CircleObject) physicObject).savePosition();
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(levelname + ".level");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeInt(numberofPhysicObjects);
            for (PhysicObject physicObject : physicObjects) {
                oos.writeObject(physicObject);
            }
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JConfig config = new JConfig();
        config.setTitle(levelname);

        new JAppLWJGL(new LevelSaver(), config); // finally create the lwjgl
        // application.
    }
}