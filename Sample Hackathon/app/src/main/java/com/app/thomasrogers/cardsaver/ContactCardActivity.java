package com.app.thomasrogers.cardsaver;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.Image;
import com.app.thomasrogers.cardsaver.data.Card;
import com.app.thomasrogers.cardsaver.data.CardContract;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactCardActivity extends AppCompatActivity {

    private static final String TAG = ContactCardActivity.class.getSimpleName();

    private LinearLayout mFieldParent;
    private ImageView mCardImage;
    private TextInputEditText mFullName;
    private TextInputEditText mTitle;
    private TextInputEditText mCompanyName;
    private TextInputEditText mEmail;
    private TextInputEditText mWebsite;
    private TextInputEditText mMainAddress;
    private TextInputEditText mAltAddress;
    private TextInputEditText mCSZAddress;
    private List<TextInputEditText> mPhone;
    private ProgressDialog mProgressDialog;
    private List<Spinner> mSpinner;
    private Switch mSaveToContact;
    private TextInputEditText mNotes;

    private Card mCard;
    private int mCardID;
    private String[] mResults;

    private enum Field
    {
        FullName,
        Title,
        Company,
        Email,
        Phone,
        Website,
        MainAddress,
        AltAddress,
        CSZAddress
    }

    private final static String EMPLOYER_PROPERTY = "EMPLOYER";
    private int mPhoneIndex = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCard = getIntent().getParcelableExtra("myCard");
        mCardID = getIntent().getIntExtra("cardID", -1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPhone = new ArrayList<>();
        mSpinner = new ArrayList<>();

        mFieldParent = (LinearLayout) findViewById(R.id.fieldContainer);
        mCardImage = (ImageView) findViewById(R.id.cardImage);
        mFullName = (TextInputEditText) findViewById(R.id.fullName);
        mTitle = (TextInputEditText) findViewById(R.id.title);
        mCompanyName = (TextInputEditText) findViewById(R.id.companyName);
        mEmail = (TextInputEditText) findViewById(R.id.email);
        mWebsite = (TextInputEditText) findViewById(R.id.website);
        mMainAddress = (TextInputEditText) findViewById(R.id.mainAddress);
        mAltAddress = (TextInputEditText) findViewById(R.id.altAddress);
        mCSZAddress = (TextInputEditText) findViewById(R.id.cszAddress);
        mPhone.add((TextInputEditText) findViewById(R.id.phone));
        mSpinner.add((Spinner) findViewById(R.id.typeSpinner));
        mSaveToContact = (Switch) findViewById(R.id.saveToContacts);
        mNotes = (TextInputEditText) findViewById(R.id.notes);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.phone_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.get(0).setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardImage.setImageBitmap(BitmapFactory.decodeByteArray(mCard.getImage(), 0, mCard.getImage().length));
        try {
            showProgressDialog();
            callRekognition(mCard.getImage());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error While Scanning Image", Toast.LENGTH_LONG).show();
            hideProgressDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_card_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
        }

        return super.onOptionsItemSelected(item);
    }

    private void callRekognition(final byte[] image) throws IOException {

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    Float similarityThreshold = 0F;
                    ByteBuffer sourceImageBytes= ByteBuffer.wrap(image);
                    ByteBuffer targetImageBytes= ByteBuffer.wrap(image);

                    CognitoCachingCredentialsProvider credentialsProvider;
                    AWSCredentialsProvider credentials;
                    try {
                        credentialsProvider = new CognitoCachingCredentialsProvider(
                                getApplicationContext(),
                                "us-east-2:40b541c3-e649-406a-ab96-941986fc7045", // Identity pool ID
                                Regions.US_EAST_2 // Region
                        );
                        Map logins = new HashMap();
                        // set the Amazon login token
                        logins.put("us-east-1_Ab129fa8b", "7lhlkkfbfb4q5kpp90urffao");
                        credentials = credentialsProvider.withLogins(logins);
                    } catch (Exception e) {
                        throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                                + "Please make sure that your credentials file is at the correct "
                                + "location (/Users/userid/.aws/credentials), and is in valid format.", e);
                    }

                    AmazonRekognitionClient rekognitionClient = new AmazonRekognitionClient(credentials);

                    Image source=new Image()
                            .withBytes(sourceImageBytes);
                    Image target=new Image()
                            .withBytes(targetImageBytes);

                    CompareFacesRequest request = new CompareFacesRequest()
                            .withSourceImage(source)
                            .withTargetImage(target)
                            .withSimilarityThreshold(similarityThreshold);

                    // Call operation
                    CompareFacesResult compareFacesResult=rekognitionClient.compareFaces(request);


                    // Display results
                    List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
                    for (CompareFacesMatch match: faceDetails){
                        ComparedFace face= match.getFace();
                        BoundingBox position = face.getBoundingBox();
                        System.out.println("Face at " + position.getLeft().toString()
                                + " " + position.getTop()
                                + " matches with " + face.getConfidence().toString()
                                + "% confidence.");

                    }
                    return "Done";
                } catch (Exception e) {
                    Log.d(TAG, "failed to make API request");
                }
                return "Rekognition API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                hideProgressDialog();
            }
        }.execute();
    }

    private void convertResponseToCard(String response, boolean saveDB)
    {
        if (saveDB) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CardContract.CardEntry.COLUMN_ALL_VALUES, response);

            String stringId = Integer.toString(mCardID);
            Uri uri = CardContract.CardEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();

            getContentResolver().update(uri, contentValues, null, null);
        }

        if (response != null) {
            mResults = response.split("\\n");
        }
        else {
            mResults = null;
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
            return;
        }

        mProgressDialog = new ProgressDialog(this, R.style.NewDialog);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        mProgressDialog.dismiss();
    }
}
