package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * class that creates a Klondike model based off a game type.
 */
public class KlondikeCreator {

  /**
   * enum that describes the different Klondike models.
   */
  public enum GameType {
     BASIC,
     LIMITED,
     WHITEHEAD,
  }

  /**
   * creates a KlondikeModel with the given game type.
   * @param type the game type
   * @return the model
   */
  public static KlondikeModel create(GameType type) {
    switch (type) {
      case BASIC:
        return new BasicKlondike();
      case LIMITED:
        return new LimitedDrawKlondike(2);
      case WHITEHEAD:
        return new WhiteheadKlondike();
      default:
        throw new IllegalArgumentException("No valid game-type provided");
    }
  }
}
