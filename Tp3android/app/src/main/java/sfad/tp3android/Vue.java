package sfad.tp3android;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class Vue extends AppCompatActivity {

    public SeekBar seekBarSpeed;
    public SeekBar seekBarAngle;
    public SeekBar seekBarRadius;

    public TextView textprogressSpeed;
    public TextView textprogressRadius;
    public TextView textprogressAngle;

    public SurfaceView pane;
    public SurfaceHolder surface;
    private Canvas canvas;
    private Paint paint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue);

        seekBarSpeed = (SeekBar) findViewById(R.id.speedSlider);
        seekBarAngle = (SeekBar) findViewById(R.id.angleSlider);
        seekBarRadius = (SeekBar) findViewById(R.id.radiusSlider);

        textprogressSpeed = (TextView) findViewById(R.id.progressSpeed);
        textprogressRadius = (TextView) findViewById(R.id.progressRadius);
        textprogressAngle = (TextView) findViewById(R.id.progressAngle);

        seekBarSpeed.setOnSeekBarChangeListener(new seekBarChanger());
        seekBarAngle.setOnSeekBarChangeListener(new seekBarChanger());
        seekBarRadius.setOnSeekBarChangeListener(new seekBarChanger());

        pane = (SurfaceView) findViewById(R.id.surfaceView);
        pane.setOnClickListener(new clickSurface());

    }

    private class seekBarChanger implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (seekBar.getId() == seekBarRadius.getId())
                textprogressRadius.setText(String.valueOf(progress + 4));
            else if (seekBar.getId() == seekBarAngle.getId()) {
                textprogressAngle.setText(String.valueOf(progress));

            } else {
                textprogressSpeed.setText(String.valueOf(progress));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    /**
     * TODO afficher balle -marche pas
     */
    private class clickSurface implements SurfaceView.OnClickListener {

        @Override
        public void onClick(View v) {
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            surface = pane.getHolder();
            float x = 20;
            float y = 20;
            float radius = 10;
            canvas = surface.lockCanvas();
            synchronized (pane.getHolder()) {
                paint.setColor(Color.RED);
                canvas.drawCircle(x, y, radius, paint);

            }
            surface.unlockCanvasAndPost(canvas);
            }


        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
