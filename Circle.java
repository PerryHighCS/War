import java.awt.Color;

/**
 * A circle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael K�lling and David J. Barnes and Brian Dahlem
 * @version 2018.11.26
 */

public class Circle
{
    private int diameter;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;
        
    /**
     * Create a new circle at default position with default color.
     */
    public Circle()
    {
        diameter = 68;
        xPosition = 230;
        yPosition = 90;
        color = "blue";
    }    
    
    /**
     * Create a new circle with a specified position and color.
     */
    public Circle(int x, int y, int diameter, String color, boolean visible)
    {
        this.diameter = diameter;
        xPosition = x;
        yPosition = y;
        this.color = color;
        
        if (visible) {
            makeVisible();
        }
    }

    /**
     * Make this circle visible. If it was already visible, do nothing.
     */
    public void makeVisible()
    {
        if (!isVisible) {
            isVisible = true;
            add();
        }
    }
    
    /**
     * Make this circle invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible()
    {
        if (isVisible){
            remove();
            isVisible = false;
        }
    }   
        
    /**
     * Determine if the circle should be showing on the canvas
     * @return true if the shape is not hidden
     */
    public boolean isVisible()
    {
        return isVisible;
    }
    
    /**
     * Get the circle's X position
     */
    public int getX() {
        return xPosition;
    }
    
    /**
     * Set the circle's X position
     */
    public void setX(int x) {
        this.xPosition = x;
    }    
    
    /**
     * Get the circle's Y position
     */
    public int getY() {
        return yPosition;
    }
    
    /**
     * Set the circle's Y position
     */
    public void setY(int y) {
        this.yPosition = y;
    }
    
    /**
     * Set the circle's position on the screen
     */
    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }
    
    /**
     * Get the circle's current diameter
     */
    public int getDiameter() {
        return this.diameter;
    }
    
    /**
     * Move the circle a few pixels to the right.
     */
    public void moveRight()
    {
        moveHorizontal(20);
    }

    /**
     * Move the circle a few pixels to the left.
     */
    public void moveLeft()
    {
        moveHorizontal(-20);
    }

    /**
     * Move the circle a few pixels up.
     */
    public void moveUp()
    {
        moveVertical(-20);
    }

    /**
     * Move the circle a few pixels down.
     */
    public void moveDown()
    {
        moveVertical(20);
    }

    /**
     * Move the circle horizontally by 'distance' pixels.
     * @param distance the distance to move the circle along the x axis,
     *                  positive to the right
     */
    public void moveHorizontal(int distance)
    {
        xPosition += distance;
    }

    /**
     * Move the circle vertically by 'distance' pixels.
     * @param distance the distance to move the circle along the y axis,
     *                  positive down
     */
    public void moveVertical(int distance)
    {
        yPosition += distance;
    }

    /**
     * Change the size to the new size (in pixels). Size must be &gt;= 0.
     * @param newDiameter the diameter of the circle
     */
    public void changeSize(int newDiameter)
    {
        diameter = newDiameter;
    }
    
    /**
     * Change the color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta", "cyan", "brown", "white", and "black", or rgb hex strings
     * "#rrggbb" where rr, gg, bb are 2-hexit values for red, green, and
     * blue levels.
     * @param newColor a lower case string naming the color to change to
     */
    public void changeColor(String newColor)
    {
        color = newColor;
    }

    /**
     * Determine if a point is inside the circle
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return true if the point is inside the circle
     */
    public boolean contains(int x, int y) {
        int xCenter = xPosition + diameter / 2;
        int yCenter = yPosition + diameter / 2;
        int xDiff = x - xCenter;
        int yDiff = y - yCenter;
        return xDiff * xDiff + yDiff * yDiff <= diameter * diameter / 4;
    }

    /**
     * Add the circle to the screen.
     */
    private void add()
    {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            Color c = Canvas.getColor(color);
            canvas.add(this,(g) -> {g.setColor(c);
                                    g.fillOval(xPosition, yPosition, diameter, diameter);});
        }
    }

    /**
     * Remove the circle from the screen.
     */
    private void remove()
    {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.remove(this);
        }
    }
}
