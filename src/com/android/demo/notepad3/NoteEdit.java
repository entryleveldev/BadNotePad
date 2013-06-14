/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.demo.notepad3;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class NoteEdit extends Activity {
	private NotesDbAdapter mDbHelper;
	private EditText mTitleText;
	private EditText mBodyText;
	private Button mDateButton;
	private RatingBar mRatingBar;
	private Long mRowId;
	static final int DATE_DIALOG_ID = 0;

	//private Date mDate;

	private final java.text.DateFormat mDateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
	private final Calendar calendar = Calendar.getInstance();;
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			calendar.set(year, monthOfYear, dayOfMonth);
			String strDate = mDateFormatter.format(calendar.getTime());
			//mDate = calendar.getTime();
			mDateButton.setText(strDate);
		}
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();

		setContentView(R.layout.note_edit);
		setTitle(R.string.edit_note);

		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);
		mDateButton = (Button) findViewById(R.id.note_date);
		mRatingBar = (RatingBar) findViewById(R.id.rtRating);

		Button confirmButton = (Button) findViewById(R.id.confirm);
		Button deleteButton = (Button) findViewById(R.id.delete);
		Button cancelButton = (Button) findViewById(R.id.cancel);
		
		mRowId = (savedInstanceState == null) ? null
				: (Long) savedInstanceState
						.getSerializable(NotesDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
					: null;
		}
		populateFields();
		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				setResult(RESULT_OK);
				finish();

			}

		});
		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mDbHelper.deleteNote(mRowId);
				Toast.makeText(NoteEdit.this, "Note deleted",
						Toast.LENGTH_SHORT).show();

				finish();
			}
		});
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				populateFields();
			}
		});
	}

	private void populateFields() {
		if (mRowId != null) {
			Cursor note = mDbHelper.fetchNote(mRowId);
			startManagingCursor(note);
			mTitleText.setText(note.getString(note
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
			mBodyText.setText(note.getString(note
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
			mRatingBar.setRating(note.getFloat(note
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_RATING)));
			String note_date = getDateDisplay(note);
				mDateButton.setText(note_date);
			
			
		}
	}
	private String getDateDisplay(Cursor note){
		String display="";
		String note_date = note.getString(note
				.getColumnIndexOrThrow(NotesDbAdapter.KEY_DATE));
		if(note_date != null){
			try {
				Date dt = NotesDbAdapter.FORMATTER.parse(note_date);
				display = mDateFormatter.format(dt);
			} catch (ParseException e) {
				Log.e("NoteEdit.getDateDisplay", e.getMessage());
				
			}
			
		}
		return display;
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			int year = calendar.get(Calendar.YEAR);
			int mon = calendar.get(Calendar.MONTH);
			
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			//Log.d("DIALOG: ", "Year: " + year + " Month: " + mon + " Day: " + day);
			//mon=4;
			return new DatePickerDialog(this, mDateSetListener, year, mon,
					day);
		}
		return null;
	}	

	private void saveState() {
		String title = mTitleText.getText().toString();
		String body = mBodyText.getText().toString();
		float rating = mRatingBar.getRating();
		Date date =null;
		try {
			date = mDateFormatter.parse(mDateButton.getText().toString());
		} catch (ParseException e) {
			Log.e("saveState", e.getMessage());
		}
		
		if (mRowId == null) {
			long id = mDbHelper.createNote(title, body, date, rating);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateNote(mRowId, title, body, date, rating);
		}
	}
	public void onNoteDateButtonClick(View v) {
		showDialog(DATE_DIALOG_ID);
	}	
}
