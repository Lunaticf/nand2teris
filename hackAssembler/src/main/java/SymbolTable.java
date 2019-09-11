import java.util.HashMap;

/**
 * java.SymbolTable
 * symbol -> binary string
 * i -> 0000 0000 0001 0000
 * @author lunaticf
 */
public class SymbolTable {
    public static HashMap<String, String> symbolTable = new HashMap<>(16);

    static {
        symbolTable.put("SP",     "000000000000000");
        symbolTable.put("LCL",    "000000000000001");
        symbolTable.put("ARG",    "000000000000010");
        symbolTable.put("THIS",   "000000000000011");
        symbolTable.put("THAT",   "000000000000100");
        symbolTable.put("SCREEN", "100000000000000");
        symbolTable.put("KBD",    "110000000000000");

        // R0-R15
        for (int i = 0; i <= 15; i++) {
            symbolTable.put("R" + i, Utils.intToBinaryCodeWithPadding(i));
        }
    }


    public static String get(String key) {
        return symbolTable.get(key);
    }

    public static String put(String key, String value) {
        return symbolTable.put(key, value);
    }
}
