package edu.dartmouth.cs.alexwolf.myruns.backend;

import java.io.IOException;
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
public class DeleteServlet extends HttpServlet {

    //create the logger
    private static final Logger mLogger = Logger
            .getLogger(AddServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //get ID from delete button
        String entryId = req.getParameter(ExerciseEntry.FIELD_ID_NAME);

        //delete entry from database
        EntryDatastore.delete(entryId);

        //send message to app with the deleted entry id
        MessagingEndpoint msg = new MessagingEndpoint();
        msg.sendMessage(entryId);

        //requery and update website table
        resp.sendRedirect("/query.do");

    }

    //post calls doGet
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        doGet(req, resp);
    }
}
