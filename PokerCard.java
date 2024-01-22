package cs3500.klondike.model.hw02;

import java.util.HashMap;

/**
 * represents a PokerCard.
 */
public class PokerCard implements Card {
  private final int character;
  private final Suit symbol;

  /**
   * constructor for a PokerCard.
   * @param character which is the letter of number
   * @param symbol which is the suit
   */
  public PokerCard(int character, Suit symbol) {
    if (character > 13) {
      throw new IllegalArgumentException("No pokercard representation for a number more than 13");
    }
    this.character = character;
    this.symbol = symbol;
  }

  /**
   * represents the string representation of a PokerCard.
   * @return the string of the PokerCard
   */
  public String toString() {
    HashMap<Suit, String> second = new HashMap<>();
    second.put(Suit.DIAMOND, "♢");
    second.put(Suit.CLUBS, "♣");
    second.put(Suit.HEART, "♡");
    second.put(Suit.SPADE, "♠");
    String ans = second.get(this.symbol);
    if (this.character == 1) {
      return "A" + ans;
    } else if (this.character == 11) {
      return "J" + ans;
    } else if (this.character == 12) {
      return "Q" + ans;
    } else if (this.character == 13) {
      return "K" + ans;
    } else {
      return this.character + ans;
    }
  }

  /**
   * determines equality for cards.
   * @param other card to be compared to
   * @return boolean if cards are the same
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof Card) {
      return this.toString().equals(other.toString());
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    int running = 0;
    if (this.symbol.equals(Suit.DIAMOND)) {
      running = 1;
    } else if (this.symbol.equals(Suit.CLUBS)) {
      running = 2;
    } else if (this.symbol.equals(Suit.HEART)) {
      running = 3;
    } else {
      running = 4;
    }
    return this.character + running * 15;
  }
}
