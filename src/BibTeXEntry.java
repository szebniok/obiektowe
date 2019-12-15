import java.util.*;
import java.util.function.BiFunction;

public abstract class BibTeXEntry {
    final String type;
    final String key;
    final Set<OrFieldName> requiredFieldsNames;
    final Set<OrFieldName> optionalFieldsNames;
    final Set<BibTeXField> fields;

    public BibTeXEntry(String type, Set<OrFieldName> requiredFieldsNames, Set<OrFieldName> optionalFieldsNames,
                       String key, Collection<BibTeXField> fields) {
        this.type = type;
        this.requiredFieldsNames = requiredFieldsNames;
        this.optionalFieldsNames = optionalFieldsNames;
        this.key = key;
        this.fields = new HashSet<>();

        Optional<OrFieldName> missingRequiredField = requiredFieldsNames.stream()
                .filter(requiredName -> fields.stream().map(f -> f.name)
                        .noneMatch(requiredName::equalsString)
                ).findFirst();

        if (missingRequiredField.isPresent())
            throw new IllegalArgumentException("required field " + missingRequiredField.get() + " was not present");

        fields.stream().forEach(this::addField);
    }

    public static BiFunction<String, Collection<BibTeXField>, BibTeXEntry> constructorFromType(String type) {
        switch (type.toLowerCase()) {
            case "article":
                return Article::new;

            case "book":
                return Book::new;

            case "booklet":
                return Booklet::new;

            case "inbook":
                return InBook::new;

            case "incollection":
                return InCollection::new;

            case "inproceedings":
                return InProceedings::new;

            case "manual":
                return Manual::new;

            case "mastersthesis":
                return MastersThesis::new;

            case "misc":
                return Misc::new;

            case "phdthesis":
                return PhdThesis::new;

            case "techreport":
                return TechReport::new;

            case "unpublished":
                return Unpublished::new;
        }
        throw new IllegalArgumentException("Invalid BibTeXEntry type");
    }

    public Set<BibTeXField> getFields() {
        return this.fields;
    }

    private boolean isFieldRequired(BibTeXField field) {
        return requiredFieldsNames.stream().anyMatch(n -> n.equalsString(field.name));
    }

    private boolean isFieldOptional(BibTeXField field) {
        return optionalFieldsNames.stream().anyMatch(n -> n.equalsString(field.name));
    }

    private boolean isFieldValid(BibTeXField field) {
        return isFieldOptional(field) || isFieldRequired(field);
    }

    private void addField(BibTeXField field) {
        if (!isFieldValid(field)) return;
        fields.add(field);
    }

    private String borderLine() {
        return String.format("+%s+%s+", Utils.repeatedCharacter("-", 21), Utils.repeatedCharacter("-", 51));
    }


    @Override
    public String toString() {
        StringBuilder retval = new StringBuilder();

        // first line
        retval.append(String.format("+%s+\n", Utils.repeatedCharacter("-", 73)));

        // first row
        retval.append(String.format("| %1$-71s |\n", (type.toUpperCase() + " (" + key + ")")));

        // each entry
        for (BibTeXField field : fields) {
            retval.append(String.format("+%s+%s+\n", Utils.repeatedCharacter("-", 21), Utils.repeatedCharacter("-",
                    51)));
            retval.append(String.format("| %1$-20s| %2$-50s|\n", field.name, field.value));
        }
        retval.append(String.format("+%s+%s+\n", Utils.repeatedCharacter("-", 21), Utils.repeatedCharacter("-", 51)));

        return retval.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BibTeXEntry that = (BibTeXEntry) o;
        return type.equals(that.type) &&
                key.equals(that.key) &&
                requiredFieldsNames.equals(that.requiredFieldsNames) &&
                optionalFieldsNames.equals(that.optionalFieldsNames) &&
                fields.equals(that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, key, requiredFieldsNames, optionalFieldsNames, fields);
    }


}
