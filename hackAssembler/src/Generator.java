/**
 * 生成代码
 * @author lunaticf
 */
public class Generator {

    private static int varLocation = 16;

    public static String generateCode(String line) {

        // 如果是A指令
        if (line.startsWith(Constants.AInstruction)) {
            // 如果是不带Symbol的A指令
            if (Character.isDigit(line.charAt(1))) {
                int num = Integer.parseInt(line.substring(1));
                // 小技巧在旁边补0
                return "0" + Utils.intToBinaryCodeWithPadding(num);
            } else {
                // 带有symbol 从16开始放
                String symbol = line.substring(1);

                if (SymbolTable.get(symbol) == null) {
                    SymbolTable.put(symbol, Utils.intToBinaryCodeWithPadding(varLocation++));
                }
                return "0" + SymbolTable.get(symbol);
            }
        } else {
            // 是C指令
            return processC(line);
        }
    }

    /**
     * 处理C指令
     * dest=comp;jump
     * dest为空，comp;jump
     * jump为空，dest=comp
     * @return 代码
     */
    private static String processC(String line) {
        String code = "111";

        int temp = line.indexOf("=");
        // dest=comp类型
        if (temp != -1) {
            String dest = line.substring(0, temp);
            String comp = line.substring(temp + 1);

            code += processComp(comp);
            code += processDest(dest);
            code += "000";

            return code;
        } else {
            // comp;jump类型
            temp = line.indexOf(";");

            String comp = line.substring(0, temp);
            String jump = line.substring(temp + 1);

            code += processComp(comp);
            code += "000";
            code += processJump(jump);
            return code;
        }
    }

    private static String processComp(String comp) {
        String compA0 = processCompA0(comp);
        String compA1 = processCompA1(comp);

        return  ("".equals(compA0)?"1":"0") + ("".equals(compA0)?compA1:compA0);
    }

    private static String processCompA0(String comp) {
        switch (comp) {
            case "0":
                return "101010";
            case "1":
                return "111111";
            case "-1":
                return "111010";
            case "D":
                return "001100";
            case "A":
                return "110000";
            case "!D":
                return "001101";
            case "!A":
                return "110001";
            case "-D":
                return "001111";
            case "-A":
                return "110011";
            case "D+1":
                return "011111";
            case "A+1":
                return "110111";
            case "D-1":
                return "001110";
            case "A-1":
                return "110010";
            case "D+A":
                return "000010";
            case "D-A":
                return "010011";
            case "A-D":
                return "000111";
            case "D&A":
                return "000000";
            case "D|A":
                return "010101";
            default:
                // 表示这里没找到
                return "";
        }
    }

    private static String processCompA1(String comp) {
        switch (comp) {
            case "M":
                return "110000";
            case "!M":
                return "110001";
            case "-M":
                return "110011";
            case "M+1":
                return "110111";
            case "M-1":
                return "110010";
            case "D+M":
                return "000010";
            case "D-M":
                return "010011";
            case "M-D":
                return "000111";
            case "D&M":
                return "000000";
            case "D|M":
                return "010101";
            default:
                // 表示这里没找到
                return "";
        }
    }

    private static String processDest(String dest) {
        switch (dest) {
            case "M":
                return "001";
            case "D":
                return "010";
            case "MD":
                return "011";
            case "A":
                return "100";
            case "AM":
                return "101";
            case "AD":
                return "110";
            case "AMD":
                return "111";
            default:
                // should never reach here
                return "";
        }
    }

    private static String processJump(String jump) {
        switch (jump) {
            case "JGT":
                return "001";
            case "JEQ":
                return "010";
            case "JGE":
                return "011";
            case "JLT":
                return "100";
            case "JNE":
                return "101";
            case "JLE":
                return "110";
            case "JMP":
                return "111";
            default:
                // should never reach here
                return "";
        }
    }
}
