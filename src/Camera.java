import javafx.geometry.Rectangle2D;

public class Camera {
    private double x;
    private double y;
    private boolean isAttachedToBoid;
    private Rectangle2D window;
    public Camera(double x, double y){
        this.x = x;
        this.y = y;
        this.window = new Rectangle2D(0,0,800,700);
    }
    public void update(double deltaTimeSeconds, Boids boids, Border border){
        if (isAttachedToBoid){
            this.x = boids.getX() - border.getBorder().getWidth()/2;
            this.y = boids.getY() - border.getBorder().getHeight()/2;
        }
        else{
            this.x = 0;
            this.y = 0;
        }
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public void setAttachedToBoid(boolean isAttachedToBoid){
        this.isAttachedToBoid = isAttachedToBoid;
    }
    public boolean isAttachedToBoid() {
        return isAttachedToBoid;
    }
    public void setWindow(Rectangle2D window){
        this.window = window;
    }
    public Rectangle2D getWindow(){
        return window;
    }
}

