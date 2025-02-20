import java.util.ArrayList;
import java.util.List;

/**
 * A class to represent a playing card that can be displayed on the canvas
 */
public class Card {
    private int xPosition;
    private int yPosition;
    private Image image;
    private String rank;
    private String suit;
    private boolean isVisible;
    private boolean isFaceUp;

    private Image backImage;

    String filename;

    /**
     * Create a new card
     * @param rank the rank of the card (1=Ace, 2-10,  11=Jack, 12=Queen, 13=King)
     * @param suit the suit of the card (Hearts, Diamonds, Clubs, Spades)
     */
    public Card(int rank, String suit) {
        if (rank > 1 && rank < 11) {
            this.rank = Integer.toString(rank);
            filename = this.rank;
        } else if (rank == 1) {
            this.rank = "Ace";
            filename = "a";
        } else if (rank == 11) {
            this.rank = "Jack";
            filename = "j";
        } else if (rank == 12) {
            this.rank = "Queen";
            filename = "q";
        } else if (rank == 13) {
            this.rank = "King";
            filename = "k";
        }

        this.suit = suit;

        filename += this.suit.substring(0, 1).toLowerCase() + ".png";
        this.image = new Image("cards/" + filename, -1, 175);
        this.backImage = new Image("cards/back.png", -1, 175);

        this.isFaceUp = false;
        this.isVisible = false;
        this.xPosition = rank * 50;
        this.yPosition = 0;
    }

    /**
     * Make the card visible on the canvas
     */
    public void makeVisible() {
        if (!isVisible) {
            isVisible = true;
            if (isFaceUp) {
                image.makeVisible();
                backImage.makeInvisible();
            } else {
                backImage.makeVisible();
                image.makeInvisible();
            }
        }
    }

    /**
     * Remove the card from the canvas
     */
    public void makeInvisible() {
        if (isVisible) {
            isVisible = false;
            image.makeInvisible();
            backImage.makeInvisible();            
        }
    }

    /**
     * Turn the card face up
     */
    public void turnFaceUp() {
        this.isFaceUp = true;
    }

    /**
     * Turn the card face down
     */
    public void turnFaceDown() {
        this.isFaceUp = false;
    }

    /**
     * Determine if the card show its face or its back
     * @param faceUp true if the card should show its face, false if it should show its back
     */
    public void setFaceUp(boolean faceUp) {
        this.isFaceUp = faceUp;
    }

    /**
     * Set the X position of the card
     * @param x the X position of the card
     */
    public void setX(int x) {
        this.xPosition = x;
        image.setX(x);
        backImage.setX(x);
    }

    /**
     * Set the Y position of the card
     * @param y the Y position of the card
     */
    public void setY(int y) {
        this.yPosition = y;
        image.setY(y);
        backImage.setY(y);
    }

    /**
     * Set the position of the card
     * @param x the x position of the card
     * @param y the y position of the card
     */
    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        image.setPosition(x, y);
        backImage.setPosition(x, y);
    }

    /**
     * Get the X position of the card
     * @return the X position of the card 
     */
    public int getX() {
        return xPosition;
    }

    /**
     * Get the Y position of the card
     * @return the Y position of the card 
     */
    public int getY() {
        return yPosition;
    }

    /**
     * Get the width of the card
     * @return the width of the card (in pixels)
     */
    public int getWidth() {
        return image.getWidth();
    }

    /**
     * Get the height of the card
     * @return the height of the card (in pixels)
     */
    public int getHeight() {
        return image.getHeight();
    }
    
    /**
     * Determine if the card should be showing on the canvas
     * @return true if the card is not hidden
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Determine if the card is face up
     * @return true if the card is face up, false if face down
     */
    public boolean isFaceUp() {
        return isFaceUp;
    }

    /**
     * Get the rank of the card
     * @return the rank of the card (2-10, Jack, Queen, King, Ace)
     */
    public String getRank() {
        return rank;
    }

    /**
     * Get the suit of the card
     * @return the suit of the card
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Determine if a point is contained within the card
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the point is contained within the card, false otherwise
     */
    public boolean contains(int x, int y) {
        return image.contains(x, y);
    }

    /**
     * Create a deck of cards
     * @return a list of cards
     */
    public static List<Card> loadCards() {
        List<Card> cards = new ArrayList<Card>();
        for (String suit : new String[] { "Hearts", "Diamonds", "Clubs", "Spades" }) {
            for (int i = 1; i <= 13; i++) {
                cards.add(new Card(i, suit));
            }
        }
        return cards;
    }
}
