package com.example.zdy_view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;
    private Bitmap bmsrc;
    private Bitmap bitcopy;
    private Canvas canvas;
    float startx;
    float starty;
    private Button but_red;
    private Button but_green;
    private Button but_cu;
    private Button but_bc;
    private Paint paint;
    private Button but_cz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);
        but_red = findViewById(R.id.but1);
        but_green = findViewById(R.id.but2);
        but_cu = findViewById(R.id.but3);
        but_bc = findViewById(R.id.but4);
        but_cz = findViewById(R.id.but5);
        but_bc.setOnClickListener(click);
        but_green.setOnClickListener(click);
        but_red.setOnClickListener(click);
        but_cu.setOnClickListener(click);
        but_cz.setOnClickListener(click);
        //加载原图
        bmsrc = BitmapFactory.decodeResource(getResources(), R.drawable.a);
        //创建背景白纸，并设置宽高
        bitcopy = Bitmap.createBitmap(bmsrc.getWidth(), bmsrc.getHeight(), bmsrc.getConfig());
        //创建画板，参数就是刚创建的白纸
        canvas = new Canvas(bitcopy);
        //创建画笔
        paint = new Paint();
        //准备在画板上作画
        canvas.drawBitmap(bmsrc,new Matrix(), paint);


        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case  MotionEvent.ACTION_DOWN:
                        //判断白纸是否为空
                        if (bitcopy==null){

                            bitcopy= Bitmap.createBitmap(iv.getWidth(),iv.getHeight(),Bitmap.Config.ARGB_8888);
                            canvas= new Canvas(bitcopy);
                            canvas.drawColor(Color.BLACK);
                        }
                        startx=event.getX();
                        starty=event.getY();
                        break;
                    case  MotionEvent.ACTION_MOVE:
                        float stopX = event.getX();
                        float stopY = event.getY();
                        canvas.drawLine(startx, starty, stopX, stopY, paint);
                        startx = event.getX();
                        starty = event.getY();
                        iv.setImageBitmap(bitcopy);
                        break;
                    case  MotionEvent.ACTION_UP:
                        break;default:
                        break;
                }


                return true;
            }
        });
    }
    public View.OnClickListener click= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.but1:
                    paint.setColor(Color.RED);
                    break;
                case R.id.but2:
                    paint.setColor(Color.GREEN);
                    break;
                case R.id.but3:
                    paint.setStrokeWidth(20);
                    break;
                case R.id.but4:
                    saveBitmap();
                    break;
                case R.id.but5:
                    resumeCanvas();
                    break;
                default:
                    break;
            }
        }

        private void resumeCanvas() {
            if (bitcopy != null) {
                bitcopy = Bitmap.createBitmap(iv.getWidth(),
                        iv.getHeight(), Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitcopy);
                canvas.drawColor(Color.WHITE);
                iv.setImageBitmap(bitcopy);
                Toast.makeText(MainActivity.this, "清除画板成功，可以重新开始绘图", Toast.LENGTH_SHORT).show();
            }
        }

        private void saveBitmap() {
            try {
                // 保存图片到SD卡上
                File file = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".png");
                FileOutputStream stream = new FileOutputStream(file);
                bitcopy.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Toast.makeText(MainActivity.this, "保存图片成功", Toast.LENGTH_SHORT).show();
                // Android设备Gallery应用只会在启动的时候扫描系统文件夹
                // 这里模拟一个媒体装载的广播，用于使保存的图片可以在Gallery中查看
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
                intent.setData(Uri.fromFile(Environment
                        .getExternalStorageDirectory()));
                sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };
    }


