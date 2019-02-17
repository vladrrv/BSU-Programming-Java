import java.util.*;

public class CharacterFrequency {
    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println(args[0]);
            printTable(new StringBuilder(args[0]));
        }
    }
    static void printTable(StringBuilder str) {
        Map<Character, Integer> m = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            Character ch = str.charAt(i);
            if (m.containsKey(ch)) {
                m.put(ch, m.get(ch)+1);
            }
            else {
                m.put(ch, 1);
            }
        }
        System.out.println(makeTable(m));
    }
    static String makeTable(Map<Character, Integer> map) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<Character, Integer>> iter = map.entrySet().iterator();
        sb.append("\n+------+------+\n| Char | Freq |\n+------+------+\n");
        while (iter.hasNext()) {
            Map.Entry<Character, Integer> entry = iter.next();
            sb.append("| ");
            sb.append(entry.getKey());
            sb.append("    |  ");
            sb.append(entry.getValue());
            sb.append("   |\n");
        }
        sb.append("+------+------+");
        return sb.toString();
    }
}
