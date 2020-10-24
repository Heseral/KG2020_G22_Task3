

public class ScreenConverter {
    private double cornerX, cornerY, realW, realH;
    private int screenW, screenH;

    public ScreenConverter(double cornerX, double cornerY, double realW, double realH, int screenW, int screenH) {
        this.cornerX = cornerX;
        this.cornerY = cornerY;
        this.realW = realW;
        this.realH = realH;
        this.screenW = screenW;
        this.screenH = screenH;
    }

    public ScreenPoint r2s(RealPoint p) {
        int x = (int)((p.getX() - cornerX) * screenW / realW);
        int y = (int)((cornerY - p.getY()) * screenH / realH);
        return new ScreenPoint(x, y);
    }

    public RealPoint s2r(ScreenPoint p) {
        double x = p.getX() * realW / screenW + cornerX;
        double y = cornerY - p.getY() * realH / screenH;
        return new RealPoint(x, y);
    }

    public double getCornerX() {
        return cornerX;
    }

    public void setCornerX(double cornerX) {
        this.cornerX = cornerX;
    }

    public double getCornerY() {
        return cornerY;
    }

    public void setCornerY(double cornerY) {
        this.cornerY = cornerY;
    }

    public double getRealW() {
        return realW;
    }

    public void setRealW(double realW) {
        this.realW = realW;
    }

    public double getRealH() {
        return realH;
    }

    public void setRealH(double realH) {
        this.realH = realH;
    }

    public int getScreenW() {
        return screenW;
    }

    public void setScreenW(int screenW) {
        this.screenW = screenW;
    }

    public int getScreenH() {
        return screenH;
    }

    public void setScreenH(int screenH) {
        this.screenH = screenH;
    }
}
