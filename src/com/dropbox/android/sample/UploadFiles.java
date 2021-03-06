/*
 * Copyright (c) 2011 Dropbox, Inc.
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */


package com.dropbox.android.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.UploadRequest;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxFileSizeException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

/**
 * Here we show uploading a file in a background thread, trying to show
 * typical exception handling and flow of control for an app that uploads a
 * file from Dropbox.
 */

public class UploadFiles extends AsyncTask<Void, Object, Boolean>{

	private DropboxAPI<?> mApi;
	private String mPath;
	private File mFile;

	private long mFileLen;
	private UploadRequest mRequest;
	private Context mContext;
	private ProgressDialog mDialog = null;

	private String mErrorMsg;

	private String filename;
	public UploadFiles(Context context, DropboxAPI<?> api, String dropboxPath,
			String localPath) {
		mContext = context.getApplicationContext();
		mApi = api;
		mPath = dropboxPath;
		mDialog = new ProgressDialog(context);
		// We set the context this way so we don't accidentally leak activities
				mDialog.setMax(100);
		mDialog.setMessage("Start Uploading Files");
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setProgress(0);

		mDialog.setButton(ProgressDialog.BUTTON_POSITIVE, "Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// This will cancel the putFile operation
				if(mRequest!=null) 
					mRequest.abort();
			}
		});
		mDialog.show();
		
		// ProgressDialog.show(this, "title...", "message...");
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		// By creating a request, we get a handle to the putFile operation,
		// so we can cancel it later if we want to
		String rootDir = Environment.getExternalStorageDirectory().toString()+"/Pictures/VehicleStateEstimation/vid/";
		Log.d("Files", "Path: " + rootDir);
		File f = new File(rootDir);        
		File file[] = f.listFiles();
		Log.d("Files", "Size: "+ file.length);
		for (int i=0; i < file.length; i++)
		{
			Log.d("Files", "FileName:" + file[i].getName());
			if(file[i].isDirectory()){
				String folder_name = "/" + file[i].getName() + "/";
				File nodeFiles[] = file[i].listFiles();
				for(int j = 0;j<nodeFiles.length;j++){
					Log.d("Node Files", "FileName" + nodeFiles[j].getName());
					filename = nodeFiles[j].toString();
										
					upload(nodeFiles[j]);
				}


			}
		}
		return true;
	}
	public void upload(File uploadFile){
		try{

			FileInputStream fis = new FileInputStream(uploadFile);
			String path = mPath + uploadFile.getName();
			publishProgress(uploadFile.getName());
			//self.publishProgress(uploadFile.getName());
			mRequest = mApi.putFileOverwriteRequest(path, fis, uploadFile.length(),
					new ProgressListener() {
				@Override
				public long progressInterval() {
					// Update the progress bar every half-second or so
					return 100;
				}

				@Override
				public void onProgress(long bytes, long total) {
					publishProgress(bytes);
				}
			});

			if (mRequest != null) {
				mRequest.upload();

			}


		} catch (DropboxUnlinkedException e) {
			// This session wasn't authenticated properly or user unlinked
			mErrorMsg = "This app wasn't authenticated properly.";
		} catch (DropboxFileSizeException e) {
			// File size too big to upload via the API
			mErrorMsg = "This file is too big to upload";
		} catch (DropboxPartialFileException e) {
			// We canceled the operation
			mErrorMsg = "Upload canceled";
		} catch (DropboxServerException e) {
			// Server-side exception.  These are examples of what could happen,
			// but we don't do anything special with them here.
			if (e.error == DropboxServerException._401_UNAUTHORIZED) {
				// Unauthorized, so we should unlink them.  You may want to
				// automatically log the user out in this case.
			} else if (e.error == DropboxServerException._403_FORBIDDEN) {
				// Not allowed to access this
			} else if (e.error == DropboxServerException._404_NOT_FOUND) {
				// path not found (or if it was the thumbnail, can't be
				// thumbnailed)
			} else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
				// user is over quota
			} else {
				// Something else
			}
			// This gets the Dropbox error, translated into the user's language
			mErrorMsg = e.body.userError;
			if (mErrorMsg == null) {
				mErrorMsg = e.body.error;
			}
		} catch (DropboxIOException e) {
			// Happens all the time, probably want to retry automatically.
			mErrorMsg = "Network error.  Try again.";
		} catch (DropboxParseException e) {
			// Probably due to Dropbox server restarting, should retry
			mErrorMsg = "Dropbox error.  Try again.";
		} catch (DropboxException e) {
			// Unknown error
			mErrorMsg = "Unknown error.  Try again.";
		} catch (FileNotFoundException e) {
		}
	
	}

	
	protected void onPreExecute(){
		mDialog.show();
	}
	@Override
	protected void onProgressUpdate(Object... progress) {
		Log.e("mFileLen", ""+mFileLen);
		if(progress[0] instanceof String){
			mDialog.setMessage((String) progress[0]); 
			mDialog.setProgress(0);
			return;
		}
		int percent = (int)(100.0*(double) (Long)progress[0]/mFileLen + 0.5);
		mDialog.setProgress(percent);
	}
	

	@Override
	protected void onPostExecute(Boolean result) {
		mDialog.dismiss();

		if (result) {
			showToast("File " +filename+" successfully uploaded");
		} else {
			showToast(mErrorMsg);
		}
		asynFinished();

	}
	private boolean asynFinished(){
		return true;
	}
	private void showToast(String msg) {
		Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
		error.show();
	}
}
