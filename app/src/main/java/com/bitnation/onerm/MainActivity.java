package com.bitnation.onerm;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Button mIncrementWeightButton;
    private Button mDecrementWeightButton;
    private SeekBar mRepsSeekBar;
    private EditText mWeightText;
    private TextView mOneRepMax;
    private TableLayout mPctTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mIncrementWeightButton = (Button) findViewById(R.id.inc_weight_button);
        mDecrementWeightButton = (Button) findViewById(R.id.dec_weight_button);
        mRepsSeekBar = (SeekBar) findViewById(R.id.repCountSeekBar);
        mWeightText = (EditText) findViewById(R.id.weight_rep_text);
        mOneRepMax = (TextView) findViewById(R.id.one_rep_val);
        mPctTable = (TableLayout) findViewById(R.id.pct_table);

        mWeightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSeq, int start, int before, int count) {
                //Log.d("onerm", "onTextChanged fired with value: " + charSeq);
                float weightVal;
                int max = 0;
                if (charSeq.length() > 0) {
                    weightVal = Float.valueOf(String.valueOf(charSeq));
                    max = computeMax(weightVal, mRepsSeekBar.getProgress() + 1);
                } else {
                    weightVal = 0;
                }
                mOneRepMax.setText(String.valueOf(max));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mIncrementWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strWeight = mWeightText.getText().toString();
                float weightVal = strWeight.isEmpty() ? 0 : Float.valueOf(strWeight);
                if (weightVal < 0) {
                    weightVal = 0;
                }
                weightVal += 2.5;
                mWeightText.setText(String.valueOf(weightVal));
                int max = computeMax(weightVal, mRepsSeekBar.getProgress() + 1);
                mOneRepMax.setText(String.valueOf(max));
            }
        });

        mDecrementWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strWeight = mWeightText.getText().toString();
                float weightVal = strWeight.isEmpty() ? 0 : Float.valueOf(strWeight);
                if (weightVal <= 2.5) {
                    weightVal = 0;
                } else {
                    weightVal -= 2.5;
                }
                mWeightText.setText(String.valueOf(weightVal));
                int max = computeMax(weightVal, mRepsSeekBar.getProgress() + 1);
                mOneRepMax.setText(String.valueOf(max));
            }
        });

        mRepsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView numRepsText = (TextView) findViewById(R.id.num_reps_text);
                if (0 == progress) {
                    numRepsText.setText((progress + 1) + " rep");
                } else {
                    numRepsText.setText((progress + 1) + " reps");
                }
                String strWeight = mWeightText.getText().toString();
                float weightVal = strWeight.isEmpty() ? 0 : Float.valueOf(strWeight);
                int max = computeMax(weightVal, (progress + 1));
                mOneRepMax.setText(String.valueOf(max) + " lb");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private int computeMax(float weightVal, float reps) {
        //Log.d("onerm", "enter computeMax with weight: " + weightVal + ", reps: " + reps);
        int max = (int) weightVal;

        if (weightVal > 0 && reps > 1) {
            max = Math.round(weightVal * (1 + (reps / 30)));
        }
        mPctTable.removeAllViewsInLayout();

        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);

        TableRow.LayoutParams tableRowLayoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);


        for (int i = 100; i >= 5; i -= 5) {
            float r = ((float) i) / 100;
            int pctMax = Math.round(max * r);
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(tableLayoutParams);

            TextView pctText = new TextView(this);
            pctText.setText(String.valueOf(i) + "%");
            pctText.setLayoutParams(tableRowLayoutParams);
            pctText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            pctText.setTextSize(getResources().getDimension(R.dimen.result_size));
            tr.addView(pctText);

            TextView numRepsText = new TextView(this);
            numRepsText.setText(String.valueOf(pctMax) + " lbs");
            numRepsText.setLayoutParams(tableRowLayoutParams);
            numRepsText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            numRepsText.setTextSize(getResources().getDimension(R.dimen.result_size));
            tr.addView(numRepsText);

            mPctTable.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }
        //Log.d("onerm", "new max is: " + max);
        /*Toast toast = Toast.makeText(getApplicationContext(), "max updated: " + max, Toast.LENGTH_SHORT);
        toast.show();*/
        return max;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
