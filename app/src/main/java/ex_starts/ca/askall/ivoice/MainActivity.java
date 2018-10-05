package ex_starts.ca.askall.ivoice;

import android.app.Activity;
import android.os.Bundle;
//import android.view.inputmethod.EditorInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import java.text.NumberFormat;

public class MainActivity extends Activity
implements OnEditorActionListener, OnClickListener {

    private EditText billAmountEditText;
    private TextView tipPercentTextView;
    private TextView tipTextView;
    private TextView totalTextView;
    private Button   percentUp;
    private Button   percentDown;
    private float tipPercent = .15f;
    private String billAmountString = "";

    //property for debugging - LogCat
    private static final String TAG = "TipCalculatorActivity";

    //define SharedPreferences object
    private  SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        billAmountEditText = (EditText)findViewById(R.id.etSubTotal);
        tipTextView = (TextView)findViewById(R.id.tvDiscountAmountNumber);
        totalTextView = (TextView)findViewById(R.id.tvTotalAmountNumber);
        tipPercentTextView = (TextView)findViewById(R.id.tvDiscount_percent_number);
        percentUp = (Button)findViewById(R.id.btnUp);
        percentDown = (Button)findViewById(R.id.btnDown);
        //set the listener
        billAmountEditText.setOnEditorActionListener(this);
        percentUp.setOnClickListener(this);
        percentDown.setOnClickListener(this);

        //get SharedPreferences Object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause(){
        //save instance variables
        Editor editor = savedValues.edit();
        editor.putString(("billAmountString"), billAmountString);
        editor.putFloat("tipPercent", tipPercent);
        editor.commit();

        super.onPause();

    }
    @Override
    public void onResume() {
        super.onResume();  //1. call the superclass

        //get the instance variables
        billAmountString = savedValues.getString("billAmountString", "");
        tipPercent = savedValues.getFloat("tipPercent", 0.15f);

        //sets the bill Amount on its widget
        billAmountEditText.setText(billAmountString);

        //calculate and display
        calculateAndDisplay();
    }
    //Implements the listener
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
            calculateAndDisplay();
        }
        return false;
    }

    public  void calculateAndDisplay(){
        //get the bill amount
        billAmountString = billAmountEditText.getText().toString();
        float billAmount;
        if(billAmountString.equals("")){
            billAmount = 0;
        }
        else{
            billAmount = Float.parseFloat(billAmountString);
        }

        //calculate tip and total

        float tipAmount = billAmount*tipPercent;
        float totalAmount = billAmount + tipAmount;

        //Debugging statements (LogCat) - import Log class
        Log.d(TAG, "Calculate& Display Method called.");
        Log.d(TAG, "Bill Amount: " + billAmount);
        Log.d(TAG, "total Amount: " + totalAmount);


        //display the results with formatting
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipTextView.setText(currency.format(tipAmount));
        totalTextView.setText(currency.format(totalAmount));

        NumberFormat percentage = NumberFormat.getPercentInstance();
        tipPercentTextView.setText(percentage.format(tipPercent));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUp:
                //float tipPercent = Float.parseFloat(tipPercentTextView.getText().toString());
                tipPercent += .01f;
                calculateAndDisplay();
                break;
            case R.id.btnDown:
                tipPercent -= .01f;
                calculateAndDisplay();
                break;
        }
    }
}
