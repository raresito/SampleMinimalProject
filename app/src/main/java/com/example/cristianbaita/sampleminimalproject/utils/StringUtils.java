package com.example.cristianbaita.sampleminimalproject.utils;

import android.text.TextUtils;

import com.example.cristianbaita.sampleminimalproject.constants.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class StringUtils {

    /**
     * <p>The maximum size to which the padding constant(s) can expand.</p>
     */
    private static final int PAD_LIMIT = 8192;

    /**
     * The empty String {@code ""}.
     * @since 2.0
     */
    public static final String EMPTY = "";

    public static final String DEFAULT_APP_LANGUAGE = "en";

	public static String getTruncatedText(String str, int... input) // [0] = maxLen, [1] = lenFromEnd
	{
		int maxLen = input[0];
		int lenFromEnd = 0;
		if (input.length == 2)
		{
			lenFromEnd = input[1];
		}
		int len;
		if (str != null && (len = str.length()) > maxLen)
		{
			return str.substring(0, maxLen - lenFromEnd - 3) + "..." + str.substring(len - lenFromEnd, len);
		}
		return str;
	}

	/**
	 * Gets the date from string.
	 * 
	 * @param string
	 *            the string
	 * @return the date from string
	 */
	public static Calendar getDateFromString(String string)
	{
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_LONG, Locale.getDefault());
		Date date = null;
		try
		{
			date = format.parse(string);
		} catch (ParseException e)
		{
			date = new Date();
		} catch (NullPointerException e)
		{
			date = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal;
	}

	/**
	 * Gets the string from date.
	 * 
	 * @param year
	 *            the year
	 * @param month
	 *            the month
	 * @param dayOfMonth
	 *            the day of month
	 * @return the string from date
	 */
	public static String getStringFromDate(int year, int month, int dayOfMonth)
	{
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_LONG, Locale.getDefault());

		Calendar cal = Calendar.getInstance();
		cal.set(year, month, dayOfMonth);

		String birthday = format.format(cal.getTime());

		return birthday;
	}

	public static String hexaToString(String hex)
	{

		StringBuilder output = new StringBuilder();
		for (int i = 0; i < hex.length(); i += 2)
		{
			String str = hex.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}

		return output.toString();
	}


	public static String concatStringsWSep(Collection<String> strings, String separator) {
	    StringBuilder sb = new StringBuilder();
	    String sep = "";
	    for(String s: strings) {
	        sb.append(sep).append(s);
	        sep = separator;
	    }
	    return sb.toString();                           

	}

    public static String[] convertStringToArray(String str, String separator)
    {
        if (TextUtils.isEmpty(str))
        {
            return null;
        }
        String[] arr = str.split(separator);
        return arr;
    }

    /**
     * <p>Repeat a String {@code repeat} times to form a
     * new String.</p>
     *
     * <pre>
     * StringUtils.repeat(null, 2) = null
     * StringUtils.repeat("", 0)   = ""
     * StringUtils.repeat("", 2)   = ""
     * StringUtils.repeat("a", 3)  = "aaa"
     * StringUtils.repeat("ab", 2) = "abab"
     * StringUtils.repeat("a", -2) = ""
     * </pre>
     *
     * @param str  the String to repeat, may be null
     * @param repeat  number of times to repeat str, negative treated as zero
     * @return a new String consisting of the original String repeated,
     *  {@code null} if null String input
     */
    public static String repeat(final String str, final int repeat) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY;
        }
        final int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= PAD_LIMIT) {
            return repeat(str.charAt(0), repeat);
        }

        final int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1 :
                return repeat(str.charAt(0), repeat);
            case 2 :
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                return new String(output2);
            default :
                final StringBuilder buf = new StringBuilder(outputLength);
                for (int i = 0; i < repeat; i++) {
                    buf.append(str);
                }
                return buf.toString();
        }
    }

    /**
     * <p>Returns padding using the specified delimiter repeated
     * to a given length.</p>
     *
     * <pre>
     * StringUtils.repeat('e', 0)  = ""
     * StringUtils.repeat('e', 3)  = "eee"
     * StringUtils.repeat('e', -2) = ""
     * </pre>
     *
     * <p>Note: this method doesn't not support padding with
     * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a>
     * as they require a pair of {@code char}s to be represented.
     * If you are needing to support full I18N of your applications
     * consider using {@link #repeat(String, int)} instead.
     * </p>
     *
     * @param ch  character to repeat
     * @param repeat  number of times to repeat char, negative treated as zero
     * @return String with repeated character
     * @see #repeat(String, int)
     */
    public static String repeat(final char ch, final int repeat) {
        final char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    /**
     * Capitalizes the first letter from a string, and converts the rest of characters to lower case.
     * @param text the text to capitalize
     * @return the capitalized string
    */
    public static String capitalizeFirstLetter(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            return text;
        }

        return (text.substring(0, 1).toUpperCase(Locale.getDefault()) + text.substring(1).toLowerCase(Locale.getDefault()));
    }

    public static boolean startsWithArabicChar(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            return false;
        }

        String textWithoutSpace = text.trim().replaceAll(" ", ""); //to ignore whitepace
        for (int i = 0; i < textWithoutSpace.length(); )
        {
            int c = textWithoutSpace.codePointAt(i);
            // range of arabic chars/symbols is from 0x0600 to 0x06ff
            // the arabic letter '??' is special case having the range from 0xFE70 to 0xFEFF
            if (c >= 0x0600 && c <= 0x06FF || (c >= 0xFE70 && c <= 0xFEFF))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    public static boolean startsWithRTLChar(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            return false;
        }

        String textWithoutSpace = text.trim().replaceAll(" ", ""); //to ignore whitepace
        for (int i = 0; i < textWithoutSpace.length(); )
        {
            int c = textWithoutSpace.codePointAt(i);
            if (c >= 0x600 && c <= 0x6ff)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    public static String forceLTRString(String str)
    {
        if (TextUtils.isEmpty(str))
        {
            return str;
        }

        return "\u202A" + str + "\u202C";
    }

    public static String forceRTLString(String str)
    {
        if (TextUtils.isEmpty(str))
        {
            return str;
        }

        return "\u202E" + str + "\u202C";
    }

    public static String replaceSpacesWithNBSP(String string)
    {
        if (TextUtils.isEmpty(string))
        {
            return string;
        }

        return string.replaceAll(" ", Constants.NON_BREAKING_SPACE_CHARACTER);
    }

    public static String addWrappingPointsToEmailAddress(String emailAddress)
    {
        if (TextUtils.isEmpty(emailAddress))
        {
            return emailAddress;
        }

        return emailAddress.replace("@", "@" + Constants.ZERO_WIDTH_SPACE_CHARACTER).replace(".", "." + Constants.ZERO_WIDTH_SPACE_CHARACTER);
    }
}
