package cs3500.klondike.controller;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Scanner;

/**
 * Controller for the BasicKlondike.
 */
public class KlondikeTextualController implements KlondikeController {
  Scanner in;
  Appendable out;
  boolean quit;

  /**
   * constructor for the controller.
   * @param r readable input
   * @param a appendable output
   */
  public KlondikeTextualController(Readable r, Appendable a) {
    if (r == null || a == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.in = new Scanner(r);
    this.out = a;
    this.quit = false;
  }

  @Override
  public void playGame(KlondikeModel model, List<Card> deck,
                       boolean shuffle, int numPiles, int numDraw) {
    if (model == null) {
      throw new IllegalArgumentException("model can't be null");
    }
    if (deck.isEmpty()) {
      throw new IllegalStateException();
    }
    model.startGame(deck, shuffle, numPiles, numDraw);
    String view = new KlondikeTextualView(model).toString();
    this.tryAppend(view + "\nScore: 0\n");
    while (!model.isGameOver() && !this.quit) {
      switch (this.in.next()) {
        case "Q":
        case "q":
          this.gameQuit(model);
          return;
        case "mpp":
          this.mPilePile(model);
          break;
        case "md":
          this.mDraw(model);
          break;
        case "mpf":
          this.mPileFoundation(model);
          break;
        case "mdf":
          this.mDrawFoundation(model);
          break;
        case "dd":
          this.dDraw(model);
          break;
        default:
          this.tryAppend("Invalid move. Play again. Please put valid move.\n");
      }
    }
    if (this.quit) {
      this.gameQuit(model);
      return;
    }
    if (model.getScore() == deck.size()) {
      this.tryAppend("You win! " + view);
    } else {
      this.tryAppend("Game over. Score: " + model.getScore() + "\n" + view);
    }
  }

  private void tryAppend(String s) {
    try {
      this.out.append(s);
    } catch (IOException ioe) {
      throw new IllegalStateException("Append failed", ioe);
    }
  }

  private void gameQuit(KlondikeModel model) {
    String output = "Game quit!\nState of game when quit:\n";
    output += new KlondikeTextualView(model).toString() +
            "\nScore: " + model.getScore() + "\n";
    this.tryAppend(output);
  }

  private void mPilePile(KlondikeModel model) {
    ArrayList<Integer> inputs = new ArrayList<>();
    for (int i = 0; i < 3; i += 1) {
      if (this.in.hasNextInt()) {
        inputs.add(this.in.nextInt() - 1);
      } else if (this.in.next().equals("q") || this.in.next().equals("Q")) {
        this.quit = true;
        return;
      }
    }
    if (inputs.size() != 3) {
      this.tryAppend("Invalid move. Play again. Not valid\n");
      return;
    }
    try {
      model.movePile(inputs.get(0), inputs.get(1) + 1, inputs.get(2));
      String output = new KlondikeTextualView(model).toString() +
              "\nScore:" + model.getScore() + "\n";
      this.tryAppend(output);
    } catch (IllegalStateException ioe) {
      this.tryAppend("Invalid move. Play again. Not valid\n");
    } catch (IllegalArgumentException ioe) {
      this.tryAppend("Invalid move. Play again. Not valid inputs\n");
    }
  }

  private void mDraw(KlondikeModel model) {
    ArrayList<Integer> inputs = new ArrayList<>();
    for (int i = 0; i < 1; i += 1) {
      if (this.in.hasNextInt()) {
        inputs.add(this.in.nextInt() - 1);
      } else if (this.in.next().equals("q") || this.in.next().equals("Q")) {
        this.quit = true;
        return;
      }
    }
    if (inputs.size() != 1) {
      this.tryAppend("Invalid move. Play again. Not valid\n");
      return;
    }
    try {
      model.moveDraw(inputs.get(0));
      String output = new KlondikeTextualView(model).toString() +
              "\nScore:" + model.getScore() + "\n";
      this.tryAppend(output);
    } catch (IllegalStateException ioe) {
      this.tryAppend("Invalid move. Play again. Not valid\n");
    } catch (IllegalArgumentException ioe) {
      this.tryAppend("Invalid move. Play again. Not valid input(s)\n");
    }
  }

  private void mPileFoundation(KlondikeModel model) {
    ArrayList<Integer> inputs = new ArrayList<>();
    for (int i = 0; i < 2; i += 1) {
      if (this.in.hasNextInt()) {
        inputs.add(this.in.nextInt() - 1);
      } else if (this.in.next().equals("q") || this.in.next().equals("Q")) {
        this.quit = true;
        return;
      }
    }
    if (inputs.size() != 2) {
      this.tryAppend("Invalid move. Play again. Not valid\n");
      return;
    }
    try {
      model.moveToFoundation(inputs.get(0), inputs.get(1));
      String output = new KlondikeTextualView(model).toString() +
              "\nScore:" + model.getScore() + "\n";
      this.tryAppend(output);
    } catch (IllegalStateException ioe) {
      this.tryAppend("Invalid move. Play again. Not valid\n");
    } catch (IllegalArgumentException ioe) {
      this.tryAppend("Invalid move. Play again. Not valid input(s)\n");
    }
  }

  private void mDrawFoundation(KlondikeModel model) {
    ArrayList<Integer> inputs = new ArrayList<>();
    for (int i = 0; i < 1; i += 1) {
      if (this.in.hasNextInt()) {
        inputs.add(this.in.nextInt() - 1);
      } else if (this.in.next().equals("q") || this.in.next().equals("Q")) {
        this.quit = true;
        return;
      }
    }
    if (inputs.size() != 1) {
      this.tryAppend("Invalid move. Play again. Not valid\n");
      return;
    }
    try {
      model.moveDrawToFoundation(inputs.get(0));
      String output = new KlondikeTextualView(model).toString() +
              "\nScore:" + model.getScore() + "\n";
      this.tryAppend(output);
    } catch (IllegalStateException ioe) {
      this.tryAppend("Invalid move. Play again. Not valid\n");
    } catch (IllegalArgumentException ioe) {
      this.tryAppend("Invalid move. Play again. Not valid input(s)\n");
    }
  }

  private void dDraw(KlondikeModel model) {
    try {
      model.discardDraw();
      String output = new KlondikeTextualView(model).toString() +
              "\nScore:" + model.getScore() + "\n";
      this.tryAppend(output);
    } catch (IllegalStateException ioe) {
      this.tryAppend("Invalid move. Play again. Not valid\n");
    }
  }
}