package com.example.teamgator;

//import com.aldebaran.qi.sdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class SpecialMenu extends RobotActivity implements RobotLifecycleCallbacks {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specialmenu);

        Button Button1 = findViewById(R.id.ExitButton);
        Button Button2 = findViewById(R.id.ExitButtonMain);

        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecialMenu.this, MenuMain.class);
                startActivity(intent);
            }
        });

        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecialMenu.this, MainActivity.class);
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

        String script = loadScriptFromTextFile("SpecialScript");

        // Create a Say action to make the robot speak
        Say say = SayBuilder.with(qiContext)
                .withText(script)
                .build();

        // Execute the Say action
        say.run();
        // The robot focus is gained.
        PhraseSet phraseSet = PhraseSetBuilder.with(qiContext)
                .withTexts("exit", "homepage", "what is on the menu")
                .build();

        Listen listen = ListenBuilder.with(qiContext)
                .withPhraseSet(phraseSet)
                .build();

        ListenResult listenResult = listen.run();
        String recognizedPhrase = listenResult.getHeardPhrase().getText();

        if (recognizedPhrase.toLowerCase().equals("exit")) {
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
            Intent intent = new Intent(SpecialMenu.this, MenuMain.class);
            startActivity(intent);

        } else if (recognizedPhrase.toLowerCase().equals("homepage")) {
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
            Intent intent = new Intent(SpecialMenu.this, MainActivity.class);
            startActivity(intent);
        } else if (recognizedPhrase.toLowerCase().equals("what is on the menu")) {
            int randomNumber = new Random().nextInt(3);
            if (randomNumber == 0) {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("Welcome to our vegetarian paradise! Our menu is a delight for herbivores, featuring flavorful plant-based dishes that satisfy every craving!")
                        .build();
                say1.run();
            } else if (randomNumber == 1) {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("Explore the art of vegetarian cuisine with our diverse menu! From hearty lentil soups to gourmet vegetable stir-fries, we've got it all!")
                        .build();
                say1.run();
            } else {
                Say say1 = SayBuilder.with(qiContext)
                        .withText("Our vegetarian menu brings you the best of nature's bounty! Savor the taste of fresh, locally sourced ingredients in every bite!")
                        .build();
                say1.run();
            }
        }
    }
    @Override
    public void onRobotFocusLost() {
        // The robot focus is lost.
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
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
}