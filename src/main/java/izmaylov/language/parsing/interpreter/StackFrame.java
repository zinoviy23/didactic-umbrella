package izmaylov.language.parsing.interpreter;

import java.util.HashMap;
import java.util.Map;

public class StackFrame {
    private Map<String, Integer> variables = new HashMap<>();

    public int getVariable(String name) {
        throw new AssertionError();
    }
}
