import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Obstacle {
    private double x;
    private double y;
    private double radius;
    private Circle display;
    public Obstacle(double centerX, double centerY,double radius, Color color){
        this.x = centerX;
        this.y = centerY;
        this.radius = radius;
        this.display = new Circle(x,y,radius);
        this.display.setFill(color);
    }
    public void update(double deltaTimeSeconds, ArrayList<Obstacle> obstacleArrayList){

    }
    public void render(Camera camera, double zoom){
        this.display.setCenterX((this.x - camera.getX() - camera.getWindow().getWidth()/2) /zoom + camera.getWindow().getWidth()/2);
        this.display.setCenterY((this.y - camera.getY() - camera.getWindow().getHeight()/2) /zoom + camera.getWindow().getHeight()/2);
        this.display.setRadius(radius / zoom);
    }

    public void setBoundingBox(Rectangle2D boundingBox) {

    }

    public Circle getDisplay(){
        return display;
    }
    public double getCenterX(){
        return x;
    }
    public double getCenterY(){
        return y;
    }
    public double getRadius(){
        return radius;
    }
    public void setCenterX(double centerX){
        this.x = centerX;
    }
    public void setCenterY(double centerY){
        this.y = centerY;
    }

}
