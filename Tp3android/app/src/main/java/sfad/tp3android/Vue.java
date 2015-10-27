package sfad.tp3android;

import android.app.usage.UsageEvents;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class Vue extends AppCompatActivity {

    private static final int MIN_RADIUS = 4;

    public SeekBar seekBarSpeed;
    public SeekBar seekBarAngle;
    public SeekBar seekBarRadius;

    public TextView textprogressSpeed;
    public TextView textprogressRadius;
    public TextView textprogressAngle;
    public TextView textnbParticle;

    public Spinner spnColor;

    public SurfaceView pane;
    public SurfaceHolder surface;
    private Canvas canvas;
    private Paint paint;

    public Button generate;
    public Button initialize;
    public Button exit;

    public ArrayList<Particle> objectList = new ArrayList<>();

    public Thread thread;
    public boolean start = false;

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
        thread = new Thread(new MovementService());

        spnColor = (Spinner) findViewById(R.id.spnColor);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.color_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnColor.setAdapter(adapter);

       /* ArrayAdapter<String> myAdapter=new
                ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                myStringArray);
        listColor.setAdapter(myAdapter);
        listColor.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                positionList =  position;
            }
        });*/

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
                ajouterparticle(Math.floor(Math.random() * pane.getWidth()), Math.floor(Math.random() * pane.getHeight()),
                        Math.floor(Math.random() * seekBarAngle.getMax()), Math.floor(Math.random() * seekBarSpeed.getMax()),
                        Math.floor(Math.random() * (seekBarRadius.getMax() - Vue.MIN_RADIUS) + Vue.MIN_RADIUS));
            }else if(v.getId() == initialize.getId()){
                reset();
            } else if (v.getId() == exit.getId()) {
                System.exit(0);
            }else{
                ajouterparticle((double) m.getX(), (double) m.getY(), Double.parseDouble(textprogressAngle.getText().toString()), Double.parseDouble
                        (textprogressSpeed.getText().toString()), Double.parseDouble(textprogressRadius.getText().toString()));
            }

            return true;
        }
    }

    private void ajouterparticle(double x, double y, double angle, double speed, double radius)
    {
        if(start == false){
            thread.start();
            start = true;
        }
        boolean valid = true;
        x = Particle.validatePosition(x, radius, pane.getWidth());
        y = Particle.validatePosition(y, radius, pane.getHeight());
        Particle part = new Particle(x, y, angle, speed, radius, getColor());
        Log.d("ASD", ""+getColor());
        // Checks if the position currently has a particle in it
        for(Particle cp : objectList){
            if(cp.isColliding(part)){
                valid = false;
                break;
            }
        }
        if(valid){
            objectList.add(part);
            textnbParticle.setText(String.valueOf(Integer.parseInt(textnbParticle.getText().toString()) + 1));
            dessin();
        }
    }

    //TODO dessine les formes mais glitch sur bouton generate (genere 2 cercle), probleme color
    private void dessin() {
        canvas = null;
        surface = pane.getHolder();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        canvas = surface.lockCanvas(null);
        synchronized (surface) {
            canvas.drawColor(Color.BLACK);
            for(Particle cp : objectList){
                paint.setColor(cp.getColor());
                canvas.drawCircle((float) cp.getX(), (float) cp.getY(), (float) cp.getRadius(), paint);
            }
        }
        surface.unlockCanvasAndPost(canvas);
    }

    private int getColor() {
        int color = Color.WHITE;
        switch(spnColor.getSelectedItem().toString()){
            case("Green"):
                color = Color.GREEN;
                break;
            case("Blue"):
                color = Color.BLUE;
                break;
            case("Yellow"):
                color = Color.YELLOW;
                break;
            case("Red"):
                color = Color.RED;
                break;
            case("Magenta"):
                color = Color.MAGENTA;
                break;
        }
        return color;
    }


    private void reset() {
        spnColor.setSelection(0);
        textnbParticle.setText(String.valueOf(0));
        objectList = new ArrayList<Particle>();
        thread = new Thread(new MovementService());
        canvas = null;
        paint.setStyle(Paint.Style.FILL);
        surface = pane.getHolder();
        canvas = surface.lockCanvas(null);
        canvas.drawColor(Color.BLACK);
        surface.unlockCanvasAndPost(canvas);
        seekBarRadius.setProgress(0);
        seekBarSpeed.setProgress(0);
        seekBarAngle.setProgress(0);
        start = false;
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



    private class MovementService implements Runnable {
        private final static long SLEEP_TIME = 2;
        boolean running;

        @Override
        public void run() {
            running = true;
            while(running){
                Particle currentParticle;
                ArrayList<Particle> currentParticles = new ArrayList<>();
                currentParticles.addAll(objectList);

                for(Particle cp : currentParticles){
                    if(running){
                        currentParticle = cp;
                        double oldX = currentParticle.getX();
                        double oldY = currentParticle.getY();
                        currentParticle.move(SLEEP_TIME);
                        if(currentParticle.getX() >= pane.getWidth()){
                            currentParticle.setX(pane.getWidth()-currentParticle.getRadius());
                        }else if(currentParticle.getX() < 0){
                            currentParticle.setX(currentParticle.getRadius());
                        }else if(currentParticle.getY() >= pane.getHeight()){
                            currentParticle.setY(pane.getHeight()-currentParticle.getRadius());
                        }else if(currentParticle.getY() < 0){
                            currentParticle.setY(currentParticle.getRadius());
                        }

                        dessin();

                        if(collideParticle(currentParticle)){
                        }
                    }
                }

                try{
                    Thread.sleep(SLEEP_TIME);
                }catch(Exception e){
                    break;
                }
            }
        }
    }

    public boolean collideParticle(Particle p){
        Particle currentParticle;
        Movement movement = p.getMovement();
        boolean returnValue = false;
        // Circle colliding wall
        // Right wall collision
        if(p.getX() + p.getRadius() >= pane.getWidth() || p.getX() - p.getRadius() < 0){
            movement.setXMovement(-movement.getXMovement());
            returnValue = true;
        } else if(p.getY() - p.getRadius() < 0 || p.getY() + p.getRadius() >= pane.getHeight()){
            movement.setYMovement(-movement.getYMovement());
            returnValue = true;
        }

        // Circles colliding other circles
        for(Particle cp : objectList){
            currentParticle = cp;
            if(!currentParticle.equals(p)){
                if(p.isColliding(currentParticle)){
                    p.collide(currentParticle);
                }
            }
        }

        return returnValue;
    }
}


