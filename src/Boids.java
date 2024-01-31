import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Boids {
    private double x;
    private double y;
    private double radius;
    private double tooCloseRadius;
    private UnitVector2 direction;
    private double viewAngleDegrees;
    private Rectangle display;
    private Rectangle2D boundingBox;
    public Boids(double x, double y, double radius, double tooCloseRadius, double viewAngleDegrees){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.tooCloseRadius = tooCloseRadius;
        this.viewAngleDegrees = viewAngleDegrees;
        this.direction = new UnitVector2( -1 + 2 * Math.random(),-1 + 2 * Math.random());
        this.display = new Rectangle(x - 2,y - 2,4,4);
        this.display.setFill(Color.WHITE);
    }
    public void updateDirection(double deltaTimeSeconds, ArrayList<Boids> boidsArrayList, ArrayList<Obstacle> obstacleArrayList){
        if (!this.getBoidsThatAreNear(boidsArrayList,radius).equals(new ArrayList<>())) {
            // Separation
            this.setDirection(this.getDirection().add(getMeanVectorBetweenBoids(this.getBoidsThatAreNear(boidsArrayList,tooCloseRadius)).multiply(1.0/500)).toUnit());
            // Alignment
            this.setDirection(this.getDirection().add(getMeanDirectionOfBoids(this.getBoidsThatAreNear(boidsArrayList,radius)).multiply(1.0/40)).toUnit());
            // Cohesion
            this.setDirection(this.getDirection().add((new Vector2(x,y)).subtract(getCenterOfMassOfBoids(this.getBoidsThatAreNear(boidsArrayList,radius))).getInvertedVector().multiply(1.0/400)).toUnit());
            // N.B. : the .multiply(number) are here to reduce the impact of other boids on the direction of the boid, this avoids the boids making 180° turns
        }
        // Avoid obstacles :
        for (Obstacle obstacle : obstacleArrayList){
            Vector2 vBetween = (new Vector2(obstacle.getCenterX(),obstacle.getCenterY())).subtract(new Vector2(this.getX(),this.getY()));
            if (vBetween.getNorm() < obstacle.getRadius() + 20) {
                this.setDirection(this.getDirection().subtract(vBetween.toUnit().multiply(1.0/2)).toUnit());
            }
        }
    }
    public void updatePosition(double deltaTimeSeconds){
        // TODO : write better code for avoiding borders ?
        keepInsideBoundingBox();

        this.x += this.direction.getX() * 5 * deltaTimeSeconds * 35;
        this.y += this.direction.getY() * 5 * deltaTimeSeconds * 35;
    }
    public void render(Camera camera, double zoom){
        this.display.setX((this.x - 5 - camera.getX() - camera.getWindow().getWidth()/2) /zoom + camera.getWindow().getWidth()/2);
        this.display.setY((this.y - 5 - camera.getY() - camera.getWindow().getHeight()/2) /zoom + camera.getWindow().getHeight()/2);
        this.display.setWidth(4 / zoom);
        this.display.setHeight(4 / zoom);
    }
    private void keepInsideBoundingBox(){
        if (this.x > (boundingBox.getMaxX() - 50)){
            this.setDirection(this.getDirection().add(new Vector2(-1,0).multiply(1.0/5)).toUnit());
        } else if(this.x < boundingBox.getMinX() + 50){
            this.setDirection(this.getDirection().add(new Vector2(1,0).multiply(1.0/5)).toUnit());
        }
        if (this.y > (boundingBox.getMaxY() - 50)){
            this.setDirection(this.getDirection().add(new Vector2(0,-1).multiply(1.0/5)).toUnit());
        } else if(this.y < boundingBox.getMinY() + 50){
            this.setDirection(this.getDirection().add(new Vector2(0,1).multiply(1.0/5)).toUnit());
        }
    }
    public Rectangle getDisplay(){
        return display;
    }
    public ArrayList<Boids> getBoidsThatAreNear(ArrayList<Boids> boidsArrayList, double radius){
        ArrayList<Boids> boidsThatAreNear = new ArrayList<>();
        for (Boids boid : boidsArrayList){
            Vector2 vBetween = (new Vector2(x,y)).subtract(new Vector2(boid.getX(),boid.getY()));
            if (vBetween.getNorm() < radius && this.getDirection().getAngleDegrees(vBetween.toUnit()) < viewAngleDegrees && boid != this){
                boidsThatAreNear.add(boid);
            }
        }
        return boidsThatAreNear;
    }
    public Vector2 getVectorBetweenBoids(Boids boid){
        return new Vector2(x - boid.getX(),y - boid.getY());
    }
    public Vector2 getMeanVectorBetweenBoids(ArrayList<Boids> boidsThatAreNear){
        Vector2 meanVector = new Vector2();
        for (Boids boid : boidsThatAreNear){
            meanVector = meanVector.add(getVectorBetweenBoids(boid));
        }
        return meanVector;
    }
    public UnitVector2 getMeanDirectionOfBoids(ArrayList<Boids> boidsThatAreNear){
        Vector2 meanDirection = new Vector2();
        for (Boids boid : boidsThatAreNear){
            meanDirection = meanDirection.add(boid.getDirection());
        }
        return meanDirection.toUnit();
    }
    public Vector2 getCenterOfMassOfBoids(ArrayList<Boids> boidsThatAreNear){
        Vector2 centerOfMass = new Vector2();
        for (Boids boid : boidsThatAreNear){
            centerOfMass = centerOfMass.add(new Vector2(boid.getX()/boidsThatAreNear.size(), boid.getY()/boidsThatAreNear.size()));
        }

        return centerOfMass;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

    public UnitVector2 getDirection() {
        return direction;
    }
    public void setDirection(UnitVector2 direction){
        this.direction = direction;
    }
    public void setBoundingBox(Rectangle2D boundingBox){
        this.boundingBox = boundingBox;
    }
    public Rectangle2D getBoundingBox(){
        return boundingBox;
    }
}
