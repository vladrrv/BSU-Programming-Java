import javax.swing.table.DefaultTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DateTableModel extends DefaultTableModel {
    private HashMap<Integer, String> data;
    private final String
            REG_DATE = "(\\d\\d?\\.){2}\\d{4}",
            REG_CALC1 = "\\s*\\=\\s*(\\d\\d?\\.){2}\\d{4}" + "\\s*([+-]\\s*\\d+)?",
            REG_CALC2 = "(\\s*\\=\\s*[A-Z]\\d+\\s*([+-]\\s*\\d+)?)",
            REG_OP = "[A-Z]\\d+",
            REG_MIN = "\\s*[=]\\s*[m][i][n]\\s*[(]" +
            "(\\s*(((\\d\\d?\\.){2}\\d{4})|([A-Z]\\d+))\\s*[,])*\\s*((((\\d)?(\\d)\\.){2}((\\d)?){3}\\d)|([A-Z]\\d+))\\s*[)]",
            REG_MAX = "\\s*[=]\\s*[m][a][x]\\s*[(]" +
            "(\\s*(((\\d\\d?\\.){2}\\d{4})|([A-Z]\\d+))\\s*[,])*\\s*((((\\d)?(\\d)\\.){2}((\\d)?){3}\\d)|([A-Z]\\d+))\\s*[)]",
            REG_NUM = "\\s*[+-]\\s*\\d+";
    private int rowCount, columnCount;
    private boolean[] parsed, parsing;

    DateTableModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        addTableModelListener(new DateTableListener(this));
        parsed = new boolean[rowCount*columnCount];
        parsing = new boolean[rowCount*columnCount];
        data = new HashMap<>();
        for (int i = 0; i < rowCount; ++i) {
            dataVector.get(i).set(0, i);
        }
        for (int i = 0; i < columnCount; ++i) {
            dataVector.get(0).set(i, ""+(char)('A'-1+i));
        }
        dataVector.get(0).set(0, "Row/Col No");
    }
    void updateSelf(int row, int column) {
        String input = (String)dataVector.get(row).get(column);
        int idx = row*columnCount+column;
        if (input.isEmpty()) {
            data.remove(idx);
        } else {
            data.put(idx, input);
        }
        recalc();
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return row > 0 && column > 0;
    }
    String getFormulaAt(int row, int column) {
        return data.get(row*columnCount+column);
    }

    private void recalc() {
        Arrays.fill(parsed, false);
        Arrays.fill(parsing, false);
        for (int i = 1; i < rowCount; ++i) {
            for (int j = 1; j < columnCount; ++j) {
                int k = i * columnCount + j;
                if (!parsed[k]) {
                    dataVector.get(i).set(j, parseCell(k));
                }
            }
        }
        //System.out.println("recalc");
    }
    private String parseCell(int i) {
        if (parsing[i]) {
            return "Incorrect!";
        }
        parsing[i] = true;
        String f = data.get(i);
        String res = null;
        if (f != null) {
            Calendar calendar = isDate(f);
            if (calendar == null) {
                calendar = calcDateOpConst(f);
            }
            if (calendar == null) {
                calendar = calcAddrOpConst(f);
            }
            if (calendar == null) {
                calendar = calcMin(f);
            }
            if (calendar == null) {
                calendar = calcMax(f);
            }
            if (calendar == null) {
                res = "Incorrect!";
            } else {
                res = toStr(calendar);
            }
        }
        parsing[i] = false;
        parsed[i] = true;
        return res;
    }
    private String toStr(Calendar calendar) {
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String dayS, monthS, yearS;
        dayS = Integer.toString(day);
        monthS = Integer.toString(month + 1);
        yearS = Integer.toString(year);
        if (dayS.length() < 2) {
            dayS = "0" + dayS;
        }
        if (monthS.length() < 2) {
            monthS = "0" + monthS;
        }
        if (yearS.length() < 2) {
            yearS = "0" + yearS;
        }
        if (yearS.length() < 3) {
            yearS = "0" + yearS;
        }
        if (yearS.length() < 4) {
            yearS = "0" + yearS;
        }
        return dayS + "." + monthS + "." + yearS;
    }
    private Calendar isDate(String f) {
        if (Pattern.compile(REG_DATE).matcher(f).matches()) {
            return toDate(f);
        }
        return null;
    }
    private Calendar calcDateOpConst(String f) {
        Matcher matcher = Pattern.compile(REG_CALC1).matcher(f);
        if (matcher.matches()) {
            matcher = Pattern.compile(REG_NUM).matcher(f);
            int k = 0;
            if (matcher.find()) {
                k = Integer.valueOf(deleteSpace(matcher.group()));
            }
            matcher = Pattern.compile(REG_DATE).matcher(f);
            String s = "";
            if (matcher.find()) {
                s = matcher.group();
            }
            Calendar calendar = toDate(s);
            calendar.add(GregorianCalendar.DAY_OF_MONTH, k);
            return calendar;
        }
        return null;
    }
    private Calendar calcAddrOpConst(String f) {
        Matcher matcher = Pattern.compile(REG_CALC2).matcher(f);
        if (matcher.matches()) {
            matcher = Pattern.compile(REG_NUM).matcher(f);
            int k = 0;
            if (matcher.find()) {
                k = Integer.valueOf(deleteSpace(matcher.group()));
            }
            matcher = Pattern.compile(REG_OP).matcher(f);
            if (matcher.find()) {
                String op = matcher.group();
                int colInd = op.charAt(0) - 'A' + 1;
                int rowInd = op.charAt(1) - '0';
                int j = rowInd * columnCount + colInd;
                if (data.containsKey(j)) {
                    String calc = parseCell(j);
                    dataVector.get(rowInd).set(colInd, calc);
                    Calendar calendar = toDate(calc);
                    if (calendar == null) return null;
                    calendar.add(GregorianCalendar.DAY_OF_MONTH, k);
                    return calendar;
                }
            }
        }
        return null;
    }
    private Calendar calcMin(String f) {
        if (Pattern.compile(REG_MIN).matcher(f).matches()) {
            List<Calendar> calendars = parse(f);
            Calendar min = Calendar.getInstance();
            if (!calendars.isEmpty()) {
                min = calendars.get(0);
            }
            for (Calendar calendar : calendars) {
                if (calendar == null) return null;
                if (calendar.compareTo(min) < 0) {
                    min = calendar;
                }
            }
            return min;
        }
        return null;
    }
    private Calendar calcMax(String f) {
        if (Pattern.compile(REG_MAX).matcher(f).matches()) {
            List<Calendar> calendars = parse(f);
            Calendar max = Calendar.getInstance();
            if (!calendars.isEmpty()) {
                max = calendars.get(0);
            }
            for (Calendar calendar : calendars) {
                if (calendar == null) return null;
                if (calendar.compareTo(max) > 0) {
                    max = calendar;
                }
            }
            return max;
        }
        return null;
    }
    private String deleteSpace(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); ++i) {
            if (sb.charAt(i) == ' ') {
                sb.deleteCharAt(i--);
            }
        }
        return sb.toString();
    }
    private List<Calendar> parse(String d) {
        Matcher matcher = Pattern.compile(REG_OP).matcher(d);
        List<Calendar> calendars = new ArrayList<>();
        while (matcher.find()) {
            String op = matcher.group();
            int colInd = op.charAt(0) - 'A' + 1;
            int rowInd = op.charAt(1) - '0';
            int j = rowInd*columnCount+colInd;
            String calc = parseCell(j);
            dataVector.get(rowInd).set(colInd, calc);
            calendars.add(toDate(calc));
        }
        matcher = Pattern.compile(REG_DATE).matcher(d);
        while (matcher.find()) {
            calendars.add(toDate(matcher.group()));
        }
        return calendars;
    }
    private Calendar toDate(String d) {
        if (d == null) return null;
        Calendar calendar;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            simpleDateFormat.setLenient(false);
            calendar = Calendar.getInstance();
            Date date = simpleDateFormat.parse(d);
            calendar.setTime(date);
        } catch (ParseException e) {
            return null;
        }
        return calendar;
    }
}
