package izmaylov.language.parsing.interpreter;

import java.util.LinkedList;
import java.util.Objects;

class ExecutionContext {
    private final LinkedList<StackFrame> executionStack = new LinkedList<>();

    void callFunction(StackFrame stackFrame) {
        executionStack.push(stackFrame);
    }

    boolean isVariableExists(String variable) {
        return !executionStack.isEmpty() && executionStack.peek().contains(variable);
    }

    int getVariableValue(String variable) {
        return Objects.requireNonNull(executionStack.peek()).getVariable(variable);
    }

    void endFunction() {
        executionStack.pop();
    }
}
