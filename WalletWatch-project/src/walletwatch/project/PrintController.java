package walletwatch.project;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

public class PrintController implements Initializable {

    // Reference to the print button in the UI
    @FXML
    private Button printbt;

    // Variables to store the username and selected date range
    private String username;
    private LocalDate fromDate;
    private LocalDate toDate;

    // This method runs when the controller is initialized (here we don't need to do anything)
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code can go here if needed
    }

    // This method receives username and date inputs from the previous UI page
    public void setdata(String username, TextField toDateField, TextField fromDateField) {
        this.username = username;

        try {
            // Parse the date strings into LocalDate objects
            this.fromDate = LocalDate.parse(fromDateField.getText());
            this.toDate = LocalDate.parse(toDateField.getText());
        } catch (DateTimeParseException e) {
            // Show an error alert if the date format is wrong
            showAlert(Alert.AlertType.ERROR, "Invalid Date", "Invalid Date Format", "Please enter dates in this format: YYYY-MM-DD");
        }
    }

    // This method is called when the print button is clicked
    @FXML
    private void printaction(ActionEvent event) {
        // Check if dates are set before proceeding
        if (fromDate == null || toDate == null) {
            showAlert(Alert.AlertType.ERROR, "Date Error", null, "Please set valid dates before printing.");
            return;
        }

        try {
            // Prepare the folder where the PDF will be saved
            String folderPath = "F:\\CSE acaedmic SMUCT\\7th";
            File folder = new File(folderPath);
            if (!folder.exists() && !folder.mkdirs()) {
                showAlert(Alert.AlertType.ERROR, "Folder Error", null, "Could not create folder:\n" + folderPath);
                return;
            }
            // File name for the PDF report
            String fileName = folderPath + "\\WalletWatch_Report_" + username + ".pdf";

            // Connect to the database (you must have your DatabaseConnection class setup)
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                showAlert(Alert.AlertType.ERROR, "Database Error", null, "Could not connect to database.");
                return;
            }

            // Calculate total income from income table
            double totalIncome = calculateTotalAmount(conn, "income_" + username);
            double totalExpenses = 0;
            Map<String, Double> expenseByCategory = new HashMap<>();

            // List of expense categories (table name prefixes)
            String[] expenseTables = {"education_", "living_", "transport_", "food_", "others_"};

            // Calculate total expenses for each category and add to the map
            for (String prefix : expenseTables) {
                double catTotal = calculateTotalAmount(conn, prefix + username);
                expenseByCategory.put(prefix.replace("_", ""), catTotal);
                totalExpenses += catTotal;
            }

            // Create the PDF document and writer
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Fonts for titles and normal text
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

            // Add report title and user/date info
            document.add(new Paragraph("Wallet Watch Report", headerFont));
            document.add(new Paragraph("User: " + username, normalFont));
            document.add(new Paragraph("From: " + fromDate + " To: " + toDate, normalFont));
            document.add(new Paragraph("\n")); // empty line for spacing

            // Add Income vs Expense pie chart title and chart image
            document.add(createCenteredParagraph("Income vs Expense Pie Chart", headerFont));
            Image pieChartImage = createPieChart(totalIncome, totalExpenses);
            if (pieChartImage != null) {
                pieChartImage.scaleToFit(400, 300);
                document.add(pieChartImage);
            }
            document.add(Chunk.NEWLINE); // add space after chart

            // Add Expense Category % bar chart title and chart image
            document.add(createCenteredParagraph("Expense Category Percentage Bar Chart", headerFont));
            Image barChartImage = createBarChart(expenseByCategory, totalExpenses);
            if (barChartImage != null) {
                barChartImage.scaleToFit(500, 300);
                document.add(barChartImage);
            }
            document.add(Chunk.NEWLINE); // add space after chart

            // Add expense tables for each category
            for (String prefix : expenseTables) {
                String catName = prefix.replace("_", "").toUpperCase() + " EXPENSES";
                document.add(createCenteredParagraph(catName, headerFont));
                addExpenseTable(document, conn, prefix + username);
                document.add(Chunk.NEWLINE);
            }

            // Add income table
            document.add(createCenteredParagraph("INCOME", headerFont));
            addIncomeTable(document, conn, "income_" + username);

            // Close document and connection
            document.close();
            conn.close();

            // Show success message with PDF location
            showAlert(Alert.AlertType.INFORMATION, "PDF Generated", null, "Report generated successfully:\n" + fileName);

            // Try to open the PDF automatically
            try {
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(new File(fileName));
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.WARNING, "Warning", null, "PDF generated but could not be auto-opened.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "PDF Generation Failed", null, e.getMessage());
        }
    }

    // Helper method to calculate total amount from a table between dates
    private double calculateTotalAmount(Connection conn, String tableName) throws SQLException {
        String query = "SELECT SUM(amount) as total FROM " + tableName + " WHERE date BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(fromDate));
            stmt.setDate(2, Date.valueOf(toDate));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
            rs.close();
        } catch (SQLException e) {
            // If table doesn't exist or error happens, just return 0 to avoid crash
            System.err.println("Error calculating total for table " + tableName + ": " + e.getMessage());
        }
        return 0;
    }

    // Create a pie chart image of Income vs Expense
    private Image createPieChart(double totalIncome, double totalExpenses) {
        try {
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Income", totalIncome);
            dataset.setValue("Expenses", totalExpenses);

            JFreeChart chart = ChartFactory.createPieChart(
                    "Income vs Expense",
                    dataset,
                    true, true, false);

            // Convert chart to image file and then to iText Image
            BufferedImage bufferedImage = chart.createBufferedImage(500, 300);
            File tempFile = File.createTempFile("piechart", ".png");
            ImageIO.write(bufferedImage, "png", tempFile);
            Image image = Image.getInstance(tempFile.getAbsolutePath());
            tempFile.deleteOnExit();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // return null if any error
        }
    }

    // Create a bar chart image showing expense percentage per category
    private Image createBarChart(Map<String, Double> expenseByCategory, double totalExpenses) {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            if (totalExpenses == 0) totalExpenses = 1; // avoid division by zero

            // Calculate % and add to dataset
            for (Map.Entry<String, Double> entry : expenseByCategory.entrySet()) {
                double percent = (entry.getValue() / totalExpenses) * 100;
                dataset.addValue(percent, "Expense %", entry.getKey());
            }

            JFreeChart barChart = ChartFactory.createBarChart(
                    "Expense Percentage by Category",
                    "Category",
                    "Percentage (%)",
                    dataset,
                    PlotOrientation.VERTICAL,
                    false, true, false);

            // Convert chart to image file and then to iText Image
            BufferedImage bufferedImage = barChart.createBufferedImage(600, 300);
            File tempFile = File.createTempFile("barchart", ".png");
            ImageIO.write(bufferedImage, "png", tempFile);
            Image image = Image.getInstance(tempFile.getAbsolutePath());
            tempFile.deleteOnExit();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // return null if any error
        }
    }

    // Add expense table data from database to PDF
    private void addExpenseTable(Document document, Connection conn, String tableName) throws SQLException, DocumentException {
        String query = "SELECT * FROM " + tableName + " WHERE date BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(fromDate));
            stmt.setDate(2, Date.valueOf(toDate));
            ResultSet rs = stmt.executeQuery();

            PdfPTable pdfTable = new PdfPTable(4); // 4 columns
            pdfTable.setWidthPercentage(100);
            pdfTable.addCell(createCenteredCell("ID"));
            pdfTable.addCell(createCenteredCell("Category"));
            pdfTable.addCell(createCenteredCell("Date"));
            pdfTable.addCell(createCenteredCell("Amount"));

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                pdfTable.addCell(String.valueOf(rs.getInt("id")));
                pdfTable.addCell(rs.getString("expense_category"));
                pdfTable.addCell(rs.getDate("date").toString());
                pdfTable.addCell(String.valueOf(rs.getDouble("amount")));
            }

            if (hasData) {
                document.add(pdfTable);
            } else {
                // Show message if no data found
                document.add(new Paragraph("No data found for this category.\n"));
            }

            rs.close();
        }
    }

    // Add income table data from database to PDF
    private void addIncomeTable(Document document, Connection conn, String tableName) throws SQLException, DocumentException {
        String query = "SELECT * FROM " + tableName + " WHERE date BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(fromDate));
            stmt.setDate(2, Date.valueOf(toDate));
            ResultSet rs = stmt.executeQuery();

            PdfPTable pdfTable = new PdfPTable(4); // 4 columns
            pdfTable.setWidthPercentage(100);
            pdfTable.addCell(createCenteredCell("ID"));
            pdfTable.addCell(createCenteredCell("Category"));
            pdfTable.addCell(createCenteredCell("Date"));
            pdfTable.addCell(createCenteredCell("Amount"));

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                pdfTable.addCell(String.valueOf(rs.getInt("id")));
                pdfTable.addCell(rs.getString("income_category"));
                pdfTable.addCell(rs.getDate("date").toString());
                pdfTable.addCell(String.valueOf(rs.getDouble("amount")));
            }

            if (hasData) {
                document.add(pdfTable);
            } else {
                // Show message if no income data found
                document.add(new Paragraph("No income data found.\n"));
            }

            rs.close();
        }
    }

    // Create table cells with centered text
    private PdfPCell createCenteredCell(String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5); // add some padding for readability
        return cell;
    }

    // Create paragraphs with centered alignment and spacing after
    private Paragraph createCenteredParagraph(String text, Font font) {
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(10f); // space after paragraph
        return p;
    }

    // Show alert messages (errors, info, warnings)
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
