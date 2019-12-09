import java.util.List;

public class Main {

    public static void main(String[] args) {
        Book haskell = new Book("Hutton:Haskell", List.of(
                new BibTeXField("author", "Graham Hutton"),
                new BibTeXField("title", "Programming in Haskell"),
                new BibTeXField("year", "2016"),
                new BibTeXField("edition", "2nd"),
                new BibTeXField("publisher", "Cambridge University Press")
        ));

        Booklet booklet = new Booklet("Kronika", List.of(
                new BibTeXField("title", "Kronika polska"),
                new BibTeXField("author", "Gal Anonim")
        ));

        BibTeX bibTeX = new BibTeX(List.of(haskell, booklet));

        System.out.println(bibTeX);
    }
}
