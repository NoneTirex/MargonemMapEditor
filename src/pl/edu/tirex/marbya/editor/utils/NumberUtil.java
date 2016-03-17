package pl.edu.tirex.marbya.editor.utils;

public class NumberUtil
{
    public static Integer parseInt(String text)
    {
        int out = 0;
        int multiply = 10;
        int limit = -Integer.MAX_VALUE;
        boolean negative = false;
        int digit;
        int multmin = limit / multiply;

        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (c == '-' && i == 0)
            {
                negative = true;
                limit = Integer.MIN_VALUE;
                multmin = limit / multiply;
                continue;
            }
            else if (c < '0' || c > '9') return null;
            digit = c - '0';
            if (out < multmin) return null;
            out *= multiply;
            if (out < limit + digit) return null;
            out -= digit;
        }
        return negative ? out : -out;
    }
}
