package sfad.tp3android;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Class controleur who draw particle on a surfaceview with different radius, angle, speed, who handle collision between particle
 */
public class Vue extends AppCompatActivity {
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

    public ArrayList<Particle> objectList;

    public Thread thread;
    public boolean start = false;

    /**
     * Initialize all the compont of the application and add listener to all button and the surface view
     *
     * @param savedInstanceState - instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue);
        objectList = new ArrayList<>();

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

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

        clickSurface touchListener = new clickSurface();
        pane = (SurfaceView) findViewById(R.id.surfaceView);
        surface = pane.getHolder();
        pane.setOnTouchListener(touchListener);
        generate.setOnTouchListener(touchListener);
        initialize.setOnTouchListener(touchListener);
        exit.setOnTouchListener(touchListener);
        thread = new Thread(new MovementService());

        spnColor = (Spinner) findViewById(R.id.spnColor);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.color_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnColor.setAdapter(adapter);
    }

    /**
     * Class who implements the onseekbarchange
     */
    private class seekBarChanger implements SeekBar.OnSeekBarChangeListener {

        /**
         * method who change the associate text for each seekbar value to watch the progress of the bar
         *
         * @param seekBar - the changed seekbar
         * @param progress - actual progress of the seekbar
         * @param fromUser - made by user
         */
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
     * class who implements ontouchlistener
     */
    private class clickSurface implements SurfaceView.OnTouchListener {

        /**
         * make different action depend of wish thing is pressed,
         * exit = exit the apllication
         * generate = generade a random particle
         * initialize = reset all compent
         *
         * @param v - the view (surfaceview)
         * @param m - the movement of the user
         *
         * @return true - is touched
         */
        @Override
        public boolean onTouch(View v, MotionEvent m) {
            if (m.getAction() == MotionEvent.ACTION_DOWN) {
                if (v.getId() == generate.getId()) {
                    Log.d("ASD", "generate");
                    ajouterparticle(Math.floor(Math.random() * pane.getWidth()), Math.floor(Math.random() * pane.getHeight()),
                            Math.floor(Math.random() * seekBarAngle.getMax()), Math.floor(Math.random() * seekBarSpeed.getMax()),
                            Math.floor(Math.random() * (seekBarRadius.getMax() - Particle.MIN_RADIUS_VALUE) + 4));
                } else if (v.getId() == initialize.getId()) {
                    reset();
                } else if (v.getId() == exit.getId()) {
                    System.exit(0);
                } else {
                    ajouterparticle((double) m.getX(), (double) m.getY(), Double.parseDouble(textprogressAngle.getText().toString()), Double.parseDouble
                            (textprogressSpeed.getText().toString()), Double.parseDouble(textprogressRadius.getText().toString()));
                }
            }
            return true;
        }
    }

    /**
     * Used to generate a particle with given parameters, if parameters are invalid it will create a particle
     * with the default values from the class
     *
     * @param x - x dimension
     * @param y - y dimension
     * @param angle - angle of the movement
     * @param speed -  speed of the movement
     * @param radius -  radius of the particle
     */
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
            draw();
        }
    }


    /**
     * Draw a particle from the array of particle in the surfaceview
     */
    private void draw() {
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

    /**
     * method who give the associate code of color for a specifiate color
     *
     * @return int - the code of the color
     */
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



    /**
     * Used to reset the graphic components of the application and empty the game pane
     */
    private void reset() {
        spnColor.setSelection(0);
        textnbParticle.setText(String.valueOf(0));
        objectList = new ArrayList<Particle>();
        thread = new Thread(new MovementService());
        canvas = null;
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


    /**
     * Service running the movement for the game.
     */
    private class MovementService implements Runnable {
        private final static long SLEEP_TIME = 25;
        boolean running;

        @Override
        public void run() {
            running = true;
            while(running){
                ArrayList<Particle> currentParticles = new ArrayList<>();
                currentParticles.addAll(objectList);

                for(Particle cp : currentParticles){
                    if(running){
                        cp.move();
                        if(cp.getX() >= pane.getWidth()){
                            cp.setX(pane.getWidth()-cp.getRadius());
                        }else if(cp.getX() < 0){
                            cp.setX(cp.getRadius());
                        }else if(cp.getY() >= pane.getHeight()){
                            cp.setY(pane.getHeight()-cp.getRadius());
                        }else if(cp.getY() < 0){
                            cp.setY(cp.getRadius());
                        }
                        collideParticle(cp);

                        draw();
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


    /**
     * Method that looks for collision between a particle and the rest of them on the game board
     * or the walls
     * @param p Particle to check for collision
     * @return True if it has collided with something, wall or particle
     */
    public void collideParticle(Particle p){
        Movement movement = p.getMovement();
        // Circle colliding wall
        // Right wall collision
        if(p.getX() + p.getRadius() >= pane.getWidth() || p.getX() - p.getRadius() < 0){
            movement.setXMovement(-movement.getXMovement());
        } else if(p.getY() - p.getRadius() < 0 || p.getY() + p.getRadius() >= pane.getHeight()){
            movement.setYMovement(-movement.getYMovement());
        }
        // Circles colliding other circles
        for(Particle cp : objectList){
            if(!cp.equals(p)){
                if(p.isColliding(cp)){
                    p.collide(cp);
                }
            }
        }
    }
}


