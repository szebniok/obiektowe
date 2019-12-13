import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        System.out.println(new BibTeXParser(Paths.get("test.bib")).getBibTeX());
    }
}
