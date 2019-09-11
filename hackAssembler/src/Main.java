import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * 一个特别laji的nand2teris汇编器
 * 本汇编器假设输入的汇编程序语法是正确的
 * @author lunaticf
 */
public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser(args[0]);

        // 行号
        int lineNo = -1;

        // First Pass
        // 用于look forward jump label
        while (parser.hasNext()) {
            String line = parser.getNext();

            // 如果是label
            // 注意label不占行数 所以不是label我们才让行号加1
            if (line.startsWith("(")) {
                SymbolTable.put(line.substring(1, line.length() - 1), Utils.intToBinaryCodeWithPadding(lineNo + 1));
            } else {
                lineNo++;
            }
        }

        parser.reset();


        // Second pass -> generate code
        StringBuilder hack = new StringBuilder();
        while (parser.hasNext()) {
            // 得到有效的一行啦
            String line = parser.getNext();
            // 跳过label
            if (line.startsWith("(")) {
                continue;
            }
            hack.append(Generator.generateCode(line)).append("\n");
        }

        FileWriter writer = null;
        try {
            String toFileName = args[0].replace("asm", "hack");

            writer = new FileWriter(toFileName);
            writer.append(hack.toString());
            writer.flush();
            writer.close();

            System.out.println("成功生成hack代码 - " + toFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
