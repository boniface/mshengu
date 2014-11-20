/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.test.api;

import junit.framework.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import zm.hashcode.mshengu.client.rest.api.resources.UnitDeliveryResource;
import zm.hashcode.mshengu.domain.products.Site;
import zm.hashcode.mshengu.domain.products.SiteServiceContractLifeCycle;
import zm.hashcode.mshengu.domain.products.SiteUnit;
import zm.hashcode.mshengu.services.products.MobileAppService;
import zm.hashcode.mshengu.services.products.SiteService;
import zm.hashcode.mshengu.services.products.impl.MobileAppServiceImpl;
import zm.hashcode.mshengu.test.AppTest;
import static zm.hashcode.mshengu.test.AppTest.ctx;

/**
 *
 * @author Ferox
 */
public class MobileAPITest extends AppTest {

    @Autowired
    private SiteService siteService;
    @Autowired
    private MobileAppService mobileAppServices;
    final String sitename1 = "BM Section";
    final String sitename2 = "Alpha Farm";
    final String sitename3 = "7 De Laan";
    final String siteUnitId1 = "MTU-000001";
    final String siteUnitId2 = "MTU-000002";
    final String siteUnitId3 = "MTU-000003";
    final String latitude = "-33.9385435";
    final String longitude = "18.5133065";
    UnitDeliveryResource unitDelivery = new UnitDeliveryResource();

//    @Test
    public void DeployUnit() {
        mobileAppServices = ctx.getBean(MobileAppService.class);

        unitDelivery.setLatitude(latitude);
        unitDelivery.setLongitude(longitude);


        unitDelivery.setSiteId(sitename1); // siteName
        unitDelivery.setUnitId(siteUnitId1);
        mobileAppServices.deployUnitToSite(unitDelivery);
        boolean wasUnit1Deployed = wasContractUpdated(sitename1, siteUnitId1);


        unitDelivery.setSiteId(sitename2); // siteName
        unitDelivery.setUnitId(siteUnitId2);
        mobileAppServices.deployUnitToSite(unitDelivery);
        boolean wasUnit2Deployed = wasContractUpdated(sitename2, siteUnitId2);

        unitDelivery.setSiteId(sitename3); // siteName
        unitDelivery.setUnitId(siteUnitId3);
        mobileAppServices.deployUnitToSite(unitDelivery);
        boolean wasUnit3Deployed = wasContractUpdated(sitename3, siteUnitId3);

        Assert.assertEquals("Was Unit1 Added to Site 1? ", true, wasUnit1Deployed);
        Assert.assertEquals("Was Unit2 Added to Site 2? ", true, wasUnit2Deployed);
        Assert.assertEquals("Was Unit3 Added to Site 3? ", true, wasUnit3Deployed);

    }

//    @Test(dependsOnMethods = {"DeployUnit"})
    public void MovedUnit() {
        mobileAppServices = ctx.getBean(MobileAppService.class);

        unitDelivery.setLatitude(latitude);
        unitDelivery.setLongitude(longitude);

        unitDelivery.setSiteId(sitename1); // siteName
        unitDelivery.setUnitId(siteUnitId2);
        mobileAppServices.deployUnitToSite(unitDelivery);
        boolean wasUnit2Moved = wasContractUpdated(sitename1, siteUnitId2);

        unitDelivery.setSiteId(sitename2); // siteName
        unitDelivery.setUnitId(siteUnitId3);
        mobileAppServices.deployUnitToSite(unitDelivery);
        boolean wasUnit3Moved = wasContractUpdated(sitename2, siteUnitId3);

        unitDelivery.setSiteId(sitename3); // siteName
        unitDelivery.setUnitId(siteUnitId1);
        mobileAppServices.deployUnitToSite(unitDelivery);
        boolean wasUnit1Moved = wasContractUpdated(sitename3, siteUnitId1);


        Assert.assertEquals("Was Unit1 Moved to Site 2? ", true, wasUnit1Moved);
        Assert.assertEquals("Was Unit2 Moved to Site 3? ", true, wasUnit2Moved);
        Assert.assertEquals("Was Unit3 Moved to Site 1? ", true, wasUnit3Moved);


    }

//    @Test(dependsOnMethods = {"MovedUnit"})
    public void RemovedUnit() {
        mobileAppServices = ctx.getBean(MobileAppServiceImpl.class);


        boolean wasUnit1Removed = wasContractUpdated(sitename1, siteUnitId1);

        boolean wasUnit2Removed = wasContractUpdated(sitename2, siteUnitId2);

        boolean wasUnit3Removed = wasContractUpdated(sitename3, siteUnitId3);


        Assert.assertEquals("Was Unit1 Removed to Site 1? ", false, wasUnit1Removed);
        Assert.assertEquals("Was Unit2 Removed to Site 2? ", false, wasUnit2Removed);
        Assert.assertEquals("Was Unit3 Removed to Site 3? ", false, wasUnit3Removed);

    }

    boolean wasSiteUnitFound(SiteServiceContractLifeCycle contractLifeCycle, String unitId) {
        boolean wasAdded = false;
        if (contractLifeCycle != null) {
            if (contractLifeCycle.getSiteUnit() != null) {
                for (SiteUnit toiletUnit : contractLifeCycle.getSiteUnit()) {
                    if (toiletUnit.getUnitId().equalsIgnoreCase(unitId)) {
                        wasAdded = true;
                    }
                }
            }
        }
        return wasAdded;
    }

    boolean wasContractUpdated(String siteId, String unitId) {
        siteService = ctx.getBean(SiteService.class);
        Site site = siteService.findByName(siteId);
        SiteServiceContractLifeCycle contractLifeCycle = site.getLastSiteServiceContractLifeCycle();
        return wasSiteUnitFound(contractLifeCycle, unitId);
    }
}
