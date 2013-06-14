package com.android.demo.notepad3;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyCursorAdapter extends SimpleCursorAdapter {
	private String[] from;
	private int[] to;
	private int maxLength;
	
	private Context mCtx;
	
	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public MyCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.from = from;
		this.to= to;
		this.mCtx = context;
	}
	/*
	@Override
	public void bindView(View view, Context arg1, Cursor cur) {
		int i=0;
		int maxChars =this.maxLength;
		for (int to_item : to) {
			TextView tv = (TextView) view.findViewById(to_item);
			int colIndex = cur.getColumnIndex(from[i++]);
			String cnt = cur.getString(colIndex);
			if(to_item == R.id.content){
				if(cnt.length() > maxChars){
					cnt = cnt.substring(0,maxChars) + "...";
				}
				
			}
			tv.setText(cnt);
		}	
	}
	*/
	
	@Override
	public void bindView(View view, Context ctx, Cursor cur) {
		Log.v("MyCursorAdapter", "bindView2");
		TextView tvListText = (TextView)view.findViewById(R.id.text1);
		TextView tvContent = (TextView)view.findViewById(R.id.content);
		
		Button viewBtn = (Button)view.findViewById(R.id.view_btn);
		viewBtn.setTag(cur.getString(cur.getColumnIndex(NotesDbAdapter.KEY_ROWID)));
		viewBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.v("MyCursorAdapter", "onClick " + v.getTag().toString());
				Intent i = new Intent(mCtx, NoteView.class);
		        i.putExtra(NotesDbAdapter.KEY_ROWID, Long.parseLong(v.getTag().toString(), 10));
		        mCtx.startActivity(i);
			}
			
		});
		
		
		tvListText.setText(cur.getString(cur.getColumnIndex(NotesDbAdapter.KEY_TITLE)));
		String body = cur.getString(cur.getColumnIndex(NotesDbAdapter.KEY_BODY));
		
		tvContent.setText(body);
		
		

	}

}
