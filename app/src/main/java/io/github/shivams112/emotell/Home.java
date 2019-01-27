
package io.github.shivams112.emotell;

        import android.app.Dialog;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.PointF;
        import android.graphics.RectF;
        import android.graphics.drawable.BitmapDrawable;
        import android.media.MediaPlayer;
        import android.net.Uri;
        import android.speech.tts.TextToSpeech;
        import android.support.constraint.ConstraintLayout;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.util.SparseArray;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.vision.Frame;
        import com.google.android.gms.vision.face.Face;
        import com.google.android.gms.vision.face.FaceDetector;
        import com.google.android.gms.vision.face.Landmark;

        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;
        import java.util.List;
        import java.util.Locale;

public class Home extends AppCompatActivity {

    private static final int RQS_LOADIMAGE = 1;
    private Button btnLoad, btnDetFace;
    private ImageView imgView;
    private Bitmap myBitmap;
    private TextView det;
    private Button song;
    private Button motivate;
    private Button joke;
    private Button hungry;
    private Button news;
    private ConstraintLayout mLayout;
     int i=1;
    private Face thisFace;
    TextToSpeech tts;
    String spp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle b;
        b = getIntent().getExtras();
        spp = b.getString("age");

        btnLoad = (Button)findViewById(R.id.btnLoad);
        btnDetFace = (Button)findViewById(R.id.btnDetectFace);
        imgView = (ImageView)findViewById(R.id.imgview);
        song = findViewById(R.id.song);
        motivate = findViewById(R.id.motivate);
        joke = findViewById(R.id.joke);
        hungry = findViewById(R.id.hungry);
        news = findViewById(R.id.news);
        mLayout = findViewById(R.id.handles);
        det = findViewById(R.id.ans);
        det.setVisibility(View.GONE);
        mLayout.setVisibility(View.GONE);

        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        ConvertTextToSpeech("Welcome to eemotell ,We have adjusted the app according to your age that is"+spp);
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });




        //t1.speak("Welcome user, we have adjusted your profile according to your age"+name,TextToSpeech.QUEUE_FLUSH,null);

        btnLoad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, RQS_LOADIMAGE);
                det.setText("");
                mLayout.setVisibility(View.GONE);
            }
        });

        btnDetFace.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                det.setVisibility(View.VISIBLE);

                if(myBitmap == null){
                    Toast.makeText(Home.this,
                            "First , Select the Image",
                            Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Home.this, "Please Wait...", Toast.LENGTH_SHORT).show();
                    detectFace();


                }
            }
        });


       song.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               // If the music is playing
         if(thisFace.getIsSmilingProbability()<0.5f){
             Uri uri = Uri.parse("https://www.youtube.com/watch?v=zMzN9sIFI9g&list=PLhZKQ5svhl7hdcQ-gnEToPNq1eJgTKdGP"); // missing 'http://' will cause crashed
             Intent intent = new Intent(Intent.ACTION_VIEW, uri);
             startActivity(intent);
         }
         else
             {
                 if(thisFace.getIsSmilingProbability()<0.5f){
                     Uri uri = Uri.parse("https://www.youtube.com/watch?v=kUjKxtJd21E&start_radio=1&list=RDQM2XaYU4g-blk"); // missing 'http://' will cause crashed
                     Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                     startActivity(intent);

         } }

           }
       });

        joke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("https://www.youtube.com/watch?v=whwQqMMyRPU&list=PLCEWvu-a4oZ-oeRr2EpLg6FTzmMgY35ox");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        hungry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.zomato.com/ncr"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.indiatoday.in/news.html"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        motivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=psgjh9jFyMs&list=PLT6xVd5QSO4iB2iqJc3yk6AwPvpv8MM6S"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQS_LOADIMAGE
                && resultCode == RESULT_OK){

            if(myBitmap != null){
                myBitmap.recycle();
            }

            try {
                InputStream inputStream =
                        getContentResolver().openInputStream(data.getData());
                myBitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                imgView.setImageBitmap(myBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    reference:
    https://search-codelabs.appspot.com/codelabs/face-detection
     */
    private void detectFace(){

        //Create a Paint object for drawing with
        Paint myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(5);
        myRectPaint.setColor(Color.GREEN);
        myRectPaint.setStyle(Paint.Style.STROKE);

        Paint landmarksPaint = new Paint();
        landmarksPaint.setStrokeWidth(10);
        landmarksPaint.setColor(Color.RED);
        landmarksPaint.setStyle(Paint.Style.STROKE);

        Paint smilingPaint = new Paint();
        smilingPaint.setStrokeWidth(4);
        smilingPaint.setColor(Color.YELLOW);
        smilingPaint.setStyle(Paint.Style.STROKE);

        boolean somebodySmiling = false;

        //Create a Canvas object for drawing on
        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);

        //Detect the Faces

        //!!!
        //Cannot resolve method setTrackingEnabled(boolean)
        //FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext()).build();
        //faceDetector.setTrackingEnabled(false);

        FaceDetector faceDetector =
                new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                        .build();

        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);

        //Draw Rectangles on the Faces
        for(int i=0; i<faces.size(); i++) {
            thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();
            tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);

            //get Landmarks for the first face
            List<Landmark> landmarks = thisFace.getLandmarks();
            for(int l=0; l<landmarks.size(); l++){
                PointF pos = landmarks.get(l).getPosition();
                tempCanvas.drawPoint(pos.x, pos.y, landmarksPaint);
            }

            //check if this face is Smiling
            final float smilingAcceptProbability = 0.5f;
            float smilingProbability = thisFace.getIsSmilingProbability();
            if(smilingProbability > smilingAcceptProbability){
                tempCanvas.drawOval(new RectF(x1, y1, x2, y2), smilingPaint);
                somebodySmiling = true;
            }
        }

        imgView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

        if(faces.size()==0){
            det.setText("No Faces Found");
            det.setText("No Faces Found");

        }

        else {

            if (somebodySmiling) {
                String sp = "I guess , You are feeling Happy ";
                det.setText(sp);
                ConvertTextToSpeech(sp +"What can i do for you");
            }
//            else if(){
//
//            }

            else {
                String sp = "I guess , You are feeling Sad";
                det.setText(sp);
                ConvertTextToSpeech(sp+"what can i do for you");
            }
            mLayout.setVisibility(View.VISIBLE);
        }
    }
    private void setDialog(boolean show){
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        //View view = getLayoutInflater().inflate(R.layout.progress);
        builder.setView(R.layout.progress);
        Dialog dialog = builder.create();
        if (show)dialog.show();
        else dialog.dismiss();
    }

//    public void onPause(){
//        if(tts !=null){
//            tts.stop();
//            tts.shutdown();
//        }
//        super.onPause();
//    }
    private void ConvertTextToSpeech( String text) {
        // TODO Auto-generated method stub
        if(text==null||"".equals(text))
        {
            text = "Content not available";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }else
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}





