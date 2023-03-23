package budget;

public class Purchase {
  final PurchaseType type;
  final String name;
  final Double prise;

  Purchase(PurchaseType type, String name, Double prise) {
    this.name = name;
    this.prise = prise;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public Double getPrise() {
    return prise;
  }

  public String getMessage() {
    return String.format("%s $%.2f", name, prise);
  }

  public PurchaseType getType() {
    return type;
  }

}
