import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ObstacleMoving extends Obstacle{
    private Vector2 direction;
    private Rectangle2D boundingBox;
    public ObstacleMoving(double x, double y, double radius, Vector2 direction, Color color) {
        super(x, y, radius, color);
        this.direction = direction;
    }
    @Override
    public void update(double deltaTimeSeconds, ArrayList<Obstacle> obstacleArrayList){
        super.update(deltaTimeSeconds,obstacleArrayList);
        this.setCenterX(this.getCenterX() + direction.getX() * 3 * deltaTimeSeconds * 35);
        this.setCenterY(this.getCenterY() + direction.getY() * 3 * deltaTimeSeconds * 35);

        //TODO : make it bounce against other obstacles
        for (Obstacle obstacle : obstacleArrayList){
            Vector2 vBetween = (new Vector2(this.getCenterX(),this.getCenterY())).subtract(obstacle.getCenterX(), obstacle.getCenterY());
            if(vBetween.getNorm() < this.getRadius() + obstacle.getRadius() && obstacle != this){
                this.setDirection(vBetween.toUnit());
            }
        }

        keepInsideBoundingBox();
    }
    public void keepInsideBoundingBox(){
        if (this.getCenterX() > (boundingBox.getMaxX() - this.getRadius())){
            this.setDirection(new Vector2(-this.getDirection().getX(),this.getDirection().getY()));
        } else if(this.getCenterX() < boundingBox.getMinX() + this.getRadius()){
            this.setDirection(new Vector2(-this.getDirection().getX(),this.getDirection().getY()));
        }
        if (this.getCenterY() > (boundingBox.getMaxY() - this.getRadius())){
            this.setDirection(new Vector2(this.getDirection().getX(),- this.getDirection().getY()));
        } else if(this.getCenterY() < boundingBox.getMinY() + this.getRadius()){
            this.setDirection(new Vector2(this.getDirection().getX(),- this.getDirection().getY()));
        }
    }
    @Override
    public void setBoundingBox(Rectangle2D boundingBox){
        this.boundingBox = boundingBox;
    }
    public Rectangle2D getBoundingBox(){
        return boundingBox;
    }
    public void setDirection(Vector2 direction){
        this.direction = direction;
    }
    public Vector2 getDirection(){
        return direction;
    }
}
