package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SERVICE")
public class ServiceEntity {
    @Id
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id",insertable = false, updatable = false)
    private ServiceOwnerEntity serviceOwner;

    private Long owner_id;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<RealizedServiceEntity> realizedServiceSet = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "SERVICE_OTA",
            joinColumns = @JoinColumn(name = "SERVICE_ID"),
            inverseJoinColumns = @JoinColumn(name = "OTA_ID")
    )
    private Set<TechnicalDevicesEntity> otaSet = new HashSet<>();

    public ServiceEntity() {
    }

    public ServiceEntity(String name, ServiceOwnerEntity serviceOwnerEntity, Set<TechnicalDevicesEntity> otaSet) {
        this.name = name;
        setServiceOwnerEntity(serviceOwnerEntity);
        addOTASet(otaSet);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceOwnerEntity getServiceOwnerEntity() {
        return serviceOwner;
    }

    public void setServiceOwnerEntity(ServiceOwnerEntity serviceOwner) {
        this.serviceOwner = serviceOwner;
        this.owner_id = serviceOwner.getId();
    }

    public Set<RealizedServiceEntity> getRealizedServiceSet() {
        return realizedServiceSet;
    }

    public void addRealizedService(RealizedServiceEntity realizedService) {
        this.realizedServiceSet.add(realizedService);
    }

    public ServiceOwnerEntity getServiceOwner() {
        return serviceOwner;
    }

    public void setServiceOwner(ServiceOwnerEntity serviceOwner) {
        this.serviceOwner = serviceOwner;
    }

    public Set<TechnicalDevicesEntity> getOtaSet() {
        return otaSet;
    }

    public void addOTASet(Set<TechnicalDevicesEntity> otaSet) {
        for (TechnicalDevicesEntity ota: otaSet
             ) {
            addOTA(ota);
        }
    }

    public void addOTA(TechnicalDevicesEntity ota){
        this.otaSet.add(ota);
        ota.addService(this);
    }

    public Set<String> getOTAList(){
        Set<String> list =new HashSet<>();
        for (TechnicalDevicesEntity ota:this.otaSet
             ) {
            list.add(ota.getName());
        }
        return list;
    }

    @Override
    public String toString() {
        return "ServiceEntity{" +
                "name='" + name + '\'' +
                ", serviceOwnerEntity=" + owner_id +
                '}';
    }
}
