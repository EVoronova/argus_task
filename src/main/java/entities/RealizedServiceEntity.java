package entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "REALIZED_SERVICE")
public class RealizedServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATE_START")
    private Date dateStart;

    @Column(name = "DATE_FINISH")
    private Date dateFinish;

    @Column(name = "ACTIVE")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "service_name", referencedColumnName = "name",insertable = false, updatable = false)
    private ServiceEntity service;

    private String service_name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "REALIZED_SERVICE_OTA",
            joinColumns = @JoinColumn(name = "SERVICE_ID"),
            inverseJoinColumns = @JoinColumn(name = "OTA_ID")
    )
    private Set<TechnicalDevicesEntity> objectsTA = new HashSet<>();

    @ManyToMany(mappedBy = "realizedServiceSet")
    private Set<SubscriberEntity> subscriberSet = new HashSet<>();
//    @ManyToOne
//    @JoinColumn(name = "sub_id", referencedColumnName = "subscriber_id",insertable = false, updatable = false)
//    private SubscriberEntity subscriber;

//    private Long sub_id;
    @ManyToMany(mappedBy = "realizedServices")
    private Set<GroupDamageEntity> groupDamageSet = new HashSet<>();

    public RealizedServiceEntity() {
    }

    public RealizedServiceEntity(Date dateStart, ServiceEntity service, boolean active) {
        this.dateStart = dateStart;
        this.service = service;
        this.active = active;
        this.service_name = service.getName();
//        this.subscriber = subscriberEntity;
//        this.sub_id = subscriberEntity.getSubscriber_id();
        service.addRealizedService(this);
        addOTASet(service.getOtaSet());
    }


    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }

    public ServiceEntity getService() {
        return service;
    }

    public void setService(ServiceEntity service) {
        this.service = service;
    }

    public Set<TechnicalDevicesEntity> getObjectsTA() {
        return objectsTA;
    }

    public void addOTA(TechnicalDevicesEntity objectTA) {
        this.objectsTA.add(objectTA);
        objectTA.addRealizedService(this);
    }

    public void addOTASet(Set<TechnicalDevicesEntity> objectTASet) {
        for (TechnicalDevicesEntity ota: objectTASet
             ) {
            addOTA(ota);
        }
    }

    public Set<GroupDamageEntity> getGroupDamageSet() {
        return groupDamageSet;
    }

    public void addGroupDamage(GroupDamageEntity groupDamage) {
        this.groupDamageSet.add(groupDamage);
    }

    public Set<SubscriberEntity> getSubscriberSet() {
        return subscriberSet;
    }

    public void addSubscriber(SubscriberEntity subscriber) {
        this.subscriberSet.add(subscriber);
    }

    @Override
    public String toString() {
        return service_name;
    }
}
