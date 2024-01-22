package cs3500.klondike;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import java.util.List;

import org.junit.Test;
import org.junit.Before;

/**
 * test cases.
 */
public class ExamplarModelTests {

  private KlondikeModel game;
  private List<Card> cardList;

  @Before
  public void setGame() {
    game = new BasicKlondike();
    cardList = game.getDeck();
  }

  // too many cards to move from a pile
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCardNumberFromPile() {
    game.startGame(cardList, false, 5, 3);
    game.movePile(0, 2, 2);
  }

  // illegal movement pile to pile
  @Test(expected = IllegalStateException.class)
  public void testIllegalMovePileToPile() {
    game.startGame(cardList, false, 7, 7);
    game.movePile(0, 1, 1);
  }

  // same pile moved
  @Test(expected = IllegalArgumentException.class)
  public void testSamePileToPile() {
    game.startGame(cardList, false, 5, 3);
    game.movePile(3, 1, 3);
  }

  // different suites on foundation
  @Test(expected = IllegalStateException.class)
  public void testRightNumberWrongSuiteToFoundation() {
    game.startGame(cardList, false, 4, 3);
    game.moveToFoundation(0, 0);
    game.moveToFoundation(2, 0);
  }

  // foundation out of bounds
  @Test(expected = IllegalArgumentException.class)
  public void testFoundationPileOutOfBounds() {
    game.startGame(cardList, false, 4, 3);
    game.moveToFoundation(0, 4);
  }

  // draw onto dest pile
  @Test(expected = IllegalStateException.class)
  public void testIllegalDrawToPile() {
    game.startGame(cardList, false, 4, 3);
    game.moveDraw(0);
  }

  // empty foundation
  @Test(expected = IllegalStateException.class)
  public void testEmptyFoundationPile() {
    game.startGame(cardList, false, 4, 3);
    game.moveToFoundation(1, 0);
  }

  // drawtofoundation illegal movement
  @Test(expected = IllegalStateException.class)
  public void testIllegalDrawToFoundation() {
    game.startGame(cardList, false, 4, 3);
    game.moveDrawToFoundation(0);
  }

  // wrong number right suite on fountain
  @Test(expected = IllegalStateException.class)
  public void testWrongNumberRightSuiteToFoundation() {
    game.startGame(cardList, false, 4, 3);
    game.moveToFoundation(0, 0);
    game.moveToFoundation(1, 0);
    game.moveToFoundation(1, 1);
    game.moveToFoundation(3, 1);
  }

  // number to empty source
  @Test(expected = IllegalStateException.class)
  public void testInvalidNewSourcePileStart() {
    game.startGame(cardList, false, 4, 3);
    game.moveToFoundation(0, 0);
    game.movePile(3, 1, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalMoveSameSuite() {
    game.startGame(cardList, false, 4, 3);
    game.movePile(0, 1, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalMoveAscendingOrder() {
    game.startGame(cardList, false, 4, 4);
    game.movePile(3, 1, 1);
  }
}
