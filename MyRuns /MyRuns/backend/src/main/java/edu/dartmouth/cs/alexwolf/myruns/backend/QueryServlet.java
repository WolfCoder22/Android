package edu.dartmouth.cs.alexwolf.myruns.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.alexwolf.myruns.backend.data.EntryDatastore;
import edu.dartmouth.cs.alexwolf.myruns.backend.data.ExerciseEntry;

/**
 * Created by alexwolf on 2/25/16.
 */
public class QueryServlet extends HttpServlet {

    //create the logger
    private static final Logger mLogger = Logger
            .getLogger(AddServlet.class.getName());


    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        //get all entrys in DB
        ArrayList<ExerciseEntry> entries= EntryDatastore.query("");

        //get writer
        PrintWriter writer = resp.getWriter();

        //create Header and page title
        writer.append("<html> <head> <title> Exercise Entry Table </title> </head> <body>");
        writer.append("<h1>Alex Wolf's Exercise Entries List </h1>");

        //make first table row with contacts
        writer.append("<table style=\"width:100%\" border=\"1\"> <tr>");
        writer.append("<td> ID </td>");
        writer.append("<td> Input Type </td>");
        writer.append("<td> Activity Type </td>");
        writer.append("<td> Date Time </td>");
        writer.append("<td> Duration </td>");
        writer.append("<td> Distance </td>");
        writer.append("<td> Average Speed </td>");
        writer.append("<td> Calories </td>");
        writer.append("<td> Climb </td>");
        writer.append("<td> Heart Rate </td>");
        writer.append("<td> Comment </td>");
        writer.append("<td>  </td> </tr>");

        //for each entry in the databse create a row with their data
        for(ExerciseEntry entry: entries){

            writer.append("<tr> <td>"+entry.mId+ "</td>");
            writer.append("<td> "+entry.mInputType+" </td>");
            writer.append("<td>" +entry.mActivityType+ "</td>");
            writer.append("<td>" +entry.mDateTime+ "</td>");
            writer.append("<td>" +entry.mDuration+ "</td>");
            writer.append("<td>" +entry.mDistance+ "</td>");
            writer.append("<td>" +entry.mAvgSpeed+ "</td>");
            writer.append("<td>" +entry.mCalorie+ "</td>");
            writer.append("<td>" +entry.mClimb+ "</td>");
            writer.append("<td>" +entry.mHeartRate+ "</td>");
            writer.append("<td>" +entry.mComment+ "</td>");

            //add delete button which return id on click to deleteServlet
            writer.append("<td> <form action='/delete.do' method =\"post\">" +
                    "<input type='submit' value='Delete'>" +
                    "<input type='hidden' name='id' value='"+entry.mId+"'></form></td> </tr>");
        }

        //finish html tags
        writer.append("</table></body></html>");

    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        doGet(req, resp);
    }
}
