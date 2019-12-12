package pl.com.bottega.funplay;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static pl.com.bottega.funplay.IfExpression.If;

class IfExpressionTest {

    private Boolean trueExpression = true;
    private Boolean falseExpression = false;
    private Supplier<Integer> mockSupplier = (Supplier<Integer>) mock(Supplier.class);


    @Test
    void evaluatesToThenExpressionIfConditionIsTrue() {
        assertThat(If(trueExpression, Integer.class)
            .Then(1)
            .Else(2)
        ).isEqualTo(1);
    }

    @Test
    void evaluatesToElseExpressionIfConditionIsFalse() {
        assertThat(If(falseExpression, Integer.class)
            .Then(1)
            .Else(2)
        ).isEqualTo(2);
    }

    @Test
    void usesThenSupplierIfConditionIsTrue() {
        assertThat(If(trueExpression, Integer.class)
            .Then(() -> 1)
            .Else(() -> 2)
        ).isEqualTo(1);
    }

    @Test
    void usesElseSupplierIfConditionIsFalse() {
        assertThat(If(falseExpression, Integer.class)
            .Then(() -> 1)
            .Else(() -> 2)
        ).isEqualTo(2);
    }

    @Test
    void doesNotCallElseSupplierIfConditionIsTrue() {
        If(trueExpression, Integer.class)
            .Then(() -> 1)
            .Else(mockSupplier);

        verifyZeroInteractions(mockSupplier);
    }

    @Test
    void doesNotCallThenSupplierIfConditionIsFalse() {
        If(falseExpression, Integer.class)
            .Then(mockSupplier)
            .Else(() -> 2);

        verifyZeroInteractions(mockSupplier);
    }

    @Test
    void canEvaluateIfExpressionWithoutElsePart() {
        var trueEval = If(trueExpression, Integer.class).ThenReturn(1);
        var falseEval = If(falseExpression, Integer.class).ThenReturn(1);
        assertThat(trueEval).isEqualTo(Optional.of(1));
        assertThat(falseEval).isEqualTo(Optional.empty());
    }

    @Test
    void doesNotCallThenSupplierWhenExpressionIsFalseWithoutElsePart() {
        var falseEval = If(falseExpression, Integer.class).ThenReturn(mockSupplier);

        verifyZeroInteractions(mockSupplier);
    }

    @Test
    void runsThenCodeWheExpressionIsTrue() {
        var thenBranch = mock(Runnable.class);
        var elseBranch = mock(Runnable.class);

        If(trueExpression).Then(thenBranch).Else(elseBranch);

        verify(thenBranch, times(1)).run();
        verifyZeroInteractions(elseBranch);
    }

    @Test
    void runsElseCodeWheExpressionIsFalse() {
        var thenBranch = mock(Runnable.class);
        var elseBranch = mock(Runnable.class);

        If(falseExpression).Then(thenBranch).Else(elseBranch);

        verify(elseBranch, times(1)).run();
        verifyZeroInteractions(thenBranch);
    }

    @Test
    void runsThenCodeWheExpressionIsTrueWhenThereIsNoElseBranch() {
        var thenBranch = mock(Runnable.class);

        If(trueExpression).ThenRun(thenBranch);

        verify(thenBranch, times(1)).run();
    }

    @Test
    void doesNotRunsThenCodeWheExpressionIsFalseWhenThereIsNoElseBranch() {
        var thenBranch = mock(Runnable.class);

        If(falseExpression).ThenRun(thenBranch);

        verifyZeroInteractions(thenBranch);
    }

    @Test
    void allowsNestedValueExpressions() {
        var elseResult = If(falseExpression, Integer.class)
            .Then(1)
            .ElsIf(falseExpression).Then(2)
            .ElsIf(falseExpression).Then(3)
            .ElsIf(falseExpression).Then(4)
            .Else(5);

        var elsIfResult = If(falseExpression, Integer.class)
            .Then(1)
            .ElsIf(trueExpression).Then(2)
            .ElsIf(falseExpression).Then(3)
            .Else(4);

        var thenResult = If(trueExpression, Integer.class)
            .Then(1)
            .ElsIf(trueExpression).Then(2)
            .ElsIf(falseExpression).Then(3)
            .Else(4);

        assertThat(elseResult).isEqualTo(5);
        assertThat(elsIfResult).isEqualTo(2);
        assertThat(thenResult).isEqualTo(1);
    }

    @Test
    void allowsNestedSuppliedExpressions() {
        var elseResult = If(falseExpression, Integer.class)
            .Then(() -> 1)
            .ElsIf(falseExpression).Then(() -> 2)
            .ElsIf(falseExpression).Then(() -> 3)
            .ElsIf(falseExpression).Then(() -> 4)
            .Else(() -> 5);

        var elsIfResult = If(falseExpression, Integer.class)
            .Then(() -> 1)
            .ElsIf(trueExpression).Then(() -> 2)
            .ElsIf(falseExpression).Then(() -> 3)
            .Else(() -> 4);

        var thenResult = If(trueExpression, Integer.class)
            .Then(() -> 1)
            .ElsIf(trueExpression).Then(() -> 2)
            .ElsIf(falseExpression).Then(() -> 3)
            .Else(() -> 4);

        assertThat(elseResult).isEqualTo(5);
        assertThat(elsIfResult).isEqualTo(2);
        assertThat(thenResult).isEqualTo(1);
    }

    @Test
    void allowsMixOfNestedValueAndSuppliedExpressions() {
        var elseResult = If(falseExpression, Integer.class)
            .Then(() -> 1)
            .ElsIf(falseExpression).Then(() -> 2)
            .ElsIf(falseExpression).Then(() -> 3)
            .ElsIf(falseExpression).Then(() -> 4)
            .Else(5);

        var elsIfResult = If(falseExpression, Integer.class)
            .Then(() -> 1)
            .ElsIf(trueExpression).Then(2)
            .ElsIf(falseExpression).Then(3)
            .Else(() -> 4);

        var thenResult = If(trueExpression, Integer.class)
            .Then(1)
            .ElsIf(trueExpression).Then(() -> 2)
            .ElsIf(falseExpression).Then(() -> 3)
            .Else(() -> 4);

        assertThat(elseResult).isEqualTo(5);
        assertThat(elsIfResult).isEqualTo(2);
        assertThat(thenResult).isEqualTo(1);
    }

    @Test
    public void allowsNestedRunnableExpressions() {
        var run0 = mock(Runnable.class);
        var run1 = mock(Runnable.class);
        var run2 = mock(Runnable.class);

        If(trueExpression).Then(run1)
            .ElseIf(trueExpression).Then(run0)
            .ElseIf(falseExpression).ThenRun(run0);

        If(falseExpression).Then(run0)
            .ElseIf(falseExpression).Then(run0)
            .ElseIf(falseExpression).ThenRun(run0);

        If(falseExpression).Then(run1)
            .ElseIf(trueExpression).Then(run2)
            .ElseIf(trueExpression).ThenRun(run0);

        verifyZeroInteractions(run0);
        verify(run1, times(1)).run();
        verify(run2, times(1)).run();
    }
}
