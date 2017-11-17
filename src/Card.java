public class Card {
    public int rank;
    public String suit;
    public boolean isFaceUp;
    
    public Card(int newRank, String newSuit)
    {
        rank = newRank;
        suit = newSuit;
        isFaceUp = false;
    }

    public int getRank()
    {
        return rank;
    }

    public String getSuit()
    {
        return suit;
    }

    public boolean isRed()
    {
        return (suit.equals("d") || suit.equals("h"));
    }

    public boolean isFaceUp()
    {
        return isFaceUp;
    }

    public void turnUp()
    {
        isFaceUp = true;
    }


    public void turnDown()
    {
        isFaceUp = false;
    }
}
