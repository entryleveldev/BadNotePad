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

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NoteView extends Activity {
	private NotesDbAdapter mDbHelper;
	private TextView mTitleText;
	private TextView mBodyText;
	private Long mRowId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();

		setContentView(R.layout.note_view);
		setTitle(R.string.edit_note);

		mTitleText = (TextView) findViewById(R.id.title);
		mBodyText = (TextView) findViewById(R.id.body);
		
		mRowId = (savedInstanceState == null) ? null
				: (Long) savedInstanceState
						.getSerializable(NotesDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
					: null;
		}
		populateFields();
		
	}

	private void populateFields() {
		if (mRowId != null) {
			Log.v("NoteView", "id "+ mRowId);
			Cursor note = mDbHelper.fetchNote(mRowId);
			//startManagingCursor(note);
			mTitleText.setText(note.getString(note
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
			mBodyText.setText(note.getString(note
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
		}
	}
	

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	
}
