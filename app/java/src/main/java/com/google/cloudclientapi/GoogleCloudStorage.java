package com.google.cloudclientapi;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class GoogleCloudStorage {

  public static String downloadFileAsString(String bucket, String filePath) {
    Storage storage = StorageOptions.getDefaultInstance().getService();
    Blob blob = storage.get(bucket, filePath);
    if (blob == null) {
      return null;
    }
    byte[] contentAsBytes = blob.getContent();
    String contentAsString = new String(contentAsBytes, StandardCharsets.UTF_8);
    return contentAsString;
  }

  public static void downloadFileToFilePath(String bucket, String srcFilePath, Path destFilePath) {
    Storage storage = StorageOptions.getDefaultInstance().getService();
    Blob blob = storage.get(bucket, srcFilePath);
    blob.downloadTo(destFilePath);
  }

  public static void upload(String bucket, String filePath, String contentAsString)
      throws UnsupportedEncodingException {
    Storage storage = StorageOptions.getDefaultInstance().getService();
    BlobId cloudStorageBlobId = BlobId.of(bucket, filePath);
    BlobInfo cloudStorageBlobInfo =
        BlobInfo.newBuilder(cloudStorageBlobId).setContentType("application/json").build();
    String utf8CharsetName = StandardCharsets.UTF_8.name();
    byte[] contentAsBytes = contentAsString.getBytes(utf8CharsetName);
    storage.create(cloudStorageBlobInfo, contentAsBytes, 0, contentAsBytes.length);
  }
}
