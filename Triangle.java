import java.awt.Color;

/**
 * A triangle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kölling and David J. Barnes and Brian Dahlem
 * @version 2018.11.26
 */

public class Triangle
{
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private Color color;
    private boolean isVisible;

    /**
     * Create a new triangle at default position with default color.
     */
    public Triangle()
    {
        height = 60;
        width = 70;
        xPosition = 210;
        yPosition = 140;
        color = Canvas.getColor("green");
        isVisible = false;
    }
    

    /**
     * Create a new triangle with a specified position and color.
     */
    public Triangle(int x, int y, int width, int height, String color, boolean visible)
    {
        this.height = height;
        this.width = width;
        xPosition = x;
        yPosition = y;
        this.color = Canvas.getColor(color);
                
        if (visible) {
            makeVisible();
        }
    }

    /**
     * Make this triangle visible. If it was already visible, do nothing.
     */
    public void makeVisible()
    {
        if (!isVisible)
        {
            isVisible = true;
            add();
        }
    }

    /**
     * Make this triangle invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible()
    {
        if (isVisible) 
        {
            remove();
            isVisible = false;
        }
    }
    
    /**
     * Determine if the triangle should be showing on the canvas
     * @return true if the shape is not hidden
     */
    public boolean isVisible()
    {
        return isVisible;
    }
            
    /**
     * Get the triangle's X position
     */
    public int getX() {
        return xPosition;
    }
    
    /**
     * Set the triangle's X position
     */
    public void setX(int x) {
        this.xPosition = x;
    }    
    
    /**
     * Get the triangle's Y position
     */
    public int getY() {
        return yPosition;
    }
    
    /**
     * Set the triangle's Y position
     */
    public void setY(int y) {
        this.yPosition = y;
    }
    
    /**
     * Set the triangle's position on the screen
     */
    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }
    
    /**
     * Determine the triangle's current width
     */
    public int getWidth() {
        return this.width;
    }
    
    /**
     * Determine the triangle's current height
     */
    public int getHeight() {
        return this.height;
    }
    
    /**
     * Change the triangle's width
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /**
     * Change the triangle's height
     */
    public void setHeight(int height) {
        this.height = height;
    }
    
    /**
     * Move the triangle a few pixels to the right.
     */
    public void moveRight()
    {
        moveHorizontal(20);
    }

    /**
     * Move the triangle a few pixels to the left.
     */
    public void moveLeft()
    {
        moveHorizontal(-20);
    }

    /**
     * Move the triangle a few pixels up.
     */
    public void moveUp()
    {
        moveVertical(-20);
    }

    /**
     * Move the triangle a few pixels down.
     */
    public void moveDown()
    {
        moveVertical(20);
    }

    /**
     * Move the triangle horizontally by 'distance' pixels.
     * @param distance the distance to move the triangle along the x axis, 
     *                  positive to the right
     */
    public void moveHorizontal(int distance)
    {
        xPosition += distance;
    }

    /**
     * Move the triangle vertically by 'distance' pixels.
     * @param distance the distance to move the triangle along the y axis,
     *                  positive down
     */
    public void moveVertical(int distance)
    {
        yPosition += distance;
    }

    /**
     * Change the size of the triangle (in pixels).
     * @param newHeight the new height of the triangle, positive points up, negative points down
     * @param newWidth the new width of the triangle, must be &gt; 0
     */
    public void changeSize(int newHeight, int newWidth)
    {
        height = newHeight;
        width = newWidth;
    }

    /**
     * Change the color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta", "cyan", "brown", "white", and "black", or rgb hex strings
     * "#rrggbb" where rr, gg, bb are 2-hexit values for red, green, and
     * blue levels.
     * @param newColor a string naming the new color for the triangle
     */
    public void changeColor(String newColor)
    {
        color = Canvas.getColor(newColor);
    }

    /**
     * Determine if a point is inside the triangle
     */
    public boolean contains(int x, int y)
    {
        int[] xpoints = {xPosition, 
                         xPosition + (width/2),
                         xPosition - (width/2)};
        int[] ypoints = {yPosition,
                         yPosition + height,
                         yPosition + height};
        
        /* Calculate area of triangle ABC */
        double A = area (xpoints[0], ypoints[0], xpoints[1], ypoints[1], xpoints[2], ypoints[2]);
      
        /* Calculate area of triangle PBC */ 
        double A1 = area (x, y, xpoints[1], ypoints[1], xpoints[2], ypoints[2]);
      
        /* Calculate area of triangle PAC */ 
        double A2 = area (xpoints[0], ypoints[0], x, y, xpoints[2], ypoints[2]);
      
        /* Calculate area of triangle PAB */  
        double A3 = area (xpoints[0], ypoints[0], xpoints[1], ypoints[1], x, y);
        
        /* Check if sum of A1, A2 and A3 is same as A */
        return Math.abs(A - (A1 + A2 + A3)) < 0.00001;
    }

    /* A utility function to calculate area of triangle 
       formed by (x1, y1) (x2, y2) and (x3, y3) */
    static double area(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        return Math.abs((x1*(y2-y3) + x2*(y3-y1)+ x3*(y1-y2))/2.0);
    }    


    /**
     * Draw a triangle with current specifications on the screen.
     */
    private void add()
    {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.add(this, (g) -> {int[] xpoints = {xPosition, 
                                                      xPosition + (width/2),
                                                      xPosition - (width/2)};
                                     int[] ypoints = {yPosition,
                                                      yPosition + height,
                                                      yPosition + height};
                                     g.setColor(color);
                                     g.fillPolygon(xpoints, ypoints, 3);});                                             
        }
    }

    /**
     * Remove the triangle from the screen.
     */
    private void remove()
    {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.remove(this);
        }
    }
}
