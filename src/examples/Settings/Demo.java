package examples.Settings;
import com.jemge.core.JApp;
import com.jemge.core.JConfig;
import com.jemge.core.JGame;
import com.jemge.core.JConfig.Version;

public class Demo extends JGame {
	public static void main(String[] args){
        JConfig config = new JConfig();
        
        config.setTitle("Settings");
        config.setGL(Version.GL_20);
        config.loadSettings("User.cfg");
        new JApp(new Demo(), config);
    }
}
