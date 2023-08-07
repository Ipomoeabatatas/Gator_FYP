package com.example.teamgator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.yuganshtyagi.smileyrating.SmileyRatingView;

public class Survey extends RobotActivity implements RobotLifecycleCallbacks {


    private RatingBar question1Rating;
    private RatingData ratingData;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private boolean promptSaid = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        QiSDK.register(this, this);

        SmileyRatingView smileyRatingView = findViewById(R.id.smiley_view);
        question1Rating = findViewById(R.id.question1Rating);
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottie_main);
        LottieAnimationView lottieAnimationView2 = findViewById(R.id.lottie_main2);
        // Get the Firebase database reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Question1Rating");

        ratingData = new RatingData();


        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to move to the next page
                moveToNextPage();
                float rating = question1Rating.getRating();
                // Call the method to save the rating to Firebase
                saveRatingToFirebase("question1Rating", rating);

            }
        });

        lottieAnimationView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to move to the next page
                moveToBackPage();

            }
        });

        // Smiley rating bar method
        question1Rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                smileyRatingView.setSmiley(rating);

            }
        });

    }
    private void saveRatingToFirebase(String questionKey, float rating) {
        // Create a new RatingData object and set the rating for the corresponding question
        RatingData ratingData = new RatingData();
        if ("question1Rating".equals(questionKey)) {
            ratingData.setQuestion1Rating(rating);
        }
        // Add more conditions for other questions as needed
        // Push the RatingData to Firebase
        databaseReference.push().setValue(ratingData);
    }


    private void moveToNextPage() {
        // Create an intent to start the next activity
        Intent intent = new Intent(this, ReviewActivity.class);

        // You can add any extras to the intent if needed
        // intent.putExtra("key", value);

        // Start the activity
        startActivity(intent);
    }

    private void moveToBackPage() {
        // Create an intent to start the next activity
        Intent intent = new Intent(this, MainActivity.class);

        // You can add any extras to the intent if needed
        // intent.putExtra("key", value);

        // Start the activity
        startActivity(intent);
    }


    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        if (!promptSaid) {
            promptSaid = true;
            sayPrompt(qiContext);
            startListening(qiContext);
        } else {
            // If prompt has already been said, start listening for user response
            startListening(qiContext);

        }
    }
    private void startListening(QiContext qiContext) {
        PhraseSet phraseSet = PhraseSetBuilder.with(qiContext)
                .withTexts("one star", "two star", "three star", "four star", "next","previous")
                .build();

        Listen listen = ListenBuilder.with(qiContext)
                .withPhraseSet(phraseSet)
                .build();
        ListenResult listenResult = listen.run();


        String recognizedPhrase = listenResult.getHeardPhrase().getText();
        if (recognizedPhrase.equalsIgnoreCase("one star")) {
            // Perform actions or make the robot say something in response to "one star"
            question1Rating.setRating(1);
            Say say1 = SayBuilder.with(qiContext)
                    .withText("We apologize for not meeting your expectations. We'll work to improve")
                    .build();
            say1.async().run().andThenConsume(ignore -> startListening(qiContext));
        } else if (recognizedPhrase.toLowerCase().contains("two star")) {
            // Set rating to 2 stars
            Say say2 = SayBuilder.with(qiContext)
                    .withText("We're sorry for any inconvenience. Please tell us how we can improve")
                    .build();
            say2.async().run().andThenConsume(ignore -> startListening(qiContext));
            question1Rating.setRating(2);
        } else if (recognizedPhrase.toLowerCase().contains("three star")) {
            // Set rating to 3 stars
            Say say2 = SayBuilder.with(qiContext)
                    .withText("We're glad you enjoyed your experience!")
                    .build();
            say2.async().run().andThenConsume(ignore -> startListening(qiContext));
            question1Rating.setRating(3);
        } else if (recognizedPhrase.toLowerCase().contains("four star")) {
            // Set rating to 4 stars
            Say say2 = SayBuilder.with(qiContext)
                    .withText("Thank you for your positive feedback!")
                    .build();
            say2.async().run().andThenConsume(ignore -> startListening(qiContext));
            question1Rating.setRating(4);
        }  else if (recognizedPhrase.toLowerCase().contains("previous")) {
            Say say2 = SayBuilder.with(qiContext)
                    .withText("Lets move back to the main menu!")
                    .build();
            say2.async().run().andThenConsume(ignore -> {
            });
            Intent intent = new Intent(Survey.this, MainActivity.class);
            startActivity(intent);
        } else if (recognizedPhrase.equalsIgnoreCase("next")) {
            // Set rating to 2 stars
            Say say2 = SayBuilder.with(qiContext)
                    .withText("Lets move on to the next question!")
                    .build();
            say2.async().run().andThenConsume(ignore -> {
                // After the robot speaks, start the next activity
                Intent intent = new Intent(Survey.this, ReviewActivity.class);
                startActivity(intent);
                float rating = question1Rating.getRating();
                saveRatingToFirebase("question1Rating", rating);
            });
        }
    }

    private void sayPrompt(QiContext qiContext) {
        Say say = SayBuilder.with(qiContext)
                .withText("Hi there, How would you rate the taste of the food?")
                .build();
        say.run();
    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        QiSDK.unregister(this);
    }

}
