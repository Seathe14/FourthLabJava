import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private int displaySize;
    private JImageDisplay ImageDisplay;
    private FractalGenerator Fractal;
    private Rectangle2D.Double Range2D;

    public FractalExplorer(int DisplaySize) {
        displaySize = DisplaySize;
        // Creating image display
        ImageDisplay = new JImageDisplay(displaySize, displaySize);
        // Creating reference to base object
        Fractal = new Mandelbrot();
        Range2D = new Rectangle2D.Double(0, 0, displaySize, displaySize);
        Fractal.getInitialRange(Range2D);
    }

    public void createAndShowGui() {
        ImageDisplay.setLayout(new BorderLayout());
        // Creating Window
        JFrame Frame = new JFrame("Fractal Explorer");
        Frame.add(ImageDisplay, BorderLayout.CENTER);


        // Button Reset position and Event Handler
        JButton resetButton = new JButton("Reset");
        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);
        Frame.add(resetButton, BorderLayout.SOUTH);

        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // MouseHandler
        MouseHandler click = new MouseHandler();
        ImageDisplay.addMouseListener(click);

        Frame.pack();
        Frame.setVisible(true);
        Frame.setResizable(false);
    }
    private void drawFractal()
    {
        for(int x = 0;x<displaySize;x++)
        {
            for(int y =0;y<displaySize;y++)
            {
                double xCoord = Fractal.getCoord(Range2D.x, Range2D.x + Range2D.width,displaySize,x);
                double yCoord = Fractal.getCoord(Range2D.y, Range2D.y + Range2D.height,displaySize,y);

                int iteration = Fractal.numIterations(xCoord,yCoord);

                if(iteration == -1)
                    ImageDisplay.drawPixel(x,y,0);
                else
                {
                    float hue = 0.5f + (float)iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue,0.7f,1.0f);
                    ImageDisplay.drawPixel(x,y,rgbColor);
                }
            }
        }
        ImageDisplay.repaint();
    }
    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Reset")) {
                Fractal.getInitialRange(Range2D);
                drawFractal();
            }
        }
    }
    private class MouseHandler extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            int x = e.getX();
            int y = e.getY();
            double xCoord = Fractal.getCoord(Range2D.x, Range2D.x + Range2D.width, displaySize,x);
            double yCoord = Fractal.getCoord(Range2D.y, Range2D.y + Range2D.height, displaySize,y);
            Fractal.recenterAndZoomRange(Range2D,xCoord,yCoord,0.5);
            drawFractal();
        }
    }
    public static void main(String[] args)
    {
        FractalExplorer FE = new FractalExplorer (800);
        FE.createAndShowGui();
        FE.drawFractal();
    }
}
