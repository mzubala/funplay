package pl.com.bottega.funplay;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FunList<T> {

    static <T> FunList<T> of(T... elements) {
        return Arrays.stream(elements).reduce(FunList.empty(), FunList::append, FunList::concat);
    }

    static <T> FunList<T> empty() {
        return null;
    }

    FunList<T> append(T element);

    FunList<T> prepend(T element);

    FunList<T> concat(FunList<T> other);

    FunList<T> reverse();

    FunList<T> remove(T element);

    Optional<T> find(Predicate<T> predicate);

    Integer size();

    Optional<T> get(Integer index);

    FunList<T> filter(Predicate<T> predicate);

    Optional<T> first();

    Optional<T> last();

    void foreach(Consumer<T> consumer);

    FunList<T> slice(Integer start, Integer end);

    <S> FunList<S> map(Function<T, S> mapper);

    <S> FunList<S> flatMap(Function<T, FunList<S>> mapper);

    <S> S foldLeft(S initial, BiFunction<S, T, S> op);

    <S> S foldRight(S initial, BiFunction<S, T, S> op);

    Optional<T> foldLeft(BinaryOperator<T> op);

    Optional<T> foldRight(BinaryOperator<T> op);
}