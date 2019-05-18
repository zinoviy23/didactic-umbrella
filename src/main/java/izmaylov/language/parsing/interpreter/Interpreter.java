package izmaylov.language.parsing.interpreter;

import izmaylov.language.parsing.parser.ast.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Interpreter {
    private Map<String, FunctionDefinition> functions;

    private ExecutionContext context;

    public int execute(Program program) {
        functions = program.getFunctionDefinitions()
                .stream()
                .collect(Collectors.toMap(FunctionDefinition::getName, f->f));

        context = new ExecutionContext();

        return execute(program.getExpression());
    }

    private int execute(Expression expression) {
        if (expression instanceof BinaryExpression) {
            return executeBinaryExpression((BinaryExpression) expression);
        }
        if (expression instanceof ConstantExpression) {
            return executeConstantExpression((ConstantExpression) expression);
        }
        if (expression instanceof IfExpression) {
            return executeIfExpression((IfExpression) expression);
        }
        if (expression instanceof Identifier) {
            return executeIdentifier((Identifier) expression);
        }
        if (expression instanceof CallExpression) {
            return executeCallExpression((CallExpression) expression);
        }

        throw new AssertionError("Cannot be here now");
    }

    private int executeBinaryExpression(BinaryExpression expression) {
        try {
            return BinaryOperations
                    .getOperation(expression.getOperation())
                    .applyAsInt(
                            execute(expression.getLeftExpression()),
                            execute(expression.getRightExpression())
                    );
        } catch (ArithmeticException e) {
            throw new RuntimeErrorException(expression.getLineNumber(), expression.toString());
        }
    }

    private int executeConstantExpression(ConstantExpression expression) {
        return expression.getMinus()
                ? -expression.getNumber()
                : expression.getNumber();
    }

    private int executeIfExpression(IfExpression expression) {
        if (execute(expression.getCondition()) != 0) {
            return execute(expression.getThenBranch());
        } else {
            return execute(expression.getElseBranch());
        }
    }

    private int executeIdentifier(Identifier expression) {
        if (!context.isVariableExists(expression.getName())) {
            throw new UnknownParameterException(expression.getLineNumber(), expression.getName());
        }

        return context.getVariableValue(expression.getName());
    }

    private int executeCallExpression(CallExpression expression) {
        Map<String, Integer> arguments = new HashMap<>();

        FunctionDefinition definition = functions.get(expression.getName());

        if (definition == null) {
            throw new UnknownFunctionException(expression.getLineNumber(), expression.getName());
        }

        if (definition.getParameters().size() != expression.getArguments().size()) {
            throw new ArgumentsNumberMismatchException(expression.getLineNumber(), expression.getName());
        }

        for (int i = 0; i < expression.getArguments().size(); i++) {
            arguments.put(definition.getParameters().get(i), execute(expression.getArguments().get(i)));
        }

        context.callFunction(new StackFrame(arguments));
        int result = execute(definition.getBody());
        context.endFunction();

        return result;
    }
}
