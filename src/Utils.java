final public class Utils {
    private Utils() {
    }

    static String repeatedCharacter(String s, int n) {
        assert s.length() == 1;
        return new String(new char[n]).replace("\0", s);
    }
}
