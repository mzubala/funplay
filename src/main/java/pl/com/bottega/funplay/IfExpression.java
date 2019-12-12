package pl.com.bottega.funplay;

import java.util.Optional;
import java.util.function.Supplier;

public final class IfExpression<V> {

    private final FunList<ThenExpression> previousElsifs;
    private final boolean condition;

    public static <V> IfExpression<V> If(boolean condition, Class<V> valueClass) {
        return new IfExpression<>(condition);
    }

    public static <V> IfExpression<V> If(boolean condition) {
        return new IfExpression<>(condition);
    }

    private IfExpression(boolean condition) {
        this(condition, FunList.empty());
    }

    private IfExpression(boolean condition, FunList<ThenExpression> previousElsifs) {
        this.condition = condition;
        this.previousElsifs = previousElsifs;
    }

    public ValueThenExpression Then(V value) {
        return new ValueThenExpression(condition, value);
    }

    public RunnableThenExpression Then(Runnable runnable) {
        return new RunnableThenExpression(condition, runnable);
    }

    public SuppliedThenExpression Then(Supplier<V> valueSupplier) {
        return new SuppliedThenExpression(condition, valueSupplier);
    }

    public Optional<V> ThenReturn(Supplier<V> supplier) {
        return evaluatePreviousAnd(new SuppliedThenExpression(condition, supplier));
    }

    public Optional<V> ThenReturn(V value) {
        return evaluatePreviousAnd(new ValueThenExpression(condition, value));
    }

    public void ThenRun(Runnable toRun) {
        evaluatePreviousAnd(new RunnableThenExpression(condition, toRun));
    }

    private Optional<V> evaluatePreviousAnd(ThenExpression expression) {
        return previousElsifs.append(expression)
            .find(expr -> expr.condition)
            .map(ThenExpression::evaluate);
    }

    private void evaluatePreviousAnd(RunnableThenExpression... expressions) {
        previousElsifs.concat(FunList.of(expressions))
            .find(expr -> expr.condition)
            .map(ThenExpression::evaluate);
    }

    private abstract class ThenExpression {

        private boolean condition;

        public ThenExpression(boolean condition) {
            this.condition = condition;
        }

        abstract V evaluate();
    }

    public class ValueThenExpression extends ThenExpression {
        private V value;

        public ValueThenExpression(boolean condition, V value) {
            super(condition);
            this.value = value;
        }

        public V Else(V value) {
            return evaluatePreviousAnd(this).orElse(value);
        }

        public V Else(Supplier<V> supplier) {
            return evaluatePreviousAnd(this).orElseGet(supplier);
        }

        public IfExpression<V> ElsIf(Boolean condition) {
            return new IfExpression<V>(condition, previousElsifs.append(this));
        }

        @Override
        public V evaluate() {
            return value;
        }
    }

    public class SuppliedThenExpression extends ThenExpression {

        private Supplier<V> valueSupplier;

        SuppliedThenExpression(boolean condition, Supplier<V> valueSupplier) {
            super(condition);
            this.valueSupplier = valueSupplier;
        }

        public V Else(Supplier<V> supplier) {
            return evaluatePreviousAnd(this).orElseGet(supplier);
        }

        public V Else(V value) {
            return evaluatePreviousAnd(this).orElse(value);
        }

        @Override
        public V evaluate() {
            return valueSupplier.get();
        }

        public IfExpression<V> ElsIf(Boolean condition) {
            return new IfExpression<V>(condition, previousElsifs.append(this));
        }
    }

    public class RunnableThenExpression extends ThenExpression {

        private Runnable runnable;

        RunnableThenExpression(boolean condition, Runnable runnable) {
            super(condition);
            this.runnable = runnable;
        }

        public void Else(Runnable toRun) {
            evaluatePreviousAnd(this, new RunnableThenExpression(true, toRun));
        }

        @Override
        public V evaluate() {
            runnable.run();
            return null;
        }

        public IfExpression<V> ElseIf(boolean condition) {
            return new IfExpression<V>(condition, previousElsifs.append(this));
        }
    }
}
