package net.sabaiso.android.trainrhythm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public class PhotoView extends View {

	Bitmap img;
	Rect img_rect;
	int img_w;
	int img_h;
	float img_a;

	public PhotoView(Context context, int image) {
		super(context);
		
		Resources r = context.getResources();
        img = BitmapFactory.decodeResource(r, image);

        img_w = img.getWidth();
        img_h = img.getHeight();
        img_rect = new Rect(0,0,img_w, img_h);
		img_a = img_w / (float)img_h;
	}

	@SuppressLint("DrawAllocation")
	@Override
    protected void onDraw(Canvas canvas) {
		int canvas_w = getWidth();
		int canvas_h = getHeight();
		float canvas_a = canvas_w / (float)canvas_h;

		Rect dst_rect;
		
		if (canvas_a > img_a) {
			// pillar box
			float scale = canvas_h / (float)img_h;
			int dst_w = (int)(img_w * scale);
			int dst_h = canvas_h;
			int dst_x = (canvas_w - dst_w) / 2;
			int dst_y = 0;
			dst_rect = new Rect(dst_x, dst_y, dst_w, dst_h);
		}
		else {
			// letter box
			float scale = canvas_w / (float)img_w;
			int dst_w = canvas_w;
			int dst_h = (int)(img_h * scale);
			int dst_x = 0;
			int dst_y = (canvas_h - dst_h) / 2;
			dst_rect = new Rect(dst_x, dst_y, dst_w, dst_h);
		}

		canvas.drawBitmap(img, img_rect, dst_rect, null);
	}
}
