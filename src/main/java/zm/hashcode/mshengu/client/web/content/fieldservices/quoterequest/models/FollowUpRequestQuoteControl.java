/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.quoterequest.models;

import com.vaadin.ui.Notification;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import zm.hashcode.mshengu.app.facade.products.UnitTypeFacade;
import zm.hashcode.mshengu.app.util.DateTimeFormatHelper;
import zm.hashcode.mshengu.domain.external.IncomingRFQ;
import zm.hashcode.mshengu.domain.products.UnitType;

/**
 *
 * @author Luckbliss
 */
public class FollowUpRequestQuoteControl {

    private DateTimeFormatHelper formatHelper;
    private FollowUpRequestQuoteBean requestedQuoteResponse;

    public ByteArrayOutputStream processFormDataToPDF(IncomingRFQ i, String total) {
        formatHelper = new DateTimeFormatHelper();
        try {
            requestedQuoteResponse = new FollowUpRequestQuoteBean();
            requestedQuoteResponse.setCollectionDate(formatHelper.getDayMonthYear(i.getCollectionDate()));
            requestedQuoteResponse.setContactNumber(i.getContactNumber());
            requestedQuoteResponse.setAlternateContactNumber(i.getTelephoneNumber());
            requestedQuoteResponse.setDeliveryAddress(i.getDeliveryAddress().replace("\n", ", ").replace("\r", ""));
            requestedQuoteResponse.setCustomerName(i.getCompanyName());
            requestedQuoteResponse.setDaysRental(i.getDaysRental());
            requestedQuoteResponse.setDeliveryDate(formatHelper.getDayMonthYear(i.getDeliveryDate()));
            requestedQuoteResponse.setEventDate(formatHelper.getDayMonthYear(i.getEventDate()));
            requestedQuoteResponse.setNumberOfJanitors(i.getNumberOfJanitors());
            requestedQuoteResponse.setNumberOfToiletRolls(i.getNumberOfToiletRolls());

            requestedQuoteResponse.setQuantityRequired1(i.getQuantityRequired1());
            requestedQuoteResponse.setQuantityRequired2(i.getQuantityRequired2());
            requestedQuoteResponse.setQuantityRequired3(i.getQuantityRequired3());
            requestedQuoteResponse.setQuotationDate(formatHelper.getDayMonthYear(i.getDateOfAction()));
            requestedQuoteResponse.setRfqNumber(i.getRefNumber());

//            System.out.println("Id1: " + i.getToiletsRequired1());
//            System.out.println("Id2: " + i.getToiletsRequired2());
//            System.out.println("Id3: " + i.getToiletsRequired3());
            UnitType toiletsRequired1 = UnitTypeFacade.getUnitTypeService().findById(i.getToiletsRequired1());

            //if the additional toilets are not entered, the if statememts prevent null pointers.
            if (i.getToiletsRequired2() != null) {
                UnitType toiletsRequired2 = UnitTypeFacade.getUnitTypeService().findById(i.getToiletsRequired2());
                requestedQuoteResponse.setToiletsRequired2(toiletsRequired2.getName());
            }
            if (i.getToiletsRequired3() != null) {
                UnitType toiletsRequired3 = UnitTypeFacade.getUnitTypeService().findById(i.getToiletsRequired3());
                requestedQuoteResponse.setToiletsRequired3(toiletsRequired3.getName());
            }

            requestedQuoteResponse.setToiletsRequired1(toiletsRequired1.getName());

            requestedQuoteResponse.setCurrentDate(formatHelper.getDayMonthYear(new Date()));

            //calculate totale
            requestedQuoteResponse.setTotal(String.format("%,.2f", Float.parseFloat(total)));

            // Open the docx Template
            String fileName = "requestedquoteresponse.docx";
            URL url = this.getClass().getResource("/fieldservicespdf/");
//            System.out.println("getResource() PATH " + url.getPath());
            FileInputStream in = new FileInputStream(url.getFile() + fileName);

            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
            IContext context = report.createContext();
            context.put("quote", requestedQuoteResponse);

            // 3) Generate pdf report by merging Java model with the Docx
            Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
//            OutputStream out = new FileOutputStream(new File("C:\\Users\\Boniface\\Documents\\" + request.getOrderNumber() + ".pdf"));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            report.convert(context, options, os);

            try {
                // return to be displayed on screen
//                FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\Boniface\\Documents\\" + request.getOrderNumber() + ".pdf"));
                return os;

            } catch (Exception e) {
                System.out.println("\n\n" + e.getMessage());
//            throw new XDocConverterException( e );
                Notification.show("Could not create the PDF file!", Notification.Type.TRAY_NOTIFICATION);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getDate(Date date) {
        if (date != null) {
            return new DateTimeFormatHelper().getDayMonthYear(date);
        }
        return null;
    }
}
