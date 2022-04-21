package com.example.calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class PopupWelcome {

    //private ProgressBar progressBar;
    //private Button button;
    private AlertDialog dialog;

    private TextInputEditText textInputEditTextLastName, textInputEditTextFirstName;
    private TextInputLayout textInputLayoutLastName, textInputLayoutFirstName;

    private Context context;

    private CardView cardView;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;
    private TextView textView;

    PopupWelcome(Context Context, View view){
        //button = view.findViewById(R.id.validate);
        //progressBar = view.findViewById(R.id.progress_bar_check_snack_test);
        context = Context;
        textInputLayoutLastName = view.findViewById(R.id.textInputLayoutUserLastName);
        textInputEditTextLastName = view.findViewById(R.id.textInputEditTextUserLastName);
        textInputLayoutFirstName = view.findViewById(R.id.textInputLayoutUserFirstName);
        textInputEditTextFirstName = view.findViewById(R.id.textInputEditTextUserFirstName);

        cardView = view.findViewById(R.id.cardView_button_save);
        constraintLayout = view.findViewById(R.id.layout_button_save);
        progressBar = view.findViewById(R.id.progress_bar_button);
        textView = view.findViewById(R.id.textView_button_save);
    }


    void showPopup(Context context, View view){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(view);
        dialogBuilder.setCancelable(false);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    void closePopup(){
        dialog.dismiss();
    }

    String[] buttonActivated(){
        progressBar.setVisibility(View.VISIBLE);
        //button.setText("Check...");
        textView.setText("Check...");
        String[] names = {null, null};
        names[0] = Objects.requireNonNull(textInputLayoutLastName.getEditText()).getText().toString().trim();
        names[1] = Objects.requireNonNull(textInputLayoutFirstName.getEditText()).getText().toString().trim();
        return names;
    }

    @SuppressLint("ResourceAsColor")
    void errorPopup(Context context){
        textInputLayoutLastName.setError(context.getText(R.string.message_lastname_incorrect));
        textInputLayoutFirstName.setError(context.getText(R.string.message_firstname_incorrect));
        textInputEditTextLastName.setTextColor(ColorStateList.valueOf(context.getColor(R.color.red)));
        textInputEditTextFirstName.setTextColor(ColorStateList.valueOf(context.getColor(R.color.red)));
        textInputLayoutLastName.setErrorIconDrawable(R.drawable.ic_baseline_error_24);
        textInputLayoutLastName.setErrorIconTintList(ColorStateList.valueOf(context.getColor(R.color.red)));
        textInputLayoutFirstName.setErrorIconDrawable(R.drawable.ic_baseline_error_24);
        textInputLayoutFirstName.setErrorIconTintList(ColorStateList.valueOf(context.getColor(R.color.red)));
        textInputLayoutFirstName.setErrorTextColor(ColorStateList.valueOf(context.getColor(R.color.red)));
        textInputLayoutLastName.setErrorTextColor(ColorStateList.valueOf(context.getColor(R.color.red)));

        progressBar.setVisibility(View.INVISIBLE);
        //button.setText("Save");
        textView.setText("Save");
    }

    void textLastNameChanged(){
        textInputLayoutLastName.setError(context.getText(R.string.message_require));
        textInputEditTextLastName.setTextColor(ColorStateList.valueOf(context.getColor(R.color.blue)));
        textInputLayoutLastName.setEndIconTintList(ColorStateList.valueOf(context.getColor(R.color.blue)));
        textInputLayoutLastName.setErrorIconDrawable(R.drawable.ic_baseline_cancel_24);
        textInputLayoutLastName.setErrorIconTintList(ColorStateList.valueOf(context.getColor(R.color.blue)));
        textInputLayoutLastName.setErrorIconOnClickListener(view1 -> textInputEditTextLastName.getEditableText().clear());
    }

    void textFirstNameChanged(){
        textInputLayoutFirstName.setError(context.getText(R.string.message_require));
        textInputEditTextFirstName.setTextColor(ColorStateList.valueOf(context.getColor(R.color.blue)));
        textInputLayoutFirstName.setEndIconTintList(ColorStateList.valueOf(context.getColor(R.color.blue)));
        textInputLayoutFirstName.setErrorIconDrawable(R.drawable.ic_baseline_cancel_24);
        textInputLayoutFirstName.setErrorIconTintList(ColorStateList.valueOf(context.getColor(R.color.blue)));
        textInputLayoutFirstName.setErrorIconOnClickListener(view12 -> textInputEditTextFirstName.getEditableText().clear());
    }

    void buttonFinished(){
        //button.setBackground(R.drawable.button_green_round);
        //button.setBackgroundColor(R.drawable.button_green_round);
        progressBar.setVisibility(View.INVISIBLE);
        //button.setText("Done");
        textView.setText("Done");
        //wait 500 ms and close
    }

}
