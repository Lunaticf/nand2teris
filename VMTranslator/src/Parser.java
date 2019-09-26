import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Parser {

    private BufferedReader fileReader;
    private String currentCmd;
    private CommandType currentCmdType;
    private String arg1;
    private String arg2;

    private final static Set<String> arithmeticCmdSet = new HashSet<>(Arrays.asList("add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"));

    private final static Set<CommandType> twoArgOps = new HashSet<>(Arrays.asList(CommandType.C_PUSH, CommandType.C_POP, CommandType.C_FUNCTIONS, CommandType.C_CALL));


    public Parser(String file) throws FileNotFoundException {
        fileReader = new BufferedReader(new FileReader(file));
    }

    public boolean hasMoreCommands() throws IOException {
        while ((currentCmd = fileReader.readLine()) != null) {
            currentCmd = currentCmd.trim();
            // 判断是否是注释或是空行
            if (currentCmd.startsWith("//") || currentCmd.equals("\r") || currentCmd.equals("\n") || currentCmd.equals("\r\n") || currentCmd.equals("")) {
                continue;
            }
            return true;
        }
        return false;
    }

    public void advance() throws IOException {
        if (currentCmd == null) {
            throw new RuntimeException("no more command! shouldn't call advance()");
        } else {
            // 对currentCmd解析
            currentCmd = currentCmd.trim();

            String[] components = currentCmd.split(" ");

            // 如果大于3个部分
            if (components.length > 3) {
                throw new IllegalArgumentException("VM Command too much arguments!");
            }

            // check是否是arithmetic指令
            if (isArithmetic(components[0])) {
                currentCmdType = CommandType.C_ARITHMETIC;
                arg1 = components[0];
            } else if ("return".equals(components[0])) {
                currentCmdType = CommandType.C_RETURN;
            } else {
                switch (components[0]) {
                    case "push":
                        currentCmdType = CommandType.C_PUSH;
                        arg1 = components[1];
                        arg2 = components[2];
                        break;
                    case "pop":
                        currentCmdType = CommandType.C_POP;
                        arg1 = components[1];
                        arg2 = components[2];
                        break;
                    default:
                        throw new RuntimeException("unknown command type!");
                }
            }
        }
    }

    public CommandType commandType() {
        return currentCmdType;
    }

    public String arg1() {
        if (currentCmdType.equals(CommandType.C_RETURN)) {
            throw new RuntimeException("can't get arg1 if the command is return");
        }
        return arg1;
    }

    public String arg2() {
        if (isTwoArgOps(currentCmdType)) {
            return arg2;
        }
        return null;
    }

    public boolean isTwoArgOps(CommandType cmdType) {
        return twoArgOps.contains(cmdType);
    }

    private boolean isArithmetic(String cmdType) {
        return arithmeticCmdSet.contains(cmdType);
    }
}
