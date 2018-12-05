package com.example.profilemaker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class EditProfile extends AppCompatActivity {
    String profileName;
    ArrayList<String> sportsList;
    ArrayList<String> teamList;
    static String fileName = "profileDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        try {
            setProfileDetails();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTextFields();
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
        TextView profileNameText = findViewById(R.id.profileNameEditContent);
        profileNameText.setText(profileName.trim());

        TextView sportsListText = findViewById(R.id.listOfSportsEditContent);
        String sportText = "";
        for(String sport : sportsList.toArray(new String[sportsList.size()])){
            if(sport != "" || sport != null) {
                sportText += sport + "\n";
            }
        }
        sportsListText.setText(sportText.trim());

        TextView teamListText = findViewById(R.id.listOfTeamsEditContent);
        String teamText = "";
        for(String team : teamList.toArray(new String[teamList.size()])){
            if(team != "" || team != null) {
                teamText += team + "\n";
            }
        }
        teamListText.setText(teamText.trim());
    }


    /**
     * This is the method for when the save changes button is pressed
     * @param view
     * @throws IOException
     */
    public void returnToDetailsAction(View view) throws IOException {
        updateProfileFile();
        Intent intent = new Intent(this, ProfileDetails.class);
        startActivity(intent);
    }

    /**
     * Updates the profile file based on the information in the edit fields
     * @throws IOException
     */
    public void updateProfileFile() throws IOException {
        File file = new File(this.getFilesDir(), fileName);
        String newProfile = "";

        EditText profileName = findViewById(R.id.profileNameEditContent);
        EditText sportsList = findViewById(R.id.listOfSportsEditContent);
        EditText teamList = findViewById(R.id.listOfTeamsEditContent);
        newProfile += "<profileName>\n";
        newProfile += profileName.getText().toString().trim() + "\n";
        newProfile += "<sportsList>\n";
        newProfile += sportsList.getText().toString().replace(" ", "").replace("\n\n", "\n").trim() + "\n";
        newProfile += "<teamList>\n";
        newProfile += teamList.getText().toString().replace(" ", "").replace("\n\n", "\n").trim();

        FileWriter writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write(newProfile);
        bw.close();
    }

}
