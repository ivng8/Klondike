package cs3500.klondike;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.WhiteheadKlondike;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * more tests for the new models.
 */
public class ExamplarExtendedModelTests {

  KlondikeModel whiteHead;
  KlondikeModel limitDraw;
  List<Card> whiteDeck;
  List<Card> limitedDeck;

  @Before
  public void setUp() {
    whiteHead = new WhiteheadKlondike();
    limitDraw = new LimitedDrawKlondike(1);
    whiteDeck = whiteHead.getDeck();
    limitedDeck = limitDraw.getDeck();
  }

  // draw until full cycle
  @Test
  public void testDrawCycle() {
    limitDraw.startGame(limitedDeck, false, 9, 1);
    for (int i = 0; i < 7; i += 1) {
      limitDraw.discardDraw();
    }
    Assert.assertFalse(limitDraw.getDrawCards().isEmpty());
  }

  // test until deck empty
  @Test (expected = IllegalStateException.class)
  public void testDrawUntilEmpty() {
    limitDraw = new LimitedDrawKlondike(0);
    limitDraw.startGame(limitedDeck, false, 9, 1);
    for (int i = 0; i < 8; i += 1) {
      limitDraw.discardDraw();
    }
    Assert.assertTrue(limitDraw.getDrawCards().isEmpty());
  }

  // test moving with basic klondike rules
  @Test (expected = IllegalStateException.class)
  public void testDifferentColor() {
    whiteHead.startGame(whiteDeck, false, 3, 1);
    whiteHead.movePile(0, 1, 2);
  }

  // test moving draw card logic
  @Test
  public void testDrawToPileThenFoundation() {
    List<String> strings = new ArrayList<>();
    strings.add("A♡");
    strings.add("2♡");
    List<Card> tailored = this.cardFinder(strings);
    whiteHead.startGame(tailored, false, 1, 1);
    whiteHead.moveToFoundation(0, 0);
    whiteHead.moveDraw(0);
    whiteHead.moveToFoundation(0, 0);
    Assert.assertEquals(whiteHead.getScore(), 2);
  }

  @Test
  public void testMoveUntilEmpty() {
    List<String> strings = new ArrayList<>();
    strings.add("A♣");
    strings.add("A♡");
    strings.add("A♠");
    strings.add("A♢");
    strings.add("2♣");
    strings.add("2♡");
    strings.add("2♠");
    strings.add("2♢");
    List<Card> tailored = this.cardFinder(strings);
    whiteHead.startGame(tailored, false, 3, 1);
    whiteHead.moveToFoundation(1, 0);
    whiteHead.movePile(1, 1, 2);
    Assert.assertFalse(whiteHead.isGameOver());
  }

  @Test (expected = IllegalStateException.class)
  public void testMoveBuiltPile() {
    List<String> strings = new ArrayList<>();
    strings.add("3♣");
    strings.add("2♣");
    strings.add("A♠");
    strings.add("3♠");
    strings.add("A♣");
    strings.add("2♠");
    List<Card> tailored = this.cardFinder(strings);
    whiteHead.startGame(tailored, false, 2, 1);
    whiteHead.movePile(1, 2, 0);
  }

  private List<Card> cardFinder(List<String> strings) {
    List<Card> tailored = new ArrayList<>();
    for (String s : strings) {
      for (Card c : whiteDeck) {
        if (c.toString().equals(s)) {
          tailored.add(c);
        }
      }
    }
    return tailored;
  }
}