package edu.dartmouth.cs.alexwolf.myruns.backend;


import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.alexwolf.myruns.backend.data.EntryDatastore;
import edu.dartmouth.cs.alexwolf.myruns.backend.data.ExerciseEntry;

/**
 * Created by alexwolf on 2/24/16.
 */
public class AddServlet extends HttpServlet {

    //create the logger
    private static final Logger mLogger = Logger
            .getLogger(AddServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //get all data
        ArrayList<ExerciseEntry> entries= EntryDatastore.query("");

        //delete all entries
        for(ExerciseEntry entry: entries){

            EntryDatastore.delete(entry.mId);

        }

        //get JSONrray String from app
        String data= req.getParameter("entries");

        //cast string to JSON Array
        JSONArray jsonArray= null;
        try {
            jsonArray = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonArray = new JSONArray();
        }

        //loop through all exercise entries
        for(int i=0; i<jsonArray.length(); i++){

            //get the entry in JSON
            JSONObject entryJSON=null;
            try {
                entryJSON= (JSONObject)jsonArray.get(i);
            } catch (com.google.appengine.labs.repackaged.org.json.JSONException e) {
                e.printStackTrace();
            }

            //get the id
            String id= null;
            try {
                id = (String) entryJSON.get(ExerciseEntry.FIELD_ID_NAME);
            } catch (com.google.appengine.labs.repackaged.org.json.JSONException e) {
                e.printStackTrace();
            }

            //create new exercise entry
            ExerciseEntry entry= null;
            try {
                entry = new ExerciseEntry(
                        id,
                        (String) entryJSON.get(ExerciseEntry.FIELD_NAME_ACTIVITY),
                        (String)  entryJSON.get(ExerciseEntry.FIELD_NAME_DURATION),
                        (String) entryJSON.get(ExerciseEntry.FIELD_NAME_DISTANCE),
                        (String) entryJSON.get(ExerciseEntry.FIELD_NAME_DATE_TIME),
                        (String)  entryJSON.get(ExerciseEntry.FIELD_NAME_AVG_SPEED),
                        (String) entryJSON.get(ExerciseEntry.FIELD_NAME_CALORIES),
                        (String)  entryJSON.get(ExerciseEntry.FIELD_NAME_CLIMB),
                        (String) entryJSON.get(ExerciseEntry.FIELD_NAME_HEARTRATE),
                        (String) entryJSON.get(ExerciseEntry.FIELD_NAME_INPUT_TYPE),
                        (String) entryJSON.get(ExerciseEntry.FIELD_NAME_COMMENT)

                );
            } catch (com.google.appengine.labs.repackaged.org.json.JSONException e) {
                e.printStackTrace();
            }

            //add entry to databse
            boolean ret =EntryDatastore.add(entry);

            //if added
            if (ret) {
                req.setAttribute("_retStr", "Add contact " + id + " succ");
                MessagingEndpoint msg = new MessagingEndpoint();
                msg.sendMessage("Added");
            }

            //if not added
            else {
                req.setAttribute("_retStr", id + " exists");
            }
        }

        //send request to start query servlet
        resp.sendRedirect("/query.do");

    }

    //post calls doGet
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        doGet(req, resp);
    }

}
