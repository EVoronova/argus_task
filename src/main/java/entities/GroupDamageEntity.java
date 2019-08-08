package entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "GROUP_DAMAGE")
public class GroupDamageEntity {
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
    @JoinColumn(name = "TYPE_RULE", referencedColumnName = "typeRule",insertable = false, updatable = false)
    private DamageRuleEntity damageRule;

    private int TYPE_RULE;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "GROUP_DAMAGE_OTA",
            joinColumns = @JoinColumn(name = "GROUP_DAMAGE_ID"),
            inverseJoinColumns = @JoinColumn(name = "OTA_ID")
    )
    private Set<TechnicalDevicesEntity> objectsTA = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "GROUP_DAMAGE_BUILDING",
            joinColumns = { @JoinColumn(name = "GROUP_DAMAGE_ID"),
                            },
            inverseJoinColumns = { @JoinColumn(name = "number"),
                                    @JoinColumn(name = "street"),
                                    @JoinColumn(name = "region")}
    )
    private Set<BuildingEntity> buildings = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "GROUP_DAMAGE_REALIZED_SERVICE",
            joinColumns = @JoinColumn(name = "GROUP_DAMAGE_ID"),
            inverseJoinColumns = @JoinColumn(name = "REALIZED_SERVICE_ID")
    )
    private Set<RealizedServiceEntity> realizedServices = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "GROUP_DAMAGE_SUBSCRIBER",
            joinColumns = @JoinColumn(name = "GROUP_DAMAGE_ID"),
            inverseJoinColumns = @JoinColumn(name = "SUBSCRIBER_ID")
    )
    private Set<SubscriberEntity> subscribers = new HashSet<>();

    public GroupDamageEntity() {
    }

    public GroupDamageEntity(Date dateStart, DamageRuleEntity damageRule, Set<TechnicalDevicesEntity> objectsTA, Set<BuildingEntity> buildings, Set<RealizedServiceEntity> realizedServices, Set<SubscriberEntity> subscribers) {
        this.dateStart = dateStart;
        this.active = true;
        setDamageRule(damageRule);
        addObjectsTA(objectsTA);
        addBuildings(buildings);
        addSubscribers(subscribers);
        addRealizedServices(realizedServices);
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public int getTYPE_RULE() {
        return TYPE_RULE;
    }

    public Set<TechnicalDevicesEntity> getObjectsTA() {
        return objectsTA;
    }

    public Set<BuildingEntity> getBuildings() {
        return buildings;
    }

    public Set<RealizedServiceEntity> getRealizedServices() {
        return realizedServices;
    }

    public Set<SubscriberEntity> getSubscribers() {
        return subscribers;
    }

    public DamageRuleEntity getDamageRule() {
        return damageRule;
    }

    public void setDamageRule(DamageRuleEntity damageRule) {
        this.damageRule = damageRule;
        this.TYPE_RULE = damageRule.getTypeRule().getInd();
        damageRule.addGroupDamage(this);
    }

    public void addBuildings(Set<BuildingEntity> buildings){
        for (BuildingEntity building: buildings
        ) {
            this.buildings.add(building);
            building.addGroupDamage(this);
        }
    }

    public void addSubscribers(Set<SubscriberEntity> subscribers){
        for (SubscriberEntity subscriber: subscribers
        ) {
            this.subscribers.add(subscriber);
            subscriber.addGroupDamage(this);
        }
    }

    public void addRealizedServices(Set<RealizedServiceEntity> realizedServices){
        for (RealizedServiceEntity realizedService: realizedServices
        ) {
            this.realizedServices.add(realizedService);
            realizedService.addGroupDamage(this);

        }
    }

    public void addObjectsTA(Set<TechnicalDevicesEntity> objectsTA){
        for (TechnicalDevicesEntity ota: objectsTA
             ) {
            this.objectsTA.add(ota);
            ota.addGroupDamage(this);
        }
    }

}
