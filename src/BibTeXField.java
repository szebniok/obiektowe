public class BibTeXField {
    String name;
    String value;

    public BibTeXField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
