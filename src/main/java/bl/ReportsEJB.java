package bl;

import entities.GroupDamageEntity;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Stateful
public class ReportsEJB {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;
    private List<Object[]> listSubReport = new ArrayList<>();
    private List<Object[]> listOTAReport = new ArrayList<>();
    private List<GroupDamageEntity> listHistoryGDReport = new ArrayList<>();

    public void generateSubGDReport(Date dateStart, Date dateFinish,int limit){
        listSubReport.clear();
        listSubReport = getListSubForGDReports(dateStart,dateFinish,limit);
    }

    public void generateOTAGDReport(Date dateStart, Date dateFinish,int limit){
        listOTAReport.clear();
        listOTAReport = getListOTAForGDReports(dateStart,dateFinish,limit);
    }

    public void generateHistoryGDReport(Date dateStart, Date dateFinish){
        listHistoryGDReport.clear();
        listHistoryGDReport = getHistoryListGDReports(dateStart,dateFinish);
    }

    public List<Object[]> getListSubReport() {
        return listSubReport;
    }

    public List<Object[]> getListOTAReport() {
        return listOTAReport;
    }

    public List<GroupDamageEntity> getListHistoryGDReport() {
        return listHistoryGDReport;
    }

    private List<GroupDamageEntity> getHistoryListGDReports(Date dateStart, Date dateFinish){
        Query query = entityManager.createQuery("select entity from GroupDamageEntity entity where entity.dateStart >=:dateStart and entity.dateFinish <=:dateFinish");
        query.setParameter("dateStart",dateStart);
        query.setParameter("dateFinish",dateFinish);
        return query.getResultList();
    }

    private List<Object[]> getListOTAForGDReports(Date dateStart, Date dateFinish, int limit){
        String sql = "SELECT \n" +
                "GROUP_DAMAGE.ID,\n" +
                "GROUP_DAMAGE.DATE_START,\n" +
                "GROUP_DAMAGE.DATE_FINISH,\n" +
                "GROUP_DAMAGE.TYPE_RULE,\n" +
                "GROUP_DAMAGE.ACTIVE,\n" +
                "OTA_NUM.OTA_ID,\n" +
                "OTA_NUM.NAME,\n" +
                "OTA_NUM.\"TYPE\",\n" +
                "OTA_NUM.\"NUMBER\",\n" +
                "OTA_NUM.STREET,\n" +
                "OTA_NUM.REGION\n" +
                "FROM GROUP_DAMAGE_OTA INNER JOIN \n" +
                "(SELECT \n" +
                "GROUP_DAMAGE_OTA.OTA_ID,\n" +
                "OTA.NAME,\n" +
                "OTA.\"TYPE\",\n" +
                "OTA.\"NUMBER\",\n" +
                "OTA.STREET,\n" +
                "OTA.REGION,\n" +
                "SUM(1) AS INT_SUM\n" +
                "FROM GROUP_DAMAGE_OTA\n" +
                "INNER JOIN OTA\n" +
                "ON GROUP_DAMAGE_OTA.OTA_ID = OTA.ID\n" +
                "INNER JOIN GROUP_DAMAGE\n" +
                "ON GROUP_DAMAGE_OTA.GROUP_DAMAGE_ID = GROUP_DAMAGE.ID\n" +
                "WHERE GROUP_DAMAGE.DATE_START >= :dateStart AND GROUP_DAMAGE.DATE_START <= :dateFinish\n" +
                "GROUP BY GROUP_DAMAGE_OTA.OTA_ID) AS OTA_NUM\n" +
                "ON GROUP_DAMAGE_OTA.OTA_ID = OTA_NUM.OTA_ID\n" +
                "INNER JOIN GROUP_DAMAGE\n" +
                "ON GROUP_DAMAGE_OTA.GROUP_DAMAGE_ID = GROUP_DAMAGE.ID\n" +
                "WHERE OTA_NUM.INT_SUM>:limit";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("dateStart",dateStart);
        query.setParameter("dateFinish",dateFinish);
        query.setParameter("limit",limit);
        return query.getResultList();
    }

    private List<Object[]> getListSubForGDReports(Date dateStart, Date dateFinish, int limit){
        String sql = "SELECT\n" +
                "GROUP_DAMAGE.DATE_START,\n" +
                "GROUP_DAMAGE.DATE_FINISH,\n" +
                "GROUP_DAMAGE.ACTIVE,\n" +
                "GROUP_DAMAGE.ID,SUB.SUBSCRIBER_ID,\n" +
                "SUB.NAME, SUB.\"NUMBER\", \n" +
                "SUB.STREET, \n" +
                "SUB.REGION\n" +
                "FROM GROUP_DAMAGE_SUBSCRIBER INNER JOIN \n" +
                "(SELECT \n" +
                "SUBSCRIBER.SUBSCRIBER_ID, \n" +
                "SUBSCRIBER.NAME, SUBSCRIBER.\"NUMBER\", \n" +
                "SUBSCRIBER.STREET, \n" +
                "SUBSCRIBER.REGION, \n" +
                "SUM(1) AS INT_SUM\n" +
                "FROM GROUP_DAMAGE_SUBSCRIBER \n" +
                "INNER JOIN SUBSCRIBER \n" +
                "ON GROUP_DAMAGE_SUBSCRIBER.SUBSCRIBER_ID = SUBSCRIBER.SUBSCRIBER_ID\n" +
                "INNER JOIN GROUP_DAMAGE\n" +
                "ON GROUP_DAMAGE_SUBSCRIBER.GROUP_DAMAGE_ID = GROUP_DAMAGE.ID\n" +
                "WHERE GROUP_DAMAGE.DATE_START >= :dateStart AND GROUP_DAMAGE.DATE_START <= :dateFinish\n" +
                "GROUP BY SUBSCRIBER.SUBSCRIBER_ID) AS SUB \n" +
                "ON GROUP_DAMAGE_SUBSCRIBER.SUBSCRIBER_ID = SUB.SUBSCRIBER_ID\n" +
                "INNER JOIN GROUP_DAMAGE \n" +
                "ON GROUP_DAMAGE.ID = GROUP_DAMAGE_SUBSCRIBER.GROUP_DAMAGE_ID\n" +
                "WHERE SUB.INT_SUM > :limit";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("dateStart",dateStart);
        query.setParameter("dateFinish",dateFinish);
        query.setParameter("limit",limit);
        return query.getResultList();
    }
}
