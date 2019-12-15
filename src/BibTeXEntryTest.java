import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BibTeXEntryTest {
    static final BibTeXField requiredField1 = new BibTeXField("requiredField1", "value");
    static final BibTeXField requiredField2 = new BibTeXField("requiredField2", "value");
    static final BibTeXField optionalField = new BibTeXField("optionalField", "value");
    static final BibTeXField excessField = new BibTeXField("excessField", "value");

    class IBibTeXEntry extends BibTeXEntry {
        public IBibTeXEntry(Set<BibTeXField> fields) {
            super("test", Set.of(OrFieldName.or("requiredField1", "requiredField2")), Set.of(OrFieldName.just(
                    "optionalField")), "testKey", fields);
        }
    }

    @Test
    void constructorWithoutRequiredFieldsShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new IBibTeXEntry(Set.of(optionalField, excessField)));
    }

    @Test
    void constructorShouldCorrectlySetFields() {
        assertEquals(Set.of(optionalField, requiredField1),
                new IBibTeXEntry(Set.of(optionalField, requiredField1)).getFields());
        assertEquals(Set.of(optionalField, requiredField2),
                new IBibTeXEntry(Set.of(optionalField, requiredField2)).getFields());
    }

    @Test
    void constructorShouldIgnoreExcessFields() {
        assertEquals(Set.of(optionalField, requiredField1), new IBibTeXEntry(Set.of(optionalField, excessField,
                requiredField1))
                .getFields());
    }

    @Test
    void constructorShouldCreateObjectWithoutOptionalFields() {
        assertEquals(Set.of(requiredField1), new IBibTeXEntry(Set.of(excessField, requiredField1)).getFields());
    }
}