package com.ksu.blooddonationproject;

import android.content.Context;
import android.graphics.Typeface;

public final class Font {

	// Font path
	private static final String fontPath = "fonts/font.ttf";
	
	public static Typeface getFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), fontPath);

    }
}
