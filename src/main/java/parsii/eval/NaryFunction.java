/*
 * Made with all the love in the world
 * by scireum in Remshalden, Germany
 *
 * Copyright by scireum GmbH
 * http://www.scireum.de - info@scireum.de
 */

package parsii.eval;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents an n-ary function.
 * <p>A n-ary function has any number of arguments which are always evaluated in order to compute the final result.</p>
 *
 * @author Andreas Haufler (aha@scireum.de)
 * @since 2013/09
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
            size = v.values().size();
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
            if (v.values().size() == 1) {
                newArgs.add(new Value(Collections.nCopies(newSize, v.values().get(0))));
            } else {
                newArgs.add(v);
            }
        }

        final List<Double> values = new ArrayList<Double>(newSize);
        double[] doubleArgs = new double[newArgs.size()];
        final List<List<Double>> vals = new ArrayList<List<Double>>(newArgs.size());
        for (Value v : newArgs) {
            vals.add(v.values());
        }
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < vals.size(); j++) {
                doubleArgs[j] = vals.get(j).get(i);
            }
            values.add(eval(doubleArgs));
        }
        return new Value(values);
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }
}
