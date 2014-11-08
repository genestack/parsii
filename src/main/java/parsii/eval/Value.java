package parsii.eval;

import java.util.*;

public class Value extends Expression {
    private final List<Double> values;

    public Value(List<Double> d) {
        values = new ArrayList<Double>(d);
    }

    public Value(int value) {
        values = Collections.singletonList((double)value);
    }

    public Value(double value) {
        values = Collections.singletonList(value);
    }

    @Override
    public Value evaluate() {
        return this;
    }

    public double doubleValue() {
        if (values.size() > 1) {
            throw new RuntimeException("Cannot convert array value to double");
        }
        return values.get(0).doubleValue();
    }

    public List<Double> values() {
        return new ArrayList<Double>(values);
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
