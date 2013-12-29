package org.alphacloud.wefoundit.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class Assets {
	public static InputStream getNativeDB(Context context) {
		try {
			return context.getAssets().open("nativedb.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
