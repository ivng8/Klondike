package cs3500.klondike;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller examplar.
 */
public class ExamplarControllerTests {

  private KlondikeModel game;
  private KlondikeController controller;
  private List<Card> deck;
  Readable in;
  Appendable out;

  @Before
  public void setGame() {
    game = new BasicKlondike();
    deck = game.getDeck();
  }

  @Test (expected = IllegalStateException.class)
  public void testUnknownMove() {
    in = new StringReader("tft");
    out = new StringBuilder("tft");
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(game, deck, false, 7, 3);
  }

  @Test (expected = IllegalStateException.class)
  public void testAllowsIllegalArgument() {
    in = new StringReader("mpp 2 1 5");
    out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(game, deck, false, 4, 3);
  }

  @Test (expected = IllegalStateException.class)
  public void testMoveDrawOutOfBounds() {
    in = new StringReader("md 5");
    out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(game, deck, false, 4, 3);
  }

  @Test
  public void testCorrectMovement() {
    ArrayList<Card> temp = new ArrayList<>();
    temp.add(deck.get(4));
    temp.add(deck.get(1));
    temp.add(deck.get(0));
    temp.add(deck.get(5));
    in = new StringReader("md 1\nmpf 1 1\nq");
    out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(game, temp, false, 1, 1);
    Assert.assertFalse(out.toString().contains("Invalid move"));
  }

  @Test
  public void testGameOverQuit() {
    in = new StringReader("q");
    out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(game, deck.subList(0, 1), false, 1, 1);
    Assert.assertEquals(out.toString().split("\n").length, 10);
  }

  @Test
  public void testCorrectPileAndFoundation() {
    ArrayList<Card> temp = new ArrayList<>();
    temp.add(deck.get(8));
    temp.add(deck.get(9));
    temp.add(deck.get(10));
    temp.add(deck.get(11));
    temp.add(deck.get(4));
    temp.add(deck.get(7));
    temp.add(deck.get(6));
    temp.add(deck.get(5));
    temp.add(deck.get(0));
    temp.add(deck.get(1));
    temp.add(deck.get(2));
    temp.add(deck.get(3));
    in = new StringReader("mpf 4 1\nmpp 3 1 1\nmpf 1 1\nq");
    out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(game, temp, false, 4, 1);
    Assert.assertFalse(out.toString().contains("Invalid move"));
  }

  @Test
  public void testArgumentReading() {
    in = new StringReader("mpf 1 1 1 1 1 1 1\nmdf 2");
    out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(game, deck.subList(0, 2), false, 1, 1);
    Assert.assertTrue(out.toString().contains("You win"));
  }

  @Test (expected = IllegalStateException.class)
  public void nonIntInput() {
    in = new StringReader("mpf 1.5 1");
    out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(game, deck, false, 4, 3);
  }
}