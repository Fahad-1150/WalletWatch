package walletwatch.project;

import javafx.beans.property.*;
import java.time.LocalDate;

public class ExpenseRecord {
    private final IntegerProperty id;
    private final StringProperty category;
    private final ObjectProperty<LocalDate> date;
    private final DoubleProperty amount;

    public ExpenseRecord(int id, String category, LocalDate date, double amount) {
        this.id = new SimpleIntegerProperty(id);
        this.category = new SimpleStringProperty(category);
        this.date = new SimpleObjectProperty<>(date);
        this.amount = new SimpleDoubleProperty(amount);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty categoryProperty() { return category; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public DoubleProperty amountProperty() { return amount; }
}
