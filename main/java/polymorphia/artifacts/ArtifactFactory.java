package polymorphia.artifacts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class ArtifactFactory {
    private static final Random random = new Random();
    private static final int MINIMUM_ATTACK_VALUE = 1;
    private static final int MAXIMUM_ATTACK_VALUE = 3;

    public static double DEFAULT_STRENGTH = 0.0;
    public static double DEFAULT_MOVING_COST = 0.2;
    public static int DEFAULT_ATTACK_VALUE = 0;

    private static final double MINIMUM_VALUE = 1.0;
    private static final double MAXIMUM_VALUE = 2.0;
    private static final double MINIMUM_ARMOR_STRENGTH = 1.0;
    private static final double MAXIMUM_ARMOR_STRENGTH = 3.0;
    private static final double MINIMUM_POTION_HEALTH = 3.0;
    private static final double MAXIMUM_POTION_HEALTH = 6.0;

    private static final Map<ArtifactType, String[]> NAMES = new HashMap<>();
    public static Map<String, Integer> NAME_INDEXES = new HashMap<>();


    static {
        NAMES.put(ArtifactType.Food, new String[]{
                "cupcake", "apple", "banana", "steak", "salad", "fries", "burger", "pizza", "eggs",
                "bacon", "muffin", "donut", "chicken", "pasta", "rice", "sushi", "taco", "burrito",
                "nachos", "chips"});
        NAMES.put(ArtifactType.Armor, new String[]{
                "leather", "chainmail", "plate", "iron", "steel", "mithril", "dragon-hide",
                "titanium", "platinum", "gold"
        });
        NAMES.put(ArtifactType.Cloak,
                new String[]{"invisibility-cloak", "no-see-um-cloak", "Harry-Potter-cloak"});
        NAMES.put(ArtifactType.Potion,
                new String[]{"Elixir of Life", "Potion of Reanimation", "Sorcerer's Serum"});
        NAMES.put(ArtifactType.Weapon,
                new String[]{"Sword", "Spear", "Mace", "Axe"});
        NAMES.put(ArtifactType.Treasure,
                new String[]{"Chest", "Holy Grail", "Jewels", "Diamonds", "Gold"});
        NAMES.put(ArtifactType.TreasureMap,
                new String[]{"X Marks the Spot", "Encoded Map", "Treasure Map"});

        for (ArtifactType type : ArtifactType.values()) {
            NAME_INDEXES.put(type.name(), 0);
        }
    }

    private static String getName(ArtifactType type) {
        int currentIndex = NAME_INDEXES.get(type.name());
        String baseName = NAMES.get(type)[currentIndex % NAMES.get(type).length];
        int repeatCount = currentIndex / NAMES.get(type).length;
        baseName += (repeatCount > 0 ? repeatCount : "");
        NAME_INDEXES.put(type.name(), currentIndex + 1);
        return baseName;
    }

    private static double getRandomValue() {
        return random.nextDouble(MINIMUM_VALUE, MAXIMUM_VALUE);
    }

    private static double getRandomStrength() {
        return random.nextDouble(MINIMUM_ARMOR_STRENGTH, MAXIMUM_ARMOR_STRENGTH);
    }

    private static double getRandomPotionHealth() {
        return random.nextDouble(MINIMUM_POTION_HEALTH, MAXIMUM_POTION_HEALTH);
    }

    private static int getRandomAttackValue() {
        return random.nextInt(MINIMUM_ATTACK_VALUE, MAXIMUM_ATTACK_VALUE);
    }

    public List<IArtifact> createFoodItems(int numberOfItems) {
        return createArtifacts(ArtifactType.Food, numberOfItems);
    }

    public List<IArtifact> createArmoredSuits(int numberOfItems) {
        return IntStream.range(0, numberOfItems)
                .mapToObj(_ -> createArmor())
                .toList();
    }

    public List<IArtifact> createArtifacts(ArtifactType type, int numberOfItems) {
        return IntStream.range(0, numberOfItems)
                .mapToObj(_ -> create(type))
                .toList();
    }

    public List<IArtifact> createPotions(int numberOfItems) {
        return IntStream.range(0, numberOfItems)
                .mapToObj(_ -> createPotion())
                .toList();
    }

    public List<IArtifact> createWeapons(int numberOfItems) {
        return IntStream.range(0, numberOfItems)
                .mapToObj(_ -> createWeapon())
                .toList();
    }

    public List<IArtifact> createTreasures(int numberOfItems) {
        return IntStream.range(0, numberOfItems)
                .mapToObj(_ -> createTreasure())
                .toList();
    }

    public List<IArtifact> createMaps(int numberOfItems) {
        return IntStream.range(0, numberOfItems)
                .mapToObj(_ -> createTreasureMap())
                .toList();
    }

    public List<IArtifact> createCloaks(int numberOfItems) {
        return IntStream.range(0, numberOfItems)
                .mapToObj(_ -> createCloak())
                .toList();
    }

    public List<IArtifact> createTreasureMaps(int numberOfItems) {
        return IntStream.range(0, numberOfItems)
                .mapToObj(_ -> createTreasureMap())
                .toList();
    }

    public IArtifact create(ArtifactType type) {
        return create(type, getName(type), getRandomValue(), getRandomValue(), getRandomValue(), getRandomAttackValue());
    }

    public IArtifact create(ArtifactType type, String name, double healthValue, double defenseValue) {
        return create(type, name, healthValue, defenseValue, DEFAULT_MOVING_COST, DEFAULT_ATTACK_VALUE);
    }

    public IArtifact create(ArtifactType type, String name, double healthValue, double defenseValue, double movingCost, int attackValue) {
        return switch (type) {
            case Food -> new Food(name, healthValue);
            case Armor -> new Armor(name, defenseValue, movingCost);
            case Potion -> new Potion(name, healthValue);
            case Cloak -> new Cloak(name, defenseValue);
            case Weapon -> new Weapon(name, attackValue);
            case Treasure -> new Treasure(name);
            case TreasureMap -> new TreasureMap(name);
        };
    }

    public IArtifact createFood(String name) {
        return create(ArtifactType.Food, name, getRandomValue(), DEFAULT_STRENGTH);
    }

    public IArtifact createFood(String name, double value) {
        return create(ArtifactType.Food, name, value, DEFAULT_STRENGTH);
    }

    public IArtifact createArmor() {
        return new Armor(getName(ArtifactType.Armor), getRandomStrength(), DEFAULT_MOVING_COST);
    }

    private IArtifact createPotion() {
        return new Potion(getName(ArtifactType.Potion), getRandomPotionHealth());
    }

    public IArtifact createTreasure() {
        return new Treasure(getName(ArtifactType.Treasure));
    }

    public IArtifact createCloak() {
        return new Cloak(getName(ArtifactType.Cloak), getRandomStrength());
    }

    public IArtifact createWeapon() {
        return new Weapon(getName(ArtifactType.Weapon), getRandomAttackValue());
    }

    public IArtifact createTreasureMap() {
        return new TreasureMap(getName(ArtifactType.TreasureMap));
    }

}
