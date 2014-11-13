package parsii.eval;

import java.util.*;

public class Value extends Expression {
    private final double[] doubleValues;
    private final String stringValue;

    public Value(double[] values) {
        this.doubleValues = values;
        this.stringValue = null;
    }

    public Value(int value) {
        this.doubleValues = new double[] { (int)value };
        this.stringValue = null;
    }

    public Value(double value) {
        this.doubleValues = new double[] { value };
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
        if (doubleValues != null && doubleValues.length > 1) {
            throw new RuntimeException("Cannot convert array value to double");
        }
        return doubleValues[0];
    }

    public double[] values() {
        return doubleValues;
    }

    public static boolean isNaN(Value value) {
        if (value.stringValue != null) {
            return true;
        }
        for (Double d : value.values()) {
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
