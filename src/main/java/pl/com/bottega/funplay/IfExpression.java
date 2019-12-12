package pl.com.bottega.funplay;

import java.util.Optional;
import java.util.function.Supplier;

public final class IfExpression<V> {

    public static <V> IfExpression<V> If(boolean condition, Class<V> valueClass) {
        return null;
    }

    public static <V> IfExpression<V> If(boolean condition) {
        return null;
    }

    public ValueThenExpression Then(V value) {
        return null;
    }

    public RunnableThenExpression Then(Runnable runnable) {
        return null;
    }

    public SuppliedThenExpression Then(Supplier<V> valueSupplier) {
        return null;
    }

    public Optional<V> ThenReturn(Supplier<V> supplier) {
        return null;
    }

    public Optional<V> ThenReturn(V value) {
        return null;
    }

    public void ThenRun(Runnable toRun) {

    }

    public class ValueThenExpression {
        private V value;

        public V Else(V value) {
            return null;
        }

        public V Else(Supplier<V> supplier) {
            return null;
        }

        public IfExpression<V> ElsIf(Boolean condition) {
            return null;
        }

    }

    public class SuppliedThenExpression {

        public V Else(Supplier<V> supplier) {
            return null;
        }

        public V Else(V value) {
            return null;
        }

        public IfExpression<V> ElsIf(Boolean condition) {
            return null;
        }
    }

    public class RunnableThenExpression {

        public void Else(Runnable toRun) {
        }

        public IfExpression<V> ElseIf(boolean condition) {
            return null;
        }
    }
}
