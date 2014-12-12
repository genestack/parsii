/*
 * Made with all the love in the world
 * by scireum in Remshalden, Germany
 *
 * Copyright by scireum GmbH
 * http://www.scireum.de - info@scireum.de
 */

package parsii.eval;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an n-ary function.
 * <p>A n-ary function has any number of arguments which are always evaluated in order to compute the final result.</p>
 */
public abstract class NaryFunction implements Function {

    @Override
    public int getNumberOfArguments() {
        return -1;
    }

    @Override
    public Value eval(List<Expression> args) {
        final List<Value> values = new ArrayList<Value>(args.size());
        for (Expression e : args) {
            Value a = e.evaluate();
            if (a.isNaN()) {
                return a;
            }
            values.add(a);
        }
        return evaluate(values);
    }

    /**
     * Performs the computation of the n-ary function
     *
     * @param args    function arguments
     * @return the result of calling the function with arguments
     */
    protected abstract double eval(double[] args);

    protected Value evaluate(List<Value> args) {
        int newSize = 1;
        for (Value v : args) {
            // TODO: Why don't we check for NaN here???
            // If you say, that eval(List<Expression>) checks that, then make the current method `private`.
            final int size = v.values().length;
            if (size == 1) {
                continue;
            }
            if (newSize == 1) {
                newSize = size;
            } else if (newSize != size) {
                throw new RuntimeException("Cannot operate on list values of unequal lengths!");
            }
        }

        // TODO: remove `newArgs` list: `args` list is more than enough
        final List<Value> newArgs = new ArrayList<Value>(args.size());
        for (Value v : args) {
            if (v.values().length == 1 && newSize > 1) {
                final double[] fill = new double[newSize];
                Arrays.fill(fill, v.values()[0]);
                newArgs.add(new Value(fill));
            } else {
                newArgs.add(v);
            }
        }

        final double[] result = new double[newSize];
        final double[] doubleArgs = new double[newArgs.size()];
        // TODO: remove `vals` list: `args` list if more than enough
        final List<double[]> vals = new ArrayList<double[]>(newArgs.size());
        for (Value v : newArgs) {
            // TODO: Why don't we check for NaN here???
            vals.add(v.values());
        }
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < vals.size(); j++) {
                // TODO: Why don't we check for NaN here??? Or we assume "eval(double[])" will do these checks?
                doubleArgs[j] = vals.get(j)[i];
            }
            result[i] = eval(doubleArgs);
        }
        return new Value(result);
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }
}
