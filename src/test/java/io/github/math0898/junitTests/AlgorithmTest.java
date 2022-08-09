package io.github.math0898.junitTests;

public class AlgorithmTest {

    public static String prefixParse (String toParse) {
        String prefix = "";
        for (int i = 0; i < toParse.length(); i += 2) {
            System.out.println("-->" + toParse.charAt(i));
            if (toParse.charAt(i) == '&') prefix = toParse.substring(0, i + 2);
            else break;
        }
        return prefix;
    }

    public static void main (String[] args) {
        String toParse = "&7Lore should be able to be really long and then automatically indented at the correct position based on a configuration option.";
        System.out.println(toParse);
        System.out.println(prefixParse(toParse));
        toParse = "&7&lThis Item Has a Really Cool Name";
        System.out.println(toParse);
        System.out.println(prefixParse(toParse));
    }
}
