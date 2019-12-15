import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BibTeX {
    Set<BibTeXEntry> entries;

    public BibTeX(Set<BibTeXEntry> entries) {
        this.entries = entries;
    }

    public void filterByType(String type) {
        filtered(entry -> entry.type.equalsIgnoreCase(type));
    }

    public void filterByLastName(String lastName) {
        filtered(entry -> {
            Optional<BibTeXField> nameField = entry.fields.stream()
                    .filter(f -> Set.of("author", "editor").contains(f.name))
                    .findFirst();

            if (!nameField.isPresent()) return false;

            return Arrays.stream(nameField.get().value.split(" and "))
                    .map(fullName -> fullName.split(",")[0])
                    .anyMatch(lastName::equalsIgnoreCase);
        });
    }

    @Override
    public String toString() {
        return entries.stream().collect(
                StringBuilder::new,
                (acc, s2) -> acc.append(s2).append("\n"),
                (acc, s2) -> acc.append(s2).append("\n")
        ).toString();
    }

    private void filtered(Predicate<BibTeXEntry> predicate) {
        entries = entries.stream().filter(predicate).collect(Collectors.toSet());
    }
}
