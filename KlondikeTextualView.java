package cs3500.klondike.view;

import java.io.IOException;

import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class KlondikeTextualView implements TextView {
  private final KlondikeModel model;
  private Appendable a;

  public KlondikeTextualView(KlondikeModel model) {
    this.model = model;
  }

  /**
   * second constructor.
   * @param model model
   * @param a appendable
   */
  public KlondikeTextualView(KlondikeModel model, Appendable a) {
    this.model = model;
    this.a = a;
  }

  /**
   * appends a model's string representation to an Appendable.
   * @throws IOException if append fails
   */
  public void render() throws IOException {
    try {
      this.a.append(model.toString());
    } catch (IOException ioe) {
      throw new IOException("Append failed", ioe);
    }
  }

  /**
   * the string representation of a game state.
   * @return a string representation of a game state
   */
  // Your implementation goes here
  public String toString() {
    String running = "Draw: ";
    running += this.drawHelper();
    running += "\nFoundation:";
    running += this.foundationHelper();
    running += this.cascadeHelper();
    return running;
  }

  private String drawHelper() {
    String running = "";
    for (int i = 0; i < this.model.getDrawCards().size(); i += 1) {
      running += this.model.getDrawCards().get(i).toString();
      if (i != this.model.getNumDraw() - 1) {
        running += ",";
        running += " ";
      }
    }
    return running;
  }

  private String foundationHelper() {
    String running = "";
    for (int j = 0; j < this.model.getNumFoundations(); j += 1) {
      running += " ";
      if (this.model.getCardAt(j) == null) {
        running += "<none>";
      } else {
        running += this.model.getCardAt(j).toString();
      }
      if (j != this.model.getNumFoundations() - 1) {
        running += ",";
      }
    }
    return running;
  }

  private String cascadeHelper() {
    String running = "";
    for (int j = 0; j < this.model.getNumRows(); j += 1) {
      running += "\n";
      for (int i = 0; i < this.model.getNumPiles(); i += 1) {
        running += " ";
        if (j == 0 && this.model.getPileHeight(i) == 0) {
          running += " X";
        } else if (j > this.model.getPileHeight(i) - 1) {
          running += "  ";
        } else if (this.model.isCardVisible(i, j)) {
          if (this.model.getCardAt(i, j).toString().contains("10")) {
            running = running.substring(0, running.length() - 1);
          }
          running += this.model.getCardAt(i, j).toString();
        } else {
          running += " ?";
        }
      }
    }
    return running;
  }
}
