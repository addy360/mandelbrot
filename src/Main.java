import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main extends JPanel {
    private final int width = 800;
    private final int height = 800;
    private BufferedImage image;

    public Main() {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        createFractal();
    }

    private void createFractal() {
        int maxIterations = 1000;
        double zoom = 100;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double zx, zy, cX, cY;
                zx = zy = 0;
                cX = (x - width / 2.0) / zoom;
                cY = (y - height / 2.0) / zoom;

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
        jFrame.setSize(800, 800);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}