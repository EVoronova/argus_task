package entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

@Entity
@Table(name = "SUBSCRIBER")
public class SubscriberEntity {
    @Id
    @Column(name = "SUBSCRIBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriber_id;

    @Column(name = "NAME")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "SERVICE_SUB",
            joinColumns = @JoinColumn(name = "SUB_ID"),
            inverseJoinColumns = @JoinColumn(name = "SERVICE_ID")
    )
    private Set<RealizedServiceEntity> realizedServiceSet = new HashSet<>();

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "number", referencedColumnName = "number",insertable = false, updatable = false),
            @JoinColumn(name = "street", referencedColumnName = "street",insertable = false, updatable = false),
            @JoinColumn(name = "region", referencedColumnName = "region",insertable = false, updatable = false),}
            )

    private BuildingEntity building;
    @AttributeOverrides({
            @AttributeOverride(name = "number",
                    column = @Column(name = "NUMBER")),
            @AttributeOverride(name = "street",
                    column = @Column(name = "STREET")),
            @AttributeOverride(name = "region",
                    column = @Column(name = "REGION"))
    })
    private String number;
    private String street;
    private String region;

    @ManyToMany(mappedBy = "subscribers")
    private Set<GroupDamageEntity> groupDamageSet = new HashSet<>();

    public SubscriberEntity() {
    }

    public Long getSubscriber_id() {
        return subscriber_id;
    }

    public SubscriberEntity(String name, BuildingEntity building, Set<ServiceEntity> serviceEntitySet) {
        this.name = name;
        setBuilding(building);
        addRealizedServices(serviceEntitySet);
        addOTASet(serviceEntitySet);
    }

    public BuildingEntity getBuilding() {
        return building;
    }

    public void setBuilding(BuildingEntity building) {
        this.building = building;
        this.number = building.getNumber();
        this.street = building.getStreet();
        this.region = building.getRegion();
        building.addSubscriber(this);
    }
    private void addOTASet(Set<ServiceEntity> services){
        for (ServiceEntity service: services
             ) {
            this.building.addObjectsTA(service.getOtaSet());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RealizedServiceEntity> getRealizedServiceSet() {
        return realizedServiceSet;
    }

    public void addRealizedService(ServiceEntity service) {
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        RealizedServiceEntity realizedService = new RealizedServiceEntity(date,service,true);
        this.realizedServiceSet.add(realizedService);

        realizedService.addSubscriber(this);
    }

    public void addRealizedServices(Set<ServiceEntity> services) {
        for (ServiceEntity service: services
             ) {
            addRealizedService(service);
        }
    }

    public Set<GroupDamageEntity> getGroupDamageSet() {
        return groupDamageSet;
    }

    public void addGroupDamage(GroupDamageEntity groupDamage) {
        this.groupDamageSet.add(groupDamage);
    }

    public Set<String> getServiceList(){
        Set<String> list =new HashSet<>();
        for (RealizedServiceEntity service : this.realizedServiceSet
        ) {
            list.add(service.getService().getName());
        }
        return list;
    }
    @Override
    public String toString() {
        return name;
    }
}
