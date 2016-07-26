package alexwolf.cs.dartmouth.edu.stressmeter;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;


/**
 * Created by alexwolf on 1/21/16.
 */
public class ImageChooseFragment extends Fragment {
    View inflatedView;
    Button mChangeImagesButton;
    int mGridIdNum;
    final String Grid_ID_Number = "Grid_ID_Number";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //get saved grid if exists
        if (savedInstanceState != null) {
            //get Grid Id Number for Picture loading
            mGridIdNum = savedInstanceState.getInt(Grid_ID_Number);
        }
        //set grid view to 1
        else {
            mGridIdNum = 1;
        }

        //inflate the fragment to the view
        inflatedView = inflater.inflate(R.layout.image_choose, container, false);

        //Set up the More images button with onClick Listener
        mChangeImagesButton = (Button) inflatedView.findViewById(R.id.changeImages);
        mChangeImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //change Grid_ID_Number
            public void onClick(View v) {
                if (mGridIdNum == 1 || mGridIdNum == 2) {
                    mGridIdNum += 1;

                    //set up new adptor for new grid
                    setUpGridView(mGridIdNum);

                } else {
                    mGridIdNum = 1;

                    //set up new adptor for new grid
                    setUpGridView(mGridIdNum);
                }

            }
        });

        //set up the gridView with proper images
        setUpGridView(1);

        //return the inflated view
        return inflatedView;

    }

    //sets up the grid view and specific adaptor
    public void setUpGridView(final int mGridIdNum) {

        final MainActivity main = (MainActivity) getActivity();

        //get the gridview and create the adapter
        GridView gridview = (GridView) inflatedView.findViewById(R.id.gridview);
        ImageAdapter ImgAd = new ImageAdapter(this.getActivity());

        //set the Grid number id to the adapter
        ImgAd.mGridId = mGridIdNum;

        //set adapater to the gridview
        gridview.setAdapter(ImgAd);

        //set up on click listen for each image
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                //stop music player
                main.getMediaPlayer().release();

                //turn off vibrator
                main.v.cancel();

                //Create intent to Launch Image Confirm Activity n
                Intent i = new Intent(getActivity(), ImageConfirm.class);

                //add position of image and grid number to the intent
                i.putExtra("Image_Position", position);
                i.putExtra("Grid_Number", mGridIdNum);

                //Start the activity for a result
                startActivity(i);
            }
        });

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        //save the current grid on the View
        outState.putInt(Grid_ID_Number, mGridIdNum);
        super.onSaveInstanceState(outState);

    }


}