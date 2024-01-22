package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a stub implementation of the {@link cs3500.klondike.model.hw02.KlondikeModel}
 * interface. You may assume that the actual implementation of BasicKlondike will have a
 * zero-argument (i.e. default) constructor, and that all the methods below will be
 * implemented.  You may not make any other assumptions about the implementation of this
 * class (e.g. what fields it might have, or helper methods, etc.).
 * 
 * <p>Once you've implemented all the constructors and methods on your own, you can
 * delete the placeholderWarning() method.
 */
public class BasicKlondike implements cs3500.klondike.model.hw02.KlondikeModel {
  protected List<Card> drawPile;
  protected List<List<Card>> cascadePile;
  protected List<List<Card>> fountainPile;
  protected List<Integer> visibility;
  protected List<Card> drawDeck;
  protected boolean start;

  /**
   * empty constructor for BasicKlondike.
   */
  public BasicKlondike() {
    this.drawPile = new ArrayList<>();
    this.cascadePile = new ArrayList<>();
    this.fountainPile = new ArrayList<>();
    this.visibility = new ArrayList<>();
    this.drawDeck = new ArrayList<>();
    this.start = false;
  }

  @Override
  public List<Card> getDeck() {
    List<Card> deck = new ArrayList<>();
    for (int i = 1; i < 14; i += 1) {
      deck.add(new PokerCard(i, Suit.DIAMOND));
      deck.add(new PokerCard(i, Suit.CLUBS));
      deck.add(new PokerCard(i, Suit.HEART));
      deck.add(new PokerCard(i, Suit.SPADE));
    }
    return deck;
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
    new Utils().cascadeErrors(source.get(source.size() - numCards), dest);
    new Utils().moveCard(source, dest, numCards);
    int index = this.cascadePile.get(srcPile).size() - 1 - numCards;
    if (index < 0) {
      index = 0;
    }
    if (this.visibility.get(srcPile) > index) {
      this.visibility.set(srcPile, index);
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
    }
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    new Utils().outOfBoundsArray(this.cascadePile, srcPile);
    new Utils().outOfBoundsArray(this.fountainPile, foundationPile);
    List<Card> source = this.cascadePile.get(srcPile);
    List<Card> dest = this.fountainPile.get(foundationPile);
    if (source.isEmpty()) {
      throw new IllegalStateException("Empty source pile");
    }
    new Utils().foundationErrors(source.get(source.size() - 1), dest);
    new Utils().moveCard(source, dest, 1);
    int index = source.size() - 1;
    if (index < 0) {
      index = 0;
    }
    if (this.visibility.get(srcPile) > index) {
      this.visibility.set(srcPile, index);
    }
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
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
    this.drawDeck.add(this.drawPile.get(0));
    this.drawPile.remove(0);
  }

  @Override
  public int getNumRows() {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    int max = 0;
    for (int i = 0; i < this.cascadePile.size(); i += 1) {
      if (this.cascadePile.get(i).size() > max) {
        max = this.cascadePile.get(i).size();
      }
    }
    return max;
  }

  @Override
  public int getNumPiles() {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    return this.cascadePile.size();
  }

  @Override
  public int getNumDraw() {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    return this.drawPile.size();
  }

  @Override
  public boolean isGameOver() {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    if (!this.drawPile.isEmpty()) {
      return false;
    }
    List<Card> landableFountain = new ArrayList<>();
    for (List<Card> current : this.fountainPile) {
      if (!current.isEmpty()) {
        landableFountain.add(current.get(current.size() - 1));
      }
    }
    List<Card> landableCascade = new ArrayList<>();
    for (List<Card> current : this.cascadePile) {
      if (!current.isEmpty()) {
        landableCascade.add(current.get(current.size() - 1));
      }
    }
    List<Card> movable = new ArrayList<>();
    for (int i = 0; i < this.cascadePile.size(); i += 1) {
      for (int j = this.visibility.get(i); j < this.cascadePile.get(i).size(); j += 1) {
        movable.add(this.cascadePile.get(i).get(j));
      }
    }
    for (Card card : movable) {
      for (Card value : landableCascade) {
        if (new Utils().validCascade(card, value)) {
          return false;
        }
      }
      for (Card item : landableFountain) {
        if (new Utils().validFoundation(card, item)) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public int getScore() {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    int accumulator = 0;
    for (List<Card> cards : this.fountainPile) {
      accumulator += cards.size();
    }
    return accumulator;
  }

  @Override
  public int getPileHeight(int pileNum) {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    new Utils().outOfBoundsArray(this.cascadePile, pileNum);
    return this.cascadePile.get(pileNum).size();
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    new Utils().outOfBoundsArray(this.cascadePile, pileNum);
    new Utils().outOfBoundsList(this.cascadePile.get(pileNum), card);
    return card >= this.visibility.get(pileNum);
  }

  @Override
  public Card getCardAt(int pileNum, int card) {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    new Utils().outOfBoundsArray(this.cascadePile, pileNum);
    new Utils().outOfBoundsList(this.cascadePile.get(pileNum), card);
    if (!this.isCardVisible(pileNum, card)) {
      throw new IllegalArgumentException("Card is not visible");
    }
    return this.cascadePile.get(pileNum).get(card);
  }

  @Override
  public Card getCardAt(int foundationPile) {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    new Utils().outOfBoundsArray(this.fountainPile, foundationPile);
    List<Card> source = this.fountainPile.get(foundationPile);
    if (source.isEmpty()) {
      return null;
    } else {
      return source.get(source.size() - 1);
    }
  }

  @Override
  public List<Card> getDrawCards() {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    return new ArrayList<>(this.drawPile);
  }

  @Override
  public int getNumFoundations() {
    if (!this.start) {
      throw new IllegalStateException("Game hasn't started");
    }
    return this.fountainPile.size();
  }
}
