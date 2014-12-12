/*
 * Made with all the love in the world
 * by scireum in Remshalden, Germany
 *
 * Copyright by scireum GmbH
 * http://www.scireum.de - info@scireum.de
 */

package parsii.eval;

import java.util.List;

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
        final Value a = args.get(0).evaluate();
        if (a.isNaN()) {
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
        // TODO: Why don't we check NaN here???
        // If you say, that eval(List<Expression>) checks that, then make the current method `private`.
        final double[] as = a.values();
        final double[] result = new double[as.length];
        int i = 0;
        for (double d : as) {
            // TODO: Why don't we check NaN here???
            result[i++] = eval(d);
        }
        return new Value(result);
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }
}
