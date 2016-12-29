package com.haeki.ticTacToeGame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    int[] sumRows = new int[3];
    int[] sumColls = new int[3];
    HashMap<Integer, CustomImgButton> buttons = new HashMap<>();
    int sumCross1 = 0;
    int sumCross2 = 0;
    int activePlayer = 0;
    TableLayout tableLayout;
    boolean singlePlayer = true;
    Random r;
    int animationDuration;
    int turns = 0;
    Method test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("START GAME");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        singlePlayer = getIntent().getBooleanExtra(MenueActivity.PLAY_MODE, true);
        r = new Random();
        animationDuration = 900;
        try {
            test = this.getClass().getMethod("test");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("CREATE VIEW");
        tableLayout = (TableLayout) findViewById(R.id.gameBoard);

        fillButtonMap();
        if(singlePlayer) {
            activePlayer = 1;
        } else {
            activePlayer = r.nextInt(2);
            if (activePlayer == 0) {
                activePlayer = -1;
            }
            updateActivePlayer();
        }
        try {
            test.invoke(this,null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void test() {
        Log.v("Method Test", "Test");
    }

    private void crossfade() {
        View gameOverText = findViewById(R.id.gameOverText);
        View replayButton = findViewById(R.id.replayButton);
        View menueButton = findViewById(R.id.menueButton);
        final View activePlayerText = findViewById(R.id.activePlayerText);
        final View activePlayerImg = findViewById(R.id.activePlayerImage);
        final View gameBoard = findViewById(R.id.gameBoard);

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        gameOverText.setAlpha(0f);
        gameOverText.setVisibility(View.VISIBLE);
        replayButton.setAlpha(0f);
        replayButton.setVisibility(View.VISIBLE);
        menueButton.setAlpha(0f);
        menueButton.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        gameOverText.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);

        replayButton.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);

        menueButton.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        gameBoard.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        gameBoard.setVisibility(View.GONE);
                    }
                });

        activePlayerText.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        activePlayerText.setVisibility(View.GONE);
                    }
                });

        activePlayerImg.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        activePlayerImg.setVisibility(View.GONE);
                    }
                });
    }

    void fillButtonMap() {
        for(int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            for(int j = 0; j < tableRow.getChildCount(); j++) {
                buttons.put(i + (j*10), (CustomImgButton) tableRow.getChildAt(j));
            }
        }
    }

    CustomImgButton getButton(int key) {
        return buttons.get(key);
    }

    CustomImgButton getButton(int row, int col) {
        return buttons.get(row + (col*10));
    }

    void backToMenue(View view) {
        finish();
    }

    void restartGame(View view) {
        recreate();
    }

    void updateButton(View view) {
        CustomImgButton button = (CustomImgButton) view;
        if(button != null && button.getDrawable() == null) {
            if(button.updateUsingPlayer(activePlayer)) {
                activePlayer *= -1;
                turns++;
                checkForWin();
                updateActivePlayer();
            }
        }
    }

    void calcSums() {
        sumColls = new int[3];
        for(int i = 0; i < 3; i++) {
            sumRows[i] = 0;
            for (int j = 0; j < 3; j++) {
                CustomImgButton imageButton = getButton(i,j);
                sumRows[i] += imageButton.getUsingPlayer();
                sumColls[j] += imageButton.getUsingPlayer();
            }
        }
        sumCross1 = getButton(0,0).getUsingPlayer();
        sumCross1 += getButton(1,1).getUsingPlayer();
        sumCross1 += getButton(2,2).getUsingPlayer();

        sumCross2 = getButton(0,2).getUsingPlayer();
        sumCross2 += getButton(1,1).getUsingPlayer();
        sumCross2 += getButton(2,0).getUsingPlayer();

    }

    void checkForWin() {
        calcSums();

        if(turns >=9) {
            gameOver(0);
        }

        if(sumCross1 == 3) {
            highlightCross1();
            gameOver(1);
        }
        if(sumCross1 == -3) {
            highlightCross1();
            gameOver(-1);
        }
        if(sumCross2 == 3) {
            highlightCross2();
            gameOver(1);
        }
        if(sumCross2 == -3) {
            highlightCross2();
            gameOver(-1);
        }

        for(int i = 0; i < 3; i++) {
            if(sumRows[i] == 3) {
                highlightRow(i);
                gameOver(1);
            }
            if(sumRows[i] == -3) {
                highlightRow(i);
                gameOver(-1);
            }
            if(sumColls[i] == 3) {
                highlightCollum(i);
                gameOver(1);
            }
            if(sumColls[i] == -3) {
                highlightCollum(i);
                gameOver(-1);
            }
        }
    }

    void highlightCross1() {
        getButton(0,0).setBackgroundColor(Color.GREEN);
        getButton(1,1).setBackgroundColor(Color.GREEN);
        getButton(2,2).setBackgroundColor(Color.GREEN);
    }

    void highlightCross2() {
        getButton(0,2).setBackgroundColor(Color.GREEN);
        getButton(1,1).setBackgroundColor(Color.GREEN);
        getButton(2,0).setBackgroundColor(Color.GREEN);
    }

    void highlightRow(int rowNumber) {
        for(int i = 0; i < 3; i++) {
            getButton(rowNumber,i).setBackgroundColor(Color.GREEN);
        }
    }

    void highlightCollum(int collNumber) {
        for(int i = 0; i < 3; i++) {
            getButton(i,collNumber).setBackgroundColor(Color.GREEN);
        }
    }

    void gameOver(int player) {
        disableEnableBoard(false);
        TextView gameOverText = (TextView) findViewById(R.id.gameOverText);
        SharedPreferences settings = getSharedPreferences("stats", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        if(singlePlayer) {
            editor.putInt(StatsActivity.SP_GAME_COUNT, (settings.getInt(StatsActivity.SP_GAME_COUNT, 0) + 1));
            if(player == 0) {
                editor.putInt(StatsActivity.SP_DRAW_COUNT, (settings.getInt(StatsActivity.SP_DRAW_COUNT, 0) + 1));
                gameOverText.setText(R.string.draw);
            }
            if(player == 1) {
                editor.putInt(StatsActivity.SP_WIN_COUNT, (settings.getInt(StatsActivity.SP_WIN_COUNT, 0) + 1));
                gameOverText.setText(R.string.win);
            } else if(player == -1) {
                editor.putInt(StatsActivity.SP_LOOS_COUNT, (settings.getInt(StatsActivity.SP_LOOS_COUNT, 0) + 1));
                gameOverText.setText(R.string.loose);
            }
        } else {
            editor.putInt(StatsActivity.MP_GAME_COUNT, (settings.getInt(StatsActivity.MP_GAME_COUNT, 0) + 1));
            if(player == 0) {
                editor.putInt(StatsActivity.MP_DRAW_COUNT, (settings.getInt(StatsActivity.MP_DRAW_COUNT, 0) + 1));
                gameOverText.setText(R.string.draw);
            }
            if(player == 1) {
                editor.putInt(StatsActivity.MP_P1_WIN_COUNT, (settings.getInt(StatsActivity.MP_P1_WIN_COUNT, 0) + 1));
                gameOverText.setText(R.string.winP1);
            } else if(player == -1) {
                editor.putInt(StatsActivity.MP_P2_WIN_COUNT, (settings.getInt(StatsActivity.MP_P2_WIN_COUNT, 0) + 1));
                gameOverText.setText(R.string.winP2);
            }
        }

        editor.commit();
        crossfade();
    }

    void disableEnableBoard(boolean bool) {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                getButton(i,j).setEnabled(bool);
            }
        }
    }

    boolean placeInCross1() {
        if(getButton(0,0).getUsingPlayer() == 0) {
            getButton(0,0).callOnClick();
            return true;
        }
        if(getButton(1,1).getUsingPlayer() == 0) {
            getButton(1,1).callOnClick();
            return true;
        }
        if(getButton(2,2).getUsingPlayer() == 0) {
            getButton(2,2).callOnClick();
            return true;
        }
        return  false;
    }

    boolean placeInCross2() {
        if(getButton(0,2).getUsingPlayer() == 0) {
            getButton(0,2).callOnClick();
            return true;
        }
        if(getButton(1,1).getUsingPlayer() == 0) {
            getButton(1,1).callOnClick();
            return true;
        }
        if(getButton(2,0).getUsingPlayer() == 0) {
            getButton(2,0).callOnClick();
            return true;
        }
        return false;
    }

    boolean playBot() {
        if(sumCross1 == -2) {
            return placeInCross1();
        }
        if(sumCross2 == -2) {
            return placeInCross2();
        }
        for(int i = 0; i < 3; i++) {
            if(sumRows[i] == -2) {
                for(int j = 0; j < 3; j++) {
                    if(getButton(i,j).getUsingPlayer() == 0) {
                        getButton(i,j).callOnClick();
                        return true;
                    }
                }
            }
            if(sumColls[i] == -2) {
                for(int j = 0; j < 3; j++) {
                    if(getButton(j,i).getUsingPlayer() == 0) {
                        getButton(j,i).callOnClick();
                        return true;
                    }
                }
            }
        }

        for(int i = 0; i < 3; i++) {
            if(sumRows[i] == 2) {
                for(int j = 0; j < 3; j++) {
                    if(getButton(i,j).getUsingPlayer() == 0) {
                        getButton(i,j).callOnClick();
                        return true;
                    }
                }
            }
            if(sumColls[i] == 2) {
                for(int j = 0; j < 3; j++) {
                    if(getButton(j,i).getUsingPlayer() == 0) {
                        getButton(j,i).callOnClick();
                        return true;
                    }
                }
            }
        }

        if(sumCross1 == 2) {
            return placeInCross1();
        }
        if(sumCross2 == 2) {
            return placeInCross2();
        }

        int searching = 0;
        while (searching < 20) {
            CustomImgButton imgButton = getButton(r.nextInt(3),r.nextInt(3));
            if(imgButton.getUsingPlayer() == 0) {
                imgButton.callOnClick();
                searching = 100;
                return true;
            }
            searching++;
        }
        return false;
    }

    void updateActivePlayer() {
        ImageView imgView = (ImageView) findViewById(R.id.activePlayerImage);
        if(imgView != null) {
            if (activePlayer == 1) {
                imgView.setImageResource(R.drawable.circle_transparent_small);
            } else if (activePlayer == -1) {
                imgView.setImageResource(R.drawable.cross_transparent_small);
                if(singlePlayer) {
                    playBot();
                }
            } else {
                System.out.println("Wrong Player Number");
            }
        }
    }
}
