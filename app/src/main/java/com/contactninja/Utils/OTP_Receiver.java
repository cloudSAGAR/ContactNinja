package com.contactninja.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.chaos.view.PinView;

public class OTP_Receiver extends BroadcastReceiver {
    private  static PinView pinview;
    public void setEditText(PinView pinview)
    {
        OTP_Receiver.pinview=pinview;
    }
    // OnReceive will keep trace when sms is been received in mobile
    @Override
    public void onReceive(Context context, Intent intent) {
        //message will be holding complete sms that is received
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for(SmsMessage sms : messages)
        {

            String msg = sms.getMessageBody();
            String otp =msg.substring(0,6);
            pinview.setText(otp);
        }
    }
}