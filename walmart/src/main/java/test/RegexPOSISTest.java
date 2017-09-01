package test;

/**
 * Created by vn0gshm on 22/08/17.
 */

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPOSISTest {

    private static Pattern pattern;

    @BeforeClass
    public static void setUp() {
        String regex = "^([^[0-9]\\p{Punct}|\\s])(.*)";
        pattern = Pattern.compile(regex);
    }

    @Test
    public void testRegexSpaceEnd() {
        String text = "Testessss   ";
        Matcher matcher = pattern.matcher(text);
        Assert.assertFalse(pattern.matcher(text).matches());
    }

    @Test
    public void testRegexInvalidCaracter() {
        String text = "abcds_";
        Matcher matcher = pattern.matcher(text);
        Assert.assertFalse(pattern.matcher(text).matches());
    }

    @Test
    public void testRegexWhenStartsWithWhitespace() {
        String text = " abcds";
        Matcher matcher = pattern.matcher(text);
        Assert.assertFalse(pattern.matcher(text).matches());
    }

}
