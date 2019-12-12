package pl.com.bottega.funplay;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

class FunListTest {

    @Test
    void listsWithTheSameElementsAreEqual() {
        final var l1 = FunList.of("1", "2", "3");
        final var l2 = FunList.of("1", "2", "3");
        final var l3 = FunList.of(1, 2, 4, 5);

        assertThat(l1).isEqualTo(l2);
        assertThat(l2).isEqualTo(l1);
        assertThat(l1).isNotEqualTo(l3);
        assertThat(l2).isNotEqualTo(l3);
    }

    @Test
    void emptyListsAreEqual() {
        assertThat(FunList.empty()).isEqualTo(FunList.empty());
    }

    @Test
    void emptyListsAreNotEqualToNonEmptyLists() {
        assertThat(FunList.of("1")).isNotEqualTo(FunList.empty());
    }

    @Test
    void thereIsOnlyOneInstanceOfEmptyList() {
        assertThat(FunList.empty() == FunList.empty()).isTrue();
    }

    @Test
    void ofAcceptsEmptyArray() {
        assertThat(FunList.of()).isEqualTo(FunList.empty());
    }

    @Test
    void returnsListSize() {
        assertThat(FunList.empty().size()).isEqualTo(0);
        assertThat(FunList.of(1).size()).isEqualTo(1);
        assertThat(FunList.of(1, 2, 3, 4).size()).isEqualTo(4);
    }

    @Test
    void appendsElementsToTheList() {
        assertThat(
            FunList.empty().append("1").append("2").append("3")
        ).isEqualTo(FunList.of("1", "2", "3"));
    }

    @Test
    void prependsElementsToTheList() {
        assertThat(
            FunList.empty().prepend("1").prepend("2").prepend("3")
        ).isEqualTo(FunList.of("3", "2", "1"));
    }

    @Test
    void concatenatesLists() {
        assertThat(
            FunList.of("1", "2").concat(FunList.of("3", "4", "5"))
        ).isEqualTo(
            FunList.of("1", "2", "3", "4", "5")
        );
        assertThat(
            FunList.empty().concat(FunList.of(1, 2, 3))
        ).isEqualTo(
            FunList.of(1, 2, 3)
        );
        assertThat(
            FunList.of(1, 2, 3).concat(FunList.empty())
        ).isEqualTo(
            FunList.of(1, 2, 3)
        );
        assertThat(
            FunList.empty().concat(FunList.empty())
        ).isEqualTo(
            FunList.of(1, 2, 3)
        );
    }

    @Test
    void reversesList() {
        assertThat(FunList.empty().reverse()).isEqualTo(FunList.empty());
        assertThat(FunList.of(1, 2, 3).reverse()).isEqualTo(FunList.of(3, 2, 1));
    }

    @Test
    void removesElements() {
        assertThat(FunList.empty().remove(1)).isEqualTo(FunList.empty());
        assertThat(FunList.of(1, 2, 3).remove(2).remove(1)).isEqualTo(FunList.of(3));
        assertThat(FunList.of(1, 2, 3).remove(2).remove(1).remove(3)).isEqualTo(FunList.empty());
    }

    @Test
    void getsElementsByIndex() {
        var list = FunList.of(1, 2, 3, 4);

        assertThat(list.get(0)).isEqualTo(Optional.of(1));
        assertThat(list.get(-1)).isEqualTo(Optional.empty());
        assertThat(list.get(2)).isEqualTo(Optional.of(3));
        assertThat(list.get(5)).isEqualTo(Optional.empty());
        assertThat(FunList.empty().get(0)).isEqualTo(Optional.empty());
    }

    @Test
    void filtersElementsByPredicate() {
        var list = FunList.of(1, 2, 3, 4);

        assertThat(list.filter(i -> i % 2 == 0)).isEqualTo(FunList.of(2, 4));
        assertThat(list.filter(i -> false)).isEqualTo(Optional.empty());
        assertThat(list.filter(i -> true)).isEqualTo(list);
    }

    @Test
    void returnsFirstElement() {
        assertThat(FunList.of(1, 2, 3, 4).first()).isEqualTo(Optional.of(1));
        assertThat(FunList.of(1).first()).isEqualTo(Optional.of(1));
        assertThat(FunList.empty().first()).isEqualTo(Optional.empty());
    }

    @Test
    void returnsLastElement() {
        assertThat(FunList.of(1, 2, 3, 4).last()).isEqualTo(Optional.of(4));
        assertThat(FunList.of(1).last()).isEqualTo(Optional.of(1));
        assertThat(FunList.empty().last()).isEqualTo(Optional.empty());
    }

    @Test
    void iteratesOverElementsOfNonEmptyList() {
        var list = FunList.of(1, 2, 3, 4);
        var consumer = new Consumer<Integer>() {
            FunList<Integer> consumed = FunList.empty();

            @Override
            public void accept(Integer o) {
                consumed = consumed.append(o);
            }
        };

        list.foreach(consumer);

        assertThat(consumer.consumed).isEqualTo(list);
    }

    @Test
    void iteratesOverEmptyList() {
        var consumer = mock(Consumer.class);

        FunList.empty().foreach(consumer);

        verifyZeroInteractions(consumer);
    }

    @Test
    void slicesList() {
        var list = FunList.of(1, 2, 3, 4);
        var empty = FunList.empty();

        assertThat(list.slice(0, 1)).isEqualTo(FunList.of(1, 2));
        assertThat(list.slice(0, 0)).isEqualTo(FunList.of(1));
        assertThat(list.slice(1, 3)).isEqualTo(FunList.of(2, 3, 4));
        assertThat(list.slice(3, 100)).isEqualTo(FunList.of(4));
        assertThat(list.slice(-1, 2)).isEqualTo(FunList.of(1, 2, 3));
        assertThat(list.slice(-10, -1)).isEqualTo(empty);
        assertThat(list.slice(5, 7)).isEqualTo(empty);
        assertThat(empty.slice(0, 0)).isEqualTo(empty);
        assertThat(list.slice(3, 0)).isEqualTo(empty);
    }

    @Test
    void mapsList() {
        var sourceList = FunList.of("word", "other", "long-word");

        var mapped = sourceList.map(String::length);

        assertThat(mapped).isEqualTo(FunList.of(4, 5, 9));
    }

    @Test
    void mapsEmptyList() {
        var empty = FunList.empty();
        var mapper = mock(Function.class);

        var mapped = empty.map(mapper);

        assertThat(mapped).isEqualTo(empty);
        verifyZeroInteractions(mapper);
    }

    @Test
    void flatMapsList() {
        var sourceList = FunList.of("word", "other string", "very long string");

        var mapped = sourceList.flatMap(s -> FunList.of(s.split(" ")));

        assertThat(mapped).isEqualTo(FunList.of("word", "other", "string", "very", "long", "string"));
    }

    @Test
    void flatMapsEmptyList() {
        var empty = FunList.empty();
        var mapper = mock(Function.class);

        var mapped = empty.flatMap(mapper);

        assertThat(mapped).isEqualTo(empty);
        verifyZeroInteractions(mapper);
    }

    @Test
    void foldsListFromTheLeft() {
        var numbers = FunList.of(1, 2, 3, 4, 5);

        var numbersStr = numbers.foldLeft(new StringBuilder(), (acc, element) -> {
            acc.append(element.toString());
            return acc;
        }).toString();

        assertThat(numbersStr).isEqualTo("12345");
    }

    @Test
    void foldsListFromTheRight() {
        var numbers = FunList.of(1, 2, 3, 4, 5);

        var numbersStr = numbers.foldRight(new StringBuilder(), (acc, element) -> {
            acc.append(element.toString());
            return acc;
        }).toString();

        assertThat(numbersStr).isEqualTo("54321");
    }

    @Test
    void foldsEmptyList() {
        var empty = FunList.empty();
        var op = mock(BiFunction.class);


        assertThat(empty.foldLeft("", op)).isEqualTo("");
        assertThat(empty.foldRight("", op)).isEqualTo("");
        verifyZeroInteractions(op);
    }

    @Test
    void foldsListToSingleElementOfTheListTypeFromTheLeft() {
        var numbers = FunList.of(1, 2, 3, 4, 5);

        var sum = numbers.foldLeft(Integer::sum).get();
        var singleElementSum = numbers.slice(0, 0).foldLeft(Integer::sum).get();

        assertThat(sum).isEqualTo(15);
        assertThat(singleElementSum).isEqualTo(1);
    }

    @Test
    void foldsListToSingleElementOfTheListTypeFromTheRight() {
        var numbers = FunList.of(1, 2, 3, 4, 5);

        var sum = numbers.foldRight((acc, element) -> acc - element).get();
        var singleElementSum = numbers.slice(0, 0).foldRight((acc, element) -> acc - element).get();

        assertThat(sum).isEqualTo(-5);
        assertThat(singleElementSum).isEqualTo(1);
    }

    @Test
    void foldsEmptyListToSingleElementOfTheListType() {
        var empty = FunList.empty();
        var op = mock(BinaryOperator.class);

        assertThat(empty.foldRight(op)).isEqualTo(Optional.empty());
        assertThat(empty.foldLeft(op)).isEqualTo(Optional.empty());
        verifyZeroInteractions(op);
    }

    @Test
    void findsElementsInTheList() {
        var empty = FunList.empty();
        var nonEmpty = FunList.of("1", "2", "3");

        assertThat(empty.find(el -> true)).isEqualTo(Optional.empty());
        assertThat(nonEmpty.find(e -> e.equals("2"))).isEqualTo(Optional.of("2"));
        assertThat(nonEmpty.find(e -> false)).isEqualTo(Optional.empty());
    }
}
