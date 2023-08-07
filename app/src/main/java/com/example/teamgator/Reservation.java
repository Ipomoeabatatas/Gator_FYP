package com.example.teamgator;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Reservation extends RobotActivity implements RobotLifecycleCallbacks {

    private boolean privacyAccepted = false;
    private int selectedHour;
    private int selectedMinute;
    private int selectedDate;
    private EditText contactNumberField;
    private QiContext qiContext;
    private boolean promptSaid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        QiSDK.register(this, this);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setEnabled(false);

        CheckBox privacyCheckbox = findViewById(R.id.privacyCheckbox);
        privacyCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                privacyAccepted = isChecked;
                submitButton.setEnabled(privacyAccepted);

                if (isChecked) {
                    showPrivacyPolicyDialog();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitButtonClicked(v); // Call the onSubmitButtonClicked method
            }
        });

        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle exit button click
                finish();
            }
        });

        EditText timeField = findViewById(R.id.timeField);
        timeField.setOnClickListener(v -> {
            // Display time picker dialog
            showTimePickerDialog();
        });

        EditText dateField = findViewById(R.id.dateField);
        dateField.setOnClickListener(v -> {
            // Display date picker dialog
            showDatePickerDialog();
        });

        EditText remarksField = findViewById(R.id.remarksField);
        remarksField.setOnClickListener(v -> {
            speak("Please enter any remarks or additional information for your reservation and Number of guests dining in.");
        });

        contactNumberField = findViewById(R.id.contactNumberField);
    }

    // Method to show the privacy policy dialog and speak it out
    private void showPrivacyPolicyDialog() {
        String originalPrivacyPolicy = "We understand and prioritize the importance of your privacy. " +
                "Rest assured that any customer details you provide will be treated with utmost care and confidentiality. " +
                "We have implemented robust security measures to ensure that your personal information remains secure and protected. " +
                "Your data will be used solely for the purpose of enhancing our services and will never be shared with any third parties without your explicit consent. " +
                "Your trust is essential to us, and we are dedicated to maintaining the privacy and security of your information. " +
                "By checking this box, you agree to our privacy policy.";

        // Simplified version of the privacy policy for Pepper to say
        String simplifiedPrivacyPolicy = "We prioritize your privacy and ensure the security of your information. " +
                "Customer details are treated with care and used solely to enhance our services. " +
                "No data is shared with third parties without your consent.";

        AlertDialog.Builder builder = new AlertDialog.Builder(Reservation.this);
        builder.setTitle("Privacy Policy");
        builder.setMessage(originalPrivacyPolicy);

        builder.setPositiveButton("OK", (dialog, which) -> {
            // Handle the positive button click if needed
        });

        builder.setOnDismissListener(dialog -> {
            if (privacyAccepted && !promptSaid) {
                // Speak the simplified privacy policy only if the checkbox is checked and it hasn't been spoken before
                speak(simplifiedPrivacyPolicy);
                promptSaid = true;

                // Also, speak the thank you message
                speak("Thank you for accepting our privacy policy. Your reservation can now be submitted.");
            }
        });

        builder.show();
    }



    private void showTimePickerDialog() {
        NumberPicker timePicker = new NumberPicker(this);
        timePicker.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f)); // Adjust weight
        timePicker.setMinValue(0);
        timePicker.setMaxValue(23); // 24-hour format
        timePicker.setValue(selectedHour);

        NumberPicker minutePicker = new NumberPicker(this);
        minutePicker.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f)); // Adjust weight
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(selectedMinute);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(timePicker);
        linearLayout.addView(minutePicker);

        new AlertDialog.Builder(this)
                .setTitle("Select Time")
                .setView(linearLayout)
                .setPositiveButton("OK", (dialog, which) -> {
                    selectedHour = timePicker.getValue();
                    selectedMinute = minutePicker.getValue();

                    // Update the time field with the selected time
                    updateTimeField(selectedHour, selectedMinute);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }



    // Method to update the time field with the selected time
    private void updateTimeField(int hour, int minute) {
        EditText timeField = findViewById(R.id.timeField);
        String formattedTime = String.format("%02d:%02d", hour, minute);
        timeField.setText(formattedTime);
    }

    // Method to show the date picker dialog
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Reservation.this,
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate = dayOfMonth;

                    // Update the date field with the selected date
                    updateDateField(year1, month1, dayOfMonth);
                }, year, month, day);

        // Set the minimum selectable date to the current date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.show();
    }


    // Method to update the date field with the selected date
    private void updateDateField(int year, int month, int dayOfMonth) {
        EditText dateField = findViewById(R.id.dateField);
        String formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
        dateField.setText(formattedDate);
    }
    // Method to perform speech synthesis
    private void speak(String textToSpeak) {
        new SpeechSynthesisTask().execute(textToSpeak);
    }

    public void onSubmitButtonClicked(View view) {
        // Get form field values (name, email, contact number, remarks, time, date)
        String name = ((EditText) findViewById(R.id.nameField)).getText().toString().trim();
        String email = ((EditText) findViewById(R.id.emailField)).getText().toString().trim();
        String contactNumber = contactNumberField.getText().toString().trim();
        String remarks = ((EditText) findViewById(R.id.remarksField)).getText().toString().trim();
        String time = ((EditText) findViewById(R.id.timeField)).getText().toString();
        String date = ((EditText) findViewById(R.id.dateField)).getText().toString();
        String remark = ((EditText) findViewById(R.id.remarksField)).getText().toString();


        // Validate name
        if (name.isEmpty()) {
            ((EditText) findViewById(R.id.nameField)).setError("Name is required");
            speak("Oh no, you need to enter your name");
            ((EditText) findViewById(R.id.nameField)).requestFocus();
            return;
        }

        // Validate contact number
        if (contactNumber.isEmpty()) {
            contactNumberField.setError("Contact number is required");
            speak("Oh no, you have to enter your contact number");
            contactNumberField.requestFocus();
            return;
        }

        if (contactNumber.length() != 8) {
            contactNumberField.setError("Contact number should be 8 digits");
            speak("Contact number should be 8 digits");
            contactNumberField.requestFocus();
            return;
        }

        // Validate email
        if (email.isEmpty()) {
            ((EditText) findViewById(R.id.emailField)).setError("Email is required");
            speak("Oh no, please enter your email");
            ((EditText) findViewById(R.id.emailField)).requestFocus();
            return;
        }

        // Validate date
        if (date.isEmpty()) {
            ((EditText) findViewById(R.id.dateField)).setError("Date is required");
            speak("Oh no, please enter the date");
            ((EditText) findViewById(R.id.dateField)).requestFocus();
            return;
        }

        // Validate time
        if (time.isEmpty()) {
            ((EditText) findViewById(R.id.timeField)).setError("Time is required");
            speak("Oh no, please enter the time");
            ((EditText) findViewById(R.id.timeField)).requestFocus();
            return;
        }

        // Validate time
        if (remark.isEmpty()) {
            ((EditText) findViewById(R.id.remarksField)).setError("Please enter the No.of guests");
            speak("Oh no, please enter the number of guests");
            ((EditText) findViewById(R.id.timeField)).requestFocus();
            return;
        }

        String informationToSpeak = "Your reservation has been submitted. Thank you for booking with us, " +
                name + ". We will contact you at for further details.";


        // Run the speech synthesis in the background thread using AsyncTask
        new SpeechSynthesisTask().execute(informationToSpeak);

        Toast.makeText(Reservation.this, "Reservation submitted", Toast.LENGTH_SHORT).show();

        // Reset the form fields and attributes
        resetForm();

        // Create a new BookingData object with the user's input
        BookingData bookingData = new BookingData(name, email, date, time, contactNumber, remarks);

        // Get a reference to the Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Bookings");

        // Generate a new unique key for this booking
        String bookingKey = databaseReference.push().getKey();

        // Upload the booking details to Firebase under the generated key
        databaseReference.child(bookingKey).setValue(bookingData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // The data has been successfully uploaded
                        // Perform any additional actions if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred during the upload
                    }
                });
    }

    private void resetForm() {
        // Clear form fields
        EditText nameField = findViewById(R.id.nameField);
        EditText emailField = findViewById(R.id.emailField);
        EditText contactNumberField = findViewById(R.id.contactNumberField);
        EditText remarksField = findViewById(R.id.remarksField);
        EditText timeField = findViewById(R.id.timeField);
        EditText dateField = findViewById(R.id.dateField);

        nameField.setText("");
        emailField.setText("");
        contactNumberField.setText("");
        remarksField.setText("");
        timeField.setText("");
        dateField.setText("");

        // Reset attributes
        privacyAccepted = false;
        selectedHour = 0;
        selectedMinute = 0;
        selectedDate = 0;

        // Reset privacy policy checkbox
        CheckBox privacyCheckbox = findViewById(R.id.privacyCheckbox);
        privacyCheckbox.setChecked(false);

        // Reset promptSaid for privacy policy
        promptSaid = false;
    }


    // AsyncTask for background speech synthesis
    private class SpeechSynthesisTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String informationToSpeak = params[0];
            sayInformation(informationToSpeak);
            return null;
        }
    }

    private void sayInformation(String informationToSpeak) {
        if (qiContext != null) {
            try {
                Say say = SayBuilder.with(qiContext)
                        .withText(informationToSpeak)
                        .build();

                say.run();
            } catch (Exception e) {
                // Handle any potential exceptions here
                Log.e("Reservation", "Speech synthesis error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        this.qiContext = qiContext;
        // Say something when the app starts
        String welcomeMessage = "Welcome to the reservation section. Please enter your details accordingly.";
        sayInformation(welcomeMessage);
    }


    @Override
    public void onRobotFocusLost() {
        this.qiContext = null;
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // Handle robot focus refusal if needed.
    }

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }
}
