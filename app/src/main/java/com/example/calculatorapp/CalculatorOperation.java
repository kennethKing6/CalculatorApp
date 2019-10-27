package com.example.calculatorapp;

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
 * author Kouadio Kenneth.
 */
public class CalculatorOperation {


    /**
     * The pattern I use to get the operations with higher precedence, meaning * and /.
     * for example, 55*2 + 5 *2 - 6 * 2. This pattern will help me retrieve
     * 55*2, 5 *2 and 6 * 2 while leaving behind +-.
     */
    private static final String MY_HIGHER_PRECEDENCE_PATTERN = "(\\d+)([*|/])(\\d+)";
    /**
     * The pattern I use to get the operations with lower precedence, meaning + and -.
     * for example, 55+ 3 - 6 . This pattern will help me retrieve
     * get the final result of the operation
     */
    private static final String MY_LOWER_PRECEDENCE_PATTERN = "(\\d+)([+|-])(\\d+)";

    /**
     * it contains the final operation of + and - only .
     * I use it to do the final step of math aaddition and operation
     */
    private static String finalOperation;

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
        double result;
        String[] values;
        if (pattern.contains("*")) {
            values = pattern.split("[*]");
            result = Integer.parseInt(values[0]) * Integer.parseInt(values[1]);
        } else if (pattern.contains("/")) {
            values = pattern.split("[/]");
            result = Integer.parseInt(values[0]) / Integer.parseInt(values[1]);

        } else if (pattern.contains("+")) {
            values = pattern.split("[+]");
            result = Integer.parseInt(values[0]) + Integer.parseInt(values[1]);

        } else {
            values = pattern.split("[-]");
            result = Integer.parseInt(values[0]) - Integer.parseInt(values[1]);

        }
        return result;
    }

    /**
     * @return returns the result of the + and - operation remaining
     */
    public static String solve() {

        Pattern pattern = Pattern.compile(MY_LOWER_PRECEDENCE_PATTERN);

        Matcher matcher = pattern.matcher(finalOperation);

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
}
