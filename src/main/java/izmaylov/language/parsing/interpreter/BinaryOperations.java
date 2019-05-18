package izmaylov.language.parsing.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntBinaryOperator;

class BinaryOperations {
    private static Map<String, IntBinaryOperator> operations = new HashMap<>();

    static {
        operations.put("+", Integer::sum);
        operations.put("-", (a, b) -> a - b);
        operations.put("*", (a, b) -> a * b);
        operations.put("/", (a, b) -> a / b);
        operations.put("%", (a, b) -> a % b);
        operations.put("=", (a, b) -> a == b ? 1 : 0);
        operations.put(">", (a, b) -> a > b ? 1 : 0);
        operations.put("<", (a, b) -> a < b ? 1 : 0);
    }

    static IntBinaryOperator getOperation(String operation) {
        return operations.get(operation);
    }

    private BinaryOperations() {}
}
