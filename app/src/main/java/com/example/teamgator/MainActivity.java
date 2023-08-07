package com.example.teamgator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.humanawareness.HumanAwareness;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private HumanAwareness humanAwareness;
    private QiContext qiContext;
    private Animate animate;
    private Say say;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get references to your buttons
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);

        button1.setBackgroundColor(ContextCompat.getColor(this, R.color.PapayaWhip));
        button2.setBackgroundColor(ContextCompat.getColor(this, R.color.MistyRose));
        button3.setBackgroundColor(ContextCompat.getColor(this, R.color.PapayaWhip));
        button4.setBackgroundColor(ContextCompat.getColor(this, R.color.MistyRose));

        // Set an OnClickListener for button1
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the SecondActivity
                Intent intent = new Intent(MainActivity.this, MenuMain.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the SecondActivity
                Intent intent = new Intent(MainActivity.this, Reservation.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Survey.class);
                startActivity(intent);
            }
        });

        // QNA
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Faq.class);
                startActivity(intent);
            }
        });
        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

        this.qiContext = qiContext;
        // Get the Touch service from the QiContext.
        String script = loadScriptFromTextFile("Script");

        // Create a Say action to make the robot speak
        Say say = SayBuilder.with(qiContext)
                .withText(script)
                .build();

        // Execute the Say action
        say.run();

        PhraseSet phraseSet = PhraseSetBuilder.with(qiContext)
                .withTexts("menu", "reservation", "survey", "others")
                .build();

        Listen listen = ListenBuilder.with(qiContext)
                .withPhraseSet(phraseSet)
                .build();

        ListenResult listenResult = listen.run();
        String recognizedPhrase = listenResult.getHeardPhrase().getText();
        if (recognizedPhrase.toLowerCase().equals("menu")) {
            // Perform actions or make the robot say something in response to "Hello"
            // For example, say "Hi there!"
            Say say1 = SayBuilder.with(qiContext)
                    .withText("Sure")
                    .build();
            say1.run();
            Intent intent = new Intent(MainActivity.this, MenuMain.class);
            startActivity(intent);

        } else if (recognizedPhrase.toLowerCase().equals("reservation")) {
            // Perform actions or make the robot say something in response to "Hi"
            // For example, say "Hello!"
            Say say1 = SayBuilder.with(qiContext)
                    .withText("Sure")
                    .build();
            say1.run();
            Intent intent = new Intent(MainActivity.this, Reservation.class);
            startActivity(intent);
        }
        else if (recognizedPhrase.toLowerCase().equals("others")) {
//            // Perform actions or make the robot say something in response to "Hi"
//            // For example, say "Hello!"
            Say say1 = SayBuilder.with(qiContext)
                    .withText("No problem!")
                    .build();
            say1.run();
            Intent intent = new Intent(MainActivity.this, Faq.class);
            startActivity(intent);
        }
        else if (recognizedPhrase.toLowerCase().equals("survey")) {
            // Perform actions or make the robot say something in response to "Hi"
            // For example, say "Hello!"
            Say say1 = SayBuilder.with(qiContext)
                    .withText("No problem!")
                    .build();
            say1.run();
            Intent intent = new Intent(MainActivity.this, Survey.class);
            startActivity(intent);
        }
    };

    @Override
    public void onRobotFocusLost () {
    }
    @Override
    public void onRobotFocusRefused (String reason){
        // The robot focus is refused.
    }
    private String loadScriptFromTextFile (String filename){
        StringBuilder scriptBuilder = new StringBuilder();
        try {
            InputStream inputStream = getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                scriptBuilder.append(line);
                scriptBuilder.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scriptBuilder.toString();
    }
}