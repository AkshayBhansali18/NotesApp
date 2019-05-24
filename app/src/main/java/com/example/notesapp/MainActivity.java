package com.example.notesapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    int priority;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionref = db.collection("Collection");
    EditText editText_title, editText_description, editText_priority;
    TextView textView_display;
    ArrayList<Note> notes = new ArrayList<>();
QueryDocumentSnapshot lastresult;
    String title, description;
    Button saveButton, button_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_display = (TextView) findViewById(R.id.textView_display);
        editText_title = (EditText) findViewById(R.id.editText_title);
        editText_priority = (EditText) findViewById(R.id.editText_priority);
        editText_description = (EditText) findViewById(R.id.editText_description);
        saveButton = (Button) findViewById(R.id.saveButton);
        button_load = (Button) findViewById(R.id.button_load);
        textView_display.setMovementMethod(new ScrollingMovementMethod());

    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
               String data="";
                for(DocumentChange dc:queryDocumentSnapshots.getDocumentChanges())
                {
                    int oldindex=dc.getOldIndex();
                    int newindex=dc.getNewIndex();
                    QueryDocumentSnapshot snapshots=dc.getDocument();
                   String id=snapshots.getId();
                    long priority=snapshots.getLong("priority");
                    String title=snapshots.getString("title");
                    String desc=snapshots.getString("description");
                    data+=id+"\n"+priority+"\n"+title+"\n"+desc+"\n"+oldindex+"\n"+newindex+"\n";

                }
                textView_display.setText(data);
            }
        });

    }

    public void saveData(View view) {
        title = editText_title.getText().toString();
        description = editText_description.getText().toString();
        priority = Integer.parseInt(editText_priority.getText().toString());

        Note note = new Note(title, description, priority);
        collectionref.add(note).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful())
                    Toast.makeText(MainActivity.this, "Successfully added note", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Note could not be added", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void loadData(View view) {
        QuerySnapshot querySnapshot;
        int limit=3;
        Query query;
        if(lastresult==null)
        {
            query=collectionref.orderBy("priority").limit(3);
        }
        else
        {
            query=collectionref.orderBy("priority").limit(limit+3);
        }
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String data="";
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots)
                {

                Note note = queryDocumentSnapshot.toObject(Note.class);
                String id = queryDocumentSnapshot.getId();
                note.setDocumentId(id);
                int priority = note.getPriority();
                String title = note.getTitle();
                String desc = note.getDescription();
                String val=note.getDocumentId();
                data = data + "Title " + title + "\n " + "Description " + desc + "\n" + " Priority " + priority + "\n"+val+ " ";
                   lastresult= (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1);
                    textView_display.append(data + "\n");
                }



            }
        });

    }
}




