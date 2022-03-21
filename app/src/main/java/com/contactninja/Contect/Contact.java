package com.contactninja.Contect;
import com.contactninja.Utils.Global;

import java.util.ArrayList;

public class Contact {
    public String id="";
    public String name="";
    public String last_name="";
    public ArrayList<ContactEmail> emails=new ArrayList<>();
    public ArrayList<ContactPhone> numbers=new ArrayList<>();

    public Contact(String id, String name,String last_name) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.emails = new ArrayList<ContactEmail>();
        this.numbers = new ArrayList<ContactPhone>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public ArrayList<ContactEmail> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<ContactEmail> emails) {
        this.emails = emails;
    }

    public ArrayList<ContactPhone> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<ContactPhone> numbers) {
        this.numbers = numbers;
    }



    @Override
    public String toString() {
        String result = name;
        if(Global.IsNotNull(result)){
            if (numbers.size() > 0) {
                ContactPhone number = numbers.get(0);
                result += " (" + number.number + " - " + number.type + ")";
            }
            if (emails.size() > 0) {
                ContactEmail email = emails.get(0);
                result += " [" + email.address + " - " + email.type + "]";
            }
        }
        return result;
    }

    public void addEmail(String address, String type) {
        emails.add(new ContactEmail(address, type));
    }

    public void addNumber(String number, String type,String countryCode) {
        numbers.add(new ContactPhone(number, type,countryCode));
    }
}
