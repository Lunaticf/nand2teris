import java.io.FileNotFoundException;
import java.io.IOException;

public class VMTranslator {

    public static void main(String[] args) throws IOException {

        String file = args[0];
        Parser parser = new Parser(file);

        file = file.replace("vm", "asm");
        CodeWriter codeWriter = new CodeWriter(file);

        while (parser.hasMoreCommands()) {
            parser.advance();

            // 如果是算术指令
            if (parser.commandType() == CommandType.C_ARITHMETIC) {
                codeWriter.writeArithmetic(parser.arg1(), parser.arg2());
            } else if (parser.commandType() == CommandType.C_PUSH || parser.commandType() == CommandType.C_POP) {
                codeWriter.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
            }
        }

        codeWriter.close();
    }
}
