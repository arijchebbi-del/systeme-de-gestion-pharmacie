package brainstorm.pharmacy_app.Utils;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class PdfReportGenerator {
    public static void generateReport(String title, ResultSet resultSet, String totalRev, String nbSales, String avgBasket) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(title.replace(" ", "_") + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                document.add(new Paragraph(title).setFontSize(22).setBold());
                document.add(new Paragraph("Generated on: " + java.time.LocalDateTime.now()));
                document.add(new Paragraph("\n"));

                document.add(new Paragraph("Financial Summary").setBold().setFontSize(14).setUnderline());
                document.add(new Paragraph("Total Revenue: " + totalRev));
                document.add(new Paragraph("Number of Sales: " + nbSales));
                document.add(new Paragraph("Average Basket: " + avgBasket));
                document.add(new Paragraph("\n--- Detailed Transaction List ---\n").setItalic());

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                Table table = new Table(columnCount);

                for (int i = 1; i <= columnCount; i++) {
                    table.addCell(new Paragraph(metaData.getColumnName(i)).setBold());
                }

                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        table.addCell(new Paragraph(resultSet.getString(i)));
                    }
                }
                document.add(table);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public static void generateReport(String title, ResultSet resultSet) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(title.replace(" ", "_") + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try {
                PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph(title).setFontSize(20).setBold());
                document.add(new Paragraph("Generated on: " + java.time.LocalDateTime.now()));
                document.add(new Paragraph("\n"));

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                Table table = new Table(columnCount);

                for (int i = 1; i <= columnCount; i++) {
                    table.addCell(new Paragraph(metaData.getColumnName(i)).setBold());
                }

                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        table.addCell(new Paragraph(resultSet.getString(i)));
                    }
                }

                document.add(table);
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}