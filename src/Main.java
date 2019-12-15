import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    static void printHelp(Options options) {
        new HelpFormatter().printHelp("java -jar BibTeX.jar [OPTION] FILE", options);
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "Display this help and exit");
        options.addOption("l", "lastname", true, "Specifies the last name of author/editor of the entries to be displayed");
        options.addOption("t", "type", true, "Specifies the type of the entries to be displayed");

        try {
            HelpFormatter helpFormatter = new HelpFormatter();
            CommandLine cmd = new DefaultParser().parse(options, args);

            if (cmd.getArgs().length != 1) {
                printHelp(options);
                return;
            }

            Path file = Paths.get(cmd.getArgs()[0]);
            BibTeX bibTeX = new BibTeXParser(file).getBibTeX();

            if (cmd.hasOption("l")) {
                bibTeX.filterByLastName(cmd.getOptionValue('l'));
            }
            if (cmd.hasOption('t')) {
                bibTeX.filterByType(cmd.getOptionValue('t'));
            }
            System.out.println(bibTeX);
        } catch (ParseException e) {
            printHelp(options);
        }
    }
}
