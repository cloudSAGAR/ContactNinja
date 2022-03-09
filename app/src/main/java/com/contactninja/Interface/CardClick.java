package com.contactninja.Interface;

import android.annotation.SuppressLint;

import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.Broadcast_image_list;

import java.util.List;

public interface CardClick {
    void Onclick(@SuppressLint("UnknownNullness") List<BZcardListModel.Bizcard> bizcardList);
}
