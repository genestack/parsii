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
            if (Value.isNaN(a)) {
                return a;
            }
            values.add(a);
        }
        return evaluate(values);
    }

    /**
     * Performs the computation of the n-ary function
     *
     * @param a the first argument of the function
     * @param b the second argument of the function
     * @return the result of calling the function with a and b
     */
    protected abstract double eval(double[] b);

    protected Value evaluate(List<Value> args) {
        int newSize = 1;
        int size;
        for (Value v : args) {
            size = v.values().length;
            if (size == 1) {
                continue;
            }
            if (newSize == 1) {
                newSize = size;
            } else {
                if (newSize != size) {
                    throw new RuntimeException("Cannot operate on list values of unequal lengths!");
                }
            }
        }

        final List<Value> newArgs = new ArrayList<Value>(args.size());
        for (Value v : args) {
            if (v.values().length == 1) {
                final double[] fill = new double[newSize];
                Arrays.fill(fill, v.values()[0]);
                newArgs.add(new Value(fill));
            } else {
                newArgs.add(v);
            }
        }

        final double[] result = new double[newSize];
        final double[] doubleArgs = new double[newArgs.size()];
        final List<double[]> vals = new ArrayList<double[]>(newArgs.size());
        for (Value v : newArgs) {
            vals.add(v.values());
        }
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < vals.size(); j++) {
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
