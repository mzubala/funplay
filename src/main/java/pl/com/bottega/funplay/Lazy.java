package pl.com.bottega.funplay;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Lazy<T> {

    private volatile Supplier<? extends T> supplier;
    private T value;

    private Lazy(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    public static <T> Lazy<T> of(Supplier<? extends T> supplier) {
        return new Lazy<>(supplier);
    }

    public T get() {
        return supplier == null ? value : computeValue();
    }

    private synchronized T computeValue() {
        final var s = supplier;
        if(s != null) {
            value = s.get();
            supplier = null;
        }
        return value;
    }

    public <S> Lazy<S> map(Function<T, S> mapper) {
        return Lazy.of(() -> mapper.apply(get()));
    }

    public <S> Lazy<S> flatMap(Function<T, Lazy<S>> mapper) {
        return Lazy.of(() -> mapper.apply(get()).get());
    }

    public Optional<T> filter(Predicate<T> tester) {
        return Optional.ofNullable(get()).filter(tester);
    }
}
