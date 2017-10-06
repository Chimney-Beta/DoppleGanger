package com.app.thomasrogers.cardsaver.data;

/**
 * Created by thomasrogers on 1/1/17.
 */

import android.net.Uri;
import android.provider.BaseColumns;


public class CardContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.app.thomasrogers.cardsaver";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_CARDS = "cards";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class CardEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARDS).build();

        // Task table and column names
        public static final String TABLE_NAME = "cards";

        public static final String COLUMN_IMAGE = "image";

        public static final String COLUMN_VCARD_JSON = "vcard";

        public static final String COLUMN_ALL_VALUES = "imageValues";

        public static final String COLUMN_DATE_CREATED = "date";

        public static final String COLUMN_LAST_NAME = "last";

        public static final String COLUMN_COMPANY_NAME = "company";
    }
}