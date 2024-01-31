import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class GameScene extends Scene {
    private ArrayList<Boids> boidsArrayList;
    private ArrayList<Obstacle> obstacleArrayList;
    private Camera camera;
    private AnimationTimer timer;
    private Border border;
    private double lastUpdateTimeSeconds;
    private boolean cameraIsAttachedToBoid;
    private ArrayList<ArrayList<Boids>> boidsInEachArea;
    public GameScene(GUI parentGUI, Pane pane, double v, double v1, boolean b) {
        super(pane, v, v1, b);
        this.setFill(Color.BLACK);

        camera = new Camera(0,0);
        cameraIsAttachedToBoid = false;
        camera.setWindow(new Rectangle2D(0,0,v,v1));

        border = new Border(new Rectangle2D(0,0,v,v1),2,Color.WHITE);
        pane.getChildren().add(border.getBorderDisplay());

        obstacleArrayList = new ArrayList<>();
        obstacleArrayList.add(new ObstacleMoving(800 / 2.0, 700 / 2.0, 50,new UnitVector2((-1 + Math.random())/2,(-1 + Math.random())/2),Color.BLUE));
        obstacleArrayList.add(new ObstacleMoving(200,200,20,new UnitVector2((-1 + Math.random())/2,(-1 + Math.random())/2),Color.YELLOW));
        obstacleArrayList.add(new ObstacleMoving(650,500,60,new UnitVector2((-1 + Math.random())/2,(-1 + Math.random())/2),Color.GREEN));
        for (Obstacle obstacle : obstacleArrayList){
            obstacle.setBoundingBox(new Rectangle2D(0,0,v,v1));
            pane.getChildren().add(obstacle.getDisplay());
        }

        boidsArrayList = new ArrayList<>();
        for (int i = 0; i < 500  ; i++){
            Boids boid =  new Boids(Math.floor(v * Math.random()),Math.floor(v1 * Math.random()),75,35,270.0 / 2);
            pane.getChildren().add(boid.getDisplay());
            boid.setBoundingBox(border.getBorder());
            boidsArrayList.add(boid);
        }
        boidsArrayList.get(0).getDisplay().setFill(Color.RED);

        //TODO for performance improvement (needed for number of particle larger than 1000) :
        // divide the map in equal sized squares, maybe twice the size of the boids radius
        // add all boids to a Dictionary<Boids,mapDivisionSquare> which tells which void is in which square
        // at every boid.update() call, only check for the boids that are in the same map division as the one we're testing on

        //determine where each boid is at the start :
        //boidsInEachArea = new ArrayList<>();
        //for (int i =0; i < 15 * 7; i++){
        //    boidsInEachArea.add(new ArrayList<>());
        //    Rectangle2D potentialArea = new Rectangle2D(100 * i%15,100*i/7,100,100);
        //    for (Boids boids : boidsArrayList){
        //        if (potentialArea.contains(boids.getX(),boids.getY())) boidsInEachArea.get(i).add(boids);
        //    }
        //}
        //System.out.println(boidsInEachArea.get(0).size());

        this.setOnKeyPressed(keyEvent -> {
            cameraIsAttachedToBoid = !cameraIsAttachedToBoid;
            camera.setAttachedToBoid(cameraIsAttachedToBoid);
        });
        this.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.SECONDARY){
                pushAwayBoids(mouseEvent.getX(),mouseEvent.getY(),150);
            } else{
                gatherInBoids(mouseEvent.getX(),mouseEvent.getY(),150);
            }
        });
        timer = new AnimationTimer() {
            @Override
            public void handle(long time) {
                double deltaTimeSeconds = (double) time / 1000000000 - lastUpdateTimeSeconds;
                if (deltaTimeSeconds > 1.0 / 60){
                    update(deltaTimeSeconds);
                    lastUpdateTimeSeconds = (double) time / 1000000000;
                    render();
                }
            }
        };
        timer.start();
        lastUpdateTimeSeconds = (double) System.nanoTime() / 1000000000;
    }
    public void update(double deltaTimeSeconds){
        camera.update(deltaTimeSeconds,boidsArrayList.get(0),border);
        for (Boids boid : boidsArrayList){
            boid.updateDirection(deltaTimeSeconds,boidsArrayList,obstacleArrayList);
            boid.updatePosition(deltaTimeSeconds);
        }
        for (Obstacle obstacle : obstacleArrayList) obstacle.update(deltaTimeSeconds,obstacleArrayList);
        border.update(deltaTimeSeconds);
    }
    public void render(){
        double zoom = 0.5;
        for (Boids boid : boidsArrayList) boid.render(camera,cameraIsAttachedToBoid?zoom:1);
        for (Obstacle obstacle : obstacleArrayList) obstacle.render(camera,cameraIsAttachedToBoid?zoom:1);

        border.render(camera,cameraIsAttachedToBoid?zoom:1);
    }
    public void pushAwayBoids(double x, double y, double radius){
        for (Boids boid : boidsArrayList){
            Vector2 vBetween = (new Vector2(x,y)).subtract(new Vector2(boid.getX(),boid.getY()));
            if (vBetween.getNorm() < radius){
                boid.setDirection(boid.getDirection().add((new Vector2(x,y)).subtract(boid.getCenterOfMassOfBoids(boid.getBoidsThatAreNear(boidsArrayList,radius))).getInvertedVector().multiply(1.0)).toUnit());
            }
        }
    }
    public void gatherInBoids(double x, double y, double radius){
        for (Boids boid : boidsArrayList){
            Vector2 vBetween = (new Vector2(x,y)).subtract(new Vector2(boid.getX(),boid.getY()));
            if (vBetween.getNorm() < radius){
                boid.setDirection(boid.getDirection().add((new Vector2(x,y)).subtract(boid.getCenterOfMassOfBoids(boid.getBoidsThatAreNear(boidsArrayList,radius))).multiply(1.0)).toUnit());
            }
        }
    }
}
