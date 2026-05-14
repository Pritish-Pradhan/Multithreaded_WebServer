package task;

public enum RequestPriority {

    HIGH(3),
    MEDIUM(2),
    LOW(1);

    private final int value;

    RequestPriority(int value) {

        this.value = value;
    }

    public int getValue() {

        return value;
    }
}