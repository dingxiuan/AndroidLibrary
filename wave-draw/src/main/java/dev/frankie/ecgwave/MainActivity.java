package dev.frankie.ecgwave;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import dev.frankie.view.EcgSurfaceView2;
import dev.frankie.view.EcgView;

public class MainActivity extends AppCompatActivity {

    private List<Float> datas = new ArrayList<>();

    private Queue<Float> data0Q = new LinkedList<>();
    private Queue<Float> data1Q = new LinkedList<>();


    EcgSurfaceView2 ecgTextureView;
    EcgView ecgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ecgTextureView = findViewById(R.id.ecg_texture_view);
        ecgView = findViewById(R.id.ecg_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
