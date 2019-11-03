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
     * This pattern matches *. or .*
     */
    public static final String BAD_EXPRESSION = "([.])([+[-]*/])|([+[-]*/]([.]))";
    /**
     * This operator is used to match 7., 4., 0. type of numbers
     */
    public static final String DOT_NUMBER_LEFT = "(\\d+[.]\\d+)|(\\d+[.])|([.]\\d+|(\\d+))";
    /**
     * This variable is the regex for a negative value. The format is
     * -2.0. I used this format because the result out of this class is always a double.
     */
    public static final String NEGATIVE_RESULT_FORMAT = "([-])(\\d+)([.])(\\d+)";


    /**
     * The pattern is used to match operations for * and / operations first.
     */
    public static final String MY_HIGHER_PRECEDENCE_PATTERN = "(\\d+[.]\\d+|\\d+)([*|/])(\\d+[.]\\d+|\\d+)";

    /**
     * The pattern is used to match operations for positive operation.eg: 7+5-7 or 7.0+5-7
     * This pattern matches 7+5 or matches 7.0+5 or 5-7
     */
    public static final String MY_LOWER_POSITIVE_PRECEDENCE_PATTERN = "(\\d+[.]\\d+|\\d+)([+|-])(\\d+[.]\\d+|\\d+)";

    /**
     * The pattern is used to match operations for negative operation.eg: -7+-5 or -7.0+ -5 or -7.0+-8.0
     */
    public static final String MY_LOWER_NEGATIVE_PRECEDENCE_PATTERN = "(-\\d+[.]\\d+|-\\d+)([+|-])(-\\d+[.]\\d+|-\\d+)";
    /**
     * The pattern is used to match operations for negative operation.eg: -7+5 or -7.0+5 or -7+8.0
     */
    public static final String MY_LOWER_NEGATIVE_PRECEDENCE_PATTERN_TWO = "(-\\d+[.]\\d+|-\\d+)([+|-])(\\d+[.]\\d+|\\d+)";

    private CalculatorOperation() {
    }

    /**
     * This method is used to deconstruct the operation that the user enters by looking at
     * * and / in order to deal with the higher precendence values first and store then
     *
     * @param userInput
     */
    public static String deconstructHPrecedence(String userInput) {
        String cleaned = cleanOperation(userInput);
        /**
         * This object helps in creating a format of 2+4-5. Where the
         * operations of multiplication and addition are already calculated
         */


        StringBuffer sb = new StringBuffer();

        if (!cleaned.matches(BAD_EXPRESSION)) {


            Pattern pattern = Pattern.compile(MY_HIGHER_PRECEDENCE_PATTERN);

            Matcher matcher = pattern.matcher(cleaned);


            matcher.find();
            matcher.appendReplacement(sb, "" + calculatePrecedence(matcher.group()));


            matcher.appendTail(sb);

            Log.e(TAG, "the end result: " + sb.toString());
        }


        return sb.toString();

    }


    private static double calculatePrecedence(String pattern) {
        double result = 0.0;
        String[] values;

        if (pattern.matches(MY_HIGHER_PRECEDENCE_PATTERN) || pattern.matches(MY_LOWER_POSITIVE_PRECEDENCE_PATTERN)) {
            if (pattern.contains("*")) {
                values = pattern.split("[*]");
                result = Double.parseDouble(values[0]) * Double.parseDouble(values[1]);
            } else if (pattern.contains("/")) {
                values = pattern.split("[/]");
                result = Double.parseDouble(values[0]) / Double.parseDouble(values[1]);

            } else if (pattern.contains("+")) {
                values = pattern.split("[+]");
                result = Double.parseDouble(values[0]) + Double.parseDouble(values[1]);

            } else if (pattern.contains("-")) {
                values = pattern.split("[-]");
                result = Double.parseDouble(values[0]) - Double.parseDouble(values[1]);

            }
        } else if (pattern.matches(MY_LOWER_NEGATIVE_PRECEDENCE_PATTERN)) {

            if (pattern.contains("--")) {
                values = pattern.split("(--)");
                result = Double.parseDouble(values[0]) - Double.parseDouble("-" + values[1]);
            } else {
                values = pattern.split("[+]");
                result = Double.parseDouble(values[0]) + Double.parseDouble(values[1]);
            }


        } else if (pattern.matches(MY_LOWER_NEGATIVE_PRECEDENCE_PATTERN_TWO)) {


            if (pattern.matches("(-\\d+[.]\\d+|-\\d+)([-])(\\d+[.]\\d+|\\d+)")) {
                values = pattern.substring(1).split("-");
                result = Double.parseDouble("-" + values[0]) - Double.parseDouble(values[1]);
            } else {
                values = pattern.split("[+]");
                result = Double.parseDouble(values[0]) + Double.parseDouble(values[1]);
            }

        } else {
            Log.e(TAG, "Nothing matched this pattern " + pattern);
        }


        return result;
    }

    /**
     * @return returns the result of the + and - operation remaining
     */
    public static String solve(String finalOperation, String format) {

        String cleaned = cleanOperation(finalOperation);
        /**
         * This object helps in creating a format of 2+4-5. Where the
         * operations of multiplication and addition are already calculated
         */

        StringBuffer sb = new StringBuffer();


        if (!cleaned.matches(BAD_EXPRESSION)) {
            Pattern pattern = Pattern.compile(format);

            Matcher matcher = pattern.matcher(cleanOperation(cleaned));


            matcher.find();
            matcher.appendReplacement(sb, "" + calculatePrecedence(matcher.group()));


            matcher.appendTail(sb);
        }

        return sb.toString();
    }

    /**
     * This method makes sure that the operation only has 7.0 but not 7. or .1
     * type of numbers
     *
     * @param operation is the operation that we get from the user
     * @return
     */
    private static String cleanOperation(String operation) {


        Pattern pattern = Pattern.compile(DOT_NUMBER_LEFT);
        Matcher matcher = pattern.matcher(operation);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            if (matcher.group().matches("(\\d+[.])")) {

                Log.e(TAG, " I found " + matcher.group());

                Log.e(TAG, "7. format matched in " + matcher.group());
                matcher.appendReplacement(sb, (new StringBuffer(matcher.group())).insert(matcher.group().length(), "0").toString());
                Log.e(TAG, "7. format matched out " + sb.toString());
            } else if (matcher.group().matches("([.]\\d+)")) {

                Log.e(TAG, " I found " + matcher.group());

                Log.e(TAG, "7. format matched in " + matcher.group());
                matcher.appendReplacement(sb, (new StringBuffer(matcher.group())).insert(0, "0").toString());
                Log.e(TAG, "7. format matched out " + sb.toString());
            }


        }

        operation = matcher.appendTail(sb).toString();

        //Check for bad expression like .* or *.
        pattern = Pattern.compile(BAD_EXPRESSION);
        matcher = pattern.matcher(operation);

        while (matcher.find()) {
            //This is bad expression
            operation = matcher.group();
        }


        Log.e(TAG, "Ending operation: " + operation);


        return operation;
    }
}
