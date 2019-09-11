import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 用于读取整个汇编程序 并分成SingleLine
 * @author lunaticf
 */
public class Parser {
    private String[] lines;
    private int pos = 0;

    Parser(String filename) {
        try {
            String s = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
            lines = s.split("\n");
        } catch (IOException e) {
            System.out.println("读取文件发生错误");
            e.printStackTrace();
        }
    }

    public boolean hasNext() {
        if (pos >= lines.length) {
            return false;
        }

        lines[pos] = lines[pos].trim();
        // 判断空行和注释
        while ("".equals(lines[pos]) || lines[pos].startsWith(Constants.commentFlag) ) {
            pos++;
            if (pos == lines.length) {
                return false;
            }
        }
        return true;
    }

    public String getNext() {
        // 判断语句是否有注释
        int commentPos = lines[pos].indexOf("//");
        if (commentPos != -1) {
            return lines[pos++].substring(0, commentPos).trim();
        }
        return lines[pos++].trim();
    }

    public void reset() {
        pos = 0;
    }
}
