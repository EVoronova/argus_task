package bl.beans;

import bl.DamageGroupEJB;
import entities.TechnicalDevicesEntity;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class DropdownView implements Serializable {

    @EJB
    DamageGroupEJB damageGroupEJB;
    private Map<String, Map<String,String>> data = new HashMap<>();
    private String name;
    private String id;
    private String idNode;
    private String idCable;
    private String idConnector;
    private Map<String,Long> otaNode;
    private Map<String,Long> otaCable;
    private Map<String,Long> otaConnector;

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void initNode() {
        if(otaNode == null) {
            otaNode = new HashMap<>();
        }else {
            otaNode.clear();
        }
        List<Object[]> listOTANode = damageGroupEJB.getAllota(TechnicalDevicesEntity.TypeTA.node);
        for (Object[] obj: listOTANode
        ) {
            Long id =  Long.valueOf(((BigInteger) obj[0]).toString());
            String name = (String) obj[1];
            otaNode.put(name,id);
        }
    }

    public void initCable() {
        if(otaCable == null) {
            otaCable = new HashMap<>();
        }else {
            otaCable.clear();
        }
        List<Object[]> listOTANode = damageGroupEJB.getAllota(TechnicalDevicesEntity.TypeTA.cable);
        for (Object[] obj: listOTANode
        ) {
            Long id =  Long.valueOf(((BigInteger) obj[0]).toString());
            String name = (String) obj[1];
            otaCable.put(name,id);
        }
    }

    public void initConnector() {
        if(otaConnector == null) {
            otaConnector = new HashMap<>();
        }else {
            otaConnector.clear();
        }
        List<Object[]> listOTANode = damageGroupEJB.getAllota(TechnicalDevicesEntity.TypeTA.connector);
        for (Object[] obj: listOTANode
        ) {
            Long id =  Long.valueOf(((BigInteger) obj[0]).toString());
            String name = (String) obj[1];
            otaConnector.put(name,id);
        }
    }

    public String getIdNode() {
        return idNode;
    }

    public void setIdNode(String idNode) {
        this.idNode = idNode;
    }

    public String getIdCable() {
        return idCable;
    }

    public void setIdCable(String idCable) {
        this.idCable = idCable;
    }

    public String getIdConnector() {
        return idConnector;
    }

    public void setIdConnector(String idConnector) {
        this.idConnector = idConnector;
    }

    public void init(Map<String,Long> map, TechnicalDevicesEntity.TypeTA typeTA) {
        if(map == null) {
            map = new HashMap<>();
        }else {
            map.clear();
        }
        List<Object[]> listOTANode = damageGroupEJB.getAllota(typeTA);
        for (Object[] obj: listOTANode
        ) {
            Long id =  Long.valueOf(((BigInteger) obj[0]).toString());
            String name = (String) obj[1];
            map.put(name,id);
        }
    }


    public Map<String, Long> getOtaNode() {
        initNode();
        return otaNode;
    }

    public String getName() {
        return name;
    }

    public Map<String, Long> getOtaCable() {
        initCable();
        return otaCable;
    }

    public void setOtaCable(Map<String, Long> otaCable) {
        this.otaCable = otaCable;
    }

    public Map<String, Long> getOtaConnector() {
        initConnector();
        return otaConnector;
    }

    public void setOtaConnector(Map<String, Long> otaConnector) {
        this.otaConnector = otaConnector;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOtaNode(Map<String, Long> otaNode) {
        this.otaNode = otaNode;
    }

    //    public void displayLocation() {
//        FacesMessage msg;
//        if(city != null && country != null)
//            msg = new FacesMessage("Selected", city + " of " + country);
//        else
//            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid", "City is not selected.");
//
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//    }
}
