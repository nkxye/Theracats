package com.bilonvaldez.theracats;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatRandomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatRandomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String firebaseID = "";
    private Cat currentCat;
    private FirebaseDatabase fdbInstance;
    Button btnShuffle;
    Button btnSave;
    ImageView imgCat;

    public CatRandomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CatRandomFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static CatRandomFragment newInstance(String param1, String param2) {
        CatRandomFragment fragment = new CatRandomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        firebaseID = task.getResult();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cat_random, container, false);
        btnShuffle = v.findViewById(R.id.btnShuffle);
        btnSave = v.findViewById(R.id.btnSave);
        imgCat = v.findViewById(R.id.imgCat);
        MyAsyncTask asyncTask = new MyAsyncTask();
        asyncTask.execute();

        btnShuffle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnSave.setEnabled(false);
                btnShuffle.setEnabled(false);
                MyAsyncTask asyncTask = new MyAsyncTask();
                asyncTask.execute();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v)
            {
                btnSave.setEnabled(false);
                btnShuffle.setEnabled(false);
                btnSave.setBackgroundColor(Color.GREEN);
                btnSave.setTextColor(Color.WHITE);
                btnSave.setText("Saved!");
                saveCatData(currentCat);
                MyAsyncTask asyncTask = new MyAsyncTask();
                asyncTask.execute();
            }
        });

        return v;
    }

    private void saveCatData(Cat catRecord) {
        fdbInstance = FirebaseDatabase.getInstance();
        DatabaseReference dbref = fdbInstance.getReference("cats");
        catRecord.setUniqueId(firebaseID);
        String catId = dbref.push().getKey();
        if (catId != null) {
            dbref.child(catId).setValue(catRecord);
        } else {
            Log.d("Theracat Dev", "Firebase Database Error has occurred.");
        }
    }

    public class MyAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String outcome = "";

            try {
                Log.d("Theracat Dev", "inside doInBackground");

                String apiUrl = "https://api.thecatapi.com/v1/images/search";
                URL catApiEndpoint = new URL(apiUrl);
                HttpsURLConnection myConnection = (HttpsURLConnection) catApiEndpoint.openConnection();
                myConnection.setRequestProperty("x-api-key", "3bd141b4-bbe4-404c-998d-0935f7454464");

                if (myConnection.getResponseCode() == 200) {
                    outcome = "Connection Successful!";
                    Log.d("Theracat Dev", "Processing data from API");
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody,
                            StandardCharsets.UTF_8);
                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    jsonReader.beginArray();

                    String imageUrl = "";
                    String imageId = "";

                    while(jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        while(jsonReader.hasNext()) {
                            String key = jsonReader.nextName();

                            if (key.equals("id")) {
                                imageId = jsonReader.nextString();
                            } else if (key.equals("url")) {
                                imageUrl = jsonReader.nextString();
                            } else {
                                jsonReader.skipValue();
                            }
                        }
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();

                    String createdAt = new java.util.Date().toString();
                    currentCat = new Cat(imageUrl, imageId, createdAt);
                } else {
                    outcome = "Connection Failed!";
                    Log.d("Theracat Dev", "Cannot connect to the API");
                }
            } catch(Exception e) {
                outcome = "Connection Failed!";
                e.printStackTrace();
                return e.getMessage();
            }

            return outcome;
        }

        @SuppressLint("SetTextI18n")
        public void onPostExecute(String s) {
            btnSave.setEnabled(true);
            btnShuffle.setEnabled(true);
            btnSave.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btnSave.setText("Yes, save it!");
            Picasso.get().load(currentCat.getImageUrl()).into(imgCat);
        }
    }
}