import java.util.Set;

public class BibTeX {
    Set<BibTeXEntry> entries;

    public BibTeX(Set<BibTeXEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return entries.stream().collect(
                StringBuilder::new,
                (acc, s2) -> acc.append(s2).append("\n"),
                (acc, s2) -> acc.append(s2).append("\n")
        ).toString();
    }
}
