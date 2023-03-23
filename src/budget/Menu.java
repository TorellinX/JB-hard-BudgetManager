package budget;

import java.util.Map;

public enum Menu {
  MAIN("Choose your action:\n",
      Map.of(
          1, ") Add income\n",
          2, ") Add purchase\n",
          3, ") Show list of purchases\n",
          4, ") Balance\n",
          5, ") Save\n",
          6, ") Load\n",
          7, ") Analyze (Sort)\n",
          0, ") Exit"),
      7),

  ADD("Choose the type of purchase\n",
      Map.of(
          1, ") Food\n",
          2, ") Clothes\n",
          3, ") Entertainment\n",
          4, ") Other\n",
          5, ") Back"),
      5),

  SHOW("Choose the type of purchases\n",
      Map.of(
          1, ") Food\n",
          2, ") Clothes\n",
          3, ") Entertainment\n",
          4, ") Other\n",
          5, ") All\n",
          6, ") Back"),
      6),

  SORT_MAIN("How do you want to sort?\n",
      Map.of(
          1, ") Sort all purchases\n",
          2, ") Sort by type\n",
          3, ") Sort certain type\n",
          4, ") Back"),
      4),
  SORT_CERTAIN("Choose the type of purchase\n",
      Map.of(
          1, ") Food\n",
          2, ") Clothes\n",
          3, ") Entertainment\n",
          4, ") Other"),
      4);

  final String prompt;
  final Map<Integer, String> items;
  final int max;

  Menu(String prompt, Map<Integer, String> items, int max) {
    this.prompt = prompt;
    this.items = items;
    this.max = max;
  }

  String getItem(int key) {
    return items.get(key);
  }

  boolean containsZeroItem() {
    return items.size() > max;
  }
}
