package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "OTA")
public class TechnicalDevicesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private TypeTA type;
    private String number;
    private String street;
    private String region;

    public TechnicalDevicesEntity() {
    }

    public TechnicalDevicesEntity(String name, TypeTA type) {
        this.name = name;
        this.type = type;
    }

    @Column(name = "BUILDING")
    public BuildingEntity getBuilding() {
        return building;
    }

    public void setBuilding(BuildingEntity building) {
        this.building = building;
        setAddress(building);
    }
    private void setAddress(BuildingEntity building){
        this.number = building.getNumber();
        this.street = building.getStreet();
        this.region = building.getRegion();
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "TYPE")
    @Enumerated(EnumType.ORDINAL)
    public TypeTA getType() {
        return type;
    }

    public void setType(TypeTA type) {
        this.type = type;
    }

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "number", referencedColumnName = "number",insertable = false, updatable = false),
            @JoinColumn(name = "street", referencedColumnName = "street",insertable = false, updatable = false),
            @JoinColumn(name = "region", referencedColumnName = "region",insertable = false, updatable = false),})
    private BuildingEntity building;

    @ManyToMany(mappedBy = "objectsTA")
    private Set<RealizedServiceEntity> realizedServiceSet = new HashSet<>();

    @ManyToMany(mappedBy = "objectsTA")
    private Set<GroupDamageEntity> groupDamageSet = new HashSet<>();

    @ManyToMany(mappedBy = "otaSet")
    private Set<ServiceEntity> serviceSet = new HashSet<>();

    public Set<RealizedServiceEntity> getRealizedServiceSet() {
        return realizedServiceSet;
    }

    public void addRealizedService(RealizedServiceEntity realizedService) {
        this.realizedServiceSet.add(realizedService);
    }

    public Set<ServiceEntity> getServiceSet() {
        return serviceSet;
    }

    public void addServiceSet(Set<ServiceEntity> serviceSet) {
        for (ServiceEntity service: serviceSet
             ) {
            addService(service);
        }
    }
    public void addService(ServiceEntity service){
        this.serviceSet.add(service);
    }

    public Set<GroupDamageEntity> getGroupDamageSet() {
        return groupDamageSet;
    }

    public void addGroupDamage(GroupDamageEntity groupDamage) {
        this.groupDamageSet.add(groupDamage);
    }

    public enum  TypeTA{
        connector(0), cable(1), node(2);
        private int ind;

        TypeTA(int ind){
            this.ind = ind;
        }

        public int getInd() {
            return ind;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
