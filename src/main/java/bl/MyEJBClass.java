package bl;

//import entities.UserEntity;
//import entities.instruments.Cable;
import entities.*;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Stateless
public class MyEJBClass {

    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;


    public boolean deleteSubscriber(SubscriberEntity subscriberEntity ){
        SubscriberEntity sub = entityManager.find(SubscriberEntity.class,subscriberEntity.getSubscriber_id());
        entityManager.persist(sub);
        entityManager.remove(sub);
        entityManager.flush();
        return true;
    }

    public List<BuildingEntity> verifyAddressIsExist(String region, String street, String number){
        Query query = entityManager.createQuery("select entity from BuildingEntity entity where entity.street =:street and entity.number = :number and entity.region = :region");
        query.setParameter("region",region);
        query.setParameter("street",street);
        query.setParameter("number",number);
        return query.getResultList();
    }

    public List<BuildingEntity> getAllBuildings(){
        Query query = entityManager.createQuery("select entity from BuildingEntity entity");
        return query.getResultList();
    }

    public List<ServiceEntity> getAllServices(){
        Query query = entityManager.createQuery("select entity from ServiceEntity entity");
        return query.getResultList();
    }

    public List<String> getAllServiceName(){
        Query query = entityManager.createQuery("select entity.name from ServiceEntity entity");
        return query.getResultList();
    }

    public List<TechnicalDevicesEntity> getAllOTAByRegion(String region){
        Query query = entityManager.createQuery("select entity from TechnicalDevicesEntity entity where entity.region = :region");
        query.setParameter("region",region);
        return query.getResultList();
    }
    public List<TechnicalDevicesEntity> getAllOTAByAddress(BuildingEntity buildingEntity){
        Query query = entityManager.createQuery("select entity from TechnicalDevicesEntity entity where entity.region = :region and entity.street = :street and entity.number=:number");
        query.setParameter("region",buildingEntity.getRegion());
        query.setParameter("street",buildingEntity.getStreet());
        query.setParameter("number",buildingEntity.getNumber());
        return query.getResultList();
    }

    public List<TechnicalDevicesEntity> getAllDevices(){
        Query query = entityManager.createQuery("select entity from TechnicalDevicesEntity entity");
        return query.getResultList();
    }
    public List<DamageRuleEntity> getAllDamageRules(){
        Query query = entityManager.createQuery("select entity from DamageRuleEntity entity");
        return query.getResultList();
    }
    public List<SubscriberEntity> getAllSubscriber(){
        Query query = entityManager.createQuery("select entity from SubscriberEntity entity");
        return query.getResultList();
    }
    public List<GroupDamageEntity> getAllDG(){
        Query query = entityManager.createQuery("select entity from GroupDamageEntity entity where entity.active=true");
        return query.getResultList();
    }

    public List<ServiceEntity> getServiceByName(List<String> nameList){
        Query query = entityManager.createQuery("select entity from ServiceEntity entity where entity.name in(:nameList)");
        query.setParameter("nameList",nameList);
        return query.getResultList();
    }


}
