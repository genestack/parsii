/*
 * Made with all the love in the world
 * by scireum in Remshalden, Germany
 *
 * Copyright by scireum GmbH
 * http://www.scireum.de - info@scireum.de
 */

package parsii;

import java.util.*;

import org.junit.Assert;
import org.junit.Test;
import parsii.eval.*;
import parsii.tokenizer.ParseException;

import static org.junit.Assert.*;

/**
 * Tests the {@link Parser} class.
 *
 * @author Andreas Haufler (aha@scireum.de)
 * @since 2013/09
 */
public class ParserTest {
    @Test
    public void simple() throws ParseException {
        assertEquals(-109d, Parser.parse("1 - (10 - -100)").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(0.01d, Parser.parse("1 / 10 * 10 / 100").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(-89d, Parser.parse("1 + 10 - 100").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(91d, Parser.parse("1 - 10 - -100").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(91d, Parser.parse("1 - 10  + 100").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(-109d, Parser.parse("1 - (10 + 100)").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(-89d, Parser.parse("1 + (10 - 100)").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(100d, Parser.parse("1 / 1 * 100").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(0.01d, Parser.parse("1 / (1 * 100)").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(0.01d, Parser.parse("1 * 1 / 100").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(7d, Parser.parse("3+4").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(7d, Parser.parse("3      +    4").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(-1d, Parser.parse("3+ -4").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(-1d, Parser.parse("3+(-4)").evaluate().doubleValue(), Functions.EPSILON);
    }

    @Test
    public void number() throws ParseException {
        assertEquals(4003.333333d, Parser.parse("3.333_333+4_000").evaluate().doubleValue(), Functions.EPSILON);
    }

    @Test
    public void precedence() throws ParseException {
        // term vs. product
        assertEquals(19d, Parser.parse("3+4*4").evaluate().doubleValue(), Functions.EPSILON);
        // product vs. power
        assertEquals(20.25d, Parser.parse("3^4/4").evaluate().doubleValue(), Functions.EPSILON);
        // relation vs. product
        assertEquals(1d, Parser.parse("3 < 4*4").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(0d, Parser.parse("3 > 4*4").evaluate().doubleValue(), Functions.EPSILON);
        // brackets
        assertEquals(28d, Parser.parse("(3 + 4) * 4").evaluate().doubleValue(), Functions.EPSILON);
    }

    @Test
    public void variables() throws ParseException {
        Scope scope = Scope.create();

        Variable a = scope.getVariable("a");
        Variable b = scope.getVariable("b");
        Expression expr = Parser.parse("3*a + 4 * b", scope);
        assertEquals(0d, expr.evaluate().doubleValue(), Functions.EPSILON);
        a.setValue(new Value(2));
        assertEquals(6d, expr.evaluate().doubleValue(), Functions.EPSILON);
        b.setValue(new Value(3));
        assertEquals(18d, expr.evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(18d, expr.evaluate().doubleValue(), Functions.EPSILON);

        a.setValue(new Value(new double[] {1d, 2d, 3d}));
        b.setValue(new Value(new double[] {1d, 2d, 3d}));

        double[] testValues = expr.evaluate().values();
        assertArrayEquals(new double[]{7d, 14d, 21d}, testValues, Functions.EPSILON);

        expr = Parser.parse("if(a<10,log(a/b),log(b/a))", scope);
        final int BIG_ARRAY_LENGTH = 150000;
        double[] apoints = new double[BIG_ARRAY_LENGTH];
        double[] bpoints = new double[BIG_ARRAY_LENGTH];
        double[] logs = new double[BIG_ARRAY_LENGTH];
        for (int i = 0; i < BIG_ARRAY_LENGTH; i++) {
            apoints[i] = i * 1.0;
            bpoints[i] = i * 1.0 + 100;
            logs[i] = apoints[i] < 10 ? Math.log10(i * 1.0 / (i * 1.0 + 100)) : -Math.log10(i * 1.0 / (i * 1.0 + 100));
        }

        a.setValue(new Value(apoints));
        b.setValue(new Value(bpoints));
        testValues = expr.evaluate().values();
        assertArrayEquals(logs, testValues, Functions.EPSILON);
    }

    @Test
    public void stringValue() throws ParseException {
        Parser.registerFunction("count_occurrences", new Function() {
            @Override
            public int getNumberOfArguments() {
                return 2;
            }

            @Override
            public Value eval(List<Expression> args) {
                final String first = args.get(0).evaluate().stringValue();
                final String second = args.get(1).evaluate().stringValue();

                int lastIndex = 0;
                int count = 0;

                while (lastIndex != -1) {
                    lastIndex = first.indexOf(second, lastIndex);
                    if (lastIndex != -1) {
                        count++;
                        lastIndex += second.length();
                    }
                }

                return new Value(count);
            }

            @Override
            public boolean isNaturalFunction() {
                return true;
            }
        });
        assertEquals(3d, Parser.parse("count_occurrences(\"blablabla\",'bla')").evaluate().doubleValue(), Functions.EPSILON);
    }

    @Test
    public void functions() throws ParseException {
        assertEquals(0d, Parser.parse("1 + sin(-pi) + cos(pi)").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(4.72038341576d, Parser.parse("tan(sqrt(euler ^ (pi * 3)))").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(3d, Parser.parse("| 3 - 6 |").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(3d, Parser.parse("if(3 > 2 && 2 < 3, 2+1, 1+1)").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(2d, Parser.parse("if(3 < 2 || 2 > 3, 2+1, 1+1)").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(2d, Parser.parse("min(3,2)").evaluate().doubleValue(), Functions.EPSILON);

        // Test a var arg method...
        Parser.registerFunction("avg", new Function() {
            @Override
            public int getNumberOfArguments() {
                return -1;
            }

            @Override
            public Value eval(List<Expression> args) {
                Value avg = new Value(0);
                if (args.isEmpty()) {
                    return avg;
                }
                return Functions.DIVIDE.eval(Arrays.<Expression>asList(Functions.SUM.eval(args), new Value(args.size())));
            }

            @Override
            public boolean isNaturalFunction() {
                return true;
            }
        });
        assertEquals(3.25d, Parser.parse("avg(3,2,1,7)").evaluate().doubleValue(), Functions.EPSILON);
    }

    @Test
    public void scopes() throws ParseException {
        Scope root = Scope.create();
        Variable a = root.getVariable("a").withValue(new Value(1));
        Scope subScope1 = Scope.createWithParent(root);
        Scope subScope2 = Scope.createWithParent(root);
        Variable b1 = subScope1.getVariable("b").withValue(new Value(2));
        Variable b2 = subScope2.getVariable("b").withValue(new Value(3));
        Variable c = root.getVariable("c").withValue(new Value(4));
        Variable c1 = subScope1.getVariable("c").withValue(new Value(5));
        assertEquals(c, c1);
        Variable d = root.getVariable("d").withValue(new Value(9));
        Variable d1 = subScope1.create("d").withValue(new Value(7));
        assertNotEquals(d, d1);
        Expression expr1 = Parser.parse("a + b + c + d", subScope1);
        Expression expr2 = Parser.parse("a + b + c + d", subScope2);
        assertEquals(15d, expr1.evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(18d, expr2.evaluate().doubleValue(), Functions.EPSILON);
        a.setValue(new Value(10));
        b1.setValue(new Value(20));
        b2.setValue(new Value(30));
        c.setValue(new Value(40));
        c1.setValue(new Value(50));
        assertEquals(87d, expr1.evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(99d, expr2.evaluate().doubleValue(), Functions.EPSILON);
    }

    @Test
    public void errors() throws ParseException {
        // We expect the parser to continue after an recoverable error!
        try {
            Parser.parse("test(1 2)+sin(1,2)*34-34.45.45+");
            assertTrue(false);
        } catch (ParseException e) {
            assertEquals(5, e.getErrors().size());
        }

        // We expect the parser to report an invalid quantifier.
        try {
            Parser.parse("1x");
            assertTrue(false);
        } catch (ParseException e) {
            assertEquals(1, e.getErrors().size());
        }

        // We expect the parser to report an unfinished expression
        try {
            Parser.parse("1(");
            assertTrue(false);
        } catch (ParseException e) {
            assertEquals(1, e.getErrors().size());
        }
    }

    @Test
    public void relationalOperators() throws ParseException {
        // Test for Issue with >= and <= operators (#4)
        assertEquals(1d, Parser.parse("5 <= 5").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(1d, Parser.parse("5 >= 5").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(0d, Parser.parse("5 < 5").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(0d, Parser.parse("5 > 5").evaluate().doubleValue(), Functions.EPSILON);
    }

    @Test
    public void quantifiers() throws ParseException {
        assertEquals(1000d, Parser.parse("1K").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(1000d, Parser.parse("1M * 1m").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(1d, Parser.parse("1n * 1G").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(1d, Parser.parse("(1M / 1k) * 1m").evaluate().doubleValue(), Functions.EPSILON);
        assertEquals(1d, Parser.parse("1u * 10 k * 1000  m * 0.1 k").evaluate().doubleValue(), Functions.EPSILON);
    }

    @Test
    public void getVariables() throws ParseException {
        Scope s = Scope.create();
        Parser.parse("a*b+c", s);
        assertTrue(s.getNames().contains("a"));
        assertTrue(s.getNames().contains("b"));
        assertTrue(s.getNames().contains("c"));
        assertFalse(s.getNames().contains("x"));

        // pi and euler are always defined...
        assertEquals(5, s.getVariables().size());
    }


}
