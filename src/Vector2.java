public class Vector2 {
    private double x;
    private double y;
    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Vector2(){
        this.x = 0;
        this.y = 0;
    }
    public void normalize(){
        double norm = Math.sqrt(x * x + y * y);
        this.x /= norm;
        this.y /= norm;
    }
    public double dotProduct(Vector2 vector2){
        return this.x * vector2.getX() + this.y * vector2.getY();
    }
    public double getNorm(){
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }
    public Vector2 subtract(Vector2 vector2){
        return new Vector2(this.x - vector2.getX(),this.y - vector2.getY());
    }
    public Vector2 subtract(double x, double y){
        return new Vector2(this.x - x,this.y - y);
    }

    public Vector2 add(Vector2 vector2){
        return new Vector2(this.x + vector2.getX(), this.y + vector2.getY());
    }
    public Vector2 multiply(double norm){
        return new Vector2(this.x * norm,this.y * norm);
    }
    public double getAngleDegrees(Vector2 vector2){
        //TODO
        return 0.5 + this.toUnit().dotProduct(vector2.toUnit()) / 2;

    }
    public Vector2 getInvertedVector(){
        return new Vector2(- this.x, - this.y);
    }
    public UnitVector2 toUnit(){
        return new UnitVector2(this.x,this.y);
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public String toString(){
        return "x: " + x + "y: " + y;
    }
}
