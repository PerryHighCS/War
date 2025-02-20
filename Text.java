/**
 * A text object that can be drawn on the canvas.
 *
 * @author Brian Dahlem
 * @version 2019-1-18
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;

public class Text
{
    private int xPosition;
    private int yPosition;
    private String text;
    private Color color;
    private boolean isVisible;
    private float size;
    private Font font;
    private FontMetrics metrics;
    private Rectangle2D bounds;

    /**
     * Create text at default position with default color.
     */
    public Text()
    {
        xPosition = 50;
        yPosition = 10;
        size = 24;
        String text = "some text";
        color = Canvas.getColor("black");
        isVisible = false;
        font = new Font("Helvetica", Font.PLAIN, (int)size);
        metrics = Canvas.getFontMetrics(font);
        bounds = metrics.getStringBounds(text, Canvas.getGraphicsContext());
    }

    /**
     * Create text at a specified position and color.
     */
    public Text(String text, int x, int y, int size, String color, boolean visible)
    {
        xPosition = x;
        yPosition = y;
        this.text = text;
        this.size = size;
        this.color = Canvas.getColor(color);
        
        font = new Font("Helvetica", Font.PLAIN, (int)size);
        metrics = Canvas.getFontMetrics(font);        
        bounds = metrics.getStringBounds(text, Canvas.getGraphicsContext());
        
        if (visible) {
            makeVisible();
        }
    }

    /**
     * Make this text visible. If it was already visible, do nothing.
     */
    public void makeVisible()
    {
        if (!isVisible) {
            isVisible = true;
            add();
        }
    }

    /**
     * Make this text invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible()
    {
        if (isVisible){
            remove();
            isVisible = false;
        }
    }
    
    /**
     * Determine if the text should be showing on the canvas
     * @return true if the shape is not hidden
     */
    public boolean isVisible()
    {
        return isVisible;
    }
    
    
    /**
     * Get the text's X position
     */
    public int getX() {
        return xPosition;
    }
    
    /**
     * Set the text's X position
     */
    public void setX(int x) {
        this.xPosition = x;
    }    
    
    /**
     * Get the text's Y position
     */
    public int getY() {
        return yPosition;
    }
    
    /**
     * Set the text's Y position
     */
    public void setY(int y) {
        this.yPosition = y;
    }
    
    /**
     * Set the text's position on the screen
     */
    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }
    
    /**
     * Move the text a few pixels to the right.
     */
    public void moveRight()
    {
        moveHorizontal(20);
    }

    /**
     * Move the text a few pixels to the left.
     */
    public void moveLeft()
    {
        moveHorizontal(-20);
    }

    /**
     * Move the text a few pixels up.
     */
    public void moveUp()
    {
        moveVertical(-20);
    }

    /**
     * Move the text a few pixels down.
     */
    public void moveDown()
    {
        moveVertical(20);
    }

    /**
     * Move the text horizontally by 'distance' pixels.
     * @param distance the distance to move the rectangle along the x axis, 
     *                  positive to the right
     */
    public void moveHorizontal(int distance)
    {
        xPosition += distance;
    }

    /**
     * Move the text vertically by 'distance' pixels.
     * @param distance the distance to move the rectangle along the y axis, 
     *                  positive down
     */
    public void moveVertical(int distance)
    {
        yPosition += distance;
    }
    
    /**
     * Change the color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta", "cyan", "brown", "white", and "black", or rgb hex strings
     * "#rrggbb" where rr, gg, bb are 2-hexit values for red, green, and
     * blue levels.
     * @param newColor a string naming the new color for the text
     */
    public void changeColor(String newColor)
    {
        color = Canvas.getColor(newColor);
    }

    /**
     * Determine if a point is inside of the bounds of this text
     */
    public boolean contains(int x, int y) {        
        return bounds.contains(x - xPosition, y - yPosition);
    }
    
    /**
     * Draw text with current specifications on screen.
     */
    private void add()
    {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.add(this, (g) -> {g.setColor(color);
                                     g.setFont(font);
                                     g.drawString(text, 
                                                  xPosition, yPosition);});
        }
    }

    /**
     * Remove the text from the screen.
     */
    private void remove()
    {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.remove(this);
        }
    }
}
