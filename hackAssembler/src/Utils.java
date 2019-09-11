/**
 * 一些helper方法
 * @author lunaticf
 */
public class Utils {
    public static String intToBinaryCodeWithPadding(int num) {
        return String.format("%15s", Integer.toBinaryString(num)).replace(' ', '0');
    }
}
