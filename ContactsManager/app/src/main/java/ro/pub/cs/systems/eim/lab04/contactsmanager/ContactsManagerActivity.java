package ro.pub.cs.systems.eim.lab04.contactsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {
    final public static int CONTACTS_MANAGER_REQUEST_CODE = 2017;

    private Button showAdditionalDetails;
    private Button save, cancel;
    private LinearLayout additionalFieldsContainer;
    private static final String hide = "HIDE ADDITIONAL FIELDS";

    // fields showed default
    private EditText nameText, phoneText, emailText, addressText;
    // fields hide
    private EditText jobTitleText, companyText, websiteText, imText;

    /**
     * TODO 4.2
     * Listener class for buttons
     * implements methods View.OnClickListener and override method onCLick depending on button's id
     */
    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {
        public void onClick(View v) {
            if (v.getId() == R.id.show_additional_fields) {
                int visibility = additionalFieldsContainer.getVisibility();
                // check the visibility of the hidden fields
                if (visibility == View.VISIBLE) {
                    showAdditionalDetails.setText(showAdditionalDetails.getText().toString());
                    additionalFieldsContainer.setVisibility(View.INVISIBLE);
                }
                else {
                    showAdditionalDetails.setText(hide);
                    additionalFieldsContainer.setVisibility(View.VISIBLE);
                }
            }

            // store the contacts
            else if (v.getId() == R.id.save) {
                /**
                 * get text from all EditText fields
                 * the first four - default fields
                 * the last four - hidden fields
                 */
                String name = nameText.getText().toString();
                String phone = phoneText.getText().toString();
                String email = emailText.getText().toString();
                String address = addressText.getText().toString();

                String jobTitle = jobTitleText.getText().toString();
                String company = companyText.getText().toString();
                String website = websiteText.getText().toString();
                String im = imText.getText().toString();

                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                // complete the default information into extra fields of intent
                if (name != null)
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                if (phone != null)
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, phone);
                if (email != null)
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, email);
                if (address != null)
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, address);

                // complete the hidden information into extra fields of intent
                if (jobTitle != null)
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, jobTitle);
                if (company != null)
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, company);
                if (website != null)
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, website);
                if (im != null)
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, im);

                /**
                 * insert contacts contract
                 * we have to set permission of read and write contacts in AndroidManifest
                 */
                ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
                if (website != null) {
                    ContentValues websiteRow = new ContentValues();
                    websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                    websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                    contactData.add(websiteRow);
                }
                if (im != null) {
                    ContentValues imRow = new ContentValues();
                    imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                    imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                    contactData.add(imRow);
                }
                intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
                //startActivity(intent);
                // TODO 6
                startActivityForResult(intent, CONTACTS_MANAGER_REQUEST_CODE);
            }

            // finish the Android application
            else if (v.getId() == R.id.cancel) {
                setResult(Activity.RESULT_CANCELED, new Intent()); // TODO 6
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        /**
         * TODO 4.1
         * get references for buttons save, cancel, show/hide additional details
         * use findViewById method
         */
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        showAdditionalDetails = findViewById(R.id.show_additional_fields);
        additionalFieldsContainer = findViewById(R.id.additional_fields_container);

        /**
         * TODO 4.3
         * use method setOnCLickListener in order to have an instance for the listener class
         * handling of button access
         */
        save.setOnClickListener(buttonClickListener);
        cancel.setOnClickListener(buttonClickListener);
        showAdditionalDetails.setOnClickListener(buttonClickListener);

        /**
         * TODO 6
         * check the intention which the application starts
         * if the intention is not null => information is taken from the extra section identifiably
         * by a specific key
         */
        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * TODO 6
     * check the application code and return the result when the native app is closed
     * @param requestCode - the code reported by app
     * @param resultCode - to set the code
     * @param intent - new intention
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == CONTACTS_MANAGER_REQUEST_CODE) {
            setResult(resultCode, new Intent());
            finish();
        }
    }
}
