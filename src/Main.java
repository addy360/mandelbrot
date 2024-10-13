import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends JPanel {
    private final static int width = 1600;
    private final static int height = 1000;

    private double centerX = 0;
    private double centerY = 0;
    private double zoomFactor = 200;

    public Main() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                double offsetX = (x - width / 2.0) / zoomFactor;
                double offsetY = (y - height / 2.0) / zoomFactor;
                centerX += offsetX;
                centerY += offsetY;

                if (SwingUtilities.isLeftMouseButton(e)) {
                    zoomFactor *= 1.5;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    zoomFactor /= 1.5;
                }

                repaint();
            }
        });
    }

    private void createFractal(Graphics g) {
        int maxIterations = 1000;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double zx = 0, zy = 0;
                double cX = (x - width / 2.0) / zoomFactor + centerX;
                double cY = (y - height / 2.0) / zoomFactor + centerY;

                int iterations = 0;
                while (zx * zx + zy * zy < 4 && iterations < maxIterations) {
                    double tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iterations++;
                }

                int color = iterations == maxIterations ? 0 : Color.HSBtoRGB(iterations / 256f, 1, iterations / (iterations + 8f));
                g.setColor(new Color(color));
                g.drawLine(x, y, x, y);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        createFractal(g);
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Mandelbrot Set");
        Main main = new Main();
        jFrame.add(main);
        jFrame.setSize(width, height);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}
