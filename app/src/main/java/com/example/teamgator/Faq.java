package com.example.teamgator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Say;

public class Faq extends RobotActivity implements RobotLifecycleCallbacks {

    private QiContext qiContext;
    private Say say;

    // Quiz answers
    private int score = 0;
    private final int totalQuestions = 5;
    private boolean isQuizSubmitted = false;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        QiSDK.register(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        QiSDK.unregister(this, this);
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        this.qiContext = qiContext;

        // Initialize TextToSpeech instance
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // TextToSpeech engine is initialized, you can start speaking
                }
            }
        });

        String welcomeMessage = "Welcome to the Others section. Please explore the different aspect of Faq & Other. Click on about us to find out more about us. Click on faq to see some frequently asked questions. Click on quiz to test yourself. And lastly, click on animation to see pepper do some interesting moves.";
        sayInformation(welcomeMessage);
    }


    @Override
    public void onRobotFocusLost() {
    }

    @Override
    public void onRobotFocusRefused(String reason) {
    }

    // About Us command button
    public void goToAboutUs(View view) {
        setContentView(R.layout.about_us);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Say say = SayBuilder.with(qiContext).withText("Welcome to the About Us page. Our restaurant is heavily influenced by the Spanish culture. As you can already tell, our dishes are all cuisines from Spain, and the theme colors are centered around warmth. We wish you a pleasant dining experience.").build();
                say.run();
            }
        }).start();
    }

    // FAQS button command
    public void goToFaqs(View view) {
        setContentView(R.layout.activity_faqs);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Say say = SayBuilder.with(qiContext).withText("Welcome to the FAQs page. Find answers to commonly asked questions about our restaurant.").build();
                say.run();
            }
        }).start();
    }

    // Quiz button command
    public void goToQuiz(View view) {
        setContentView(R.layout.activity_quiz);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Say say = SayBuilder.with(qiContext).withText("Welcome to the Quiz page. Let us test your knowledge").build();
                say.run();
            }
        }).start();
    }

    // Animate button command
    public void goToAnimate(View view) {
        setContentView(R.layout.activity_animate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Say say = SayBuilder.with(qiContext).withText("Welcome to the animation page. Interact with Pepper and see it perform exciting moves.").build();
                say.run();
            }
        }).start();
    }

    // Back button command (Home page)
    public void goBack(View view) {
        finish();
    }

    // Back button command (Other pages)
    public void goBack2(View view) {
        setContentView(R.layout.activity_home);
    }

    // Wave button command
    public void Wave(View view) {
        // Check if qiContext is available
        if (qiContext == null) {
            return;
        }

        // Say something before playing the animation
        new Thread(new Runnable() {
            @Override
            public void run() {
                Say say = SayBuilder.with(qiContext).withText("Hello there!").build();
                say.run();
            }
        }).start();

        // Play the animation
        playAnimation(R.raw.hello_a001);
    }

    // Dance button command
    public void Dance(View view) {
        // Check if qiContext is available
        if (qiContext == null) {
            return;
        }

        // Say something before playing the animation
        new Thread(new Runnable() {
            @Override
            public void run() {
                Say say = SayBuilder.with(qiContext).withText("Let's dance!").build();
                say.run();
            }
        }).start();

        // Play the animation
        playAnimation(R.raw.disco_a001);
    }

    // Elephant button command
    public void Elephant(View view) {
        // Check if qiContext is available
        if (qiContext == null) {
            return;
        }

        // Say something before playing the animation
        new Thread(new Runnable() {
            @Override
            public void run() {
                Say say = SayBuilder.with(qiContext).withText("Look, I'm an elephant!").build();
                say.run();
            }
        }).start();

        // Play the animation
        playAnimation(R.raw.elephant_a001);
    }

    // Basketball button command
    public void Basketball(View view) {
        // Check if qiContext is available
        if (qiContext == null) {
            return;
        }

        // Say something before playing the animation
        new Thread(new Runnable() {
            @Override
            public void run() {
                Say say = SayBuilder.with(qiContext).withText("Kobe!").build();
                say.run();
            }
        }).start();

        // Play the animation
        playAnimation(R.raw.basketball_a001);
    }

    private void playAnimation(int animationResource) {
        if (qiContext == null) {
            return;
        }

        // Load and execute the animation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Animation animation = AnimationBuilder.with(qiContext)
                            .withResources(animationResource)
                            .build();

                    Animate animate = AnimateBuilder.with(qiContext)
                            .withAnimation(animation)
                            .build();

                    animate.run(); // Execute the animation on the background thread

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void toggleAnswerVisibility(View view) {
        TextView answerTextView = null;

        switch (view.getId()) {
            case R.id.question1_faqs:
                answerTextView = findViewById(R.id.answer1_faqs);
                sayAnswer("We are open every day from 9 am to 10 pm.");
                break;
            case R.id.question2_faqs:
                answerTextView = findViewById(R.id.answer2_faqs);
                sayAnswer("Yes we do, you can find it at the special menu tab.");
                break;
            case R.id.question3_faqs:
                answerTextView = findViewById(R.id.answer3_faqs);
                sayAnswer("We accept cash, nets, pay wave, and pay lah.");
                break;
            case R.id.question4_faqs:
                answerTextView = findViewById(R.id.answer4_faqs);
                sayAnswer("We serve all types of Spanish cuisines.");
                break;
            case R.id.question5_faqs:
                answerTextView = findViewById(R.id.answer5_faqs);
                sayAnswer("Yes, reservations can be made in the reservations tab.");
                break;
            // Add cases for more questions here
            default:
                // If no question matches, do nothing
                break;
        }

        // Toggle visibility of the answerTextView
        if (answerTextView != null) {
            if (answerTextView.getVisibility() == View.VISIBLE) {
                answerTextView.setVisibility(View.GONE);
            } else {
                answerTextView.setVisibility(View.VISIBLE);
            }
        }
    }


    private void sayAnswer(String answerText) {
        if (qiContext != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Say say = SayBuilder.with(qiContext).withText(answerText).build();
                    say.run();
                }
            }).start();
        }
    }

    // Submit Quiz button command
    public void submitQuiz(View view) {
        if (isQuizSubmitted) {
            // Quiz already submitted, show the score again
            showScoreToast();
            return;
        }

        // Calculate the score
        calculateScore();

        // Mark the quiz as submitted
        isQuizSubmitted = true;

        // Show the score
        showScoreToast();
    }

    private void calculateScore() {
        // Get the RadioGroups and correct answers
        RadioGroup answerRadioGroup1 = findViewById(R.id.answerRadioGroup1);
        RadioGroup answerRadioGroup2 = findViewById(R.id.answerRadioGroup2);
        RadioGroup answerRadioGroup3 = findViewById(R.id.answerRadioGroup3);
        RadioGroup answerRadioGroup4 = findViewById(R.id.answerRadioGroup4);
        RadioGroup answerRadioGroup5 = findViewById(R.id.answerRadioGroup5);

        int correctAnswer1 = R.id.answer1RadioButton1; // Replace with the ID of the correct answer for question 1
        int correctAnswer2 = R.id.answer2RadioButton2; // Replace with the ID of the correct answer for question 2
        int correctAnswer3 = R.id.answer4RadioButton3; // Replace with the ID of the correct answer for question 3
        int correctAnswer4 = R.id.answer2RadioButton4; // Replace with the ID of the correct answer for question 4
        int correctAnswer5 = R.id.answer3RadioButton5; // Replace with the ID of the correct answer for question 4

        // Check if the selected answers are correct and update the score accordingly
        if (answerRadioGroup1.getCheckedRadioButtonId() == correctAnswer1) {
            score++;
        }

        if (answerRadioGroup2.getCheckedRadioButtonId() == correctAnswer2) {
            score++;
        }

        if (answerRadioGroup3.getCheckedRadioButtonId() == correctAnswer3) {
            score++;
        }

        if (answerRadioGroup4.getCheckedRadioButtonId() == correctAnswer4) {
            score++;
        }

        if (answerRadioGroup5.getCheckedRadioButtonId() == correctAnswer5) {
            score++;
        }
    }

    private void showScoreToast() {
        String scoreText = score + "/" + totalQuestions;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Faq.this, "Your Score: " + scoreText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // TTS engine is initialized, you can start speaking
        }
    }
        private void speakAnswer (String answerText){
            if (textToSpeech != null) {
                textToSpeech.speak(answerText, TextToSpeech.QUEUE_FLUSH, null, null);
            }
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
    }


