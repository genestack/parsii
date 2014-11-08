/*
 * Made with all the love in the world
 * by scireum in Remshalden, Germany
 *
 * Copyright by scireum GmbH
 * http://www.scireum.de - info@scireum.de
 */

package parsii.eval;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents an unary function.
 * <p>An unary function has one arguments which is always evaluated in order to compute the final result.</p>
 *
 * @author Andreas Haufler (aha@scireum.de)
 * @since 2013/09
 */
public abstract class UnaryFunction implements Function {

    @Override
    public int getNumberOfArguments() {
        return 1;
    }

    @Override
    public Value eval(List<Expression> args) {
        Value a = args.get(0).evaluate();
        if (Value.isNaN(a)) {
            return a;
        }
        return eval(a);
    }

    /**
     * Performs the computation of the unary function
     *
     * @param a the argument of the function
     * @return the result of calling the function with a as argument
     */
    protected abstract double eval(double a);

    protected Value eval(Value a) {
        final List<Double> as = a.values();
        final List<Double> values = new ArrayList<Double>(as.size());
        for (Double d : as) {
            values.add(eval(d));
        }
        return new Value(values);
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }
}
