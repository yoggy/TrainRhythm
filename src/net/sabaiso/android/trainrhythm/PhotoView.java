package net.sabaiso.android.trainrhythm;

import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;

public class PhotoView extends View {

	Handler handler = new Handler();

	int img_idx_a = 1;
	int img_idx_b = 0;
	float cross_fade = 1.00f;
	float cross_fade_d = 0.01f;
	Vector<Bitmap> imgs = new Vector<Bitmap>();
	Paint p_solid_black, p_solid_whilte;

	Runnable start_cross_fade_task = new Runnable() {
		public void run() {
			on_start_fade();
		}
	};

	Runnable increment_cross_fade_task = new Runnable() {
		public void run() {
			on_fade_timer();
		}
	};

	public PhotoView(Context context) {
		super(context);

		Resources r = context.getResources();
		imgs.add(BitmapFactory.decodeResource(r, R.drawable.strap01));
		imgs.add(BitmapFactory.decodeResource(r, R.drawable.strap02));

		p_solid_black = new Paint();
		p_solid_black.setColor(Color.BLACK);
		p_solid_whilte = new Paint();
		p_solid_whilte.setColor(Color.WHITE);
	}

	public void onResume() {
		cross_fade = 1.0f; // stop fade timer...

		handler.postDelayed(start_cross_fade_task, 7000);
	}

	public void onPause() {
		cross_fade = 1.0f; // stop fade timer...
		handler.removeCallbacks(start_cross_fade_task);
		handler.removeCallbacks(increment_cross_fade_task);
	}

	public void on_start_fade() {
		start_fade_timer(this.img_idx_b, this.img_idx_a);
		handler.postDelayed(start_cross_fade_task, 10000);
	}

	public void start_fade_timer(int img_a_idx, int img_b_idx) {
		this.img_idx_a = img_a_idx;
		this.img_idx_b = img_b_idx;
		this.cross_fade = 0.0f;
		on_fade_timer();
	}

	public void on_fade_timer() {
		cross_fade += cross_fade_d;
		if (cross_fade >= 1.0f) {
			cross_fade = 1.0f;
		} else {
			handler.postDelayed(increment_cross_fade_task, 30);
		}
		this.invalidate();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// clear background
		canvas.drawRect(0, 0, getWidth(), getHeight(), p_solid_black);

		// draw
		draw_image(canvas, img_idx_a, 1.0f - cross_fade);
		draw_image(canvas, img_idx_b, cross_fade);
	}

	private void draw_image(Canvas canvas, int img_idx, float alpha) {
		if (img_idx < 0 || imgs.size() <= img_idx)
			return;

		Bitmap img = imgs.get(img_idx);

		int img_w = img.getWidth();
		int img_h = img.getHeight();
		float img_a = img_w / (float) img_h;
		Rect img_rect = new Rect(0, 0, img_w, img_h);

		int canvas_w = getWidth();
		int canvas_h = getHeight();
		float canvas_a = canvas_w / (float) canvas_h;

		Rect dst_rect;

		if (canvas_a > img_a) {
			// pillar box
			float scale = canvas_h / (float) img_h;
			int dst_w = (int) (img_w * scale);
			int dst_h = canvas_h;
			int dst_x = (canvas_w - dst_w) / 2;
			int dst_y = 0;
			dst_rect = new Rect(dst_x, dst_y, dst_w, dst_h);
		} else {
			// letter box
			float scale = canvas_w / (float) img_w;
			int dst_w = canvas_w;
			int dst_h = (int) (img_h * scale);
			int dst_x = 0;
			int dst_y = (canvas_h - dst_h) / 2;
			dst_rect = new Rect(dst_x, dst_y, dst_w, dst_h);
		}

		Paint p_alpha = new Paint();
		p_alpha.setAlpha((int) (alpha * 255));
		canvas.drawBitmap(img, img_rect, dst_rect, p_alpha);
	}
}
