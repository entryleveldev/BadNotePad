package com.android.demo.notepad3.test;

import java.util.Date;

import com.android.demo.notepad3.NotesDbAdapter;

import android.test.AndroidTestCase;
import android.util.Log;

public class NotesDbAdapterTests extends AndroidTestCase {
	private static final String TAG = "NotesDbAdapterTests";
	private NotesDbAdapter dbAdapter;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// Do some initial setup here
		Log.d(TAG, "In setUp");
		dbAdapter = new NotesDbAdapter(this.getContext());
		dbAdapter.open();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		// Do some clean up here
		Log.d(TAG, "In tearDown");
		dbAdapter.close();
	}

	// Test createNote
	public void testCreateNote() {
		long id = dbAdapter.createNote("Menelao", 
				"Hermano de Agamenon",
				new Date(), 5f);
		Log.d(TAG, "Note id: " + id);
		assertTrue("Failed to insert note", id > 0);
	}
}
