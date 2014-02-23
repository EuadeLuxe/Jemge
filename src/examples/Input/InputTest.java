package examples.Input;

import com.jemge.core.JAppLWJGL;
import com.jemge.core.JConfig;
import com.jemge.core.JGame;
import com.jemge.core.Jemge;

public class InputTest extends JGame {

    @Override
    public void create(){
        super.create();

        Jemge.inputManager.addKeyListener(new TestKeyListener());
    }

    public static void main(String[] args) {
        JConfig config = new JConfig();
        config.setTitle("InputTest");

        new JAppLWJGL(new InputTest(), config);
    }
}
