package examples.Settings;

import com.jemge.core.JAppLWJGL;
import com.jemge.core.JConfig;
import com.jemge.core.JConfig.Version;
import com.jemge.core.JGame;

public class Demo extends JGame {
	public static void main(String[] args){
        JConfig config = new JConfig();
        
        config.setTitle("Settings");
        config.setGL(Version.GL_20);
        config.loadSettings("User.cfg");
        new JAppLWJGL(new Demo(), config);
    }
}
