
import java.text.DecimalFormat;

/**
 * Created by gorz on 25.02.14.
 */
public class Formatter {

    private DecimalFormat ceil;
    private DecimalFormat fraction;
    private DecimalFormat exp;

    public Formatter() {
        ceil = new DecimalFormat("################");
        fraction = new DecimalFormat("#.################");
        exp = new DecimalFormat("#.###############E00");
    }

    public String format(double d) {
        double abs = Math.abs(d);
        String result = "";
        if(abs >= 10.0 && abs <= 9999999999999999.0) {
            result = formatWithDifferentLength(abs);
        } else if(abs > 0.0000000000000001 && abs < 10) {
            result = fraction.format(abs);
        } else if(abs == 0.0) {
            result = "0";
        } else {
            result = exp.format(abs);
        }
        if(d < 0) {
            result = "-"+result;
        }
        return customizeNumber(result);
    }

    private String formatWithDifferentLength(double abs) {
        String ceilPart = ceil.format(abs);
        StringBuilder b = new StringBuilder(18);
        for(int i=0; i<ceilPart.length(); i++) {
            b.append('#');
        }
        if(ceilPart.length() < 16) {
            b.append('.');
            for(int i=0; i<16-ceilPart.length(); i++) {
                b.append('#');
            }
        }
        DecimalFormat ceilAndFraction = new DecimalFormat(b.toString());
        return ceilAndFraction.format(abs);
    }

    private String customizeNumber(String str) {
        if(str.endsWith(",")) {
            return str.substring(0, str.length()-1);
        } else if(str.endsWith(",0")) {
            return str.substring(0, str.length()-2);
        } else if(str.contains("E")) {
            String parts[] = str.split("E");
            if(!parts[1].startsWith("-")) {
                parts[1] = "+"+parts[1];
            }
            if(!parts[0].contains(",")) {
                parts[0] += ",";
            }
            return parts[0] + "e" + parts[1];
        }
        return str;
    }
}
