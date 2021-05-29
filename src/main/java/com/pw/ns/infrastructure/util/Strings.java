package com.pw.ns.infrastructure.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Strings {

    public static String format(String template, Object... arguments)
    {
        return String.format(template.replaceAll("\\{}", "%s"), arguments);
    }
}
