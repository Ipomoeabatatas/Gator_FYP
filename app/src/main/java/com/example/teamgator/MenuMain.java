package com.example.teamgator;

//import com.aldebaran.qi.sdk;

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
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.touch.Touch;
import com.aldebaran.qi.sdk.object.touch.TouchSensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class MenuMain extends RobotActivity implements RobotLifecycleCallbacks {
    private QiContext qiContext;

    private TouchSensor leftTouchSensor;

    private TouchSensor headTouchSensor;

    private TouchSensor rightTouchSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menumain);

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        button3.setBackgroundColor(ContextCompat.getColor(this, R.color.Wheat));

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the NormalMenu
                Intent intent = new Intent(MenuMain.this, NormalMenu.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the SpecialMenu
                Intent intent = new Intent(MenuMain.this, SpecialMenu.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create an Intent to navigate to the SpecialMenu
                Intent intent = new Intent(MenuMain.this, MainActivity.class);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create an Intent to navigate to the Tic Tac Toe
                Intent intent = new Intent(MenuMain.this, Game.class);
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
        // Load the script from the .txt file
        String script = loadScriptFromTextFile("MenuScript");

        // Create a Say action to make the robot speak
        Say say = SayBuilder.with(qiContext)
                .withText(script)
                .build();

        // Execute the Say action
        say.run();

        Touch touch = qiContext.getTouch();

        // Get the head touch sensor.
        headTouchSensor = touch.getSensor("Head/Touch");
        leftTouchSensor = touch.getSensor("LHand/Touch");
        rightTouchSensor = touch.getSensor("RHand/Touch");

        // Add onStateChanged listener.
        headTouchSensor.addOnStateChangedListener(touchState -> {
            if (touchState.getTouched()) {
                Intent intent = new Intent(MenuMain.this, Game.class);
                startActivity(intent);
            }
        });
        leftTouchSensor.addOnStateChangedListener(touchState -> {
            if (touchState.getTouched()) {
                Intent intent = new Intent(MenuMain.this, NormalMenu.class);
                startActivity(intent);
            }
        });
        rightTouchSensor.addOnStateChangedListener(touchState -> {
            if (touchState.getTouched()) {
                Intent intent = new Intent(MenuMain.this, SpecialMenu.class);
                startActivity(intent);
            }
        });

        // Create a PhraseSet with the texts "Hello" and "Hi"
        PhraseSet phraseSet = PhraseSetBuilder.with(qiContext)
                .withTexts("normal menu", "special menu", "exit", "game")
                .build();

        Listen listen = ListenBuilder.with(qiContext)
                .withPhraseSet(phraseSet)
                .build();

        ListenResult listenResult = listen.run();
        String recognizedPhrase = listenResult.getHeardPhrase().getText();

        if (recognizedPhrase.toLowerCase().equals("normal menu")) {
            int randomNumber = new Random().nextInt(3);
            if (randomNumber == 0) {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("Sure! My friend!")
                        .build();
                say1.run();
            } else if (randomNumber == 1) {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("No Problem!")
                        .build();
                say1.run();
            } else {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("Alright, let me handle it!")
                        .build();
                say1.run();
            }
            Intent intent = new Intent(MenuMain.this, NormalMenu.class);
            startActivity(intent);

        } else if (recognizedPhrase.toLowerCase().equals("game")) {
            int randomNumber = new Random().nextInt(3);
            if (randomNumber == 0) {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("Sure! My friend!")
                        .build();
                say1.run();
            } else if (randomNumber == 1) {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("No Problem!")
                        .build();
                say1.run();
            } else {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("Alright, let me handle it!")
                        .build();
                say1.run();
            }
            Intent intent = new Intent(MenuMain.this, Game.class);
            startActivity(intent);

        } else if (recognizedPhrase.toLowerCase().equals("special menu")) {
            int randomNumber = new Random().nextInt(3);
            if (randomNumber == 0) {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("Sure! My friend!")
                        .build();
                say1.run();
            } else if (randomNumber == 1) {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("No Problem!")
                        .build();
                say1.run();
            } else {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("Alright, let me handle it!")
                        .build();
                say1.run();
            }
            Intent intent = new Intent(MenuMain.this, SpecialMenu.class);
            startActivity(intent);

        } else if (recognizedPhrase.toLowerCase().equals("exit")) {
            int randomNumber = new Random().nextInt(3);
            if (randomNumber == 0) {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("Sure! My friend!")
                        .build();
                say1.run();
            } else if (randomNumber == 1) {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("No Problem!")
                        .build();
                say1.run();
            } else {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("Alright, let me handle it!")
                        .build();
                say1.run();
            }
            Intent intent = new Intent(MenuMain.this, MainActivity.class);
            startActivity(intent);

        } else {
            int randomNumber = new Random().nextInt(3);
            if (randomNumber == 0) {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("Please say again!")
                        .build();
                say1.run();
            } else if (randomNumber == 1) {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("I cant understand what you want to say!")
                        .build();
                say1.run();
            } else {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("I have not learned this before!")
                        .build();
                say1.run();
            }
        }
    }
    private String loadScriptFromTextFile(String filename) {
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
    @Override
    public void onRobotFocusLost() {
        // The robot focus is lost.
    }
    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }
}

