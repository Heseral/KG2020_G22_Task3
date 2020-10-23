

import java.awt.*;

public class DDALineDrawer implements LineDrawer {
    private  PixelDrawer pd;

    public  DDALineDrawer(PixelDrawer pd){
        this.pd= pd;
    }
    @Override
    public void  drawLine (ScreenPoint p1, ScreenPoint p2){
        int x1 = p1.getX(), y1 = p1.getY();
        int x2 = p2.getX(), y2 = p2.getY();
        double dx =x2-x1;
        double dy=y2-y1;

        if(Math.abs(dy)>Math.abs(dx)){
            double reversek= dx/dy;

            if(y1>y2){
                int tmp=y2;y2=y1; y1=tmp;
                tmp =x2;x2=x1;x1=tmp;
            }
            for(int i=y1;i<y2;i++){
                double j =(i-y1)* reversek+x1;
                pd.colorPixel((int) j, i, Color.RED);
            }
        }else {

            double k = dy/dx;
            if(x1>x2) {
                int tmp = y2;
                y2 = y1;
                y1 = tmp;
                tmp = x2;
                x2 = x1;
                x1 = tmp;
            }
                for(int j =x1; j<=x2; j++){
                    double i =(j-x1)* k+y1;
                    pd.colorPixel(j,(int)i,Color.BLUE);

                }
            }

        }
    }

