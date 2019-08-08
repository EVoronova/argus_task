package entities;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "BUILDING")
@IdClass(AddressEntity.class)
public class BuildingEntity {

    public BuildingEntity() {}

    public BuildingEntity(AddressEntity addressEntity) {
        setAddress(addressEntity);
    }

    @Id
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

    public void setAddress(AddressEntity addressEntity) {
        number = addressEntity.getNumber();
        street = addressEntity.getStreet();
        region = addressEntity.getRegion();
    }

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<TechnicalDevicesEntity> objectsTA = new HashSet<>();

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<SubscriberEntity> subscriberSet = new HashSet<>();

    @ManyToMany(mappedBy = "buildings")
    private Set<GroupDamageEntity> groupDamageSet = new HashSet<>();

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Set<SubscriberEntity> getSubscriberSet() {
        return subscriberSet;
    }

    public void addSubscriber(SubscriberEntity subscriber) {
        this.subscriberSet.add(subscriber);
    }
    public Set<TechnicalDevicesEntity> getObjectsTA() {
        return objectsTA;
    }

    public void addObjectTA(TechnicalDevicesEntity objectTA) {
        this.objectsTA.add(objectTA);
        objectTA.setBuilding(this);
    }
    public void addObjectsTA(Set<TechnicalDevicesEntity> objectsTA) {
        for (TechnicalDevicesEntity ota: objectsTA
             ) {
            addObjectTA(ota);
        }
    }

    public String getAddress(){
        return "Регион: " + region + ", ул. " + street + ", д. " + number;
    }

    @Override
    public String toString() {
        return "Регион: " + region + ", ул. " + street + ", д. " + number;
    }

    public Set<GroupDamageEntity> getGroupDamageSet() {
        return groupDamageSet;
    }

    public void addGroupDamage(GroupDamageEntity groupDamage) {
        this.groupDamageSet.add(groupDamage);
    }
}


