package bl;


//import entities.UserEntity;
//import entities.instruments.Cable;

import entities.*;
import org.primefaces.model.DualListModel;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@SessionScoped
public class MyCDIClass implements Serializable {
    private String login;
    private String password;
    private String region;
    private String street;
    private String number;
    private String name;
    private List<ServiceEntity> listService;
    private DualListModel<String> dualListServices;

    private boolean createSubSuccess;

    @EJB
    private MyEJBClass myEJBClass;
    @EJB
    private InitDBService initDBService;
    @EJB
    private DamageGroupEJB damageGroupEJB;
    @EJB
    private ReportsEJB reportsEJB;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ServiceEntity> getListService() {
        return listService;
    }

    public DualListModel<String> getDualListServices() {
        List<String> servicesSource = getAllServiceName();
        List<String> servicesTarget = new ArrayList<String>();
        dualListServices = new DualListModel<String>(servicesSource, servicesTarget);
        return dualListServices;
    }

    public void setDualListServices(DualListModel<String> dualListServices) {
        this.dualListServices = dualListServices;
    }

    public void setListService(List<ServiceEntity> listService) {
        this.listService = listService;
    }

    public MyEJBClass getMyEJBClass() {
        return myEJBClass;
    }

    public InitDBService getInitDBService() {
        return initDBService;
    }

    public void setInitDBService(InitDBService initDBService) {
        this.initDBService = initDBService;
    }

    public void setMyEJBClass(MyEJBClass myEJBClass) {
        this.myEJBClass = myEJBClass;
    }


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void createSubscriber(){
        createSubSuccess = initDBService.createSubscriber(region,street,number,name,dualListServices.getTarget());
        if(createSubSuccess){
            FacesContext.getCurrentInstance().addMessage
                    (null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Абонент создан!", null));
        }
    }
    public void createUser(){
        initDBService.createUser(login, password);
    }


    public void deleteSubscriber(SubscriberEntity subscriberEntity){
        myEJBClass.deleteSubscriber(subscriberEntity);
    }
    public void closeGD(GroupDamageEntity groupDamageEntity){
        damageGroupEJB.closeGM(groupDamageEntity);
    }

    public List<BuildingEntity> getAllBuildings(){
        return myEJBClass.getAllBuildings();
    }

    public List<ServiceEntity> getAllServices(){
        return myEJBClass.getAllServices();
    }

    public List<String> getAllServiceName(){
        return myEJBClass.getAllServiceName();
    }


    public List<TechnicalDevicesEntity> getAllDevices(){
        return myEJBClass.getAllDevices();
    }
    public List<DamageRuleEntity> getAllDamageRules(){
        return myEJBClass.getAllDamageRules();
    }
    public List<SubscriberEntity> getAllSubscribers(){
        return myEJBClass.getAllSubscriber();
    }
    public List<GroupDamageEntity> getAllDG(){
        return myEJBClass.getAllDG();
    }
    public void test(String name){
        int k=0;
    }

    public void createGPByNode(Long id){
        if(id!=null) {
            damageGroupEJB.createGDByNode(id);
        }
    }
    public void createGPByCable(Long id){
        if(id!=null){
            damageGroupEJB.createGDByCable(id);
        }
    }
    public void createGPByConnector(Long id){
        if(id!=null){
            damageGroupEJB.createGDByConnector(id);
        }
    }

    public void generateSubGDReport(Date dateStart, Date dateFinish){
        if(dateStart!=null&&dateFinish!=null) {
            reportsEJB.generateSubGDReport(java.sql.Date.valueOf(DateComponent.utilDateToLocalDate(dateStart)), java.sql.Date.valueOf(DateComponent.utilDateToLocalDate(dateFinish)), 1);
        }
    }
    public void generateOTAGDReport(Date dateStart, Date dateFinish){
        if(dateStart!=null&&dateFinish!=null) {
            reportsEJB.generateOTAGDReport(java.sql.Date.valueOf(DateComponent.utilDateToLocalDate(dateStart)), java.sql.Date.valueOf(DateComponent.utilDateToLocalDate(dateFinish)), 1);
        }
    }
    public void generateHistoryGDReport(Date dateStart, Date dateFinish){
        if(dateStart!=null&&dateFinish!=null) {
            reportsEJB.generateHistoryGDReport(java.sql.Date.valueOf(DateComponent.utilDateToLocalDate(dateStart)), java.sql.Date.valueOf(DateComponent.utilDateToLocalDate(dateFinish)));
        }
    }
    public List<Object []> getListSubGDPerot(){
        return reportsEJB.getListSubReport();
    }
    public List<Object []> getListOTAGDReport(){
        return reportsEJB.getListOTAReport();
    }
    public List<GroupDamageEntity> getListHistoryGDReport(){
        return reportsEJB.getListHistoryGDReport();
    }
//    public List<TechnicalDevicesEntity> getAllRealizedOTAByNode(){
//        return damageGroupEJB.getAllota(TechnicalDevicesEntity.TypeTA.node);
//    }
}
