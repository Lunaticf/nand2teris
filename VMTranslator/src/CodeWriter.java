import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CodeWriter {
    private BufferedWriter writer;

    private int jumpCount = 0;
    private Set<String> directSegment = new HashSet<>(Arrays.asList("temp", "static", "pointer"));
    private Set<String> indirectSegment = new HashSet<>(Arrays.asList("local", "argument", "this", "that"));


    public CodeWriter(String file) throws IOException {
        writer = new BufferedWriter(new FileWriter(file));
    }

    public void writeArithmetic(String command, String arg) throws IOException {
        String asmLine;
        switch (command) {
            case "add":
                asmLine = arithmeticBinTemplate1() + "M=M+D\n";
                break;
            case "sub":
                asmLine = arithmeticBinTemplate1() + "M=M-D\n";
                break;
            case "or":
                asmLine = arithmeticBinTemplate1() + "M=M|D\n";
                break;
            case "and":
                asmLine = arithmeticBinTemplate1() + "M=M&D\n";
                break;
            case "neg":
                asmLine = arithmeticBinTemplate2() + "D=0\nM=D-M\n";
                break;
            case "not":
                asmLine = arithmeticBinTemplate2() + "M=!M\n";
                break;
            case "eq":
                asmLine = arithmeticBinTemplate3("JEQ");
                jumpCount++;
                break;
            case "gt":
                asmLine = arithmeticBinTemplate3("JGT");
                jumpCount++;
                break;
            case "lt":
                asmLine = arithmeticBinTemplate3("JLT");
                jumpCount++;
                break;
            default:
                throw new RuntimeException("unknown command!");
        }
        writer.write(asmLine);
    }

    public void writePushPop(CommandType command, String segment, String arg) throws IOException {
        String asmLine = "";
        if (command == CommandType.C_PUSH) {
            if ("constant".equals(segment)) {
                asmLine = writeToStack(arg) + increStack();
            } else if (directSegment.contains(segment)) {
                asmLine = getDirectAddress(segment, arg) + writeToStackByA() + increStack();
            } else if (indirectSegment.contains(segment)){
                asmLine = getInDirectAddress(segment, arg) + writeToStackByA() + increStack();
            } else {
                throw new RuntimeException("unknown segment!");
            }
        } else if (command == CommandType.C_POP) {
            if (directSegment.contains(segment)) {
                asmLine = decreStack() +  getDirectAddress(segment, arg) + writeToAByPop();
            } else if (indirectSegment.contains(segment)){
                asmLine = decreStack() + getInDirectAddress(segment, arg) + writeToAByPop();
            } else {
                throw new RuntimeException("unknown segment!");
            }
        }
        writer.write(asmLine);
    }


    /**
     * 对于直接的段 获取地址 段标志加上偏移即可
     * @return
     */
    private String getDirectAddress(String segment, String arg) {
        return "@" + segToSymbol(segment) +"\n" +
                "D=A\n" +
                "@" + arg + "\n" +
                "A=D+A\n";
    }

    private String getInDirectAddress(String segment, String arg) {
        return "@" + segToSymbol(segment) +"\n" +
                "D=M\n" +
                "@" + arg + "\n" +
                "A=D+A\n";
    }

    private String segToSymbol(String segment) {
        switch (segment) {
            case "local":
                return "LCL";
            case "argument":
                return "ARG";
            case "this":
                return "THIS";
            case "that":
                return "THAT";
            case "pointer":
                return "THIS";
            case "temp":
                return "R5";
            case "static":
                return "16";
            default:
                return "";
        }
    }

    private String arithmeticBinTemplate1() {
        return  "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "A=A-1\n";
    }

    private String arithmeticBinTemplate2() {
        return  "@SP\n" +
                "A=M-1\n";
    }

    private String arithmeticBinTemplate3(String type) {
        return  "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "A=A-1\n" +
                "D=M-D\n" +
                "@" + jumpTrueName() + "\n" +
                "D;" + type + "\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=0\n" +
                "@" + jumpContinue() + "\n" +
                "0;JMP\n" +
                "(" + jumpTrueName() + ")\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=-1\n" +
                "(" + jumpContinue() + ")\n";
    }

    private String jumpTrueName() {
        return "TRUE" + jumpCount;
    }

    private String jumpContinue() {
        return "CONTINUE" + jumpCount;
    }

    /**
     * 增加栈指针 for push
     * @return
     */
    private String increStack() {
        return "@SP\nM=M+1\n";
    }

    /**
     * 减小栈指针 for push
     * @return
     */
    private String decreStack() {
        return "@SP\nM=M-1\n";
    }

    /**
     * 往sp所在的位置写入
     * @return
     */
    private String writeToStack(String num) {
        return "@" + num + "\nD=A\n@SP\nA=M\nM=D\n";
    }

    /**
     * 往sp所在的位置写入 A为地址
     * @return
     */
    private String writeToStackByA() {
        return "D=M\n@SP\nA=M\nM=D\n";
    }

    /**
     * 往A所在的位置pop一个元素 (此时栈已减小)
     * 因为A保存了位置 而取stack元素的时候又要改变A 所以先把A存到R13
     * @return
     */
    private String writeToAByPop() {
        return  "D=A\n" +
                "@R13\n" +
                "M=D\n" +
                "@SP\nA=M\nD=M\n" +
                "@R13\n" +
                "A=M\n" +
                "M=D\n";

    }


    public void close() throws IOException {
        writer.close();
    }

}
