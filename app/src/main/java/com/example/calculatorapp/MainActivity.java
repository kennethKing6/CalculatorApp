package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    TextView workoutTextView;
    TextView resultTextView;
    private boolean hasCalculated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        workoutTextView = findViewById(R.id.work_out_text_view);
        resultTextView = findViewById(R.id.results_text_view);
        hasCalculated = false;
    }

    public void displayNumber(View view) {
        if (workoutTextView.getText().toString().charAt(0) == '0' || hasCalculated) {
            workoutTextView.setText(((TextView) view).getText().toString());
            hasCalculated = false;
        } else {

            workoutTextView.append(((TextView) view).getText().toString());

        }

    }

    public void displayMathSymbol(View view) {
        /*get the last char from the string and return the result if two symbols are used following each other*/

        String symbol = ((TextView) view).getText().toString();
        String workout = workoutTextView.getText().toString();

        char lastChar = workout.charAt(workout.length() - 1);
        Log.e(TAG, "last char " + lastChar);

        if (isSymbol(lastChar)) {

            StringBuilder sb = new StringBuilder(workout);


            resultTextView.setText(doTheMathOperation(sb.deleteCharAt(workout.length() - 1).toString()));
            Log.e(TAG, "the operation was made " + workoutTextView.getText().toString());
            //Delete everything from the workoutTextview and show the result in resultTextView
            workoutTextView.setText(sb.toString());

        } else {
            if (!workoutTextView.getText().toString().isEmpty()) {
                if (!hasCalculated)
                    workoutTextView.append(symbol);
                else {
                    workoutTextView.setText(getString(R.string.initial_value));
                    hasCalculated = false;
                }

            } else
                Toast.makeText(this, "\u0247", Toast.LENGTH_SHORT).show();

        }


    }

    /**
     * @param value is the last character
     * @return true if it is a mathematical symbol
     */
    private boolean isSymbol(char value) {
        boolean symbol = false;

        switch (value) {
            case '*':
                symbol = true;
                break;
            case '/':
                symbol = true;
                break;
            case '+':
                symbol = true;
                break;
            case '-':
                symbol = true;
                break;
        }
        return symbol;
    }

    private String doTheMathOperation(String userInput) {
        String operation = userInput;
        String result = null;
        while (operation.contains("*") || operation.contains("/")) {
            operation = CalculatorOperation.deconstructHPrecedence(operation);
        }

        while (operation.contains("+") || operation.contains("-")) {


            Log.e(TAG,"the input: " + operation);
            /**
             * break the loop if it is a negative value
             */
            if (operation.matches(CalculatorOperation.NEGATIVE_RESULT_FORMAT)) {

                break;

            }

            operation = CalculatorOperation.solve(operation);

        }
        result = operation;
        hasCalculated = true;
        return result;
    }

    public void equal(View view) {
        String workout = workoutTextView.getText().toString();

        char lastChar = workout.charAt(workout.length() - 1);
        if (!isSymbol(lastChar)) {
            DecimalFormat numberFormat = new DecimalFormat("#.####");
            resultTextView.setText(numberFormat.format(Double.parseDouble(doTheMathOperation(workout))));

        }
    }
}
