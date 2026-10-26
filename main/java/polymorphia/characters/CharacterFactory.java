package polymorphia.characters;

import polymorphia.RandomDie;
import polymorphia.artifacts.IArtifact;
import polymorphia.strategy.IStrategy;
import polymorphia.strategy.StrategyFactory;

import java.util.*;
import java.util.stream.IntStream;


public class CharacterFactory {
    private static final Random random = new Random();
    public static final Double DEFAULT_KNIGHT_HEALTH = 10.0;
    public static final Double DEFAULT_GUARD_HEALTH = 5.0;
    public static final Double DEFAULT_GLUTTON_HEALTH = 6.0;
    public static final Double DEFAULT_DEMON_HEALTH = 15.0;
    public static final Double DEFAULT_CREATURE_HEALTH = 5.0;
    public static final Double DEFAULT_COWARD_HEALTH = 8.0;
    public static final Double DEFAULT_SEEKER_HEALTH = 10.0;

    public static String[] KNIGHT_NAMES = new String[]{"Arwen", "Sir Lancelot", "Lady Brienne", "King Arthur", "Sir Jamey", "Aragorn", "Isildur"};
    public static String[] COWARD_NAMES = new String[]{"Fry", "Sir Robin", "Sir Scaredy Cat", "Lady Faints-a-lot", "Lady Runaway", "Sir Chicken", "Lady Hides-a-lot"};
    public static String[] GLUTTON_NAMES = new String[]{"Sir Eats-a-lot", "Sir Gobbles", "Lady Munches", "Lady Snacks", "Sir Nibbles", "Lady Noshes"};
    public static String[] CREATURE_NAMES = new String[]{"Orc", "Troll", "Dragon", "Ogre", "Shelob", "Evil Wizard", "Goblin", "Ghoul"};
    public static String[] DEMON_NAMES = new String[]{"Satan", "Beelzebub", "Devil", "Incubus", "Lucifer", "Succubus"};
    public static String[] GUARD_NAMES = new String[]{"Gollum", "Black Knight", "Smaug"};
    public static String[] SEEKER_NAMES = new String[]{"Bilbo", "Lara", "Benjamin"};
    public static String[] RANDOM_NAMES = new String[]{"Crazy", "Unpredictable"};

    public static Map<String, String[]> NAMES = new HashMap<>();

    static {
        NAMES.put("Knight", KNIGHT_NAMES);
        NAMES.put("Coward", COWARD_NAMES);
        NAMES.put("Glutton", GLUTTON_NAMES);
        NAMES.put("Creature", CREATURE_NAMES);
        NAMES.put("Demon", DEMON_NAMES);
        NAMES.put("Guard", GUARD_NAMES);
        NAMES.put("Seeker", SEEKER_NAMES);
        NAMES.put("Random", RANDOM_NAMES);
    }

    private static StrategyFactory strategyFactory = new StrategyFactory();

    int currentIndex = 0;

    public Character createKnight(String name) {
        return createKnight(name, DEFAULT_KNIGHT_HEALTH);
    }

    public Character createKnight(String name, double initialHealth) {
        return new Adventurer(name, initialHealth, RandomDie.sixSided(), strategyFactory.newFighterStrategy());
    }

    public Character createGlutton(String name) {
        return new Adventurer(name, DEFAULT_GLUTTON_HEALTH, RandomDie.sixSided(), strategyFactory.newGluttonStrategy());
    }

    public Character createGlutton(String name, double initialHealth) {
        return new Adventurer(name, initialHealth, RandomDie.sixSided(), strategyFactory.newGluttonStrategy());
    }
    public Character createCoward(String name) {
        return new Adventurer(name, DEFAULT_COWARD_HEALTH, RandomDie.sixSided(), strategyFactory.newCowardStrategy());
    }
    public Character createRandom(String name) {
        return new Adventurer(name, DEFAULT_KNIGHT_HEALTH, RandomDie.sixSided(), strategyFactory.newRandomStrategy());
    }
    public Character createCreature(String name) {
        return createCreature(name, DEFAULT_CREATURE_HEALTH);
    }
    public Character createCreature(String name, double initialHealth) {
        return new Creature(name, initialHealth, RandomDie.sixSided(), strategyFactory.newCreatureStrategy());
    }

    public Character createDemon(String name) {
        return new Creature(name, DEFAULT_DEMON_HEALTH, RandomDie.sixSided(), strategyFactory.newCreatureStrategy());
    }

    public Character createSeeker(String name) {
        return new Adventurer(name, DEFAULT_SEEKER_HEALTH, RandomDie.sixSided(), strategyFactory.newSeekerStrategy());
    }

    public Character createGuard(String name) {
        return new Creature(name, DEFAULT_GUARD_HEALTH, RandomDie.sixSided(), strategyFactory.newGuardStrategy());
    }

    public List<Character> createCharacters(int numKnights, int numCowards, int numGluttons, int numCreatures, int numDemons, int numSeekers) {
        List<Character> characters = new ArrayList<>();
        characters.addAll(createKnights(numKnights));
        characters.addAll(createCowards(numCowards));
        characters.addAll(createGluttons(numGluttons));
        characters.addAll(createCreatures(numCreatures));
        characters.addAll(createDemons(numDemons));
        characters.addAll(createSeekers(numSeekers));
        Collections.shuffle(characters);
        return characters;
    }

    public List<Character> createCharacters(int numberOfCharacters) {
        numberOfCharacters = Math.max(numberOfCharacters, 6);
        return createCharacters(numberOfCharacters / 6, numberOfCharacters / 6, numberOfCharacters / 6,
                numberOfCharacters / 6, numberOfCharacters / 6, numberOfCharacters / 6);
    }

    public List<Character> createKnights(int numberOfCharacters) {
        return IntStream.range(0, numberOfCharacters)
                .mapToObj(_ -> createKnight())
                .toList();
    }

    public List<Character> createCowards(int numberOfCharacters) {
        return IntStream.range(0, numberOfCharacters)
                .mapToObj(_ -> createCoward())
                .toList();
    }

    public List<Character> createGluttons(int numberOfCharacters) {
        return IntStream.range(0, numberOfCharacters)
                .mapToObj(_ -> createGlutton())
                .toList();
    }

    public List<Character> createRandoms(int numberOfCharacters) {
        return IntStream.range(0, numberOfCharacters)
                .mapToObj(_ -> createRandom())
                .toList();
    }

    private Character createRandom() {
        return createRandom(getNextName("Random"));
    }

    public List<Character> createCreatures(int numberOfCharacters) {
        return IntStream.range(0, numberOfCharacters)
                .mapToObj(_ -> createCreature())
                .toList();
    }

    public List<Character> createDemons(int numberOfCharacters) {
        return IntStream.range(0, numberOfCharacters)
                .mapToObj(_ -> createDemon())
                .toList();
    }

    public List<Character> createSeekers(int numberOfCharacters) {
        return IntStream.range(0, numberOfCharacters)
                .mapToObj(_ -> createSeeker())
                .toList();
    }

    public List<Character> createHumans(String[] names) {
        return Arrays.stream(names)
                .map(name -> createHuman(name))
                .toList();
    }

    public Character createKnight() {
        return createKnight(getNextName("Knight"));
    }

    public Character createCoward() {
        return createCoward(getNextName("Coward"));
    }

    public Character createGlutton() {
        return createGlutton(getNextName("Glutton"));
    }

    public Character createCreature() {
        return createCreature(getNextName("Creature"));
    }

    public Character createDemon() {
        return createDemon(getNextName("Demon"));
    }

    public Character createSeeker() {
        return createSeeker(getNextName("Seeker"));
    }

    public Character createGuard() {
        return createGuard(getNextName("Guard"));
    }

    private String getNextName(String characterType) {
        String[] names = NAMES.get(characterType);
        currentIndex = (currentIndex + 1) % names.length;
        return names[currentIndex];
    }

    public Character newArmoredCharacter(Character wrappedCharacter, IArtifact armor) {
        return new ArmoredCharacter(wrappedCharacter, armor);
    }
    public Character createCoward(String name, double initialHealth) {
        return new Adventurer(name, initialHealth, RandomDie.sixSided(), strategyFactory.newCowardStrategy());
    }

    public Character createDemon(String name, double initialHealth) {
        return new Creature(name, initialHealth, RandomDie.sixSided(), strategyFactory.newCreatureStrategy());
    }

    public Character createGuard(String name, double initialHealth) {
        return new Creature(name, initialHealth, RandomDie.sixSided(), strategyFactory.newGuardStrategy());
    }

    public Character createSeeker(String name, double initialHealth) {
        return new Creature(name, initialHealth, RandomDie.sixSided(), strategyFactory.newSeekerStrategy());
    }

    public Character newCloakedCharacter(Character myself, IArtifact cloak) {
        return new CloakedCharacter(myself, cloak);
    }

    public Character newReincarnatableMeCharacter(Character myself, IArtifact potion) {
        return new ReincarnatableCharacter(myself, potion);
    }

    public Character newWeaponizedCharacter(Character myself, IArtifact weapon) {
        return new WeaponizedCharacter(myself, weapon);
    }

    public Character createHuman(String name) {
        return new Adventurer(name, CharacterFactory.DEFAULT_KNIGHT_HEALTH, RandomDie.sixSided(), strategyFactory.newHumanStrategy());
    }

    public Character createAPIPlayer(String name) {
        return new APIPlayer(name);
    }

    public Character createCharacter(CharacterType type, String name, double health, IStrategy strategy) {
        return switch (type) {
            case ADVENTURER -> new Adventurer(name, health, RandomDie.sixSided(), strategy);
            case CREATURE -> new Creature(name, health, RandomDie.sixSided(), strategy);
        };
    }

}
