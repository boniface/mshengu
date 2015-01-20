/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fieldservices.customer.models;

import com.vaadin.ui.Notification;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import zm.hashcode.mshengu.app.util.DateTimeFormatHelper;

/**
 *
 * @author Luckbliss
 */
public class NewCustomerControl {

    private DateTimeFormatHelper formatHelper;

    public ByteArrayOutputStream processPDF() {

        try {
            // Open the docx Template
            String fileName = "newcustomerform.pdf";
            URL url = this.getClass().getResource("/fieldservicespdf/");
//            System.out.println("getResource() PATH " + url.getPath());
            FileInputStream in = new FileInputStream(url.getFile() + fileName);
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            int read;
            byte[] buf = new byte[1024];

            try {
                while ((read = in.read(buf)) != -1) {
                    os.write(buf, 0, read);
                }
            } finally {
                in.close();
                os.close();
            }

            return os;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("\n\n" + e.getMessage());
            Notification.show("Could not create the PDF file!", Notification.Type.TRAY_NOTIFICATION);
        }

        return null;
    }
}
