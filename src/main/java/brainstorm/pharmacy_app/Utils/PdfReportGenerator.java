package brainstorm.pharmacy_app.Utils;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.itextpdf.layout.element.Image;
import java.net.URL;
import java.util.List;

public class PdfReportGenerator {
    //Surcharge du méthode generateReport
    //Revenue Report
    public static void generateReport(String title, ResultSet resultSet, String totalRev, String nbSales, String avgBasket) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(title.replace(" ", "_") + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                 PdfDocument pdf = new PdfDocument(writer)) {
                Document document = new Document(pdf);
                Table headerTable = new Table(UnitValue.createPercentArray(new float[]{2, 6, 2})).useAllAvailableWidth();
                headerTable.setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);

                //Logo
                try {
                    URL logoUrl = PdfReportGenerator.class.getResource("/images/DashbordAppLogo.png");
                    if (logoUrl != null) {
                        Image logo = new Image(com.itextpdf.io.image.ImageDataFactory.create(logoUrl));
                        logo.setWidth(100);
                        headerTable.addCell(new com.itextpdf.layout.element.Cell()
                                .add(logo)
                                .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE)
                                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                    }
                } catch (Exception e) {
                    headerTable.addCell(new com.itextpdf.layout.element.Cell().setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                }

                //Title
                headerTable.addCell(new com.itextpdf.layout.element.Cell()
                        .add(new Paragraph(title).setFontSize(18).setBold())
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                        .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE)
                        .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));

                //Date
                String formattedDate = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                headerTable.addCell(new com.itextpdf.layout.element.Cell()
                        .add(new Paragraph("Generated on:\n" + formattedDate).setFontSize(9))
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT)
                        .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE)
                        .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));

                document.add(headerTable);
                document.add(new Paragraph("\n"));


                document.add(new Paragraph("Financial Summary").setBold().setFontSize(12).setUnderline());
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

    //Suppliers Report
    public static void generateReport(String title, ResultSet resultSet) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(title.replace(" ", "_") + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                 PdfDocument pdf = new PdfDocument(writer)) {

                pdf.setDefaultPageSize(com.itextpdf.kernel.geom.PageSize.A4.rotate());
                Document document = new Document(pdf);
                Table headerTable = new Table(UnitValue.createPercentArray(new float[]{2, 6, 2})).useAllAvailableWidth();
                headerTable.setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);
                //Logo
                try {
                    URL logoUrl = PdfReportGenerator.class.getResource("/images/DashbordAppLogo.png");
                    if (logoUrl != null) {
                        Image logo = new Image(com.itextpdf.io.image.ImageDataFactory.create(logoUrl));
                        logo.setWidth(100);
                        headerTable.addCell(new com.itextpdf.layout.element.Cell()
                                .add(logo)
                                .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE)
                                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                    }
                } catch (Exception e) {
                    headerTable.addCell(new com.itextpdf.layout.element.Cell().setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                }
                //Title
                headerTable.addCell(new com.itextpdf.layout.element.Cell()
                        .add(new Paragraph(title).setFontSize(25).setBold())
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                        .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE)
                        .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                //Date
                String formattedDate = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                headerTable.addCell(new com.itextpdf.layout.element.Cell()
                        .add(new Paragraph("Generated on:\n" + formattedDate).setFontSize(9))
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT)
                        .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE)
                        .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));

                document.add(headerTable);
                document.add(new Paragraph("\n"));


                float[] columnWidths = {2.5f, 2.5f, 4.5f, 2.5f, 1.5f, 1f, 1f, 2f};
                Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i <= 8; i++) {
                    table.addHeaderCell(new com.itextpdf.layout.element.Cell()
                            .add(new Paragraph(metaData.getColumnLabel(i)).setBold().setFontSize(10)));
                }

                while (resultSet.next()) {
                    for (int i = 1; i <= 8; i++) {
                        Object val = resultSet.getObject(i);
                        table.addCell(new com.itextpdf.layout.element.Cell()
                                .add(new Paragraph(val != null ? val.toString() : "").setFontSize(9.5f)));
                    }
                }

                document.add(table);
                document.close();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }







    //Stock Report
    public static void generateStockReport(String title, ResultSet resultSet) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(title.replace(" ", "_") + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                 PdfDocument pdf = new PdfDocument(writer)) {
                Document document = new Document(pdf);
                Table headerTable = new Table(UnitValue.createPercentArray(new float[]{2, 6, 2})).useAllAvailableWidth();
                headerTable.setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);

                //Logo
                try {
                    URL logoUrl = PdfReportGenerator.class.getResource("/images/DashbordAppLogo.png");
                    if (logoUrl != null) {
                        Image logo = new Image(com.itextpdf.io.image.ImageDataFactory.create(logoUrl));
                        logo.setWidth(100);
                        headerTable.addCell(new com.itextpdf.layout.element.Cell()
                                .add(logo)
                                .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE)
                                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                    }
                } catch (Exception e) {
                    headerTable.addCell(new com.itextpdf.layout.element.Cell().setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));
                }

                //Title
                headerTable.addCell(new com.itextpdf.layout.element.Cell()
                        .add(new Paragraph(title).setFontSize(25).setBold())
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                        .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE)
                        .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));

                //Date
                String formattedDate = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                headerTable.addCell(new com.itextpdf.layout.element.Cell()
                        .add(new Paragraph("Generated on:\n" + formattedDate).setFontSize(9))
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT)
                        .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE)
                        .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));

                document.add(headerTable);
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