/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.services.products.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import zm.hashcode.mshengu.client.rest.api.resources.UnitDeliveryResource;
import zm.hashcode.mshengu.client.rest.api.resources.UnitServiceResource;
import zm.hashcode.mshengu.domain.products.Site;
import zm.hashcode.mshengu.domain.products.SiteServiceContractLifeCycle;
import zm.hashcode.mshengu.domain.products.SiteUnit;
import zm.hashcode.mshengu.domain.products.UnitLocationLifeCycle;
import zm.hashcode.mshengu.domain.products.UnitServiceLog;
import zm.hashcode.mshengu.domain.products.UnitType;
import zm.hashcode.mshengu.domain.ui.util.Sequence;
import zm.hashcode.mshengu.domain.ui.util.Status;
import zm.hashcode.mshengu.services.products.MobileAPIAppService;
import zm.hashcode.mshengu.services.products.SiteService;
import zm.hashcode.mshengu.services.products.SiteServiceContractLifeCycleService;
import zm.hashcode.mshengu.services.products.SiteUnitService;
import zm.hashcode.mshengu.services.products.UnitLocationLifeCycleService;
import zm.hashcode.mshengu.services.products.UnitServiceLogService;
import zm.hashcode.mshengu.services.products.UnitTypeService;
import zm.hashcode.mshengu.services.ui.util.SequenceService;
import zm.hashcode.mshengu.services.ui.util.StatusService;

/**
 *
 * @author Luckbliss
 */
@Service
public class MobileAPIAppServiceImpl implements MobileAPIAppService {

    @Autowired
    private SiteUnitService siteUnitService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private SiteServiceContractLifeCycleService siteServiceContractLifeCycleService;
    @Autowired
    private UnitLocationLifeCycleService unitLocationLifeCycleService;
    @Autowired
    private UnitServiceLogService unitServiceLogService;
    @Autowired
    private UnitTypeService unitTypeService;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private StatusService statusService;

    @Override
    public String deployUnitToSite(UnitDeliveryResource unitDelivery) {
        final String unitId = unitDelivery.getUnitId().trim();
        final String siteName = unitDelivery.getSiteId().trim();
        final String latitude = unitDelivery.getLatitude().trim();
        final String longitude = unitDelivery.getLongitude().trim();
        String siteId = "";

        Site site = siteService.findByName(siteName);
        if(site != null){
            siteId = site.getId();
        }

        removePerviousDeployment(unitId); // remove previous deployment

        UnitLocationLifeCycle unitLifeCycle = persistUnitLocationLifeCycle(siteName, latitude, longitude); // create a new life cycle (deployment) for the toilet unit 

        SiteUnit updatedSiteUnit = updatedSiteUnit(unitId, siteName, unitLifeCycle); //update toilet unite

        if (updatedSiteUnit != null) {
            SiteServiceContractLifeCycle newSiteServiceContractLifeCycle = updateSiteServiceContractLifeCycleAdd(siteName, updatedSiteUnit); // create a new life cycle (contract) for the toilet Site 

            updateSite(siteName, newSiteServiceContractLifeCycle); //update the site
        }
        return "ok";
    }

    /**
     * Remove a site from the previous Site where it was deployed
     *
     * @param latitude
     * @param longitude
     * @return siteUnitLocationLifeCycle
     */
    private void removePerviousDeployment(String unitId) {
        SiteUnit siteUnit = siteUnitService.findByUnitId(unitId);
        if (siteUnit != null) {
            UnitLocationLifeCycle unitLifeCycle = siteUnitService.getUnitCurrentLocation(siteUnit.getId());

            if (unitLifeCycle != null) {
                if (unitLifeCycle.getSiteName() != null) {
                    String previosSiteName = unitLifeCycle.getSiteName();
                    SiteServiceContractLifeCycle newSiteServiceContractLifeCycle = updateSiteServiceContractLifeCycleRemove(previosSiteName, siteUnit);
                    if (newSiteServiceContractLifeCycle != null) {
                        updateSite(previosSiteName, newSiteServiceContractLifeCycle);
                    }
                }
            }
        }
    }

    /**
     * Save a new SiteUnitLocationLifeCycle
     *
     * @param latitude
     * @param longitude
     * @return siteUnitLocationLifeCycle
     */
    private UnitLocationLifeCycle persistUnitLocationLifeCycle(String siteName, String latitude, String longitude) {

        UnitLocationLifeCycle unitLifeCycle = new UnitLocationLifeCycle.Builder(new Date())
                .siteName(siteName)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        unitLocationLifeCycleService.persist(unitLifeCycle);
        return unitLifeCycle;
    }

    /**
     * Adds the unitLocationLifeCycle to the siteUnit and updates it
     *
     * @param unitId
     * @param unitLocationLifeCycle
     * @return updatedSiteUnit
     */
    private SiteUnit updatedSiteUnit(String unitId, String siteName, UnitLocationLifeCycle unitLocationLifeCycle) {
        SiteUnit siteUnit = siteUnitService.findByUnitId(unitId);
//        System.out.println("Unit id -" + unitId + "-");
        UnitType unitType = null;
        List<UnitLocationLifeCycle> unitLocationLifeCycleList = new ArrayList<>();
        if (siteUnit != null) {
            if (siteUnit.getUnitLocationLifeCycle() != null) {
                unitLocationLifeCycleList.addAll(siteUnit.getUnitLocationLifeCycle());
            }
            if (unitType != null) {
                unitType = siteUnit.getUnitType();
            } else {
                unitType = unitTypeService.findByName("Basic Atlas");
            }
            unitLocationLifeCycleList.add(unitLocationLifeCycle);
            SiteUnit updatedSiteUnit = new SiteUnit.Builder(unitType)
                    .siteUnit(siteUnit)
                    .siteName(siteName)
                    .operationalStatus(getStatus(siteName))
                    .unitLocationLifeCycle(unitLocationLifeCycleList)
                    .build();
            //SAVE UNIT
            siteUnitService.merge(updatedSiteUnit);
            return updatedSiteUnit;
        } else {
            return null;
        }

    }

    /**
     * 1st Finds the current SiteServiceContractLifeCycle for a specific Site
     * 2nd creates a new SiteServiceContractLifeCycle based on the latest one
     * 3rd adds the new site and updates the number of units
     *
     * @param siteName
     * @param updatedSiteUnit
     * @return newSiteServiceContractLifeCycle
     */
    private SiteServiceContractLifeCycle updateSiteServiceContractLifeCycleAdd(String siteName, SiteUnit updatedSiteUnit) {
        Site site = siteService.findByName(siteName);

        SiteServiceContractLifeCycle newSiteServiceContractLifeCycle = null;

        if (site != null) {

            SiteServiceContractLifeCycle siteServiceContractLifeCycle = site.getLastSiteServiceContractLifeCycle();

            Set<SiteUnit> siteUnits = new HashSet<>();
            Set<SiteUnit> currentUnitsAtThisSite = siteServiceContractLifeCycle.getSiteUnit();

            siteUnits.add(updatedSiteUnit);
            siteUnits.addAll(currentUnitsAtThisSite);

            int numberOfunits = siteUnits.size();
            newSiteServiceContractLifeCycle = new SiteServiceContractLifeCycle.Builder(new Date())
                    .siteServiceContractLifeCycle(siteServiceContractLifeCycle)
                    .numberOfUnits(numberOfunits)
                    .siteUnit(siteUnits)
                    .build();

            siteServiceContractLifeCycleService.persist(newSiteServiceContractLifeCycle);
        }

        return newSiteServiceContractLifeCycle;
    }

    private SiteServiceContractLifeCycle updateSiteServiceContractLifeCycleAdd(String siteName, Set<SiteUnit> newUnitsAtThisSite) {
        Site site = siteService.findByName(siteName);


        SiteServiceContractLifeCycle siteServiceContractLifeCycle = site.getLastSiteServiceContractLifeCycle();

        Set<SiteUnit> siteUnits = new HashSet<>();
        Set<SiteUnit> currentUnitsAtThisSite = siteServiceContractLifeCycle.getSiteUnit();

        siteUnits.addAll(newUnitsAtThisSite);
        siteUnits.addAll(currentUnitsAtThisSite);

        int numberOfunits = siteUnits.size();

        SiteServiceContractLifeCycle newSiteServiceContractLifeCycle = new SiteServiceContractLifeCycle.Builder(new Date())
                .siteServiceContractLifeCycle(siteServiceContractLifeCycle)
                .numberOfUnits(numberOfunits)
                .siteUnit(siteUnits)
                .build();

        siteServiceContractLifeCycleService.persist(newSiteServiceContractLifeCycle);

        return newSiteServiceContractLifeCycle;
    }

    /**
     * 1st Finds the current SiteServiceContractLifeCycle for a specific Site
     * 2nd creates a new SiteServiceContractLifeCycle based on the latest one
     * 3rd removes the new site and updates the number of units
     *
     * @param siteName
     * @param updatedSiteUnit
     * @return newSiteServiceContractLifeCycle
     */
    private SiteServiceContractLifeCycle updateSiteServiceContractLifeCycleRemove(String siteName, SiteUnit updatedSiteUnit) {
        Site site = siteService.findByName(siteName);
        SiteServiceContractLifeCycle newSiteServiceContractLifeCycle = null;
        if (site != null) {

            SiteServiceContractLifeCycle siteServiceContractLifeCycle = site.getLastSiteServiceContractLifeCycle();

//            System.out.println("Site name -" + site.getName() + "-");
//            System.out.println("Site id  -" + site.getId() + "-");
            Set<SiteUnit> siteUnits = new HashSet<>();
            if (siteServiceContractLifeCycle != null) {
                if (siteServiceContractLifeCycle.getSiteUnit() != null) {
                    siteUnits.addAll(siteServiceContractLifeCycle.getSiteUnit());
                }
            }else{
                System.out.println("current siteServiceContractLifeCycle ofr site -" + site.getName() + "-not fount");
            }
            siteUnits.remove(updatedSiteUnit);
            int numberOfunits = siteUnits.size();

            newSiteServiceContractLifeCycle = new SiteServiceContractLifeCycle.Builder(new Date())
                    .siteServiceContractLifeCycle(siteServiceContractLifeCycle)
                    .numberOfUnits(numberOfunits)
                    .siteUnit(siteUnits)
                    .build();

            siteServiceContractLifeCycleService.persist(newSiteServiceContractLifeCycle);

        }
        return newSiteServiceContractLifeCycle;
    }

    private void updateSite(String siteName, SiteServiceContractLifeCycle newSiteServiceContractLifeCycle) {
        Site site = siteService.findByName(siteName);

        if (site != null) {

            Set<SiteServiceContractLifeCycle> siteServiceContractLifeCycleList = new HashSet<>();
            Set<SiteServiceContractLifeCycle> currentSiteServiceContractLifeCycle = site.getSiteServiceContractLifeCycle();

            siteServiceContractLifeCycleList.add(newSiteServiceContractLifeCycle);
            siteServiceContractLifeCycleList.addAll(currentSiteServiceContractLifeCycle);


            Site newSite = new Site.Builder(site.getName())
                    .site(site)
                    .siteServiceContractLifeCycle(siteServiceContractLifeCycleList)
                    .build();

            siteService.merge(newSite);
        }
    }

    /**
     *
     * @param unitService
     * @param statusMessasge
     */
    @Override
    public void createUnitServiceLog(UnitServiceResource unitService, String statusMessasge) {
        final Map<String, Boolean> task = unitService.getServices();
        final String unitId = unitService.getUnitId().trim();
        final boolean pumpOut = task.get("pumpOut");
        final boolean washBucket = task.get("washBucket");
        final boolean suctionOut = task.get("suctionOut");
        final boolean scrubFloor = task.get("scrubFloor");
        final boolean rechargeBacket = task.get("rechargeBacket");
        final boolean cleanPerimeter = task.get("cleanPerimeter");
        final String incidents = unitService.getIncident().trim();
        SiteUnit siteUnit = siteUnitService.findByUnitId(unitId);

        if (siteUnit != null) {
            String siteName = siteUnit.getSiteName();

            UnitServiceLog unitServiceLog = null;

            if ("WITHIN".equals(statusMessasge)) {
                unitServiceLog = new UnitServiceLog.Builder(new Date())
                        .serviceTime(new Date())
                        .pumpOut(pumpOut)
                        .washBucket(washBucket)
                        .suctionOut(suctionOut)
                        .scrubFloor(scrubFloor)
                        .rechargeBacket(rechargeBacket)
                        .cleanPerimeter(cleanPerimeter)
                        .statusMessage(statusMessasge)
                        .incident(incidents)
                        .siteName(siteName)
                        .build();
                unitServiceLogService.persist(unitServiceLog);
            } else {
                unitServiceLog = new UnitServiceLog.Builder(new Date())
                        .serviceTime(new Date())
                        .statusMessage("User Away From UNIT")
                        .build();
                unitServiceLogService.persist(unitServiceLog);

            }

            if (unitServiceLog != null) {
                addServiceLogToSite(siteUnit, unitServiceLog);
            }
        }





    }

    private void addServiceLogToSite(SiteUnit siteUnit, UnitServiceLog unitServiceLog) {
        List<UnitServiceLog> newUnitServiceLogs = new ArrayList<>();


        List<UnitServiceLog> unitServiceLogList = siteUnit.getUnityLogs();
        newUnitServiceLogs.addAll(unitServiceLogList);
        newUnitServiceLogs.add(unitServiceLog);

        SiteUnit updatedUniteSite = new SiteUnit.Builder(siteUnit.getUnitType())
                .siteUnit(siteUnit)
                .unityLogs(newUnitServiceLogs)
                .build();

        //SAVE UNIT
        siteUnitService.merge(updatedUniteSite);
    }

    /**
     *
     * @param unitService
     * @return
     */
    @Override
    public String checkCoordinates(UnitServiceResource unitService) {

        return siteUnitService.checkCoordinates(unitService);
    }

    @Override
    public String bulkTagUnitsToWharehouse(UnitType unitType, Sequence unitIdSequence, int quantity, Site site) {
        String siteName = site.getName();
        String latitude = "0";
        String longitude = "0";// create a new life cycle (deployment) for the toilet unit 
        if (site.getLocation() != null) {
            latitude = site.getLocation().getLatitude();
            longitude = site.getLocation().getLongitude();
        }

        Set<SiteUnit> newUnitsAtThisSite = createToiletSiteUnits(unitType, unitIdSequence, quantity, siteName, latitude, longitude); // Get List of New Site Units added to wharehose

        SiteServiceContractLifeCycle newSiteServiceContractLifeCycle = updateSiteServiceContractLifeCycleAdd(siteName, newUnitsAtThisSite); // create a new life cycle (contract) for the toilet Site 

        updateSite(siteName, newSiteServiceContractLifeCycle); //update the site
        return null;
    }

    private Set<SiteUnit> createToiletSiteUnits(UnitType unitType, Sequence unitIdSequence, int quantity, String siteName, String latitude, String longitude) {
        int sequenceCount = unitIdSequence.getValue();

        Set<SiteUnit> siteUnitList = new HashSet<>();
        for (int count = 1; count <= quantity; count++) {

            UnitLocationLifeCycle unitLifeCycle = persistUnitLocationLifeCycle(siteName, latitude, longitude); // creating first life cycle
            List<UnitLocationLifeCycle> unitLifeCycleList = new ArrayList<>();
            unitLifeCycleList.add(unitLifeCycle);

            List<UnitServiceLog> unitServiceLogList = new ArrayList<>();


            final SiteUnit siteUnit = new SiteUnit.Builder(unitType)
                    .description(unitType.getName())
                    .operationalStatus(getStatus(siteName))
                    .unitId(unitIdSequence.getNamingCode() + "-" + formatUnitNumber(sequenceCount))
                    .unitLocationLifeCycle(unitLifeCycleList)
                    .unityLogs(unitServiceLogList)
                    .build();
            siteUnitService.persist(siteUnit);
            sequenceCount++;
            siteUnitList.add(siteUnit);
        }
        updateUnitSequence(unitIdSequence, sequenceCount);
        return siteUnitList;
    }

    private String formatUnitNumber(int unitId) {
        //100000
        if (unitId < 10) {
            return "00000" + unitId; //000001-000009
        } else if (unitId < 100) {
            return "0000" + unitId;  //000010-000099
        } else if (unitId < 1000) {
            return "000" + unitId;   //000100-000999
        } else if (unitId < 10000) {
            return "00" + unitId;    //001000-009999
        } else if (unitId < 100000) {
            return "0" + unitId;     //010000-099999
        } else {
            return "" + unitId;
        }
    }

    private void updateUnitSequence(Sequence unitIdSequence, int newSequenceVaue) {
        if (!StringUtils.isEmpty(unitIdSequence)) {
            Sequence newUnitIdSequence = new Sequence.Builder(unitIdSequence.getName())
                    .sequenceType(unitIdSequence.getSequenceType())
                    .namingCode(unitIdSequence.getNamingCode())
                    .value(newSequenceVaue)
                    .id(unitIdSequence.getId())
                    .build();
            sequenceService.merge(newUnitIdSequence);
        }
    }

    private Status getStatus(String siteName) {
        Status status;
        if ("MSHENGU_WAREHOUSE".equalsIgnoreCase(siteName)) {
            status = statusService.findByName("STOCK");
        } else {
            status = statusService.findByName("DEPLOYED");
        }

        if (status != null) {
            return status;
        } else {
            return null;
        }
    }
}
