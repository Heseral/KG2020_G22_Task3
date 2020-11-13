package pixels;

public class ScreenPoint {
    private int x;
    private int y;

    public ScreenPoint(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public ScreenPoint(double x, double y) {
        this((int) Math.round(x), (int) Math.round(y));
    }

    @Override
    public String toString() {
        return "(" + getX() + "; " + getY() + ")";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
