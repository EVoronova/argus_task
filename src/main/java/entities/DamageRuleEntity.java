package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DAMAGE_RULE")
public class DamageRuleEntity {
    @Id
    @Enumerated(EnumType.ORDINAL)
    private TypeRule typeRule;

    @OneToMany(mappedBy = "damageRule", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<GroupDamageEntity> groupDamageSet = new HashSet<>();

    public DamageRuleEntity() {
    }

    public DamageRuleEntity(TypeRule typeRule) {
        this.typeRule = typeRule;
    }

    public Set<GroupDamageEntity> getGroupDamageSet() {
        return groupDamageSet;
    }

    public void addGroupDamage(GroupDamageEntity groupDamage) {
        this.groupDamageSet.add(groupDamage);
    }

    public TypeRule getTypeRule() {
        return typeRule;
    }

    public void setTypeRule(TypeRule typeRule) {
        this.typeRule = typeRule;
    }

    public enum  TypeRule{
        NodeRule(0,"Правило 'по узлу'"), ConnectorRule(1,"Правило 'по коннектору'"), CableRule(2,"Правило 'по кабелю'"),
        AddressRule(3,"Правило 'по адресу'"), RegionRule(4,"Правило 'по региону'");

        private int ind;
        private String name;

        TypeRule(int ind,String name){
            this.ind = ind;
            this.name = name;
        }

        public int getInd() {
            return ind;
        }

        public String getName() {
            return name;
        }
    }
}
