public class UnitVector2 extends Vector2{
    public UnitVector2(double x,double y){
        super(x,y);
        if(x != 0 || y != 0){
            normalize();
        } else{
            this.setX(0);
            this.setY(0);
        }
    }
}
