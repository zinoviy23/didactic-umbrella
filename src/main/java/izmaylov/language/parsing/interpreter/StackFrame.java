package izmaylov.language.parsing.interpreter;

import java.util.HashMap;
import java.util.Map;

class StackFrame {
    private Map<String, Integer> variables;

    boolean contains(String name) {
        return variables.containsKey(name);
    }

    int getVariable(String name) {
        return variables.get(name);
    }

    StackFrame(Map<String, Integer> variables) {
        this.variables = variables;
    }
}
