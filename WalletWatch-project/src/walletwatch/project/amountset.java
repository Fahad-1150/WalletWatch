/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license/default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package walletwatch.project;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author MERAZ IT
 */
public class amountset {
    public SimpleIntegerProperty id;
    public SimpleStringProperty expense_category;
    public SimpleStringProperty date;
    public SimpleDoubleProperty amount;

    public amountset(int id, String expense_category, String date, double amount) {
        this.id = new SimpleIntegerProperty(id);
        this.expense_category = new SimpleStringProperty(expense_category);
        this.date = new SimpleStringProperty(date);
        this.amount = new SimpleDoubleProperty(amount);
    }

    public int getId() {
        return id.get();
    }

    public String getExpense_category() {
        return expense_category.get();
    }

    public String getDate() {
        return date.get();
    }

    public double getAmount() {
        return amount.get();
    }
}
