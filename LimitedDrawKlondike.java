package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a stub implementation of the {@link cs3500.klondike.model.hw02.KlondikeModel} interface.
 * You may assume that the actual implementation of LimitedDrawKlondike will have a one-argument
 * constructor, and that all the methods below will be implemented.  You may not make
 * any other assumptions about the implementation of this class (e.g. what fields it might have,
 * or helper methods, etc.).
 */
public class LimitedDrawKlondike extends BasicKlondike {

  private List<Integer> drawPileCounter;
  private final int numTimesRedraw;

  /**
   * limited draw constructor.
   * @param numTimesRedrawAllowed the amount of times a draw card can be drawn
   */
  public LimitedDrawKlondike(int numTimesRedrawAllowed) {
    if (numTimesRedrawAllowed < 0) {
      throw new IllegalArgumentException("Can't redraw negative times");
    }
    this.drawPile = new ArrayList<>();
    this.cascadePile = new ArrayList<>();
    this.fountainPile = new ArrayList<>();
    this.visibility = new ArrayList<>();
    this.drawDeck = new ArrayList<>();
    this.start = false;
    this.drawPileCounter = new ArrayList<>();
    this.numTimesRedraw = numTimesRedrawAllowed;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    if (deck == null) {
      throw new IllegalArgumentException("Null deck");
    }
    if (numPiles < 1 || numPiles * (numPiles + 1) / 2 > deck.size() ||
            numDraw < 1 || numDraw > deck.size()) {
      throw new IllegalArgumentException("Invalid piles or draw number");
    }
    if (!new Utils().validDeck(deck)) {
      throw new IllegalArgumentException("Invalid deck");
    }
    if (this.start) {
      throw new IllegalStateException("Game already started");
    }
    this.start = true;
    if (shuffle) {
      Collections.shuffle(deck);
    }
    for (Card card : deck) {
      if (card.toString().substring(0, 1).equals("A")) {
        this.fountainPile.add(new ArrayList<>());
      }
    }
    for (int i = 0; i < numPiles; i += 1) {
      this.cascadePile.add(new ArrayList<Card>());
    }
    int runningIndex = 0;
    for (int j = 0; j < numPiles; j += 1) {
      for (int k = j; k < numPiles; k += 1) {
        this.cascadePile.get(k).add(deck.get(runningIndex));
        runningIndex += 1;
      }
    }
    for (int a = 0; a < numPiles; a += 1) {
      this.visibility.add(a);
    }
    for (int b = numPiles * (numPiles + 1) / 2; b < deck.size(); b += 1) {
      this.drawDeck.add(deck.get(b));
      this.drawPileCounter.add(this.numTimesRedraw);
    }
    for (int c = 0; c < numDraw; c += 1) {
      if (!drawDeck.isEmpty()) {
        this.drawPile.add(this.drawDeck.get(0));
        this.drawDeck.remove(0);
      }
    }
  }

  @Override
  public void moveDraw(int destPile) {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    new Utils().outOfBoundsArray(this.cascadePile, destPile);
    if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("draw deck is empty");
    }
    Card first = this.drawPile.get(0);
    List<Card> dest = this.cascadePile.get(destPile);
    new Utils().cascadeErrors(first, dest);
    List<Card> holder = new ArrayList<Card>();
    holder.add(first);
    new Utils().moveCard(holder, dest, 1);
    this.drawPile.remove(0);
    if (!this.drawDeck.isEmpty()) {
      new Utils().moveCard(this.drawDeck, this.drawPile, 1);
      this.drawPileCounter.remove(0);
    }
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) throws IllegalStateException {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("draw deck is empty");
    }
    new Utils().outOfBoundsArray(this.fountainPile, foundationPile);
    List<Card> fountain = this.fountainPile.get(foundationPile);
    Card first = this.drawPile.get(0);
    new Utils().foundationErrors(first, fountain);
    List<Card> holder = new ArrayList<Card>();
    holder.add(first);
    new Utils().moveCard(holder, fountain, 1);
    this.drawPile.remove(0);
    if (!this.drawDeck.isEmpty()) {
      new Utils().moveCard(this.drawDeck, this.drawPile, 1);
      this.drawPileCounter.remove(0);
    }
  }

  @Override
  public void discardDraw() {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("No cards to discard");
    }
    if (!this.drawDeck.isEmpty()) {
      this.drawPile.add(this.drawDeck.get(0));
      this.drawDeck.remove(0);
    }
    if (this.drawPileCounter.get(0) != 0) {
      this.drawDeck.add(this.drawPile.get(0));
      this.drawPileCounter.add(this.drawPileCounter.get(0) - 1);
      this.drawPileCounter.remove(0);
    }
    this.drawPile.remove(0);
  }
}
