package com.ake3m.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class StringUtils {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String slugify(String input) {
        var nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        var normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        var slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
