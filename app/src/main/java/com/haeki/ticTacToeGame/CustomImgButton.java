package com.haeki.ticTacToeGame;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by haeki on 08.09.2016.
 */
public class CustomImgButton extends ImageButton{


    int usingPlayer = 0;

    public CustomImgButton(Context context) {
        super(context);
    }

    public CustomImgButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImgButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getUsingPlayer() {
        return usingPlayer;
    }

    public void setUsingPlayer(int usingPlayer) {
        this.usingPlayer = usingPlayer;
    }

    public boolean updateUsingPlayer(int player) {
        if(!isEnabled()) {
            return false;
        }
        setUsingPlayer(player);
        if(player == 1) {
            setImageResource(R.drawable.circle_transparent);
            return true;
        } else if(player == -1) {
            setImageResource(R.drawable.cross_transparent);
            return true;
        } else if(player == 0) {
            setImageResource(0);
            return  true;
        } else {
            return false;
        }
    }
}
