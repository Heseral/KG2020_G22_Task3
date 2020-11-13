package gui;

import figures.ellipse.Ellipse;
import util.GlobalVar;

import javax.swing.*;
import java.awt.*;
public class MainWindow extends JFrame {

    private final JTextField[] transformationMatrix = new JTextField[] {
            new JTextField("N/A"), // a
            new JTextField("N/A"), // b
            new JTextField("N/A"), // p
            new JTextField("N/A"), // c
            new JTextField("N/A"), // d
            new JTextField("N/A"), // q
            new JTextField("N/A"), // m
            new JTextField("N/A"), // n
            new JTextField("N/A")  // s
    };

    private final JLabel[] transformationMatrixLabels = new JLabel[] {
            new JLabel("a"),
            new JLabel("b"),
            new JLabel("p"),
            new JLabel("c"),
            new JLabel("d"),
            new JLabel("q"),
            new JLabel("m"),
            new JLabel("n"),
            new JLabel("s")
    };

    private Ellipse selectedEllipse = null;

    public MainWindow() throws HeadlessException {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.X_AXIS));
        editPanel.setMaximumSize(new Dimension(GlobalVar.SCREEN_WIDTH, 50));
        editPanel.setMinimumSize(new Dimension(GlobalVar.SCREEN_WIDTH, 50));
        DrawPanel drawPanel = new DrawPanel(this);

        // [a b p] a, b, c, d - масштаб, сдвиг, вращение
        // [c d q] m,n - перенос(двумерное смещение
        // [m n s] s - полное масштабрирование
        //         pq - получение проекций

        for (int i = 0; i < 9; i++) {
            transformationMatrix[i].setEditable(false);
        }

        JButton apply = new JButton("Сохранить и преобразовать");
        apply.addActionListener(e -> {
            if (selectedEllipse == null) {
                return;
            }
            // todo вообще в эллипсе не сохранять матрицу преобразования после преобразования. Просто изменять ее
            // todo параметры, например для a и d это ширина и высота. Но как с остальными поступать?
            for (int row = 0, i = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++, i++) {
                    selectedEllipse.getTransformationMatrix()[row][col] = Double.parseDouble(transformationMatrix[i].getText());
                }
            }
            drawPanel.setShouldEllipseBeRecreated(true);
            drawPanel.repaint();
        });

        for (int i = 0; i < 9; i++) {
            editPanel.add(transformationMatrixLabels[i]);
            editPanel.add(transformationMatrix[i]);
        }

        editPanel.add(apply);
        mainPanel.add(editPanel);
        mainPanel.add(drawPanel);
        add(mainPanel);
    }

    public void onEllipseDeselected() {
        for (int i = 0; i < 9; i++) {
            transformationMatrix[i].setText("N/A");
            transformationMatrix[i].setEditable(false);
        }
    }

    public void onEllipseSelected(Ellipse selectedEllipse) {
        this.selectedEllipse = selectedEllipse;
        for (int row = 0, i = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++, i++) {
                transformationMatrix[i].setEditable(true);
                transformationMatrix[i].setText(String.valueOf(selectedEllipse.getTransformationMatrix()[row][col]));
            }
        }
    }
}
