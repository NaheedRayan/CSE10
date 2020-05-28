package com.example.cse10;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;


public class CGPA_Calculator extends AppCompatActivity {

    private Button calculate;
    private Button reset;

    private TextInputLayout grade1;
    private TextInputLayout grade2;
    private TextInputLayout grade3;
    private TextInputLayout grade4;
    private TextInputLayout grade5;
    private TextInputLayout grade6;
    private TextInputLayout grade7;
    private TextInputLayout grade8;
    private TextInputLayout grade9;
    private TextInputLayout grade10;

    private TextInputLayout credit1;
    private TextInputLayout credit2;
    private TextInputLayout credit3;
    private TextInputLayout credit4;
    private TextInputLayout credit5;
    private TextInputLayout credit6;
    private TextInputLayout credit7;
    private TextInputLayout credit8;
    private TextInputLayout credit9;
    private TextInputLayout credit10;


    private TextView textView_gpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgpa__calculator);

        /*for deleting status bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //hooks
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);

        textView_gpa = findViewById(R.id.textView_gpa);

        grade1 = findViewById(R.id.gradePoint1);
        grade2 = findViewById(R.id.gradePoint2);
        grade3 = findViewById(R.id.gradePoint3);
        grade4 = findViewById(R.id.gradePoint4);
        grade5 = findViewById(R.id.gradePoint5);
        grade6 = findViewById(R.id.gradePoint6);
        grade7 = findViewById(R.id.gradePoint7);
        grade8 = findViewById(R.id.gradePoint8);
        grade9 = findViewById(R.id.gradePoint9);
        grade10 = findViewById(R.id.gradePoint10);

        credit1 = findViewById(R.id.Credit1);
        credit2 = findViewById(R.id.Credit2);
        credit3 = findViewById(R.id.Credit3);
        credit4 = findViewById(R.id.Credit4);
        credit5 = findViewById(R.id.Credit5);
        credit6 = findViewById(R.id.Credit6);
        credit7 = findViewById(R.id.Credit7);
        credit8 = findViewById(R.id.Credit8);
        credit9 = findViewById(R.id.Credit9);
        credit10 = findViewById(R.id.Credit10);


        //for background animation
        LinearLayout linearLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();


        //when calculate is clicked
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //for scrolling back to top
                ScrollView scrollView = findViewById(R.id.scroll_view_cgpa);
                scrollView.fullScroll(ScrollView.FOCUS_UP);

                calculateResult();

            }
        });
        //when reset is clicked
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //for scrolling back to top
                ScrollView scrollView = findViewById(R.id.scroll_view_cgpa);
                scrollView.fullScroll(ScrollView.FOCUS_UP);

                textView_gpa.setText("0.0");
            }
        });
    }

    private void calculateResult() {

        double[] list = new double[10];
        list[0] = Float.parseFloat(grade1.getEditText().getText().toString()) * Float.parseFloat(credit1.getEditText().getText().toString());
        list[1] = Float.parseFloat(grade2.getEditText().getText().toString()) * Float.parseFloat(credit2.getEditText().getText().toString());
        list[2] = Float.parseFloat(grade3.getEditText().getText().toString()) * Float.parseFloat(credit3.getEditText().getText().toString());
        list[3] = Float.parseFloat(grade4.getEditText().getText().toString()) * Float.parseFloat(credit4.getEditText().getText().toString());
        list[4] = Float.parseFloat(grade5.getEditText().getText().toString()) * Float.parseFloat(credit5.getEditText().getText().toString());
        list[5] = Float.parseFloat(grade6.getEditText().getText().toString()) * Float.parseFloat(credit6.getEditText().getText().toString());
        list[6] = Float.parseFloat(grade7.getEditText().getText().toString()) * Float.parseFloat(credit7.getEditText().getText().toString());
        list[7] = Float.parseFloat(grade8.getEditText().getText().toString()) * Float.parseFloat(credit8.getEditText().getText().toString());
        list[8] = Float.parseFloat(grade9.getEditText().getText().toString()) * Float.parseFloat(credit9.getEditText().getText().toString());
        list[9] = Float.parseFloat(grade10.getEditText().getText().toString()) * Float.parseFloat(credit10.getEditText().getText().toString());

        double totalCredit = 0;
        totalCredit = Float.parseFloat(credit1.getEditText().getText().toString())
                + Float.parseFloat(credit2.getEditText().getText().toString())
                + Float.parseFloat(credit3.getEditText().getText().toString())
                + Float.parseFloat(credit4.getEditText().getText().toString())
                + Float.parseFloat(credit5.getEditText().getText().toString())
                + Float.parseFloat(credit6.getEditText().getText().toString())
                + Float.parseFloat(credit7.getEditText().getText().toString())
                + Float.parseFloat(credit8.getEditText().getText().toString())
                + Float.parseFloat(credit9.getEditText().getText().toString())
                + Float.parseFloat(credit10.getEditText().getText().toString());

        double sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += list[i];
        }

        float answer = (float) (sum/totalCredit) ;

        float formated_answer = Float.parseFloat(String.format("%.3f", answer));

        if(formated_answer > 4.0){
            Toast.makeText(this, "GPA cant be more than 4.0", Toast.LENGTH_SHORT).show();
            return;
        }


        //for value animation
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f , formated_answer);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textView_gpa.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();


        //textView_gpa.setText(String.format("%.3f", answer));
        //textView_gpa.setText(Float.toString(answer));

    }
}
