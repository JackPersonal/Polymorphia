package polymorphia;

public class FixedDie implements Die {
    private final int value;

    public FixedDie(int roll) {
        this.value = roll;
    }

    @Override
    public int roll() {
        return value;
    }

}
