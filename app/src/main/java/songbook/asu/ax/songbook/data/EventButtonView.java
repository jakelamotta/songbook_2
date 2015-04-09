package songbook.asu.ax.songbook.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.util.Log;
import android.view.View;

import songbook.asu.ax.songbook.R;

/**
 * Created by Kristian on 2015-04-09.
 */
public class EventButtonView extends View {

    private static final String LOG_TAG = EventButtonView.class.getSimpleName();
    private final int resID = 1;

    public EventButtonView(Context context){
        super(context);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(LOG_TAG,"Custom view was clicked");
            }
        });

        setClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint textPaint = new Paint();
        textPaint.setARGB(200,200,200,200);
        canvas.drawCircle(10,10,10,textPaint);
    }
}
