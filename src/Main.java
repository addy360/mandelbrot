import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Main extends JPanel {
    private final static int width = 1600;
    private final static int height = 1000;
    private final BufferedImage image;

    private double centerX = 0;
    private double centerY = 0;
    private double zoomFactor = 200; // initial zoom

    public Main() {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        createFractal();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get the x, y position of the mouse click
                int x = e.getX();
                int y = e.getY();

                // Calculate new center based on mouse position and current zoom level
                double offsetX = (x - width / 2.0) / zoomFactor;
                double offsetY = (y - height / 2.0) / zoomFactor;
                centerX += offsetX;
                centerY += offsetY;

                // Zoom in (or out) based on left (zoom in) or right click (zoom out)
                if (SwingUtilities.isLeftMouseButton(e)) {
                    zoomFactor *= 1.5; // Zoom in
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    zoomFactor /= 1.5; // Zoom out
                }

                createFractal();
                repaint();
            }
        });
    }

    private void createFractal() {
        int maxIterations = 1000;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double zx, zy, cX, cY;
                zx = zy = 0;

                // Convert pixel to complex number based on zoom and center position
                cX = (x - width / 2.0) / zoomFactor + centerX;
                cY = (y - height / 2.0) / zoomFactor + centerY;

                int iterations = 0;
                while (zx * zx + zy * zy < 4 && iterations < maxIterations) {
                    double tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iterations++;
                }

                int color = iterations == maxIterations ? 0 : Color.HSBtoRGB(iterations / 256f, 1, iterations / (iterations + 8f));
                image.setRGB(x, y, color);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Mandlebrot Set");
        Main main = new Main();
        jFrame.add(main);
        jFrame.setSize(width, height);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}