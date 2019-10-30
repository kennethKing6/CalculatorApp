package com.example.calculatorapp;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class purpose is to simulate a calculator operation.
 * It currently supports multtiplication,division,subtraction and division.
 * <p>
 * Note: any operation that contains * and / are taken first and to simplify the operation in the
 * format. 6+5-2. Meaning all division and multiplication are done first before the result is *
 * calculated. I call those with * and / high precedence
 * <p>
 * <p>
 * author Kouadio Kenneth.
 * date: 27 Oct 2019.
 */
public class CalculatorOperation {

    private static final String TAG = CalculatorOperation.class.getSimpleName();


    /**
     * Possible math combinations that could arise
     * <p>
     * help me match combinations for:
     * -7+2
     * 7+-2
     * -7+-2
     */
    public static final String NEGATIVE_COMBINATION_ONE = "(.\\d+)([+|-])(\\d+)|" +
            "(\\d+)([+|-])(.\\d+)|" +
            "(.\\d+)([+|-])(.\\d+)";

    /**
     * Possible math combinations that could arise
     * <p>
     * help me match combinations for:
     * -7.0+2
     * 7.0+-2
     * -7.0+-2
     */
    public static final String NEGATIVE_COMBINATION_TWO = "(.\\d+[.]\\d+)([+|-])(\\d+)|" +
            "(\\d+[.]\\d+)([+|-])(.\\d+)|" +
            "(.\\d+[.]\\d+)([+|-])(.\\d+)";

    /**
     * Possible math combinations that could arise
     * <p>
     * help me match combinations for:
     * -7+2.0
     * 7+-2.0
     * -7+-2.0
     */
    public static final String NEGATIVE_COMBINATION_THREE = "(.\\d+)([+|-])(\\d+[.]\\d+)|" +
            "(\\d+)([+|-])(.\\d+[.]\\d+)|" +
            "(.\\d+)([+|-])(.\\d+[.]\\d+)";

    /**
     * Possible math combinations that could arise
     * <p>
     * help me match combinations for:
     * -7.0+2.0
     * 7.0+-2.0
     * -7+-2.0
     */
    public static final String NEGATIVE_COMBINATION_FOUR = "(.\\d+[.]\\d+)([+|-])(\\d+[.]\\d+)|" +
            "(\\d+[.]\\d+)([+|-])(.\\d+[.]\\d+)|" +
            "(.\\d+)([+|-])(.\\d+[.]\\d+)";
    /**
     * These two negative fields represents the format of the negative values I get from this class.
     * It is either -2.0
     */
    public static final String NEGATIVE_RESULT_FORMAT = "([-])(\\d+)([.])(\\d+)";


    /**
     * The pattern I use to get the operations with higher precedence, meaning * and /.
     * for example, 55*2 + 5 *2 - 6 * 2. This pattern will help me retrieve
     * 55*2, 5 *2 and 6 * 2 while leaving behind +-.
     */
    public static final String MY_HIGHER_PRECEDENCE_PATTERN = "(\\d+)([*|/])(\\d+)|(\\d+[.]\\d+)([*|/])(\\d+)|" +
            "(\\d+)([*|/])(\\d+[.]\\d+)|(\\d+[.]\\d+)([*|/])(\\d+[.]\\d+)";
    ;

    /**
     * The pattern I use to get the operations with lower precedence, meaning + and -.
     * for example, 55+ 3 - 6 . This pattern will help me retrieve
     * get the final result of the operation
     */
    public static final String MY_LOWER_POSITIVE_PRECEDENCE_PATTERN = "(\\d+[.]\\d+)([+|-])(\\d+[.]\\d+)|" +
            "(\\d+[.]\\d+)([+|-])(\\d+)|" +
            "(\\d+)([+|-])(\\d+[.]\\d+)|" +
            "(\\d+)([+|-])(\\d+)";


    public static final String MY_LOWER_NEGATIVE_PRECEDENCE_PATTERN = NEGATIVE_COMBINATION_ONE + "|" + NEGATIVE_COMBINATION_TWO +
            "|" + NEGATIVE_COMBINATION_THREE + "|" + NEGATIVE_COMBINATION_FOUR;

    private CalculatorOperation() {
    }

    /**
     * This method is used to deconstruct the operation that the user enters by looking at
     * * and / in order to deal with the higher precendence values first and store them in
     * an array.
     *
     * @param userInput
     */
    public static String deconstructHPrecedence(String userInput) {
        Pattern pattern = Pattern.compile(MY_HIGHER_PRECEDENCE_PATTERN);

        Matcher matcher = pattern.matcher(userInput);

        /**
         * This object helps in creating a format of 2+4-5. Where the
         * operations of multiplication and addition are already calculated
         */


        StringBuffer sb = new StringBuffer();


        while (matcher.find()) {
            matcher.appendReplacement(sb, "" + calculatePrecedence(matcher.group()));


        }

        matcher.appendTail(sb);


        return sb.toString();

    }


    private static double calculatePrecedence(String pattern) {
        double result = 0.0;
        String[] values;


        if (pattern.matches("(\\d+)([+|-])(\\d+)") || pattern.matches("(\\d+)([*|/])(\\d+)") ||
                pattern.matches("(\\d+[.]\\d+)([+|-])(\\d+)") || pattern.matches("(\\d+[.]\\d+)([*|/])(\\d+)") ||
                pattern.matches("(\\d+)([+|-])(\\d+[.]\\d+)") || pattern.matches("(\\d+)([*|/])(\\d+[.]\\d+)") ||
                pattern.matches("(\\d+[.]\\d+)([+|-])(\\d+[.]\\d+)") || pattern.matches("(\\d+)([*|/])(\\d+[.]\\d+)")) {
            if (pattern.contains("*")) {
                values = pattern.split("[*]");
                result = Double.parseDouble(values[0]) * Double.parseDouble(values[1]);
            } else if (pattern.contains("/")) {
                values = pattern.split("[/]");
                result = Double.parseDouble(values[0]) / Double.parseDouble(values[1]);

            } else if (pattern.contains("+")) {
                values = pattern.split("[+]");
                result = Double.parseDouble(values[0]) + Double.parseDouble(values[1]);

            } else {
                values = pattern.split("[-]");
                result = Double.parseDouble(values[0]) - Double.parseDouble(values[1]);

            }
        } else {
            if (pattern.contains("*")) {
                values = pattern.split("[*]");
                result = Double.parseDouble(values[0]) * Double.parseDouble(values[1]);
            } else if (pattern.contains("/")) {
                values = pattern.split("[/]");
                result = Double.parseDouble(values[0]) / Double.parseDouble(values[1]);

            } else if (pattern.contains("+")) {
                values = pattern.split("[+]");
                result = Double.parseDouble(values[0]) + Double.parseDouble(values[1]);

            } else if (pattern.contains("--")) {
                values = pattern.split("(--)");
                result = Double.parseDouble(values[0]) - Double.parseDouble("-" + values[1]);

            } else if (pattern.matches("([-]\\d+)([-])(\\d+)")) {
                values = pattern.substring(1).split("-");
                result = Double.parseDouble("-" + values[0]) - Double.parseDouble(values[1]);
            } else if (pattern.matches("(\\d+)([-])(\\d+)")) {
                values = pattern.split("-");
                result = Double.parseDouble(values[0]) - Double.parseDouble(values[1]);
            }

        }


        return result;
    }

    /**
     * @return returns the result of the + and - operation remaining
     */
    public static String solve(String finalOperation) {

        Pattern pattern = Pattern.compile(MY_LOWER_POSITIVE_PRECEDENCE_PATTERN);

        Matcher matcher = pattern.matcher(finalOperation);
        Log.e(TAG, "The solve input : " + finalOperation);


        /**
         * This object helps in creating a format of 2+4-5. Where the
         * operations of multiplication and addition are already calculated
         */

        StringBuffer sb = new StringBuffer();


        while (matcher.find()) {
            Log.e(TAG, "The match lower: " + matcher.group());
            matcher.appendReplacement(sb, "" + calculatePrecedence(matcher.group()));


        }

        matcher.appendTail(sb);
        Log.e(TAG, "The returning operation: " + sb.toString());

        return sb.toString();
    }
}
