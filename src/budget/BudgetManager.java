package budget;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BudgetManager {

  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  boolean running = true;
  double balance = 0.0;
  double total = 0.0;
  private static final File purchasesFile = new File("purchases.txt");

  void start() {
    FileManager.createFile(purchasesFile);
    while (running) {
      printMenu(Menu.MAIN);
      handleMenuInput();
    }
  }

  private void handleMenuInput() {
    int menuItem = getMenuInput(Menu.MAIN);
    System.out.println();
    switch (menuItem) {
      case 1 -> addIncome();
      case 2 -> addPurchase();
      case 3 -> showPurchases();
      case 4 -> showBalance();
      case 5 -> savePurchases();
      case 6 -> loadPurchases();
      case 7 -> analyzePurchases();
      case 0 -> exit();
      default -> throw new IllegalArgumentException("Error! Wrong menu action");
    }
    System.out.println();
  }

  private void printMenu(Menu type) {
    StringBuilder menu = new StringBuilder();
    menu.append(type.prompt);
    for (int i = 1; i <= type.max; i++) {
      menu.append(i).append(type.getItem(i));
    }
    if (type.containsZeroItem()) {
      menu.append(0).append(type.getItem(0));
    }
    System.out.println(menu);
  }

  private int getIntInput() {
    int intInput = -1;
    do {
      try {
        String str = getInput();
        intInput = Integer.parseInt(str);
      } catch (NumberFormatException e) {
        System.out.println("Error! Please, enter only a number for one menu item");
      }
    } while (intInput < 0);
    return intInput;
  }

  private double getDoubleInput() {
    double input = -1;
    do {
      try {
        String str = getInput();
        input = Double.parseDouble(str);
      } catch (NumberFormatException e) {
        System.out.println("Error! Please, enter only a positive number");
      }
    } while (input < 0);
    return input;
  }

  private String getInput() {
    String line;
    while (true) {
      try {
        line = reader.readLine().strip();
        if (line == null || line.isBlank()) {
          System.out.println("Error! Empty input. Try again.");
          continue;
        }
      } catch (IOException e) {
        continue;
      }
      return line;
    }
  }




  private int getMenuInput(Menu type) {
    int menuItemIndex = -1;
    while (true) {
      menuItemIndex = getIntInput();
      int minItem = type.containsZeroItem() ? 0 : 1;
      if (menuItemIndex < minItem || menuItemIndex > type.max) {
        System.out.println("Error! Please enter a number for the listed actions");
        continue;
      }
      return menuItemIndex;
    }
  }

  private void addIncome() {
    System.out.println("Enter income:");
    int income = -1;
    while (true) {
      income = getIntInput();
      if (income < 0) {
        System.out.println("Error! Enter only positive number for income!");
        continue;
      }
      break;
    }
    balance += income;
    System.out.println("Income was added!");
  }

  private void addPurchase() {
    final int MENU_BACK = 5;
    while (true) {
      printMenu(Menu.ADD);
      int menuInput = getMenuInput(Menu.ADD);
      if (menuInput == MENU_BACK) {
        return;
      }
      System.out.println();
      System.out.println("Enter purchase name:");
      String name = getInput();

      System.out.println("Enter its price:");
      double prise = getDoubleInput();
      PurchaseType purchaseType = PurchaseType.getPurchaseTypeByMenuIndex(menuInput);

      addPurchase(purchaseType, name, prise);

      System.out.println("Purchase was added!\n");
    }
  }

  private void addPurchase(PurchaseType type, String name, double prise) {
    total += prise;
    total = Math.round(total * 100.0) / 100.0;
    balance = balance - prise < 0 ? 0 : balance - prise;
    balance = Math.round(balance * 100.0) / 100.0;
    type.addPurchase(type, name, prise);
  }

  private void addPurchase(Purchase purchase) {
    addPurchase(purchase.getType(), purchase.getName(), purchase.getPrise());
  }

  private void showPurchases() {
    if (arePurchasesEmpty()) {
      System.out.println("The purchase list is empty!");
      return;
    }

    final int MENU_BACK = 6;
    final int MENU_ALL_PURCHASES = 5;

    while (true) {
      printMenu(Menu.SHOW);
      int menuInput = getMenuInput(Menu.SHOW);
      System.out.println();
      if (menuInput == MENU_BACK) {
        return;
      }
      String purchasesMsg;
      double localTotal;
      if (menuInput == MENU_ALL_PURCHASES) {
        System.out.println("All:");
        StringBuilder builder = new StringBuilder();
        for (PurchaseType purchaseType : PurchaseType.values()) {
          String messageOfOneType = purchaseType.getPurchasesMsg();
          if (!messageOfOneType.isBlank()) {
            builder.append(messageOfOneType).append("\n");
          }
        }
        if (!builder.isEmpty()) {
          builder.delete(builder.length() - 1, builder.length());
        }
        purchasesMsg = builder.toString();
        localTotal = total;
      } else {
        PurchaseType purchaseType = PurchaseType.getPurchaseTypeByMenuIndex(menuInput);
        System.out.println(purchaseType.getName() + ":");
        purchasesMsg = purchaseType.getPurchasesMsg();
        localTotal = purchaseType.getSum();
      }

      if (purchasesMsg.isBlank()) {
        System.out.println("The purchase list is empty!\n");
        continue;
      }

      System.out.println(purchasesMsg);
      System.out.printf("Total sum: $%.2f%n", localTotal);
      System.out.println();
    }

  }

  private boolean arePurchasesEmpty() {
    boolean isEmpty = true;
    for (PurchaseType purchaseType : PurchaseType.values()) {
      if (!purchaseType.isEmpty()) {
        isEmpty = false;
        break;
      }
    }
    return isEmpty;
  }

  private void showBalance() {
    System.out.printf("Balance: $%.2f%n", balance);
  }

  private void savePurchases() {
    String dataToSave;
    StringBuilder builderDataToSave = new StringBuilder();
    builderDataToSave.append(balance).append("\n");  // balance
    for (PurchaseType purchaseType : PurchaseType.values()) {
      String textOfOneType = purchaseType.getMsgForSaving();
      builderDataToSave.append(textOfOneType);
      if (!textOfOneType.isBlank()) {
        builderDataToSave.append("\n");
      }
    }
    if (!builderDataToSave.isEmpty()) {
      builderDataToSave.delete(builderDataToSave.length() - 1, builderDataToSave.length());
    }

    dataToSave = builderDataToSave.toString();
    try {
      FileManager.saveToFile(dataToSave, purchasesFile);
      System.out.println("Purchases were saved!");
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  private void loadPurchases() {
    ArrayList<String> loadedData;
    try {
      loadedData = FileManager.loadFromFile(purchasesFile);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return;
    }
    if (loadedData.isEmpty()) {
      System.out.println("No saved purchases found.");
      return;
    }

    double newBalance;
    String newBalanceString = loadedData.remove(0);

    if (newBalanceString == null) {
      System.out.println("No saved balance found.");
      return;
    }

    try {
      newBalance = Double.parseDouble(newBalanceString);
    } catch (NumberFormatException e) {
      System.out.println("Loading error, wrong balance format.");
      return;
    }

    ArrayList<Purchase> loadedPurchases = new ArrayList<>();
    for (String purchaseMsg : loadedData) {
      if (purchaseMsg.isBlank()) {
        continue;
      }
      try {
        Purchase loadingPurchase = parsePurchase(purchaseMsg);
        loadedPurchases.add(loadingPurchase);
      } catch (IOException e) {
        System.out.println(e.getMessage());
        return;
      }
    }

    deleteAllPurchases();
    balance = newBalance;

    for (Purchase purchase : loadedPurchases) {
      loadOnePurchase(purchase);
    }

    System.out.println("Purchases were loaded!");
  }

  private Purchase parsePurchase(String purchaseMsg) throws IOException {
    if (purchaseMsg == null || purchaseMsg.isBlank()) {
      throw new IOException("Error, purchase data is null.");
    }
    if (purchaseMsg.isBlank()) {
      throw new IOException("Error, purchase data is empty.");
    }
    String typeToken = purchaseMsg.substring(0, purchaseMsg.indexOf(' '));
    String priceToken = purchaseMsg.substring(purchaseMsg.lastIndexOf('$') + 1);
    String name = purchaseMsg.substring(purchaseMsg.indexOf(' '), purchaseMsg.lastIndexOf('$'))
        .strip();
    double prise;
    try {
      prise = Double.parseDouble(priceToken);
    } catch (NumberFormatException e) {
      throw new IOException("Parsing error, wrong prise format.");
    }
    PurchaseType type;
    try {
      type = PurchaseType.valueOf(typeToken.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IOException("Parsing error, wrong type format.");
    }
    return new Purchase(type, name, prise);
  }

  private void loadOnePurchase(Purchase purchase) {
    if (purchase == null) {
      throw new IllegalArgumentException("Error! The purchase is null");
    }
    total += purchase.getPrise();
    total = Math.round(total * 100.0) / 100.0;
    purchase.getType().addPurchase(purchase.getType(), purchase.getName(), purchase.getPrise());
  }

  private void deleteAllPurchases() {
    PurchaseType.deleteAllPurchases();
    total = 0;
  }

  private void analyzePurchases() {
    while (true) {
      printMenu(Menu.SORT_MAIN);
      int menuItem = getMenuInput(Menu.SORT_MAIN);
      System.out.println();
      switch (menuItem) {
        case 1:
          sortAll();
          break;
        case 2:
          sortByType();
          break;
        case 3:
          sortCertainType();
          break;
        case 4:
          return;
        default:
          throw new IllegalArgumentException("Error! Wrong menu input");
      }
      System.out.println();
    }
  }

  private void sortAll() {
    if (arePurchasesEmpty()) {
      System.out.println("The purchase list is empty!");
      System.out.println();
      return;
    }
    StringBuilder builder = new StringBuilder();
    builder.append("All:\n");
    ArrayList<Purchase> allPurchases = new ArrayList<>();
    for (PurchaseType type : PurchaseType.values()) {
      allPurchases.addAll(type.getPurchases());
    }
    allPurchases.sort(Comparator.comparing(Purchase::getPrise).reversed());
    for (Purchase purchase : allPurchases) {
      builder.append(purchase.getMessage());
      builder.append("\n");
    }

    builder.append("Total: $").append(total);
    System.out.println(builder);
  }

  private void sortByType() {
    ArrayList<PurchaseType> types = new ArrayList<>(Arrays.asList(PurchaseType.values()));
    types.sort(Comparator.comparing(PurchaseType::getSum).reversed());
    StringBuilder msg = new StringBuilder();
    msg.append("Types:\n");
    for (PurchaseType type : types) {
      double sum = type.getSum();
      msg.append(type.getName()).append(" - $").append(sum == 0 ? 0 : String.format("%.2f", sum));
      msg.append("\n");
    }
    msg.append("Total sum: $").append(total == 0 ? 0 : String.format("%.2f", total));
    System.out.println(msg);
  }

  private void sortCertainType() {
    printMenu(Menu.SORT_CERTAIN);
    int menuInput = getMenuInput(Menu.SORT_CERTAIN);
    System.out.println();

    PurchaseType purchaseType = PurchaseType.getPurchaseTypeByMenuIndex(menuInput);
    if (purchaseType.isEmpty()) {
      System.out.println("The purchase list is empty!");
      System.out.println();
      return;
    }
    StringBuilder builder = new StringBuilder();
    builder.append(purchaseType.getName()).append(":\n");
    ArrayList<Purchase> purchaseOfCertainType = purchaseType.getPurchases();
    purchaseOfCertainType.sort(Comparator.comparing(Purchase::getPrise).reversed());
    for (Purchase purchase : purchaseOfCertainType) {
      builder.append(purchase.getMessage());
      builder.append("\n");
    }
    builder.append("Total sum: $").append(purchaseType.getSum());
    System.out.println(builder);
  }

  private void exit() {
    running = false;
    System.out.println("Bye!");
  }
}