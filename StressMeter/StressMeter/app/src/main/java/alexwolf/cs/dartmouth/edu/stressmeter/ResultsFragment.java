package alexwolf.cs.dartmouth.edu.stressmeter;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by alexwolf on 1/22/16.
 */
public class ResultsFragment extends Fragment {

    View inflatedView;
    String CSV_PATH;
    TableLayout tableData;
    LineChartView chart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //inflate the fragament to the view
        inflatedView = inflater.inflate(R.layout.results, container,false);

        //retrieve CSV file location for data
        CSV_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/"+ "StressResults.csv";

        //retreive table layout
        tableData= (TableLayout) inflatedView.findViewById(R.id.stressResults);

        //retrieve graph
        chart= (LineChartView) inflatedView.findViewById(R.id.chart);

        //create array for graph points and lines
        List<PointValue> values = new ArrayList<PointValue>();


        //get stress data from csv
        List<String[]>  entries= getStressData(getActivity());

        //chart instance initialized
        int instance=0;

        //for every entry
        for(String[] entry: entries){

            //get data
            String time= entry[0];
            String stressLevel= entry[1];

            //create point for data and it to to graph
            PointValue point= new PointValue(instance, Integer.parseInt(stressLevel));
            values.add(point);

            //create row for table and add textViews to it
            TableRow row= new TableRow(getActivity());
            row.setBackgroundColor(Color.BLACK);
            row.setPadding(2, 2, 2, 2);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            //create text for time
            TextView timeText= new TextView(getActivity());
            timeText.setText(time);
            timeText.setBackgroundColor(Color.WHITE);
            timeText.setTextColor(Color.BLACK);
            timeText.setPadding(5,5,170,5);

            //create text for data
            TextView stressText= new TextView(getActivity());
            stressText.setText(stressLevel);
            stressText.setBackgroundColor(Color.WHITE);
            stressText.setTextColor(Color.BLACK);
            stressText.setPadding(5, 5, 400, 5);

            //add textViews to row
            row.addView(timeText);
            row.addView(stressText);

            //get the table layout and add a new row
            tableData.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            //update insatnce
            instance+=1;

        }

        //create lines on graph for each point
        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);

        //create lines array for chardata
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        //create Y axis
        Axis axisY= new Axis();
        axisY.setName("Stress Level");
        axisY.setHasLines(true);

        //create X axis
        Axis axisX= new Axis();
        axisX.setName("Instance");
        axisX.setHasLines(true);


        //get line chart data and add lines to it
        LineChartData data = new LineChartData();

        //set axis
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        //set lines on chart
        data.setLines(lines);

        //set data to chart
        chart.setLineChartData(data);

        return inflatedView;
    }

    //method to extract data from csv
    public List<String[]> getStressData(Context context){
        //create List of Longs for answers
        List<String[]> entries= new ArrayList<>();

        try {
            FileInputStream csvStream = new FileInputStream(CSV_PATH);
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(csvStream));
            String line;

            //read everyLine
            while ((line = csvReader.readLine()) != null) {

                //get time and stresslevel data
                String[] results=line.split(",");
                entries.add(results);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return entries;

    }
}
