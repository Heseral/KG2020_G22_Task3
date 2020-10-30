package gui;

import util.GlobalVar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {

    public MainWindow() throws HeadlessException {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.X_AXIS));
        editPanel.setMaximumSize(new Dimension(GlobalVar.SCREEN_WIDTH, 50));
        editPanel.setMinimumSize(new Dimension(GlobalVar.SCREEN_WIDTH, 50));
        DrawPanel drawPanel = new DrawPanel();

        // [a b p] a, b, c, d - масштаб, сдвиг, вращение
        // [c d q] m,n - перенос(двумерное смещение
        // [m n s] s - полное масштабрирование
        //         pq - получение проекций

        JLabel aLabel = new JLabel("a");
        JTextField aText = new JTextField("1");
        JLabel bLabel = new JLabel("b");
        JTextField bText = new JTextField("0");
        JLabel cLabel = new JLabel("c");
        JTextField cText = new JTextField("0");
        JLabel dLabel = new JLabel("d");
        JTextField dText = new JTextField("1");

        JLabel mLabel = new JLabel("m");
        JTextField mText = new JTextField("0");
        JLabel nLabel = new JLabel("n");
        JTextField nText = new JTextField("0");

        JLabel sLabel = new JLabel("s");
        JTextField sText = new JTextField("1");

        JLabel pLabel = new JLabel("p");
        JTextField pText = new JTextField("0");
        JLabel qLabel = new JLabel("q");
        JTextField qText = new JTextField("0");

        JButton apply = new JButton("Применить афинное преобразование");
        apply.addActionListener(e -> {

        });

        editPanel.add(aLabel);
        editPanel.add(aText);
        editPanel.add(bLabel);
        editPanel.add(bText);
        editPanel.add(cLabel);
        editPanel.add(cText);
        editPanel.add(dLabel);
        editPanel.add(dText);

        editPanel.add(mLabel);
        editPanel.add(mText);
        editPanel.add(nLabel);
        editPanel.add(nText);

        editPanel.add(sLabel);
        editPanel.add(sText);

        editPanel.add(pLabel);
        editPanel.add(pText);
        editPanel.add(qLabel);
        editPanel.add(qText);

        editPanel.add(apply);

        mainPanel.add(editPanel);
        mainPanel.add(drawPanel);
        add(mainPanel);
    }
}
