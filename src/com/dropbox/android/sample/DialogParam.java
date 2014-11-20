package com.dropbox.android.sample;

public class DialogParam {
	public String message;
	public long progress;
	public DialogParam(String m){
		this.message = m;
		progress = 0;
	}
	public void setProgress(long p){
		this.progress = p;
	}
}
