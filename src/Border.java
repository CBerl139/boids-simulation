import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Border {
    private Rectangle2D border;
    private Rectangle borderDisplay;
    public Border(Rectangle2D border, double width, Color color){
        this.border = border;

        borderDisplay = new Rectangle(border.getMinX(),border.getMinY(),border.getWidth(),border.getHeight());
        borderDisplay.setStrokeWidth(width);
        borderDisplay.setStroke(color);
    }
    public void update(double deltaTimeSeconds){

    }
    public void render(Camera camera, double zoom){
        this.borderDisplay.setX((this.border.getMinX() - camera.getX() - camera.getWindow().getWidth()/2) /zoom + camera.getWindow().getWidth()/2);
        this.borderDisplay.setY((this.border.getMinY() - camera.getY() - camera.getWindow().getHeight()/2) /zoom + camera.getWindow().getHeight()/2);
        borderDisplay.setWidth(border.getWidth() / (zoom));
        borderDisplay.setHeight(border.getHeight() / (zoom));
    }
    public Rectangle getBorderDisplay(){
        return borderDisplay;
    }
    public Rectangle2D getBorder(){
        return border;
    }
}
