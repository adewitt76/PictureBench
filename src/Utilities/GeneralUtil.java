package Utilities;

/**
 * Created by aaron on 5/16/15.
 */
public class GeneralUtil {
    public static boolean isNumerical(String string){
        for(char c: string.toCharArray()) {
            if(!Character.isDigit(c)) return false;
        }
        return true;
    }
}
