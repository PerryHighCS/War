import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Canvas is a class to allow for simple graphical drawing on a canvas. This is
 * a modification of the general purpose Canvas, specially made for the BlueJ
 * "shapes" example.
 *
 * @author: Bruce Quig Michael Kölling Brian Dahlem
 * @version 2018.11.26
 */
public class Canvas {
    // Note: The implementation of this class (specifically the handling of
    // shape identity and colors) is slightly more complex than necessary. This
    // is done on purpose to keep the interface and instance fields of the
    // shape objects in this project clean and simple for educational purposes.

    private static Canvas canvasSingleton;

    /**
     * Factory method to get the canvas singleton object.
     *
     * @return a reference to the applications canvas
     */
    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas("Picture Demo", 800, 600, Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    //  ----- instance part -----
    private JFrame frame;
    private CanvasPane canvas;
    private BufferStrategy bs;
    private Color backgroundColor;
    private final Map<Object, DrawShape> shapes;
    private boolean paused = false;
    private boolean firstShown = false;

    /**
     * Create a Canvas.
     *
     * @param title title to appear in Canvas Frame
     * @param width the desired width for the canvas
     * @param height the desired height for the canvas
     * @param bgColor the desired background color of the canvas
     */
    private Canvas(String title, int width, int height, Color bgColor) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new CanvasPane();
        frame.add(canvas);
        frame.setTitle(title);
        frame.setLocation(30, 30);

        canvas.setPreferredSize(new Dimension(width, height));

        backgroundColor = bgColor;
        frame.pack();
        shapes = new LinkedHashMap<>(1000);

        // Listen for Ctrl-S to save the picture
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // If Ctrl-S is pressed...
                if ((e.getKeyCode() == KeyEvent.VK_S) && 
                    ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)) {
                    // Ask the user for a filename to save to.
                    JFileChooser fc = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "PNG Images", "png");
                    fc.setFileFilter(filter);
                    int returnVal = fc.showSaveDialog(frame);

                    // Cancel if the user pressed "Cancel"...
                    if (returnVal == JFileChooser.CANCEL_OPTION) {
                        return;
                    }

                    // Get the name of the file the user entered
                    File file = fc.getSelectedFile();
                    String fname = file.getAbsolutePath();

                    if (!fname.endsWith(".png")) {
                        file = new File(fname + ".png");
                    }

                    // If that file exists, confirm overwrite.
                    if (file.exists()) {
                        int overwrite = JOptionPane.showConfirmDialog(frame,
                                "A file named " + file + " exists.\nOverwrite?", "File Exists",
                                JOptionPane.YES_NO_OPTION);

                        if (overwrite == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }

                    Font font = new Font("SansSerif", Font.PLAIN, 20);

                    try {
                        InputStream fnt_stream = getClass().getResourceAsStream("Caveat.ttf");
                        Font myFont = Font.createFont(Font.TRUETYPE_FONT, fnt_stream);
                        font = myFont.deriveFont(Font.BOLD, 20f);
                    } catch (FontFormatException | IOException ex) {

                    }

                    FontMetrics fm = canvas.getFontMetrics(font);
                    int fontheight = fm.getHeight();

                    // Create a buffered image from the picture
                    BufferedImage buffer = new BufferedImage(width, height + fontheight + 2,
                            BufferedImage.TYPE_INT_RGB);
                    Graphics bgc = buffer.createGraphics();
                    bgc.setColor(Color.white);
                    bgc.fillRect(0, 0, width, height + fontheight + 2);

                    redraw(bgc);

                    bgc.setColor(Color.black);
                    bgc.setFont(font);
                    bgc.drawString(frame.getTitle(), 0, height + fm.getAscent() + 1);

                    // Save the image
                    try {
                        ImageIO.write(buffer, "png", file);

                        // Inform the user of success in saving.
                        JOptionPane.showMessageDialog(frame,
                                "Image saved to: " + file, "File Saved",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (java.io.IOException exc) {
                        // Alert the user if there is an error.
                        JOptionPane.showMessageDialog(frame,
                                "Could not save image to: " + file, "File Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen when
     * made visible. This method can also be used to bring an already visible
     * canvas to the front of other windows.
     *
     * @param visible boolean value representing the desired visibility of the
     * canvas (true or false)
     */
    public void setVisible(boolean visible) {
        if (!firstShown && visible) {
            firstShown = true;
            // first time: instantiate the image and fill it with
            // the background color

            Dimension size = canvas.getSize();

            canvas.createBufferStrategy(2);
            bs = canvas.getBufferStrategy();

            Graphics graphic = bs.getDrawGraphics();
            graphic.setColor(backgroundColor);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
            graphic.dispose();
            bs.show();
        }
        frame.setVisible(visible);
    }

    /**
     * Determine the width of the canvas
     */
    public int getWidth() {
        return canvas.getWidth();
    }

    /**
     * Determine the height of the canvas
     */
    public int getHeight() {
        return canvas.getHeight();
    }

    /**
     * Pause automatic redraws
     */
    public void pause(boolean pause) {
        this.paused = pause;
    }

    /**
     * Are automatic redraws paused
     */
    public boolean isPaused() {
        return this.paused;
    }

    /**
     * Draw a given shape onto the canvas.
     *
     * @param referenceObject an object to define identity for this shape
     * @param shapeFunction a function that draws the shape on a graphics
     * context
     */
    public void add(Object referenceObject, DrawShape shapeFunction) {
        synchronized (shapes) {
            if (shapes.containsKey(referenceObject)) {
                throw new IllegalArgumentException("Shape already added to canvas");
            }

            shapes.put(referenceObject, shapeFunction);
        }

        if (!paused) {
            redraw();
        }
    }

    /**
     * Erase a given shape's from the screen.
     *
     * @param referenceObject the shape object to be erased
     */
    public void remove(Object referenceObject) {

        synchronized (shapes) {
            if (!shapes.containsKey(referenceObject)) {
                throw new IllegalArgumentException("Shape not added to canvas");
            }

            shapes.remove(referenceObject);
        }

        if (!paused) {
            redraw();
        }
    }

    /**
     * Change the name of this canvas
     *
     * @param title the new name for the canvas
     */
    public void setTitle(String title) {
        frame.setTitle(title);
    }

    /**
     * Set the canvas's background color
     *
     * @param bgColor the new background color for the canvas.
     */
    public void setBackgroundColor(Color bgColor) {
        this.backgroundColor = bgColor;
    }

    /**
     * Set the canvas's background color
     *
     * @param bgColor the new background color for the canvas.
     */
    public void setBackgroundColor(String bgColor) {
        setBackgroundColor(getColor(bgColor));
    }

    /**
     * Add a handler to deal with mouse clicks on the canvas
     */
    public void addMouseHandler(MouseInputListener ml) {
        canvas.addMouseListener(ml);
        canvas.addMouseMotionListener(ml);
    }
    
    /**
     * Redraw all shapes currently on the Canvas.
     */
    public void redraw() {
        Graphics buffer = bs.getDrawGraphics();

        redraw(buffer);

        buffer.dispose();
        bs.show();
    }

    /**
     * Get a FontMetrics object for the canvas
     */
    public static FontMetrics getFontMetrics(Font font) {
        return canvasSingleton.frame.getFontMetrics(font);
    }
    
    /**
     * Get the current graphics context for drawing on the canvas
     */
    public static Graphics getGraphicsContext() {
        return canvasSingleton.frame.getGraphics();
    }
    
    /**
     * Redraw all shapes onto a graphics context
     */
    private void redraw(Graphics buffer) {
        synchronized (shapes) {
            buffer.setColor(backgroundColor);
            Dimension size = canvas.getSize();
            buffer.fillRect(0, 0, size.width, size.height);

            shapes.forEach((k, shape) -> {
                shape.draw(buffer);
            });
        }
    }

    /**
     * Clear the whole canvas.
     */
    public void clear() {
        shapes.clear();
        redraw();
    }

    /**
     * Transform a color string into a usable color
     *
     * @param colorString the new color for the foreground of the Canvas
     */
    public static Color getColor(String colorString) {
        Color c;

        if (colorString.equals("red")) {
            c = new Color(235, 25, 25);
        } else if (colorString.equals("black")) {
            c = Color.black;
        } else if (colorString.equals("blue")) {
            c = new Color(30, 75, 220);
        } else if (colorString.equals("cyan")) {
            c = new Color(30, 229, 220);
        } else if (colorString.equals("brown")) {
            c = new Color(110, 80, 0);
        } else if (colorString.equals("yellow")) {
            c = new Color(255, 230, 0);
        } else if (colorString.equals("green")) {
            c = new Color(80, 160, 60);
        } else if (colorString.equals("magenta")) {
            c = Color.magenta;
        } else if (colorString.equals("white")) {
            c = Color.white;
        } else if (colorString.startsWith("#") && colorString.length() == 7) {
            int red = Integer.parseInt(colorString.substring(1, 3), 16);
            int green = Integer.parseInt(colorString.substring(3, 5), 16);
            int blue = Integer.parseInt(colorString.substring(5, 7), 16);
            c = new Color(red, green, blue);
        } else {
            c = Color.black;
        }

        return c;
    }

    /**
     * **********************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame.
     */
    private class CanvasPane extends java.awt.Canvas {
        @Override
        public void paint(Graphics g) {
            redraw();
        }
    }

    /**
     * ***********************************************************************
     * Inner interface DrawShape - a functional interface that allows a shape to
     * provide the canvas with a method to draw the shape
     */
    public interface DrawShape {
        public void draw(Graphics g);
    }
}
