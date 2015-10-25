package sfad.tp3android;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Vue extends AppCompatActivity {

    private static final int MIN_RADIUS = 4;

    public SeekBar seekBarSpeed;
    public SeekBar seekBarAngle;
    public SeekBar seekBarRadius;

    public TextView textprogressSpeed;
    public TextView textprogressRadius;
    public TextView textprogressAngle;
    public TextView textnbParticle;

    public SurfaceView pane;
    public SurfaceHolder surface;
    private Canvas canvas;
    private Paint paint;

    public Button generate;
    public Button initialize;
    public Button exit;

   public ArrayList<Particle> objectList = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue);
        objectList = new ArrayList<>();

        seekBarSpeed = (SeekBar) findViewById(R.id.speedSlider);
        seekBarAngle = (SeekBar) findViewById(R.id.angleSlider);
        seekBarRadius = (SeekBar) findViewById(R.id.radiusSlider);

        textprogressSpeed = (TextView) findViewById(R.id.progressSpeed);
        textprogressRadius = (TextView) findViewById(R.id.progressRadius);
        textprogressAngle = (TextView) findViewById(R.id.progressAngle);
        textnbParticle = (TextView) findViewById(R.id.nbParticle);

        seekBarSpeed.setOnSeekBarChangeListener(new seekBarChanger());
        seekBarAngle.setOnSeekBarChangeListener(new seekBarChanger());
        seekBarRadius.setOnSeekBarChangeListener(new seekBarChanger());

        generate = (Button) findViewById(R.id.generateButton);
        initialize = (Button) findViewById(R.id.resetButton);
        exit = (Button) findViewById(R.id.exitButton);

        pane = (SurfaceView) findViewById(R.id.surfaceView);
        pane.setOnTouchListener(new clickSurface());
        generate.setOnTouchListener(new clickSurface());
        initialize.setOnTouchListener(new clickSurface());
        exit.setOnTouchListener(new clickSurface());

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


    private class clickSurface implements SurfaceView.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent m) {
            if (v.getId() == generate.getId()){
                dessin(Math.floor(Math.random()*pane.getWidth()), Math.floor(Math.random()*pane.getHeight()),
                        Math.floor(Math.random()*seekBarAngle.getMax()), Math.floor(Math.random()*seekBarSpeed.getMax()),
                        Math.floor(Math.random() * (seekBarRadius.getMax() - Vue.MIN_RADIUS) + Vue.MIN_RADIUS));

            }
            else if(v.getId() == initialize.getId()){
                reset();
            } else if (v.getId() == exit.getId()) {
                System.exit(0);
            }
            else{
                dessin((double) m.getX(),(double) m.getY(),Double.parseDouble(textprogressAngle.getText().toString()),Double.parseDouble
                        (textprogressSpeed.getText().toString()),Double.parseDouble(textprogressRadius.getText().toString()));
            }

            return true;
        }
    }

    //TODO dessine les formes mais glitch, probleme set nb particle
    private void dessin(double x, double y, double angle, double speed, double radius) {
        boolean valid = true;
        x = Particle.validatePosition(x, radius, pane.getWidth());
        y = Particle.validatePosition(y, radius, pane.getHeight());
        Particle part = new Particle(x, y, angle, speed, radius);
        // Checks if the position currently has a particle in it
        for(Particle cp : objectList){
            if(cp.isColliding(part)){
                valid = false;
                break;
            }
        }
        if(valid){
            objectList.add(part);
            canvas = null;
        surface = pane.getHolder();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        canvas = surface.lockCanvas(null);


        synchronized (surface){
            paint.setColor(Color.WHITE);
            canvas.drawCircle((float) x, (float) y, (float) radius, paint);
          //  textnbParticle.setText(Integer.parseInt(textnbParticle.getText().toString()) + 1);
        }
        surface.unlockCanvasAndPost(canvas);

        }
    }

    //TODO probleme reset le pane
    private void reset() {
      //  textnbParticle.setText(0);
        objectList = new ArrayList<>();
      //  canvas.drawColor(Color.BLACK);
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


