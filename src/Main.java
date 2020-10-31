import gui.MainWindow;
import util.GlobalVar;

import javax.swing.*;

public class Main {
    // todo вынести матрицу аффинных преобразований в класс. Преобразовывать точки в этом классе через метод
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setSize(GlobalVar.SCREEN_WIDTH, GlobalVar.SCREEN_HEIGHT);
        mainWindow.setResizable(false);
        mainWindow.setVisible(true);
    }
}
