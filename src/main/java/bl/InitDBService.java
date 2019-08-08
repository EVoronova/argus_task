package bl;

import entities.*;
import org.apache.commons.lang3.StringUtils;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.util.*;

@Stateless
public class InitDBService {

    Set<RealizedServiceEntity> realizedServiceEntities = new HashSet<>();

    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    @EJB
    MyEJBClass myEJBClass;

    public boolean createSubscriber(String region, String street, String number, String name, List<String> nameList){
        if(StringUtils.isEmpty(street) || StringUtils.isEmpty(region)|| StringUtils.isEmpty(number) || StringUtils.isEmpty(name) || nameList.size()==0){
            return false;
        }

        List<ServiceEntity> serviceEntityList = myEJBClass.getServiceByName(nameList);
        Set<ServiceEntity> setService = new HashSet<>();
        setService.addAll(serviceEntityList);
        SubscriberEntity subscriber = createSub(name,getBuilding(region,street,number),setService);
        entityManager.flush();

        return true;
    }


    public boolean createUser(String login, String password){

        List<DamageRuleEntity> list = createDamageRules();
        for(int x=0; x<5; x++){
            BuildingEntity building = createBuilding("street" + x, "region" + x, "num" + x);
            Set<TechnicalDevicesEntity> otaSet = new HashSet<>();
            for (TechnicalDevicesEntity.TypeTA type: TechnicalDevicesEntity.TypeTA.values()
            ) {
                otaSet.add(createOTA(type.toString() + x, type));
            }
            ServiceOwnerEntity ownerEntity= createOwner("owner" + x);
            Set<ServiceEntity> serviceEntitySet = new HashSet<>();
            for(int y=0;y<3;y++) {
                ServiceEntity service = createService("service" + y + " " + ownerEntity.getName(), ownerEntity, otaSet);
                serviceEntitySet.add(service);
            }
            SubscriberEntity subscriber = createSub("sub" + x,building,serviceEntitySet);

            Set<BuildingEntity> buildingEntities = new HashSet<>();
            buildingEntities.add(building);

            Set<SubscriberEntity> subscriberEntities = new HashSet<>();
            subscriberEntities.add(subscriber);
            GroupDamageEntity groupDamageEntity = createGroupDamage(x,list.get(x),otaSet,buildingEntities,realizedServiceEntities,subscriberEntities,building.getRegion(),building);
            if(x>2){
                createGroupDamage(x,list.get(x),otaSet,buildingEntities,realizedServiceEntities,subscriberEntities,building.getRegion(),building);
            }
            int k=0;
        }
        entityManager.flush();

        return true;
    }
    private BuildingEntity getBuilding(String region, String street, String number){
        List<BuildingEntity> listBuilding = myEJBClass.verifyAddressIsExist(region,street,number);
        BuildingEntity building = (listBuilding.size() == 0) ? createBuilding(street, region, number) : listBuilding.get(0);
        return building;
    }



    private ServiceEntity createService(String serviceName, ServiceOwnerEntity ownerEntity, Set<TechnicalDevicesEntity> otaSet){
        ServiceEntity service = new ServiceEntity(serviceName,ownerEntity,otaSet);
        ownerEntity.addService(service);
        entityManager.persist(service);
        return service;
    }
    private ServiceOwnerEntity createOwner(String ownerName){
        ServiceOwnerEntity serviceOwner = new ServiceOwnerEntity(ownerName);
        entityManager.persist(serviceOwner);
        return serviceOwner;
    }
    private SubscriberEntity createSub(String subName,BuildingEntity building, Set<ServiceEntity> serviceEntitySet){
        SubscriberEntity subscriber = new SubscriberEntity(subName,building,serviceEntitySet);
        realizedServiceEntities.addAll(subscriber.getRealizedServiceSet());
        entityManager.persist(subscriber);
        return subscriber;
    }
    private TechnicalDevicesEntity createOTA(String name, TechnicalDevicesEntity.TypeTA typeTA){
        TechnicalDevicesEntity ota = new TechnicalDevicesEntity(name,typeTA);
        entityManager.persist(ota);
        return ota;
    }
    private RealizedServiceEntity createRealizedServices(ServiceEntity service, SubscriberEntity subscriber, Set<TechnicalDevicesEntity> setTA){
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        RealizedServiceEntity realizedService = new RealizedServiceEntity(date,service,true);

        for (TechnicalDevicesEntity ota: setTA
        ) {
            realizedService.addOTA(ota);
        }
        entityManager.persist(realizedService);
        return realizedService;
    }

    private GroupDamageEntity createGroupDamage(int num,DamageRuleEntity damageRule, Set<TechnicalDevicesEntity> ota,Set<BuildingEntity> buildings, Set<RealizedServiceEntity> realizedServices, Set<SubscriberEntity> subscribers, String region, BuildingEntity buildingEntity){
        int x = -num;
        Calendar cal = GregorianCalendar.getInstance();
        cal.add( Calendar.DAY_OF_YEAR, x);
        Date date = new Date(cal.getTime().getTime());
        Set<TechnicalDevicesEntity> otaNew = new HashSet<>();
        for (TechnicalDevicesEntity objectTA : ota
             ) {
            switch (damageRule.getTypeRule()){
                case NodeRule:
                    if(objectTA.getType().equals(TechnicalDevicesEntity.TypeTA.node)){
                        otaNew.add(objectTA);
                    }
                    break;
                case ConnectorRule:
                    if(objectTA.getType().equals(TechnicalDevicesEntity.TypeTA.connector)){
                        otaNew.add(objectTA);
                    }
                    break;
                case CableRule:
                    if(objectTA.getType().equals(TechnicalDevicesEntity.TypeTA.cable)){
                        otaNew.add(objectTA);
                    }
                    break;
                case RegionRule:
                    otaNew.addAll(myEJBClass.getAllOTAByRegion(region));
                    break;
                case AddressRule:
                    otaNew.addAll(myEJBClass.getAllOTAByAddress(buildingEntity));
                    break;
            }

        }
        GroupDamageEntity groupDamage = new GroupDamageEntity(date,damageRule,otaNew,buildings,realizedServices,subscribers);
        entityManager.persist(groupDamage);
        return groupDamage;
    }
        private List<DamageRuleEntity> createDamageRules(){
        List<DamageRuleEntity> list = new ArrayList<DamageRuleEntity>();
        for (DamageRuleEntity.TypeRule rule: DamageRuleEntity.TypeRule.values()
        ) {
            DamageRuleEntity damageRule = new DamageRuleEntity(rule);
            list.add(damageRule);
            entityManager.persist(damageRule);
        }
        return list;
    }

    private BuildingEntity createBuilding(String street, String region, String number){
        AddressEntity address= new AddressEntity(street,region,number);
        BuildingEntity building = new BuildingEntity(address);
        entityManager.persist(building);
        return building;
    }

    public List<DamageRuleEntity> getAllDamageRules(){
        Query query = entityManager.createQuery("select entity from DamageRuleEntity entity");
        return query.getResultList();
    }
}
