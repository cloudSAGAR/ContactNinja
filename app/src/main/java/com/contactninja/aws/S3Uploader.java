package com.contactninja.aws;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.File;

public class S3Uploader {

    private static final String TAG = "S3Uploader";

    private Context context;
    private TransferUtility transferUtility;
    public S3UploadInterface s3UploadInterface;

    public S3Uploader(Context context) {
        this.context = context;
        transferUtility = AmazonUtil.getTransferUtility(context);

    }

    public void initUpload(String filePath,String folder) {

       File uploadToS3 = new File(filePath);
        String[] nameList = filePath.split("/");
        String defaultFolder = folder;
        String uploadFileName = nameList[nameList.length - 1];
        String audioURL = AWSKeys.BUCKET_URL + defaultFolder + "/"+uploadFileName;
        ObjectMetadata metadataCopy = new ObjectMetadata();
        metadataCopy.setContentType("image/png");
        TransferObserver transferObserver = transferUtility.upload(AWSKeys.BUCKET_NAME, defaultFolder + "/"+ uploadFileName,
                uploadToS3, metadataCopy,  CannedAccessControlList.PublicRead
        );
        transferObserver.setTransferListener(new UploadListener());
       /* File file = new File(filePath);
        ObjectMetadata myObjectMetadata = new ObjectMetadata();
        myObjectMetadata.setContentType("image/png");
        String mediaUrl = file.getName();
        String defaultFolder="contactninja";

        TransferObserver observer = transferUtility.upload(AWSKeys.BUCKET_NAME, mediaUrl,
                file, CannedAccessControlList.PublicRead);
        observer.setTransferListener(new UploadListener());*/
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
}
