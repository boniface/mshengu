/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.test.procurement;

import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import zm.hashcode.mshengu.domain.procurement.Request;
import zm.hashcode.mshengu.services.procurement.RequestService;
import zm.hashcode.mshengu.test.AppTest;

/**
 *
 * @author Luckbliss
 */
public class PurchaseOrdeTest extends AppTest {

    @Autowired
    private RequestService requestService;


    @Test
    public void getRequests() {

        requestService = ctx.getBean(RequestService.class);
        Request request = requestService.findByOrderNumber("MSH_PO-000480");
//                                                          ("MSH_PO-000737")
        
//        Request request = requestService.
        System.out.println("Order No " + request.getOrderNumber() + " Total - NO1 - " + request.getTotal());
        
//        Request newRequest = new Request.Builder(request.getPerson())
//                    .request(request)
//                    .total(new BigDecimal("9298.98"))
//                    .build();
//        requestService.merge(newRequest);
        
        
        Request request2 = requestService.findByOrderNumber("MSH_PO-000480");
        System.out.println("Order No " + request2.getOrderNumber() + " Total - NO2 - " + request2.getTotal());
        
    }

    public void getOrder31(Request request) {
        if (request.getOrderNumber().equals("MSH_PO-0000031")) {
            Request newRequest = new Request.Builder(request.getPerson())
                    .request(request)
                    .invoiceNumber("00257034")
                    .deliveryDate(new Date(2014, 01, 31))
                    .total(new BigDecimal("5164.49"))
                    .build();
//            requestService.merge(newRequest);
        }
    }

    public void getOrder08(Request request) {
        if (request.getOrderNumber().equals("MSH_PO-0000031")) {
            Request newRequest = new Request.Builder(request.getPerson())
                    .request(request)
                    .truckId("5267af26e4b0417002e0342d")
                    .serviceProviderSupplierId("52da02fee4b01596318b5a2c")
                    .orderDate(new Date(2014, 01, 03))
                    .build();
//            requestService.merge(newRequest);
        }
    }

    public void getOrder43(Request request) {
        if (request.getOrderNumber().equals("MSH_PO-0000043")) {
            Request newRequest = new Request.Builder(request.getPerson())
                    .request(request)
                    .truckId("5267b8b5e4b0417002e03430")
                    .serviceProviderSupplierId("52da02fee4b01596318b5a2c ")
                    .orderDate(new Date(2014, 02, 07))
                    .total(new BigDecimal("1672.38"))
                    .build();
//            requestService.merge(newRequest);
        }
    }

    public void getOrder29(Request request) {
        if (request.getOrderNumber().equals("MSH_PO-0000043")) {
            Request newRequest = new Request.Builder(request.getPerson())
                    .request(request)
                    .truckId("5267b8b5e4b0417002e03430")
                    .serviceProviderSupplierId("52da02fee4b01596318b5a2c ")
                    .orderDate(new Date(2014, 02, 07))
                    .total(new BigDecimal("1672.38"))
                    .build();
//            requestService.merge(newRequest);
        }
    }
    //
}
