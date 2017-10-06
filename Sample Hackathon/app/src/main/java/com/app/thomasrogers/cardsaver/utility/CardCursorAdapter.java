package com.app.thomasrogers.cardsaver.utility;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.thomasrogers.cardsaver.R;
import com.app.thomasrogers.cardsaver.callbacks.OnClickCallback;
import com.app.thomasrogers.cardsaver.data.CardContract;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by thomasrogers on 1/2/17.
 */

public class CardCursorAdapter extends RecyclerView.Adapter<CardCursorAdapter.TaskViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private OnClickCallback mOnClick;

    public CardCursorAdapter(Context mContext, OnClickCallback onClick) {
        this.mContext = mContext;
        mOnClick = onClick;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_item, parent, false);

        return new TaskViewHolder(view, mOnClick);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        // Indices for the _id, description, and image columns
        int idIndex = mCursor.getColumnIndex(CardContract.CardEntry._ID);
        int imageIndex = mCursor.getColumnIndex(CardContract.CardEntry.COLUMN_IMAGE);
        int dateIndex = mCursor.getColumnIndex(CardContract.CardEntry.COLUMN_DATE_CREATED);
        int nameIndex = mCursor.getColumnIndex(CardContract.CardEntry.COLUMN_LAST_NAME);
        int companyIndex = mCursor.getColumnIndex(CardContract.CardEntry.COLUMN_COMPANY_NAME);
        int jsonIndex = mCursor.getColumnIndex(CardContract.CardEntry.COLUMN_VCARD_JSON);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        byte[] imageBytes = mCursor.getBlob(imageIndex);
        String dateString = mCursor.getString(dateIndex);
        String nameString = mCursor.getString(nameIndex);
        String companyString = mCursor.getString(companyIndex);
        String json = mCursor.getString(jsonIndex);

        //Set values
        holder.itemView.setTag(id);
        Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        holder.CardImageView.setImageBitmap(image);

        holder.LastName.setVisibility(View.VISIBLE);
        holder.CompanyName.setVisibility(View.VISIBLE);

        if (json == null || json.length() == 0) {
            holder.itemView.setBackgroundColor(ResourcesCompat.getColor(holder.itemView.getContext().getResources(), R.color.unfinished_card, null));
        }
        else{
            holder.itemView.setBackgroundColor(ResourcesCompat.getColor(holder.itemView.getContext().getResources(), android.R.color.white, null));
        }

        if (nameString != null && nameString.length() > 0) {
            holder.LastName.setText(nameString);
        }

        if (companyString != null && companyString.length() > 0) {
            holder.CompanyName.setText(companyString);
        }

        if (companyString == null && nameString != null) {
            holder.LastName.setVisibility(View.GONE);
            holder.CompanyName.setVisibility(View.GONE);
        }

        if (dateString != null && dateString.length() > 0) {
            holder.Date.setVisibility(View.VISIBLE);
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                Calendar c = dateFormat.getCalendar();
                c.setTime(dateFormat.parse(dateString));
                DecimalFormat formatter = new DecimalFormat("00");
                String minutesFormatted = formatter.format(c.get(Calendar.MINUTE));
                int hour;
                if (c.get(Calendar.HOUR) == 0) {
                    hour = 12;
                }
                else{
                    hour = c.get(Calendar.HOUR);
                }

                holder.Date.setText(String.format(Locale.US, "%d:%s %s\n%d/%d/%d", hour, minutesFormatted, c.get(Calendar.AM_PM) == 0 ? "a.m." : "p.m." , c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR)));
            } catch (ParseException pex) {
                pex.printStackTrace();
            }
        }
        else
        {
            holder.Date.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    // Inner class for creating ViewHolders
    class TaskViewHolder extends RecyclerView.ViewHolder {

        // Class variables for the task description and priority TextViews
        public ImageView CardImageView;
        public TextView LastName;
        public TextView CompanyName;
        public TextView Date;

        public TaskViewHolder(final View itemView, final OnClickCallback onClick) {
            super(itemView);

            CardImageView = (ImageView) itemView.findViewById(R.id.cardImage);
            LastName = (TextView) itemView.findViewById(R.id.lastName);
            CompanyName = (TextView) itemView.findViewById(R.id.companyName);
            Date = (TextView) itemView.findViewById(R.id.date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onItemClick((int)itemView.getTag());
                }
            });
        }
    }
}
