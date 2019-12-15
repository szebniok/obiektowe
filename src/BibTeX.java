import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BibTeX {
    Set<BibTeXEntry> entries;

    public BibTeX(Set<BibTeXEntry> entries) {
        this.entries = entries;
    }

    public void filterByType(String joinedTypes) {
        List<String> requestedTypes = Arrays.asList(joinedTypes.split(","));

        filtered(entry -> requestedTypes.stream().anyMatch(entry.type::equalsIgnoreCase));
    }

    public void filterByLastName(String joinedLastNames) {
        List<String> requestedLastNames = Arrays.asList(joinedLastNames.split(","));

        filtered(entry -> {
            Optional<BibTeXField> nameField = entry.fields.stream()
                    .filter(f -> Set.of("author", "editor").contains(f.name))
                    .findFirst();

            return nameField.map(bibTeXField -> Arrays.stream(bibTeXField.value.split(" and "))
                    .map(fullName -> fullName.split(",")[0])
                    .anyMatch(lastName -> requestedLastNames.stream().anyMatch(lastName::equalsIgnoreCase)))
                    .orElse(false);

        });
    }

    public void filterByFirstName(String joinedFirstNames) {
        List<String> firstNames = Arrays.stream(joinedFirstNames.split(","))
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        filtered(entry -> {
            Optional<BibTeXField> nameField = entry.fields.stream()
                    .filter(f -> Set.of("author", "editor").contains(f.name))
                    .findFirst();

            return nameField.map(bibTeXField -> Arrays.stream(bibTeXField.value.split(" and "))
                    .map(fullName -> fullName.split(",")[1])
                    .map(String::toLowerCase)
                    .anyMatch(firstName -> firstNames.stream().anyMatch(firstName::contains))).orElse(false);

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
