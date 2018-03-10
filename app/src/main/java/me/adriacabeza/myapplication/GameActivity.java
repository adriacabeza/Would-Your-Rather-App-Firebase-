package me.adriacabeza.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

/**
 * Created by usuario on 30/01/2018.
 */

public class GameActivity extends AppCompatActivity {
    TextView texto1,texto2;
    String key;
    int rand = 0;
    int int1,int2;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    Pregunta pregunta = new Pregunta("Espera...","Espera",0,0);
    private DatabaseReference DBref;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        DBref=FirebaseDatabase.getInstance().getReference();
        loadActivity();

    }

    private void loadActivity(){
        texto1  = (TextView) findViewById(R.id.red);
        texto2 = (TextView) findViewById(R.id.blue);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Random r = new Random();
                rand = (r.nextInt((int) (dataSnapshot.getChildrenCount()+1)));
                pregunta = dataSnapshot.child("pregunta"+rand).getValue(Pregunta.class);
                if(pregunta !=null) {
                    int1 = pregunta.getInt1();
                    int2 = pregunta.getInt2();
                    texto1.setText(pregunta.getOpcion1());
                    texto2.setText(pregunta.getOpcion2());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        texto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int1 = int1+1;
                mDatabase.child("pregunta"+rand).child("int1").setValue(int1);
                showstats();


            }
        });
        texto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int2 = int2+1;
                mDatabase.child("pregunta"+rand).child("int2").setValue(int2);
                showstats();
            }
        });
    }


    private void showstats() {
        int todos = int1+int2;
        double red = 100*int1/todos;
        double blue = 100*int2/todos;
        texto1.setText(texto1.getText().toString() +"\n"+ String.valueOf(red) +
                "%");
        texto2.setText(texto2.getText().toString() +"\n"+ String.valueOf(blue)+ "%");
        texto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadActivity();
            }
        });
        texto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadActivity();
            }
        });
    }

}
