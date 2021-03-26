package lab03.eim.systems.cs.pub.phonedialer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class PhoneDialerActivity extends AppCompatActivity {

    /**
     * listener class for handling push button events
     * @ BackspaceButtonClickListener - the correction button
     * @ CallImageButtonClickListener - the call button to initiate phone call
     * @ HangupImageButtonClickListener - the hang-up button to close phone call
     */
    private BackspaceButtonClickListener backspaceButtonClickListener = new BackspaceButtonClickListener();
    private CallImageButtonClickListener callImageButtonClickListener = new CallImageButtonClickListener();
    private HangupImageButtonClickListener hangupImageButtonClickListener = new HangupImageButtonClickListener();
    private GenericButtonClickListener genericButtonClickListener = new GenericButtonClickListener();

    // TODO 5 - laboratory 4
    private ContactsManagerClickListener contactsManagerClickListener = new ContactsManagerClickListener();

    /**
     * phone number showed in text label after pressing the keys on the phone
     * all buttons on the phone (0,1,...9,*,#)
     * the image button imported - call, back, hang-up
     */
    private EditText phoneNumberEditText;
    private Button genericButton;
    private ImageButton callImageButton;
    private ImageButton hangupImageButton;
    private ImageButton backspaceImageButton;

    /**
     * contact manager button that launches the ContactsManager app
     * TODO 5
     * laboratory 4
     */
    private ImageButton contactsImageButton;

    private class BackspaceButtonClickListener implements View.OnClickListener {
        // if the phone number isn't empty, the last character'll be deleted
        public void onClick(View view) {
            String nr = phoneNumberEditText.getText().toString();
            if (nr.length() > 0) {
                nr = nr.substring(0, nr.length() - 1);
                phoneNumberEditText.setText(nr);
            }
        }
    }

    private class CallImageButtonClickListener implements View.OnClickListener {
        public void onClick(View view) {
            if (ContextCompat.checkSelfPermission(PhoneDialerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PhoneDialerActivity.this,
                                                    new String[]{Manifest.permission.CALL_PHONE},
                                                    Constants.PERMISSION_REQUEST_CALL_PHONE);
            }

            else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumberEditText.getText().toString()));
                startActivity(intent);
            }
        }
    }

    private class HangupImageButtonClickListener implements View.OnClickListener {
        public void onClick(View view) {
            // call finish method in order to close the activity
            finish();
        }
    }

    private class ContactsManagerClickListener implements  View.OnClickListener {
        public void onClick(View view) {
            String nr = phoneNumberEditText.getText().toString();
            if (nr.length() > 0) {
                Intent intent = new Intent("ro.pub.cs.systems.eim.lab04.contactsmanager.intent.action.ContactsManagerActivity");
                intent.putExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY", nr);
                startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
            }

            else {
                Toast.makeText(getApplication(), getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GenericButtonClickListener implements View.OnClickListener {
        public void onClick(View view) {
            String nr = phoneNumberEditText.getText().toString();
            String btn = ((Button)view).getText().toString();
            phoneNumberEditText.setText(String.format("%s%s", nr, btn));
        }
    }

    //@SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dialer);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        backspaceImageButton = findViewById(R.id.backspaceImageButton);
        callImageButton = findViewById(R.id.callImageButton);
        hangupImageButton = findViewById(R.id.hangupImageButton);

        backspaceImageButton.setOnClickListener(backspaceButtonClickListener);
        callImageButton.setOnClickListener(callImageButtonClickListener);
        hangupImageButton.setOnClickListener(hangupImageButtonClickListener);

        // add the corresponding symbol to the phone number in order to be dialed
        for (int btn : Constants.buttonIds) {
            genericButton = (Button)findViewById(btn);
            genericButton.setOnClickListener(genericButtonClickListener);
        }

        /**
         * TODO 5 - laboratory 4
         */
        contactsImageButton = findViewById(R.id.contactsManager);
        contactsImageButton.setOnClickListener(contactsManagerClickListener);
    }

    /**
     * TODO 6 - laboratory 4
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Constants.CONTACTS_MANAGER_REQUEST_CODE) {
            Toast.makeText(this, "Activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }
}
