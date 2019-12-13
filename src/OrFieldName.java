import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrFieldName {
    String first;
    String second;

    private OrFieldName(String first, String second) {
        this.first = first;
        this.second = second;
    }

    static public OrFieldName just(String first) {
        Objects.requireNonNull(first);
        return new OrFieldName(first, null);
    }

    static public OrFieldName or(String first, String second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(first, second);
        return new OrFieldName(first, second);
    }

    public boolean equalsString(String s) {
        return s.equals(first) || s.equals(second);
    }

    @Override
    public String toString() {
        if (second != null) return "[" + first + "," + "second" + "]";
        return first;
    }

    public static class SetBuilder {
        private Stream<OrFieldName> orFields = Stream.empty();

        public SetBuilder withJustFields(String... fields) {
            orFields = Stream.concat(orFields, Arrays.stream(fields).map(OrFieldName::just));
            return this;
        }

        public SetBuilder withOrField(String first, String second) {
            orFields = Stream.concat(orFields, Stream.of(OrFieldName.or(first, second)));
            return this;
        }

        public Set<OrFieldName> build() {
            return orFields.collect(Collectors.toSet());
        }
    }
}
