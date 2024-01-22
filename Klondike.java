package cs3500.klondike;

import java.io.InputStreamReader;

import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;

import cs3500.klondike.model.hw04.KlondikeCreator;
import cs3500.klondike.model.hw04.KlondikeCreator.GameType;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

/**
 * functions as the access point to the game using main.
 */
public final class Klondike {

  /**
   * main function as the access point to the control model view.
   * @param args String arguments to start a specific game
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("No inputs");
    }
    GameType gameType;
    KlondikeModel game;
    switch (args[0]) {
      case "basic":
        gameType = GameType.BASIC;
        break;
      case "whitehead":
        gameType = GameType.WHITEHEAD;
        break;
      case "limited":
        gameType = GameType.LIMITED;

        break;
      default:
        throw new IllegalArgumentException("Not valid game type");
    }
    game = KlondikeCreator.create(gameType);
    int cascade = 7;
    int numDraw = 3;
    int reDraw = 2;
    if (args.length > 1) {
      for (int i = 1; i < args.length; i += 1) {
        int input;
        try {
          input = Integer.parseInt(args[i]);
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Not in integer format");
        }
        if (gameType.equals(GameType.LIMITED)) {
          switch (i) {
            case 1:
              reDraw = input;
              break;
            case 2:
              cascade = input;
              break;
            case 3:
              numDraw = input;
              break;
            default:
              break;
          }
          game = new LimitedDrawKlondike(reDraw);
        } else {
          if (i == 1) {
            cascade = input;
          } else if (i == 2) {
            numDraw = input;
          }
        }
      }
    }
    new KlondikeTextualController(new InputStreamReader(System.in),
            System.out).playGame(game, game.getDeck(),
            false, cascade, numDraw);
  }
}
