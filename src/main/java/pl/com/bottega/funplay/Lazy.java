package pl.com.bottega.funplay;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Lazy<T> {

    public static <T> Lazy<T> of(Supplier<? extends T> supplier) {
        return null;
    }

    public T get() {
        return null;
    }

    public <S> Lazy<S> map(Function<T, S> mapper) {
        return null;
    }

    public <S> Lazy<S> flatMap(Function<T, Lazy<S>> mapper) {
        return null;
    }

    public Optional<T> filter(Predicate<T> tester) {
        return Optional.empty();
    }
}
