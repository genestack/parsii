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
 * Represents a binary function.
 * <p>A binary function has two arguments which are always evaluated in order to compute the final result.</p>
 *
 * @author Andreas Haufler (aha@scireum.de)
 * @since 2013/09
 */
public abstract class BinaryFunction extends NaryFunction implements Function {

    @Override
    public int getNumberOfArguments() {
        return 2;
    }

    @Override
    protected double eval(double[] args) {
        return eval(args[0], args[1]);
    }

    /**
     * Performs the computation of the binary function
     *
     * @param a the first argument of the function
     * @param b the second argument of the function
     * @return the result of calling the function with a and b
     */
    protected abstract double eval(double a, double b);

    @Override
    public boolean isNaturalFunction() {
        return true;
    }
}
