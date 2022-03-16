package com.contactninja.aws.csv_aws;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.contactninja.aws.image_aws.AWSKeys;
import com.contactninja.aws.image_aws.AmazonUtil;

import java.io.File;

public class S3Uploader_csv {

    private static final String TAG = "S3Uploader";
    private Context context;
    private TransferUtility transferUtility;
    public S3UploadInterface s3UploadInterface;

    public S3Uploader_csv(Context context) {
        this.context = context;
        transferUtility = AmazonUtil_csv.getTransferUtility(context);
    }

    private class UploadListener implements TransferListener {

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "Error during upload: " + id, e);
            s3UploadInterface.onUploadError(e.toString());
            s3UploadInterface.onUploadError("Error");
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d(TAG, "onStateChanged: " + id + ", " + newState);
            if (newState == TransferState.COMPLETED) {
                s3UploadInterface.onUploadSuccess("Success");
            }
        }
    }

    public void setOns3UploadDone(S3UploadInterface s3UploadInterface) {
        this.s3UploadInterface = s3UploadInterface;
    }

    public interface S3UploadInterface {
        void onUploadSuccess(String response);

        void onUploadError(String response);

    }

    public String Csv_Upload(String filePath,String folder) {

        File uploadToS3 = new File(filePath);
        String[] nameList = filePath.split("/");
        String defaultFolder = folder;
        String uploadFileName = nameList[nameList.length - 1];
        String audioURL = AWSKeys_csv.BUCKET_URL +uploadFileName;
        ObjectMetadata metadataCopy = new ObjectMetadata();
        metadataCopy.setContentType("*/*");
        TransferObserver transferObserver = transferUtility.upload(AWSKeys_csv.BUCKET_NAME, uploadFileName,
                uploadToS3, metadataCopy,  CannedAccessControlList.PublicRead
        );
        transferObserver.setTransferListener(new UploadListener());
        return  audioURL;
    }



}

