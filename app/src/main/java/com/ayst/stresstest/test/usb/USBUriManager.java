package com.ayst.stresstest.test.usb;

import android.net.Uri;

public class USBUriManager {
    private static USBUriManager instance;
    static final int USB1_FILE_SELECT_CODE = 0;
    static final int USB2_FILE_SELECT_CODE = 1;
    private static Uri[] mUris;

    private USBUriManager() {
        mUris = new Uri[2];
    }

    public static USBUriManager getInstance() {
        if (instance == null) {
            instance = new USBUriManager();
        }
        return instance;
    }

    public void putUSBUri(Uri uri, int index) {
        if (index < 2)
            mUris[index] = uri;
    }

    public boolean isChooseSameUSB() {
        if (mUris[0] != null && mUris[1] != null) {
            String[] uri1Tokens = mUris[0].toString().split("/");
            String usb1Dir = uri1Tokens[uri1Tokens.length - 1].split("%")[0];
            String[] uri2Tokens = mUris[1].toString().split("/");
            String usb2Dir = uri2Tokens[uri2Tokens.length - 1].split("%")[0];
            return (usb1Dir.compareTo(usb2Dir) == 0);
        }
        return false;
    }
}
