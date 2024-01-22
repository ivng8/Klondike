package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Utils class of helpful methods.
 */
public class Utils {
  HashMap<String, Integer> value;

  /**
   * empty constructor for a Utils class.
   */
  public Utils() {
    this.value = new HashMap<>();
    this.value.put("A", 1);
    this.value.put("2", 2);
    this.value.put("3", 3);
    this.value.put("4", 4);
    this.value.put("5", 5);
    this.value.put("6", 6);
    this.value.put("7", 7);
    this.value.put("8", 8);
    this.value.put("9", 9);
    this.value.put("10", 10);
    this.value.put("J", 11);
    this.value.put("Q", 12);
    this.value.put("K", 13);
  }

  /**
   * checks a card can move onto another card in cascade.
   * @param src the card that moves
   * @param destination the destination of the card moving
   * @return a boolean of if the card can move
   */
  public boolean validCascade(Card src, Card destination) {
    String dest = destination.toString();
    String source = src.toString();
    int j = this.value.get(dest.substring(0, dest.length() - 1))
            - this.value.get(source.substring(0, source.length() - 1));
    return (j == 1) && this.differentColor(source, dest);
  }

  /**
   * tells if the two cards representations as strings have different colors.
   * @param source the string of the card moving
   * @param dest the string of the card of the destination
   * @return a boolean of if the cards are different colors
   */
  public boolean differentColor(String source, String dest) {
    if (source.endsWith("♢") || source.endsWith("♡")) {
      return dest.endsWith("♣") || dest.endsWith("♠");
    } else {
      return dest.endsWith("♢") || dest.endsWith("♡");
    }
  }

  /**
   * tells if a card can move onto another card in cascade.
   * @param src the card that moves
   * @param destination the card getting moved onto
   * @return a boolean of if if is a valid move
   */
  public boolean validWhiteCascade(Card src, Card destination) {
    String dest = destination.toString();
    String source = src.toString();
    int j = this.value.get(dest.substring(0, dest.length() - 1))
            - this.value.get(source.substring(0, source.length() - 1));
    return (j == 1) && !this.differentColor(source, dest);
  }

  /**
   * checks if the cards in the list are the same suit.
   * @param cards list of cards to compare
   */
  public void sameSuit(List<Card> cards) {
    String suit = cards.get(0).toString().substring(cards.get(0).toString().length()
            - 2, cards.get(0).toString().length() - 1);
    for (Card c : cards) {
      if (!c.toString().contains(suit)) {
        throw new IllegalStateException("Pile are not the same suit");
      }
    }
  }

  /**
   * moves the card(s) from a list to list.
   * @param src the source pile of the card(s) to move
   * @param dest the destination pile of the card(s) to move
   * @param num the amount of cards to move
   */
  public void moveCard(List<Card> src, List<Card> dest, int num) {
    for (int i = 0; i < num; i += 1) {
      dest.add(src.get(src.size() - num));
      src.remove(src.size() - num);
    }
  }

  /**
   * checks a card can move onto another card in foundation.
   * @param src the card moving
   * @param dest the card to be moved on
   * @return a boolean on if it is a valid move
   */
  public boolean validFoundation(Card src, Card dest) {
    String destination = dest.toString();
    String source = src.toString();
    int i = this.value.get(source.substring(0, source.length() - 1)) -
            this.value.get(destination.substring(0, destination.length() - 1));
    return (i == 1) && source.endsWith(destination.
            substring(destination.length() - 1));
  }

  /**
   * abstracted illegal argument for out of Bounds List index.
   * @param src the list
   * @param index the index called
   */
  public void outOfBoundsList(List<Card> src, int index) {
    if (index < 0 || index > src.size() - 1) {
      throw new IllegalArgumentException("Invalid index");
    }
  }

  /**
   * abstracted illegal argument for out of Bounds List index.
   * @param src the array
   * @param index the index called
   */
  public void outOfBoundsArray(List<List<Card>> src, int index) {
    if (index < 0 || index > src.size() - 1) {
      throw new IllegalArgumentException("Invalid index");
    }
  }

  /**
   * checks if a deck is valid to play the game.
   * @param deck the deck
   * @return a boolean if the deck is valid
   */
  public boolean validDeck(List<Card> deck) {
    List<Card> copy = new ArrayList<>(deck);
    List<List<Card>> solution = new ArrayList<>();
    if (copy.stream().anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("Null card");
    }
    while (copy.stream().anyMatch(c -> c.toString().contains("A"))) {
      List<Card> current = new ArrayList<>();
      int looking = 1;
      String target1 = "";
      String target2 = "";
      for (int i = 0; i < copy.size(); i += 1) {
        if (looking == 1) {
          target1 = "A";
        } else if (looking == 11) {
          target1 = "J";
        } else if (looking == 12) {
          target1 = "Q";
        } else if (looking == 13) {
          target1 = "K";
        } else {
          target1 = looking + "";
        }
        String currentCard = copy.get(i).toString();
        if (currentCard.contains(target1 + target2)) {
          looking += 1;
          target2 = currentCard.substring(currentCard.length() - 1);
          current.add(copy.get(i));
          copy.remove(copy.get(i));
          i = -1;
        }
      }
      solution.add(current);
    }
    if (!copy.isEmpty()) {
      throw new IllegalArgumentException("empty");
    }
    for (int j = 1; j < solution.size(); j += 1) {
      if (solution.get(j).size() != solution.get(0).size()) {
        throw new IllegalArgumentException("size");
      }
    }
    return true;
  }

  /**
   * errors for different cases of cards moving in cascade.
   * @param moving card moving
   * @param dest the pile its moving to
   */
  public void cascadeErrors(Card moving, List<Card> dest) {
    if (dest.isEmpty()) {
      if (!moving.toString().substring(0, 1).equals("K")) {
        throw new IllegalStateException("Can't begin new pile");
      }
    } else {
      if (!this.validCascade(moving, dest.get(dest.size() - 1))) {
        throw new IllegalStateException("Invalid inputs for moving");
      }
    }
  }

  /**
   * checks if the card is a valid move in white head cascade.
   * @param moving card moving
   * @param dest the pile being moved onto
   */
  public void cascadeWhiteErrors(Card moving, List<Card> dest) {
    if (!dest.isEmpty()) {
      if (!new Utils().validWhiteCascade(moving, dest.get(dest.size() - 1))) {
        throw new IllegalStateException("Invalid inputs for moving");
      }
    }
  }

  /**
   * errors for different cases of cards moving to foundation.
   * @param moving card moving
   * @param dest the pile its moving to
   */
  public void foundationErrors(Card moving, List<Card> dest) {
    if (dest.isEmpty()) {
      if (!moving.toString().substring(0, 1).equals("A")) {
        throw new IllegalStateException("Can't begin new pile");
      }
    } else {
      if (!this.validFoundation(moving, dest.get(dest.size() - 1))) {
        throw new IllegalStateException("Invalid move");
      }
    }
  }
}
