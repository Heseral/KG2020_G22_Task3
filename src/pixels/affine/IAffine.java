package pixels.affine;

import pixels.ScreenPoint;

public interface IAffine {
    ScreenPoint transform(ScreenPoint what, double[][] matrix);
}
