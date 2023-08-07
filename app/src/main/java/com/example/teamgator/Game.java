package com.example.teamgator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.touch.TouchSensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Game extends RobotActivity implements RobotLifecycleCallbacks {

    Button bu1;
    Button bu2;
    Button bu3;
    Button bu4;
    Button bu5;
    Button bu6;
    Button bu7;
    Button bu8;
    Button bu9;
    ImageButton bu10;
    ImageButton bu11;
    ArrayList<Button> xox = new ArrayList<Button>();
    String Player1 = "X";
    String Player2 = "O";
    AppCompatImageButton image;

    private QiContext qiContext;
    private boolean gameEnded = false;
    private int drawCount = 0;
    private final int MAX_MOVES = 9;

    private TouchSensor headTouchSensor;
    private TouchSensor leftTouchSensor;
    private TouchSensor rightTouchSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        bu1 = findViewById(R.id.bu1);
        bu2 = findViewById(R.id.bu2);
        bu3 = findViewById(R.id.bu3);
        bu4 = findViewById(R.id.bu4);
        bu5 = findViewById(R.id.bu5);
        bu6 = findViewById(R.id.bu6);
        bu7 = findViewById(R.id.bu7);
        bu8 = findViewById(R.id.bu8);
        bu9 = findViewById(R.id.bu9);
        bu10 = findViewById(R.id.bu10);
        bu11 = findViewById(R.id.bu11);

        // Set initial button text color and background color
        bu1.setTextColor(Color.BLACK);
        bu2.setTextColor(Color.BLACK);
        bu3.setTextColor(Color.BLACK);
        bu4.setTextColor(Color.BLACK);
        bu5.setTextColor(Color.BLACK);
        bu6.setTextColor(Color.BLACK);
        bu7.setTextColor(Color.BLACK);
        bu8.setTextColor(Color.BLACK);
        bu9.setTextColor(Color.BLACK);

        bu1.setBackgroundColor(Color.WHITE);
        bu2.setBackgroundColor(Color.WHITE);
        bu3.setBackgroundColor(Color.WHITE);
        bu4.setBackgroundColor(Color.WHITE);
        bu5.setBackgroundColor(Color.WHITE);
        bu6.setBackgroundColor(Color.WHITE);
        bu7.setBackgroundColor(Color.WHITE);
        bu8.setBackgroundColor(Color.WHITE);
        bu9.setBackgroundColor(Color.WHITE);

        xox.add(0, bu1);
        xox.add(1, bu2);
        xox.add(2, bu3);
        xox.add(3, bu4);
        xox.add(4, bu5);
        xox.add(5, bu6);
        xox.add(6, bu7);
        xox.add(7, bu8);
        xox.add(8, bu9);

        bu10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the NormalMenu
                Intent intent = new Intent(Game.this, MenuMain.class);
                startActivity(intent);
            }
        });
        bu11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the SpecialMenu
                Intent intent = new Intent(Game.this, MainActivity.class);
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

        Say say1 = SayBuilder.with(qiContext)
                .withText("Hello")
                .build();

        // Execute the Say action
        say1.run();

        String script = loadScriptFromTextFile("Game");

        // Create a Say action to make the robot speak
        Say say = SayBuilder.with(qiContext)
                .withText(script)
                .build();

        // Execute the Say action
        say.run();

    }

    @Override
    public void onRobotFocusLost() {
        // The robot focus is lost.
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }

    public void buClick(View view) {

        Log.d("Game", "Button clicked!");

        Button clickedButton = (Button) view;
        if (!clickedButton.getText().toString().isEmpty()) {
            Toast.makeText(this, "This field is not empty, choose another.", Toast.LENGTH_SHORT).show();
            return;
        }

        clickedButton.setText(Player1);
        clickedButton.setBackgroundColor(Color.WHITE);
        xox.remove(clickedButton);
        drawCount++;

        if (checkWinCondition(Player1)) {
            gameEnded = true;
            restartGame();
            return;
        }

        if (checkWinCondition(Player2)) {
            gameEnded = true;
            restartGame();
            return;
        }

        if (drawCount == MAX_MOVES) {
            gameEnded = true;
            restartGame();
            return;
        }

        performAIPlayerMove();
    }

    private void performAIPlayerMove() {

        Button randomButton = xox.get(new Random().nextInt(xox.size()));
        randomButton.setText(Player2);
        randomButton.setBackgroundColor(Color.WHITE);
        xox.remove(randomButton);
        drawCount++;

        if (checkWinCondition(Player1)) {
            gameEnded = true;
            restartGame();
            return;
        } else if (checkWinCondition(Player2)) {
            gameEnded = true;
            restartGame();
            return;
        } else if (drawCount == MAX_MOVES) {
            gameEnded = true;
            restartGame();
            return;
        }
    }

    private boolean checkWinCondition(String player) {
        // Check horizontal lines
        if (bu1.getText().toString().equals(player) && bu2.getText().toString().equals(player) && bu3.getText().toString().equals(player))
            return true;
        if (bu4.getText().toString().equals(player) && bu5.getText().toString().equals(player) && bu6.getText().toString().equals(player))
            return true;
        if (bu7.getText().toString().equals(player) && bu8.getText().toString().equals(player) && bu9.getText().toString().equals(player))
            return true;
        // Check vertical lines
        if (bu1.getText().toString().equals(player) && bu4.getText().toString().equals(player) && bu7.getText().toString().equals(player))
            return true;
        if (bu2.getText().toString().equals(player) && bu5.getText().toString().equals(player) && bu8.getText().toString().equals(player))
            return true;
        if (bu3.getText().toString().equals(player) && bu6.getText().toString().equals(player) && bu9.getText().toString().equals(player))
            return true;
        // Check diagonal lines
        if (bu1.getText().toString().equals(player) && bu5.getText().toString().equals(player) && bu9.getText().toString().equals(player))
            return true;
        if (bu3.getText().toString().equals(player) && bu5.getText().toString().equals(player) && bu7.getText().toString().equals(player))
            return true;

        return false;
    }
    private void clearBoard() {
        bu1.setText("");
        bu2.setText("");
        bu3.setText("");
        bu4.setText("");
        bu5.setText("");
        bu6.setText("");
        bu7.setText("");
        bu8.setText("");
        bu9.setText("");

        bu1.setBackgroundColor(Color.WHITE);
        bu2.setBackgroundColor(Color.WHITE);
        bu3.setBackgroundColor(Color.WHITE);
        bu4.setBackgroundColor(Color.WHITE);
        bu5.setBackgroundColor(Color.WHITE);
        bu6.setBackgroundColor(Color.WHITE);
        bu7.setBackgroundColor(Color.WHITE);
        bu8.setBackgroundColor(Color.WHITE);
        bu9.setBackgroundColor(Color.WHITE);

        xox.clear();
        xox.add(0, bu1);
        xox.add(1, bu2);
        xox.add(2, bu3);
        xox.add(3, bu4);
        xox.add(4, bu5);
        xox.add(5, bu6);
        xox.add(6, bu7);
        xox.add(7, bu8);
        xox.add(8, bu9);


    }

    private void restartGame() {
        if (checkWinCondition(Player1)) {
            gameEnded = false;
            drawCount = 0;
            clearBoard();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Congratulations! You win! Do you want to restart?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();

            String message = "Congratulations! You win! Do you want to restart?";
            speak(message);
        } else if (checkWinCondition(Player2)) {
            gameEnded = false;
            drawCount = 0;
            clearBoard();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Pepper wins! Do you want to restart?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();

            String message = "Pepper wins! Do you want to restart?";
            speak(message);

        } else if (drawCount == MAX_MOVES) {
            gameEnded = false;
            drawCount = 0;
            clearBoard();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("It's a draw! Do you want to restart?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();

            String message = "It's a draw! Do you want to restart?";
            speak(message);
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

    private void say23(String text) {
        if (qiContext != null) {
            Say say = SayBuilder.with(qiContext)
                    .withText(text)
                    .build();

            say.run();
        }
    }

    private void speak(String textToSpeak) {
        new SpeechSynthesisTask().execute(textToSpeak);
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
                Log.e("Game", "Speech synthesis error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}