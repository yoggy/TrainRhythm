package net.sabaiso.android.trainrhythm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class TranRhythmActivity extends Activity implements Runnable {

	PhotoView view;
	Handler handler = new Handler();
	boolean timer_flag = false;
	Vibrator vibrator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);

		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        view = new PhotoView(this);
		setContentView(view);
	}

	@Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
        startIntervalTimer();
        view.onResume();
    }

	protected void onPause() {
        stopIntervalTimer();
        vibrator.cancel();
        view.onPause();
        super.onPause();
	}

	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        hideSystemUI();
    }

    @SuppressLint("InlinedApi")
	private void hideSystemUI() {
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

	
	private void startIntervalTimer() {
		timer_flag = true;
		handler.post(this);
	}

    private void stopIntervalTimer() {
    	timer_flag = false;
    }

	@Override
	public void run() {
		
		long[] pattern = {0, 30, 120, 30, 300, 30, 120, 30};

		vibrator.vibrate(pattern, -1);
		
		if (timer_flag == true) {
			handler.postDelayed(this, 1400);
		}
	}

    
}
