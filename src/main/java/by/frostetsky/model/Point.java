package by.frostetsky.model;


public enum Point {
    ZERO("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD");

    private final String value;

    Point(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public Point next() {
        return switch (this) {
            case ZERO -> FIFTEEN;
            case FIFTEEN -> THIRTY;
            case THIRTY -> FORTY;
            case FORTY,  ADVANTAGE-> ADVANTAGE;
        };
    }

    public Point prev() {
        return switch (this) {
            case ZERO -> ZERO;
            case FIFTEEN -> ZERO;
            case THIRTY -> FIFTEEN;
            case FORTY -> THIRTY;
            case ADVANTAGE-> FORTY;
            default -> ZERO;
        };
    }
}
