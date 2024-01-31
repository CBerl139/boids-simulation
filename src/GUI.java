import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("BOIDS");

        Group root = new Group();
        Pane pane = new Pane(root);

        GameScene gameScene = new GameScene(this,pane,1500,700,true);
        stage.setScene(gameScene);

        stage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}
