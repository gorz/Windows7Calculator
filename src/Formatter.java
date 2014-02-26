
import java.text.DecimalFormat;

/**
 * Created by gorz on 25.02.14.
 */
public class Formatter {
    private DecimalFormat format;
    private DecimalFormat formatWithExponent;

    public Formatter() {
        format = new DecimalFormat("################.################");
        formatWithExponent = new DecimalFormat("################.###############E00");
    }

    public String format(double d) {
        return Double.toString(d);
        /*String result = "";
        double temp = Math.abs(d);
        String number = format.format(temp);
        int positionOfComa = number.indexOf(",");
        int ceilPartLength = 0;
        int fractionPartLength = 0;
        if(positionOfComa == -1) {
            ceilPartLength = number.length();
        } else {
            ceilPartLength = positionOfComa;
            if(number.endsWith(",0")) {
                fractionPartLength = 0;
            } else {
                fractionPartLength = number.length() - positionOfComa - 1;
            }
        }
        if(ceilPartLength + fractionPartLength > 16) {
            result = formatWithExponent.format(temp);
            result = customizeNumber(result);
        } else {
            result = formatWithDifferentLength(ceilPartLength, fractionPartLength, temp);
        }
        if(d < 0) {
            result = "-" + result;
        }
        return result;*/
    }

    private String formatWithDifferentLength(int ceilLength, int fractionLength, double d) {
        StringBuilder format = new StringBuilder(ceilLength + fractionLength + 1);
        for(int i=0; i<ceilLength; i++) {
            format.append('#');
        }
        if(fractionLength > 0) {
            format.append('.');
            for(int i=0; i<fractionLength; i++) {
                format.append('#');
            }
        }
        DecimalFormat formatter = new DecimalFormat(format.toString());
        return formatter.format(d);
    }

    private String customizeNumber(String str) {
        String parts[] = str.split("E");
        if(!parts[1].startsWith("-")) {
            parts[1] = "+"+parts[1];
        }
        if(!parts[0].contains(",")) {
            parts[0] += ",";
        }
        return parts[0] + "e" + parts[1];
    }
}
