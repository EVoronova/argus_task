package bl;

import entities.*;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Date;
import java.util.*;

@Stateful
public class DamageGroupEJB {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;


    public List<Object[]> getAllota(TechnicalDevicesEntity.TypeTA typeTA){
        String sql = "SELECT OTA_ID, NAME \n" +
                "FROM REALIZED_SERVICE_OTA \n" +
                "INNER JOIN OTA \n" +
                "ON REALIZED_SERVICE_OTA.OTA_ID = OTA.ID \n" +
                "WHERE OTA.TYPE = :type ;";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("type",typeTA.getInd());
        query.getResultList();
        return query.getResultList();
    }
   public void createGDByNode(Long id){
       createDGForOTA(id, DamageRuleEntity.TypeRule.NodeRule,TechnicalDevicesEntity.TypeTA.node);
   }
    public void createGDByCable(Long id){
        createDGForOTA(id, DamageRuleEntity.TypeRule.CableRule,TechnicalDevicesEntity.TypeTA.cable);
    }
    public void createGDByConnector(Long id){
        createDGForOTA(id, DamageRuleEntity.TypeRule.ConnectorRule,TechnicalDevicesEntity.TypeTA.connector);
    }
   private void createDGForOTA(Long id,DamageRuleEntity.TypeRule typeRule, TechnicalDevicesEntity.TypeTA typeTA){
       List<RealizedServiceEntity> serviceList = getAllRealizedServiceByType(typeTA,id);
       List<BuildingEntity> buildingList = getAllRealizedBuildingByType(typeTA,id);
       List<SubscriberEntity> subscriberList = getAllSubscriberByType(id);
       List<DamageRuleEntity> damageRuleList = getDamageRule(typeRule);
       List<TechnicalDevicesEntity> otaList = getOTA(id);

       Set<RealizedServiceEntity> serviceSet = new HashSet<>();
       serviceSet.addAll(serviceList);
       Set<BuildingEntity> buildingSet = new HashSet<>();
       buildingSet.addAll(buildingList);
       Set<SubscriberEntity> subscriberSet = new HashSet<>();
       subscriberSet.addAll(subscriberList);

       Set<TechnicalDevicesEntity> otaSet = new HashSet<>();
       otaSet.addAll(otaList);
       Date date = new Date(Calendar.getInstance().getTime().getTime());
       GroupDamageEntity groupDamage = new GroupDamageEntity(date,damageRuleList.get(0),otaSet,buildingSet,serviceSet,subscriberSet);
       entityManager.persist(groupDamage);
   }

    public boolean closeGM(GroupDamageEntity groupDamageEntity ){
        GroupDamageEntity gd = entityManager.find(GroupDamageEntity.class,groupDamageEntity.getId());
        entityManager.persist(gd);
        gd.setActive(false);
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        gd.setDateFinish(date);
        entityManager.flush();
        return true;
    }

    private List<TechnicalDevicesEntity> getOTA(Long id){
        Query query = entityManager.createQuery("select entity from TechnicalDevicesEntity entity where entity.id =:id");
        query.setParameter("id",id);
        return query.getResultList();
    }

   private List<DamageRuleEntity> getDamageRule(DamageRuleEntity.TypeRule typeRule){
       Query query = entityManager.createQuery("select entity from DamageRuleEntity entity where entity.typeRule =:typeRule");
       query.setParameter("typeRule",typeRule);
       return query.getResultList();
   }
   private List<RealizedServiceEntity> getAllRealizedServiceByType(TechnicalDevicesEntity.TypeTA typeTA, Long id){
        String sql = "SELECT SERVICE_ID\n" +
                "FROM REALIZED_SERVICE_OTA \n" +
                "INNER JOIN OTA ON REALIZED_SERVICE_OTA.OTA_ID = OTA.ID   \n" +
                "WHERE OTA.TYPE = :TYPE AND OTA_ID = :ID";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("TYPE", typeTA.getInd());
        query.setParameter("ID", id);

        List<Long> idList = toLongId(query.getResultList());

        Query query2 = entityManager.createQuery("select entity from RealizedServiceEntity entity where entity.id in(:listId)");
        query2.setParameter("listId",idList);
        return query2.getResultList();
    }
    private List<SubscriberEntity> getAllSubscriberByType(Long id){
        String sql = "SELECT SUBSCRIBER.SUBSCRIBER_ID\n" +
                "FROM REALIZED_SERVICE_OTA \n" +
                "INNER JOIN SERVICE_SUB\n" +
                "ON REALIZED_SERVICE_OTA.SERVICE_ID = SERVICE_SUB.SERVICE_ID \n" +
                "INNER JOIN SUBSCRIBER \n" +
                "ON SERVICE_SUB.SUB_ID= SUBSCRIBER.SUBSCRIBER_ID\n" +
                "WHERE REALIZED_SERVICE_OTA.OTA_ID = :ID";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("ID", id);

        List<Long> idList = toLongId(query.getResultList());

        Query query2 = entityManager.createQuery("select entity from SubscriberEntity entity where entity.subscriber_id in(:listId)");
        query2.setParameter("listId",idList);
        return query2.getResultList();
    }

    private List<BuildingEntity> getAllRealizedBuildingByType(TechnicalDevicesEntity.TypeTA typeTA, Long id){
        String sql = "SELECT NUMBER,REGION,STREET\n" +
                "FROM REALIZED_SERVICE_OTA \n" +
                "INNER JOIN OTA \n" +
                "ON REALIZED_SERVICE_OTA.OTA_ID = OTA.ID \n" +
                "WHERE OTA.TYPE = :TYPE AND OTA_ID = :ID";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("TYPE", typeTA.getInd());
        query.setParameter("ID", id);

        List<Object[]> list = query.getResultList();
        List<BuildingEntity> buildingEntityList = new ArrayList<>();
        for (Object[] obj: list
             ) {
            Query query2 = entityManager.createQuery("select entity from BuildingEntity entity where entity.number = :number and entity.street = :street and entity.region = :region");
            query2.setParameter("number",(String)obj[0]);
            query2.setParameter("street",(String)obj[2]);
            query2.setParameter("region",(String)obj[1]);
            buildingEntityList.addAll(query2.getResultList());
        }


        return buildingEntityList;
    }
   private List<Long> toLongId(List<BigInteger> bigIntegerList){
       List<Long> idList = new ArrayList<>();
       for (BigInteger strId: bigIntegerList
       ) {
           idList.add(Long.valueOf(strId.toString()));
       }
       return idList;
   }
}
