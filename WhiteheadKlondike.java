package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a stub implementation of the {@link cs3500.klondike.model.hw02.KlondikeModel} interface.
 * You may assume that the actual implementation of WhiteheadKlondike will have a zero-argument
 * (i.e. default) constructor, and that all the methods below will be implemented.  You may not make
 * any other assumptions about the implementation of this class (e.g. what fields it might have,
 * or helper methods, etc.).
 */
public class WhiteheadKlondike extends BasicKlondike {

  /**
   * constructor for whitehead.
   */
  public WhiteheadKlondike() {
    this.drawPile = new ArrayList<>();
    this.cascadePile = new ArrayList<>();
    this.fountainPile = new ArrayList<>();
    this.visibility = new ArrayList<>();
    this.drawDeck = new ArrayList<>();
    this.start = false;
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
      this.visibility.add(0);
    }
    for (int b = numPiles * (numPiles + 1) / 2; b < deck.size(); b += 1) {
      this.drawDeck.add(deck.get(b));
    }
    for (int c = 0; c < numDraw; c += 1) {
      if (!drawDeck.isEmpty()) {
        this.drawPile.add(this.drawDeck.get(0));
        this.drawDeck.remove(0);
      }
    }
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    new Utils().outOfBoundsArray(this.cascadePile, srcPile);
    new Utils().outOfBoundsArray(this.cascadePile, destPile);
    List<Card> source = this.cascadePile.get(srcPile);
    List<Card> dest = this.cascadePile.get(destPile);
    if (srcPile == destPile || source.size() < numCards || numCards < 1) {
      throw new IllegalArgumentException("Invalid inputs for moving");
    }
    List<Card> pile = new ArrayList<>();
    for (int i = source.size() - numCards; i < source.size(); i += 1) {
      pile.add(source.get(i));
    }
    new Utils().cascadeWhiteErrors(source.get(source.size() - numCards), dest);
    new Utils().sameSuit(pile);
    new Utils().moveCard(source, dest, numCards);
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
    new Utils().cascadeWhiteErrors(first, dest);
    List<Card> holder = new ArrayList<Card>();
    holder.add(first);
    new Utils().moveCard(holder, dest, 1);
    this.drawPile.remove(0);
    if (!this.drawDeck.isEmpty()) {
      new Utils().moveCard(this.drawDeck, this.drawPile, 1);
    }
  }
}
