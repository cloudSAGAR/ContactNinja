package com.contactninja.Contect;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;

import com.contactninja.MainActivity;
import com.contactninja.Utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

// new ContactFetcher(this).fetchAll();
public class ContactFetcher {

    private final Context context;

    public ContactFetcher(Context c) {
        this.context = c;
    }

    public ArrayList<Contact> fetchAll() {
        String[] projectionFields = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };
        ArrayList<Contact> listContacts = new ArrayList<>();
        CursorLoader cursorLoader = new CursorLoader(context,
                ContactsContract.Contacts.CONTENT_URI,
                projectionFields, // the columns to retrieve
                null, // the selection criteria (none)
                null, // the selection args (none)
                ContactsContract.Contacts.DISPLAY_NAME + " ASC" // the sort order (default)
        );

        Cursor c = cursorLoader.loadInBackground();

        final Map<String, Contact> contactsMap = new HashMap<>(c.getCount());

        if (c.moveToFirst()) {

            int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
            int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            do {
                String contactId = c.getString(idIndex);
                String contactDisplayName = c.getString(nameIndex);
                String first_name="";
                String last_name="";
                try {
                    String[] split_name=contactDisplayName.split(" ");

                    for (int i=0;i<split_name.length;i++)
                    {
                        if (i==0)
                        {
                            first_name=split_name[0];
                        }
                        else {
                            if (last_name.equals(""))
                            {
                                last_name=split_name[i];
                            }
                            else {
                                last_name=last_name+split_name[i];
                            }

                        }

                    }

                }
                catch (Exception e)
                {

                }

                Contact contact = new Contact(contactId, first_name,last_name);
                contactsMap.put(contactId, contact);
                listContacts.add(contact);
            } while (c.moveToNext());
        }

        c.close();

        matchContactNumbers(contactsMap);
        matchContactEmails(contactsMap);

        return listContacts;
    }

    public void matchContactNumbers(Map<String, Contact> contactsMap) {
        // Get numbers
        final String[] numberProjection = new String[]{
                Phone.NUMBER,
                Phone.TYPE,
                Phone.CONTACT_ID,
        };

        Cursor phone = new CursorLoader(context,
                Phone.CONTENT_URI,
                numberProjection,
                null,
                null,
                null).loadInBackground();

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
            final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);
            final int contactIdColumnIndex = phone.getColumnIndex(Phone.CONTACT_ID);
            TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            String country = tm.getNetworkCountryIso();
            String code1="";
            int countryCode = 0;
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(context);
            while (!phone.isAfterLast()) {
                String number = phone.getString(contactNumberColumnIndex);
                    final String contactId = phone.getString(contactIdColumnIndex);
                    Contact contact = contactsMap.get(contactId);

                    if (contact == null) {
                        continue;
                    }
                    final int type = phone.getInt(contactTypeColumnIndex);
                    String customLabel = "Custom";
                    CharSequence phoneType = Phone.getTypeLabel(context.getResources(), type, customLabel);
                    Phonenumber.PhoneNumber numberProto = null;
                    try {
                        numberProto = phoneUtil.parse(number, country.toUpperCase());
                        countryCode = numberProto.getCountryCode();
                        code1=phoneUtil.getRegionCodeForCountryCode(countryCode);
                        number = number.replace(" ", "");
                        number = number.replace("-", "");
                        if (!number.contains("+")) {
                            number = "+" + countryCode + number;
                        }
                    } catch (NumberParseException e) {
                        e.printStackTrace();
                    }
                        contact.addNumber(number, phoneType.toString(),code1);
                phone.moveToNext();
            }
        }

        phone.close();
    }

    public void matchContactEmails(Map<String, Contact> contactsMap) {
        // Get email
        final String[] emailProjection = new String[]{
                Email.DATA,
                Email.TYPE,
                Email.CONTACT_ID,
        };

        Cursor email = new CursorLoader(context,
                Email.CONTENT_URI,
                emailProjection,
                null,
                null,
                null).loadInBackground();

        if (email.moveToFirst()) {
            final int contactEmailColumnIndex = email.getColumnIndex(Email.DATA);
            final int contactTypeColumnIndex = email.getColumnIndex(Email.TYPE);
            final int contactIdColumnsIndex = email.getColumnIndex(Email.CONTACT_ID);

            while (!email.isAfterLast()) {
                final String address = email.getString(contactEmailColumnIndex);
                final String contactId = email.getString(contactIdColumnsIndex);
                final int type = email.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                Contact contact = contactsMap.get(contactId);
                if (contact == null) {
                    continue;
                }
                CharSequence emailType = Email.getTypeLabel(context.getResources(), type, customLabel);
                contact.addEmail(address, emailType.toString());
                email.moveToNext();
            }
        }

        email.close();
    }
}