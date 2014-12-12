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
 * Contains a set of predefined standard functions.
 * <p>
 * Provides mostly functions defined by {@link Math}
 * </p>
 *
 * @author Andreas Haufler (aha@scireum.de)
 * @since 2013/09
 */
public class Functions {
    /**
     * When comparing two double values, those are considered equal, if their difference is lower than the defined
     * epsilon. This is way better than relying on an exact comparison due to rounding errors
     */
    public static final double EPSILON = 0.0000000001;

    /**
     * Provides access to {@link Math#sin(double)}
     */
    public static final Function SIN = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.sin(a);
        }
    };

    /**
     * Provides access to {@link Math#sinh(double)}
     */
    public static final Function SINH = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.sinh(a);
        }
    };

    /**
     * Provides access to {@link Math#cos(double)}
     */
    public static final Function COS = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.cos(a);
        }
    };

    /**
     * Provides access to {@link Math#cosh(double)}
     */
    public static final Function COSH = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.cosh(a);
        }
    };

    /**
     * Provides access to {@link Math#tan(double)}
     */
    public static final Function TAN = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.tan(a);
        }
    };

    /**
     * Provides access to {@link Math#tanh(double)}
     */
    public static final Function TANH = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.tanh(a);
        }
    };

    /**
     * Provides access to {@link Math#abs(double)}
     */
    public static final Function ABS = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.abs(a);
        }
    };

    /**
     * Provides access to {@link Math#asin(double)}
     */
    public static final Function ASIN = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.asin(a);
        }
    };

    /**
     * Provides access to {@link Math#acos(double)}
     */
    public static final Function ACOS = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.acos(a);
        }
    };

    /**
     * Provides access to {@link Math#atan(double)}
     */
    public static final Function ATAN = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.atan(a);
        }
    };

    /**
     * Provides access to {@link Math#atan2(double, double)}
     */
    public static final Function ATAN2 = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return Math.atan2(a, b);
        }
    };


    /**
     * Provides access to {@link Math#round(double)}
     */
    public static final Function ROUND = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.round(a);
        }
    };

    /**
     * Provides access to {@link Math#floor(double)}
     */
    public static final Function FLOOR = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.floor(a);
        }
    };

    /**
     * Provides access to {@link Math#ceil(double)}
     */
    public static final Function CEIL = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.ceil(a);
        }
    };

    /**
     * Provides access to {@link Math#sqrt(double)}
     */
    public static final Function SQRT = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.sqrt(a);
        }
    };

    /**
     * Provides access to {@link Math#exp(double)}
     */
    public static final Function EXP = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.exp(a);
        }
    };

    /**
     * Provides access to {@link Math#log(double)}
     */
    public static final Function LN = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.log(a);
        }
    };

    /**
     * Provides access to {@link Math#log10(double)}
     */
    public static final Function LOG = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.log10(a);
        }
    };

    /**
     * Provides access to {@link Math#min(double, double)}
     */
    public static final Function MIN = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return Math.min(a, b);
        }
    };

    /**
     * Provides access to {@link Math#max(double, double)}
     */
    public static final Function MAX = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return Math.max(a, b);
        }
    };

    /**
     * Provides access to {@link Math#random()} which will be multiplied by the given argument.
     */
    public static final Function RND = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.random() * a;
        }
    };

    /**
     * Provides access to {@link Math#signum(double)}
     */
    public static final Function SIGN = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.signum(a);
        }
    };

    /**
     * Provides access to {@link Math#toDegrees(double)}
     */
    public static final Function DEG = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.toDegrees(a);
        }
    };

    /**
     * Provides access to {@link Math#toRadians(double)}
     */
    public static final Function RAD = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return Math.toRadians(a);
        }
    };

    /**
     * Provides an if-like function
     * <p>
     * It expects three arguments: A condition, an expression being evaluated if the condition is 1 and an
     * expression which is being evaluated if the condition is not 1.
     * </p>
     */
    public static final Function IF = new Function() {
        @Override
        public int getNumberOfArguments() {
            return 3;
        }

        @Override
        public Value eval(List<Expression> args) {
            final Value test = args.get(0).evaluate();
            // TODO: what if `test` is NaN???
            final double[] testValues = test.values();

            if (testValues.length == 1) {
                if (Double.isNaN(testValues[0])) {
                    // Can we always return a 1-dimensional NaN even for an unknown output Value size? Seems yes, we can.
                    return test;
                }
                // TODO: why do we compare " == 1" instead of " != 0"???
                return testValues[0] == 1d ? args.get(1).evaluate() : args.get(2).evaluate();
            }


            // TODO: Why don't we test for `evaluate().isNaN()` here?!
            // TODO: If I have all test items equal to "1" (or all test items are either NaN or "1") then I would expect the third Expression being not calculated;
            // TODO: If I have all test items equal to "0" (or all test items are either NaN or "0") then I would expect the second Expression being not calculated;
            final double[] first = args.get(1).evaluate().values();
            final double[] second = args.get(2).evaluate().values();

            double[] result = new double[testValues.length];
            for (int i = 0; i < testValues.length; i++) {
                // TODO: Why don't we test `testValues[i]` for NaN here?!
                result[i] = testValues[i] == 1d
                    ? (first.length == 1 ? first[0] : first[i])
                    : (second.length == 1 ? second[0] : second[i]);
            }
            return new Value(result);
        }

        @Override
        public boolean isNaturalFunction() {
            return false;
        }
    };

    public static final Function SUM = new NaryFunction() {
        @Override
        protected double eval(double[] vals) {
            double sum = 0;
            for (double v : vals) {
                sum += v;
            }
            return sum;
        }
    };

    public static final Function PRODUCT = new NaryFunction() {
        @Override
        protected double eval(double[] vals) {
            double prod = 1;
            for (double v : vals) {
                prod *= v;
            }
            return prod;
        }
    };

    public static final Function SUBTRACT = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return a - b;
        }
    };

    public static final Function DIVIDE = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return a / b;
        }
    };

    public static final Function POW = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return Math.pow(a, b);
        }
    };

    public static final Function MODULO = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return a % b;
        }
    };

    public static final Function LT = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return a < b ? 1d : 0d;
        }
    };

    public static final Function LT_EQ = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return a < b || Math.abs(a - b) < EPSILON ? 1d : 0d;
        }
    };

    public static final Function GT = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return a > b ? 1d : 0d;
        }
    };

    public static final Function GT_EQ = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return a > b || Math.abs(a - b) < EPSILON ? 1d : 0d;
        }
    };

    public static final Function EQ = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return Math.abs(a - b) < EPSILON ? 1d : 0d;
        }
    };

    public static final Function NEQ = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return Math.abs(a - b) > EPSILON ? 1d : 0d;
        }
    };

    public static final Function AND = new NaryFunction() {
        @Override
        protected double eval(double[] vals) {
            for (double v : vals) {
                if (v != 1d) {
                    return 0d;
                }
            }
            return 1d;
        }
    };

    public static final Function OR = new NaryFunction() {
        @Override
        protected double eval(double[] vals) {
            for (double v : vals) {
                if (v == 1d) {
                   return 1d;
                } 
            }
            return 0d;
        }
    };
}
