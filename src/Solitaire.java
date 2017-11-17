import java.util.*;

public class Solitaire
{
	public static void main(String[] args)
	{
        new Solitaire();
	}

	private Stack<Card> stock;
	private Stack<Card> waste;
	private Stack<Card>[] foundations;
	private Stack<Card>[] piles;
	private SolitaireDisplay display;

	public Solitaire()
	{
		foundations = (Stack<Card>[]) new Stack[4];
        for (int i = 0; i < foundations.length; i++)
        {
            foundations[i] = new Stack();
        }
		piles = new Stack[7];
        for (int i = 0; i < piles.length; i++)
        {
            piles[i] = new Stack();
        }
        stock = new Stack<Card>();
        waste = new Stack<Card>();
		display = new SolitaireDisplay(this);
        stock = createStock();
        deal();
	}

	public Card getStockCard()
	{
        if (stock.size() == 0) return null;
        return stock.peek();
	}
	
	public Card getWasteCard()
	{
		if (waste.size() == 0) return null;
        return waste.peek();
	}

	public Card getFoundationCard(int index)
	{
		if (foundations[index].isEmpty()) return null;
        return foundations[index].peek();
	}

	public Stack<Card> getPile(int index)
	{
        return piles[index];
	}

    public Stack<Card> createStock()
    {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 1; i <= 4; i++)
        {
            for (int k = 1; k <= 13; k++)
            {
                String suit = "";
                if (i == 1) suit = "h";
                if (i == 2) suit = "d";
                if (i == 3) suit = "c";
                if (i == 4) suit = "s";
                Card temp = new Card(k, suit);
                cards.add(temp);
            }
        }
        Stack<Card> stock = new Stack<Card>();
        while(cards.size() != 0)
        {
            if (cards.size() == 1) stock.push(cards.remove(0));
            else
            {
                int random = (int)(Math.random()*cards.size());
                stock.push(cards.remove(random));
            }
        }
        return stock;
    }

    public void deal()
    {
        for (int i = 0; i < piles.length; i++)
        {
            int counter = 0;
            piles[i] = new Stack<Card>();
            while (counter != i + 1)
            {
                Card temp = stock.pop();
                piles[i].push(temp);
                counter++;
            }
            piles[i].peek().turnUp();
        }
    }

    public void dealThreeCards()
    {
        for (int i = 0; i < 3; i++)
        {
            if (! stock.isEmpty())
            {
                Card temp = stock.pop();
                waste.push(temp);
                temp.turnUp();
            }
        }
    }

    public void resetStock()
    {
        while (! waste.isEmpty())
        {
            Card temp = waste.pop();
            temp.turnDown();
            stock.push(temp);
        }
    }

	public void stockClicked()
	{
		System.out.println("stock clicked");
        display.unselect();
        if (! display.isWasteSelected() && ! display.isPileSelected())
        {
            if (stock.isEmpty()) resetStock();
            else dealThreeCards();
        }

	}

	public void wasteClicked()
	{
		System.out.println("waste clicked");
        if (! waste.isEmpty())
        {
            if (! display.isWasteSelected()) display.selectWaste();
            else display.unselect();
        }
	}

	public void foundationClicked(int index)
	{
		System.out.println("foundation #" + index + " clicked");
        if (display.isWasteSelected())
        {
            if (canAddToFoundation(waste.peek(), index))
            {
                Card temp = waste.pop();
                foundations[index].push(temp);
                display.unselect();
            }
        }
        if (display.isPileSelected())
        {
            Stack<Card> selectedPile = piles[display.selectedPile()];
            if (canAddToFoundation(selectedPile.peek(), index))
            {
                Card temp = selectedPile.pop();
                foundations[index].push(temp);
                if (! selectedPile.isEmpty()) selectedPile.peek().turnUp();
                display.unselect();
            }

        }
	}

	public void pileClicked(int index)
	{
		System.out.println("pile #" + index + " clicked");
        if (display.isWasteSelected()) {
            Card temp = waste.peek();
            if (canAddToPile(temp, index))
            {
                piles[index].push(waste.pop());
                piles[index].peek().turnUp();
            }
            display.unselect();
            display.selectPile(index);
        }
        else if (display.isPileSelected())
        {
            int oldPile = display.selectedPile();
            if (index != oldPile)
            {
                Stack<Card> temp = removeFaceUpCards(oldPile);
                if (canAddToPile(temp.peek(), index))
                {
                    addToPile(temp, index);if (!piles[oldPile].isEmpty()) piles[oldPile].peek().turnUp();

                    display.unselect();
                }
                else
                {
                    addToPile(temp, oldPile);
                    display.unselect();
                    display.selectPile(index);

                }
            }
            else display.unselect();
        }
        else
        {
            display.selectPile(index);
            piles[index].peek().turnUp();
        }
	}

    private boolean canAddToPile(Card card, int index)
    {
        Stack<Card> pile = piles[index];
        if (pile.isEmpty()) return (card.getRank() == 13);
        Card top = pile.peek();
        if (! top.isFaceUp()) return false;
        return (card.isRed() != top.isRed()) && (card.getRank() == top.getRank()-1);
    }

    private Stack<Card> removeFaceUpCards(int index)
    {
        Stack<Card> cards = new Stack<Card>();
        while (! piles[index].isEmpty() && piles[index].peek().isFaceUp())
        {
            cards.push(piles[index].pop());
        }
        return cards;
    }

    private void addToPile(Stack<Card> cards, int index)
    {
        while (! cards.isEmpty())
        {
            piles[index].push(cards.pop());
        }
    }

    private boolean canAddToFoundation(Card card, int index)
    {
        if (foundations[index].isEmpty()) return (card.getRank() == 1);
        Card temp = foundations[index].peek();
        return (temp.getRank() + 1 == card.getRank()) && (temp.getSuit().equals(card.getSuit()));
    }

}