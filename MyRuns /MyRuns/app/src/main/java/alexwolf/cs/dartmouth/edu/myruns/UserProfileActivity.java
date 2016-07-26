package alexwolf.cs.dartmouth.edu.myruns;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by alexwolf on 1/17/16.
 */
public class UserProfileActivity extends Activity {

    private String mName;
    private String mEmail;
    private String mClass;
    private String mGender;
    private String mMajor;
    private String mPhone;


    private Button mSaveButton;
    private Button mChangeButton;
    private Button mCancelButton;

    private ImageView mProfilePic;
    private Uri mImageCaptureUri;
    private static final int CAPTURE_IMAGE = 111;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
    public static final int REQUEST_CODE_CROP_PHOTO = 2;
    public static final int PHOTO_PICKED=3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup UI
        setContentView(R.layout.user_profile_layout);

        //declare location of image
        mProfilePic = (ImageView) findViewById(R.id.imageProfile);

        //set up button for changing profile pic
        mChangeButton= (Button)findViewById(R.id.changebutton);
        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_photo(v);
            }
        });

        //set up button for Saving User Info
        mSaveButton= (Button)findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
                Toast.makeText(UserProfileActivity.this, "Profile Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        //Set up button for Canceling input
        mCancelButton= (Button)findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserProfileActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //retrieve current profile pic information
        mProfilePic= (ImageView) findViewById(R.id.imageProfile);
        if (savedInstanceState != null)
            mImageCaptureUri = savedInstanceState
                    .getParcelable(URI_INSTANCE_STATE_KEY);

        //load Profile and profile photo
        loadProfile();
        loadSnap();
    }

    //saves all information entered by the user
    private void saveUserInfo() {

        //retrieve user info
        EditText editText = (EditText) findViewById(R.id.edit_Name);
        mName = editText.getText().toString();

        EditText editText2 = (EditText) findViewById(R.id.user_email);
        mEmail = editText2.getText().toString();

        EditText editText3 = (EditText) findViewById(R.id.user_phone);
        mPhone = editText3.getText().toString();

        EditText editText4 = (EditText) findViewById(R.id.user_class);
        mClass = editText4.getText().toString();

        EditText editText5 = (EditText) findViewById(R.id.user_major);
        mMajor = editText5.getText().toString();

        RadioGroup genderRadio = (RadioGroup) findViewById(R.id.genderRadios);
        int genderID= genderRadio.getCheckedRadioButtonId();
        if(genderID==R.id.radio_male){
            mGender="male";
        }
        else if(genderID==R.id.radio_female){
            mGender="female";
        }


        // Get the shared preferences editor
        String key = getString(R.string.preference_name);
        SharedPreferences prefs = getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //store user information with key-value pairs
        editor.putString(getString(R.string.genderKey),mGender);
        editor.putString(getString(R.string.classKey),mClass);
        editor.putString(getString(R.string.nameKey), mName);
        editor.putString(getString(R.string.phoneKey), mPhone);
        editor.putString(getString(R.string.emailKey),mEmail);
        editor.putString(getString(R.string.majorKey), mMajor);

        //save user info shared preferences
        editor.apply();

    }


    private void loadProfile() {

        //get user information from the shared preferences editor
        String key = getString(R.string.preference_name);
        SharedPreferences prefs = getSharedPreferences(key, MODE_PRIVATE);

        //retrieve edit texts and radio buttons
        EditText editText = (EditText) findViewById(R.id.edit_Name);
        EditText editText2 = (EditText) findViewById(R.id.user_class);
        EditText editText3 = (EditText) findViewById(R.id.user_phone);
        EditText editText4 = (EditText) findViewById(R.id.user_major);
        EditText editText5 = (EditText) findViewById(R.id.user_email);
        RadioButton male = (RadioButton) findViewById(R.id.radio_male);
        RadioButton female = (RadioButton) findViewById(R.id.radio_female);

        //get specific gender data if exists
        if(prefs.contains(getString(R.string.genderKey))) {
            String mGender = prefs.getString(getString(R.string.genderKey), null);
            //check if male or female and toggle correct buttons
            if (mGender.equals("male")) {
                //set male to checker
                male.setChecked(true);
            } else if (mGender.equals("female")) {
                //set female to checked
                female.toggle();
            } else {
                male.setChecked(false);
                female.setChecked(false);
            }
        }

        //if phone data exist get it
        if(prefs.contains(getString(R.string.phoneKey))) {
            String mPhone = prefs.getString(getString(R.string.phoneKey), null);
            //set text on phone edit text
            editText3.setText(mPhone);
        }
        //if class data exists get it
        if(prefs.contains(getString(R.string.classKey))) {
            String mClass= prefs.getString(getString(R.string.classKey),null);
            //set text on class edit text
            editText2.setText(mClass);
        }
        //if major data exists get it
        if(prefs.contains(getString(R.string.majorKey))) {
            String mMajor = prefs.getString(getString(R.string.majorKey), null);
            //set text on major edit text
            editText4.setText(mMajor);
        }
        //if name data exists get it
        if(prefs.contains(getString(R.string.nameKey))) {
            String mName = prefs.getString(getString(R.string.nameKey), null);
            //set name on edit text for name
            editText.setText(mName);
        }
        //if email data exists get it
        if(prefs.contains(getString(R.string.emailKey))) {
            String mEmail = prefs.getString(getString(R.string.emailKey), null);
            //set name on email edit text
            editText5.setText(mEmail);
        }


    }


    //Display dialog to change photo by taking picture or choosing from gallerly
   public void change_photo(View v) {
        // changing the profile image, show the dialog asking the user
        // to choose between taking a picture
        //and selecting from a Gallerly
        // Go to MyRunsDialogFragment for details.
        displayDialog(MyRunsDialogFragment.DIALOG_ID_PHOTO_PICKER);
    }

    //method calls from Dialog for either taking a photo or choosing from gallerly
    public void onPhotoPickerItemSelected(int item) {
        Intent intent;

        switch (item) {
            //user wants to take a photo
            case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_CAMERA:
                // Construct intent to take photo from camera，
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Construct temporary image path and name to save the taken photo
                mImageCaptureUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "tmp_"
                        + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        mImageCaptureUri);
                intent.putExtra("return-data", true);
                try {
                    // Start a camera capturing activity with a result
                    startActivityForResult(intent, CAPTURE_IMAGE);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            //User want to choose photo from a gallerly
            case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_GALLERY:
                //create an intent to pick an image
                Intent pickImage = new Intent(Intent.ACTION_PICK);
                pickImage.setType("image/*");
                startActivityForResult(Intent.createChooser(pickImage, "Select Picture"), PHOTO_PICKED);

            default:
                return;
        }

    }


    //display the dialog for changing the photo
    public void displayDialog(int id) {
        DialogFragment fragment = MyRunsDialogFragment.newInstance(id);
        fragment.show(getFragmentManager(),
                getString(R.string.dialog_fragment_photo_picker));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            //once camera takes photo crop
            case CAPTURE_IMAGE:
                // Send image taken from camera for cropping
                cropImage();
                break;
            //once cropping is done update image view and save
            case REQUEST_CODE_CROP_PHOTO:
                // Update image view after image crop
                Bundle extras = data.getExtras();
                // Set the picture image in UI
                if (extras != null) {
                    Log.d("got image", "l");
                    mProfilePic.setImageURI((Uri) extras.getParcelable("data"));
                    Toast.makeText(UserProfileActivity.this, mProfilePic.getDrawingCache().toString(), Toast.LENGTH_SHORT).show();
                }
                //save image
                saveSnap();
                break;
            //photo was selected from gallerly
            case PHOTO_PICKED:

                //find the image URI and crop the photo
                mImageCaptureUri= data.getData();
                cropImage();



        }
    }

    // Crop and resize the image for profile
    private void cropImage() {
        // Use existing crop activity.
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mImageCaptureUri, IMAGE_UNSPECIFIED);

        // Specify image size
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);

        // Specify aspect ratio, 1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        // REQUEST_CODE_CROP_PHOTO is an integer tag you defined to
        // identify the activity in onActivityResult() when it returns
        startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
    }


    private void loadSnap() {

        // Load profile photo from internal storage
        try {
            FileInputStream fis = openFileInput(getString(R.string.profile_photo_file_name));
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            mProfilePic.setImageBitmap(bmap);
            fis.close();
        } catch (IOException e) {
            // Default profile photo if no photo saved before.
            mProfilePic.setImageResource(R.drawable.default_profile);
        }
    }

    private void saveSnap() {

        // Commit all the changes into preference file
        // Save profile image into internal storage.
        mProfilePic.buildDrawingCache();
        Bitmap bmap = mProfilePic.getDrawingCache();
        //Toast.makeText(UserProfileActivity.this, bmap.toString(), Toast.LENGTH_SHORT).show();
        try {
            FileOutputStream fos = openFileOutput(
                    getString(R.string.profile_photo_file_name), MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // Called before subsequent visible lifetimes
    // for an activity process.
    @Override
    public void onRestart(){
        super.onRestart();

    }

    // Called at the start of the visible lifetime.
    @Override
    public void onStart(){
        super.onStart();
        // Apply any required UI change now that the Activity is visible.
    }

    // Called at the start of the active lifetime.
    @Override
    public void onResume(){
        super.onResume();
    }

    // Called to save UI state changes at the
    // end of the active lifecycle.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the image capture uri before the activity goes into background
        outState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);
    }

    // Called at the end of the active lifetime.
    @Override
    public void onPause(){
        super.onPause();
    }

    // Called at the end of the visible lifetime.
    @Override
    public void onStop(){
        super.onStop();

    }

    // Sometimes called at the end of the full lifetime.
    @Override
    public void onDestroy(){
        super.onDestroy();
    }


}
