package brainstorm.pharmacy_app.Utils;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

public class PdfUtils {
    public static void addTitle(Document document, String title) {
        document.add(new Paragraph(title)
                .setBold()
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("\n"));
    }
}
