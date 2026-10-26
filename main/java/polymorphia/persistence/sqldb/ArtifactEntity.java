package polymorphia.persistence.sqldb;

import jakarta.persistence.*;
import polymorphia.artifacts.ArtifactType;

@Entity
@Table(name = "artifacts")
public class ArtifactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ArtifactType type;

    private String name;
    private double healthValue;
    private double strength;
    private double movingCost;
    private int attackValue;

    protected ArtifactEntity() {
    }

    public ArtifactEntity(ArtifactType type, String name, double healthValue, double strength, double movingCost, int attackValue) {
        this.type = type;
        this.name = name;
        this.healthValue = healthValue;
        this.strength = strength;
        this.movingCost = movingCost;
        this.attackValue = attackValue;
    }

    public Long getId() {
        return id;
    }

    public ArtifactType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getHealthValue() {
        return healthValue;
    }

    public double getStrength() {
        return strength;
    }

    public double getMovingCost() {
        return movingCost;
    }

    public int getAttackValue() {
        return attackValue;
    }
}