package parsii.eval;

public class Value extends Expression {
    private final double[] doubleValues;
    private final String stringValue;

    public Value(double[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Empty values array is not supported");
        }
        this.doubleValues = values;
        this.stringValue = null;
    }

    public Value(int value) {
        this.doubleValues = new double[] {value};
        this.stringValue = null;
    }

    public Value(double value) {
        this.doubleValues = new double[] {value};
        this.stringValue = null;
    }

    public Value(String value) {
        this.doubleValues = null;
        this.stringValue = value;
    }

    @Override
    public Value evaluate() {
        return this;
    }

    public String stringValue() {
        return stringValue;
    }

    public double doubleValue() {
        if (doubleValues.length > 1) {
            throw new RuntimeException("Cannot convert array value to double");
        }
        return doubleValues[0];
    }

    public double[] values() {
        return doubleValues;
    }

    public boolean isNaN() {
        if (stringValue != null) {
            return true;
        }
        for (double d : doubleValues) {
            if (!Double.isNaN(d)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (stringValue != null) {
            sb.append(stringValue);
        } else {
            boolean first = true;
            for (double d : values()) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(d);
                first = false;
            }
        }
        return sb.append("]").toString();
    }
}
