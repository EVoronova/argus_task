package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "SERVICE_OWNER")
public class ServiceOwnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "serviceOwner", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ServiceEntity> servicesSet = new HashSet<>();

    public ServiceOwnerEntity() {
    }

    public ServiceOwnerEntity(String name) {
        this.name = name;
    }

    public ServiceOwnerEntity(String name, Set<ServiceEntity> servicesSet) {
        this.name = name;
        this.servicesSet = servicesSet;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Set<ServiceEntity> getServicesSet() {
        return servicesSet;
    }

    public void addService(ServiceEntity service) {
        servicesSet.add(service);
    }
}
