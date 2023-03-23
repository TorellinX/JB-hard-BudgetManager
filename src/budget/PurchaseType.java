package budget;

import java.util.ArrayList;
import java.util.List;

public enum PurchaseType {
  FOOD("Food"), CLOTHES("Clothes"), ENTERTAINMENT("Entertainment"), OTHER("Other");

  private final String name;
  private double sum;
  private final List<Purchase> purchases;

  PurchaseType(String name) {
    this.name = name;
    this.sum = 0;
    this.purchases = new ArrayList<>();
  }

  double getSum() {
    return this.sum;
  }

  String getName() {
    return this.name;
  }

  ArrayList<Purchase> getPurchases() {
    return new ArrayList<>(purchases);
  }

  String getPurchasesMsg() {
    if (purchases.isEmpty()) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (Purchase purchase : purchases) {
      builder.append(purchase.getMessage()).append("\n");
    }
    builder.delete(builder.length() - 1, builder.length());
    return builder.toString();
  }

  String getMsgForSaving() {
    if (purchases.isEmpty()) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (Purchase purchase : purchases) {
      builder.append(this.getName()).append(" ").append(purchase.getMessage()).append("\n");
    }
    builder.delete(builder.length() - 1, builder.length());
    return builder.toString();
  }

  void addPurchase(PurchaseType type, String name, double prise) {
    purchases.add(new Purchase(type, name, prise));
    sum += prise;
    sum = Math.round(sum * 100.0) / 100.0;
  }

  static PurchaseType getPurchaseTypeByMenuIndex(int index) {
    if (index < 1 || index > PurchaseType.values().length) {
      throw new IllegalArgumentException("Wrong purchase type index: " + index);
    }
    return PurchaseType.values()[index - 1];
  }

  boolean isEmpty() {
    return purchases.isEmpty();
  }

  static void deleteAllPurchases() {
    for (PurchaseType type : values()) {
      type.purchases.clear();
      type.sum = 0;
    }
  }
}
