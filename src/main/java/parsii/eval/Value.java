package parsii.eval;

import java.util.*;

public class Value extends Expression {
    private final double[] values;

    public Value(double[] values) {
        this.values = values;
    }

    public Value(int value) {
        this.values = new double[] { (double)value };
    }

    public Value(double value) {
        this.values = new double[] { value };
    }

    @Override
    public Value evaluate() {
        return this;
    }

    public double doubleValue() {
        if (values.length > 1) {
            throw new RuntimeException("Cannot convert array value to double");
        }
        return values[0];
    }

    public double[] values() {
        return values;
    }

    public static boolean isNaN(Value value) {
        for (Double d : value.values()) {
            if (!Double.isNaN(d)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return values().toString();
    }
}
