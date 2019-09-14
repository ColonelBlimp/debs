/*
 * MIT License
 *
 * Copyright (c) 2019 ColonelBlimp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.veary.debs.web.internal;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.core.Money;

/**
 * Support class for creating vouchers in PDF.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
final class PdfVoucherGenerator {

    private static final Logger LOG = LogManager.getLogger(PdfVoucherGenerator.class);
    private static final String LOG_CALLED = "called";

    private static final Font defaultFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
    private static final Font defaultFontBold = FontFactory.getFont(FontFactory.HELVETICA, 11,
        Font.BOLD);
    private static final Font boldFont14 = FontFactory.getFont(FontFactory.HELVETICA, 14,
        Font.BOLD);
    private static final Font boldFont22 = FontFactory.getFont(FontFactory.HELVETICA, 22,
        Font.BOLD);

    public static ByteArrayOutputStream generateVoucher(String number, String date,
        String category, List<VoucherEntryBean> data) throws DocumentException {
        LOG.trace(LOG_CALLED);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final Document doc = new Document(PageSize.A4.rotate(), 40, 40, 40, 40);
        final PdfWriter writer = PdfWriter.getInstance(doc, baos);

        doc.open();

        addMetaData(doc, number);
        buildVoucherContent(doc, number, date, category, data);

        doc.close();
        writer.flush();
        writer.close();

        return baos;
    }

    private static void addMetaData(Document doc, String number) {
        LOG.trace(LOG_CALLED);

        doc.addTitle(number);
        doc.addSubject("Payment Voucher");
        doc.addAuthor("Mzuzu Bible College");
        doc.addCreator("Payment Voucher System");
        doc.addCreationDate();
    }

    private static void buildVoucherContent(Document doc, String number, String date,
        String category,
        List<VoucherEntryBean> data) throws DocumentException {
        LOG.trace(LOG_CALLED);

        buildVoucherHeader(doc, number, date, category);
        buildVoucherTable(doc, data);
        buildVoucherFooter(doc);
    }

    private static void buildVoucherHeader(Document doc, String number, String date,
        String category)
        throws DocumentException {
        LOG.trace(LOG_CALLED);

        final PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(90);

        final PdfPCell dateCell = new PdfPCell(new Phrase("Date: " + date, defaultFont));
        dateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dateCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(dateCell);

        final PdfPCell titleCell = new PdfPCell(new Phrase("Mzuzu Bible College", boldFont14));
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(titleCell);

        final PdfPCell numCell = new PdfPCell(new Phrase(category + ": " + number, defaultFont));
        numCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        numCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(numCell);
        table.setComplete(true);

        doc.add(table);

        final Paragraph p1 = new Paragraph("Payment Voucher", boldFont22);
        p1.setAlignment(Element.ALIGN_CENTER);
        doc.add(p1);

        final Paragraph p2 = new Paragraph(" ");
        doc.add(p2);
    }

    private static void buildVoucherTable(Document doc, List<VoucherEntryBean> data)
        throws DocumentException {
        LOG.trace(LOG_CALLED);

        final PdfPTable table = generateTableHeader();

        Money balance = new Money(BigDecimal.ZERO);

        for (final VoucherEntryBean bean : data) {
            final PdfPCell c1 = new PdfPCell(new Phrase(bean.getReference(), defaultFont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT | Element.ALIGN_MIDDLE);
            table.addCell(c1);

            final PdfPCell c2 = new PdfPCell(new Phrase(bean.getDescription(), defaultFont));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT | Element.ALIGN_MIDDLE);
            table.addCell(c2);

            final PdfPCell c3 = new PdfPCell(
                new Phrase(bean.getAmount().toString(), defaultFont));
            c3.setHorizontalAlignment(Element.ALIGN_LEFT | Element.ALIGN_MIDDLE);
            table.addCell(c3);

            balance = balance.plus(bean.getAmount());
        }

        final PdfPCell fc1 = new PdfPCell(new Phrase(" "));
        fc1.setBorder(Rectangle.NO_BORDER);
        table.addCell(fc1);

        final PdfPCell fc2 = new PdfPCell(new Phrase("TOTAL: ", defaultFontBold));
        fc2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        fc2.setBorder(Rectangle.NO_BORDER);

        table.addCell(fc2);

        final PdfPCell fc3 = new PdfPCell(new Phrase(balance.toString(), defaultFontBold));
        fc3.setHorizontalAlignment(Element.ALIGN_LEFT | Element.ALIGN_MIDDLE);
        table.addCell(fc3);

        table.setComplete(true);

        doc.add(table);
    }

    private static PdfPTable generateTableHeader() throws DocumentException {
        LOG.trace(LOG_CALLED);

        final PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(90);
        table.setWidths(new int[] { 1, 3, 1 });

        final PdfPCell hc1 = new PdfPCell(new Phrase("Reference", defaultFontBold));
        hc1.setHorizontalAlignment(Element.ALIGN_CENTER | Element.ALIGN_MIDDLE);
        table.addCell(hc1);

        final PdfPCell hc2 = new PdfPCell(new Phrase("Description", defaultFontBold));
        hc2.setHorizontalAlignment(Element.ALIGN_LEFT | Element.ALIGN_MIDDLE);
        table.addCell(hc2);

        final PdfPCell hc3 = new PdfPCell(new Phrase("Amount", defaultFontBold));
        hc3.setHorizontalAlignment(Element.ALIGN_CENTER | Element.ALIGN_MIDDLE);
        table.addCell(hc3);

        return table;
    }

    private static void buildVoucherFooter(Document doc) throws DocumentException {
        LOG.trace(LOG_CALLED);

        final Paragraph p1 = new Paragraph(" ");
        doc.add(p1);

        final PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(90);

        final PdfPCell c1 = new PdfPCell(new Phrase(" "));
        c1.setBorder(Rectangle.NO_BORDER);
        table.addCell(c1);

        final PdfPCell c2 = new PdfPCell();
        c2.addElement(new Paragraph(" Office Use Only", defaultFont));
        c2.addElement(new Paragraph(" "));
        c2.addElement(new Paragraph(" Authorized by:", defaultFont));
        c2.addElement(new Paragraph(" Date:"));
        c2.addElement(new Paragraph(" "));

        table.addCell(c2);
        table.setComplete(true);
        doc.add(table);
    }

    private PdfVoucherGenerator() {
    }
}
