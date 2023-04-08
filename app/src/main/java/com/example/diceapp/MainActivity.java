package com.example.diceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.diceapp.databinding.ActivityMainBinding;

class DiceFace{
    private int baseColor;
    private int pipColor;
    private final int pipRadius = 50;
    private final int diceRoundnessRadius = 40;
    // divide square into 4x4 grid, it has 9 intersections in between which are all the pips
    private static final int[][] PIP_POSITIONS = {
            {4},
            {0, 8},
            {2, 4, 6},
            {0, 2, 6, 8},
            {0, 2, 4, 6, 8},
            {0, 2, 3, 5, 6, 8}
    };

    private static final int[] DICE_COLORS = {
            Color.RED,
            Color.YELLOW,
            Color.BLUE,
    };

    private void getNewColors(){
        int indexBase = (int)Math.floor(Math.random()* DICE_COLORS.length);
        int indexPip = (int)Math.floor(Math.random()* DICE_COLORS.length);
        if (indexPip == indexBase){
            indexPip = (indexPip+1)%(DICE_COLORS.length);
        }
        this.baseColor = this.DICE_COLORS[indexBase];
        this.pipColor = this.DICE_COLORS[indexPip];
    }

    public void drawDice(Bitmap bitmap , int width , int height , int rollResult){
        getNewColors();
        Canvas canvas = new Canvas(bitmap);
        drawBackground(canvas,width,height);
        drawPips(canvas,width,height,rollResult);
    }

    private void drawBackground(Canvas canvas, int width, int height){
        Paint paint = new Paint();
        paint.setColor(this.baseColor);
        canvas.drawRoundRect(0,0,width,height,this.diceRoundnessRadius,this.diceRoundnessRadius , paint);
    }

    private void drawPips(Canvas canvas, int width, int height, int rollResult){
        Paint paint = new Paint();
        paint.setColor(this.pipColor);
        for (Integer position : PIP_POSITIONS[rollResult-1]) {
            float pipX = (float)((position%3 + 1)*width/4);
            float pipY = (float)((position/3 + 1)*height/4);
            canvas.drawCircle(pipX , pipY , pipRadius , paint);
        }
    }

}
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding = null;
    DiceFace dice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);


        dice = new DiceFace();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Button buttonRoll = findViewById(R.id.button_roll);
        buttonRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rollResult = getRandomRoll();
                binding.txtResult.setText(String.valueOf(rollResult));
                getDiceImage(rollResult);
                Log.d("DebugMessage" , String.valueOf(rollResult));
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    int getRandomRoll(){
        return (int)(Math.floor(Math.random()*6 + 1));
    }

    void getDiceImage(int rollResult){
        int width = binding.imgDice.getWidth();
        int height = binding.imgDice.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width , height , Bitmap.Config.ARGB_8888);
        dice.drawDice(bitmap , width , height , rollResult);
        binding.imgDice.setImageBitmap(bitmap);
    }
}



