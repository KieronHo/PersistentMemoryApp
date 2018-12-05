package com.example.profilemaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProfileDetails extends AppCompatActivity {
    String profileName;
    ArrayList<String> sportsList;
    ArrayList<String> teamList;
    public static String fileName = "profileDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        try {
            setProfileDetails();
            setTextFields();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Extracts the relevant data fields from the stored profile file
     * @throws IOException
     */
    public void setProfileDetails() throws IOException {
        profileName = "unchanged";
        sportsList = new ArrayList<>();
        teamList = new ArrayList<>();


        Context context = this;//is just straight this?
        //gets the directory of the app files
        File directory = context.getFilesDir();
        File file = new File(directory, fileName);

        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        int informationStage = 0;
        while ((line = bufferedReader.readLine()) != null) {
            switch (line) {//change which data fields are filled
                case "<profileName>"://redundant
                    informationStage = 0;
                    break;
                case "<sportsList>":
                    informationStage = 1;
                    break;
                case "<teamList>":
                    informationStage = 2;
                    break;
                default:if(line != "null" && line != "" && line != null) {
                    switch (informationStage) {//fill the data fields
                        case 0:
                            profileName = line;
                            break;
                        case 1:
                            sportsList.add(line);
                            break;
                        case 2:
                            teamList.add(line);
                            break;
                    }
                }
            }
        }
        bufferedReader.close();

    }

    /**
     * Sets the default text to appear in the edit boxes based on extracted parameter values
     */
    public void setTextFields(){
        TextView profileNameText = findViewById(R.id.profileNameContent);
        profileNameText.setText(profileName.trim());

        TextView sportsListText = findViewById(R.id.listOfSportsContent);
        String sportText = "";
        for(String sport : sportsList.toArray(new String[sportsList.size()])){
            if(sport != "" || sport != null) {
                sportText += sport + "\n";
            }
        }
        sportsListText.setText(sportText.trim());

        TextView teamListText = findViewById(R.id.listOfTeamsContent);
        String teamText = "";
        for(String team : teamList.toArray(new String[teamList.size()])){
            if(team != "" || team != null) {
                teamText += team + "\n";
            }
        }
        teamListText.setText(teamText.trim());
    }

    public void editProfileAction(View view){
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }
}
