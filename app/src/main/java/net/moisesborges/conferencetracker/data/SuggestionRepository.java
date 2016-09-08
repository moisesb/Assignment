package net.moisesborges.conferencetracker.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import net.moisesborges.conferencetracker.db.ConferenceDatabase;
import net.moisesborges.conferencetracker.db.scheme.SuggestionTable;
import net.moisesborges.conferencetracker.model.TopicSuggestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moisés on 18/08/2016.
 */

public class SuggestionRepository {

    private static SuggestionRepository sInstance;
    private SQLiteDatabase mDatabase;

    private SuggestionRepository() {
        mDatabase = ConferenceDatabase.getInstance()
                .getWritableDatabase();
    }

    public static SuggestionRepository getInstance() {
        if (sInstance == null) {
            sInstance = new SuggestionRepository();
        }

        return sInstance;
    }

    public void addTopicSuggestion(TopicSuggestion topicSuggestion) {
        long id = mDatabase.insert(SuggestionTable.NAME, null, contentValuesFor(topicSuggestion));
        topicSuggestion.setId(id);
    }

    private ContentValues contentValuesFor(TopicSuggestion topicSuggestion) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SuggestionTable.Columns.CONFERENCE_ID, topicSuggestion.getConferenceId());
        contentValues.put(SuggestionTable.Columns.DOCTOR_ID, topicSuggestion.getDoctorId());
        contentValues.put(SuggestionTable.Columns.SUGGESTION_MESSAGE, topicSuggestion.getSuggestion());
        return contentValues;
    }

    public void getTopicSuggestionsForConferenceAsync(final long conferenceId, final OnGetTopicsCallback callback) {
        AsyncTask<Void, Void, List<TopicSuggestion>> asyncTask = new AsyncTask<Void, Void, List<TopicSuggestion>>() {
            @Override
            protected List<TopicSuggestion> doInBackground(Void... params) {
                return getTopicSuggestionsForConference(conferenceId);
            }

            @Override
            protected void onPostExecute(List<TopicSuggestion> topicSuggestions) {
                super.onPostExecute(topicSuggestions);
                callback.onGetTopics(topicSuggestions);
            }
        };

        asyncTask.execute();
    }

    public List<TopicSuggestion> getTopicSuggestionsBySelection(String selection) {
        Cursor cursor = mDatabase.query(SuggestionTable.NAME, null, selection, null, null, null, null);
        cursor.moveToFirst();
        List<TopicSuggestion> topicSuggestions = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            topicSuggestions.add(getTopicSuggestion(cursor));
            cursor.moveToNext();
        }
        return topicSuggestions;
    }

    private TopicSuggestion getTopicSuggestion(Cursor cursor) {
        if (cursor.getCount() == 0 || cursor.isAfterLast()) {
            return null;
        }
        TopicSuggestion topicSuggestion = new TopicSuggestion();
        topicSuggestion.setDoctorId(cursor.getLong(cursor.getColumnIndex(SuggestionTable.Columns.DOCTOR_ID)));
        topicSuggestion.setConferenceId(cursor.getLong(cursor.getColumnIndex(SuggestionTable.Columns.CONFERENCE_ID)));
        topicSuggestion.setSuggestion(cursor.getString(cursor.getColumnIndex(SuggestionTable.Columns.SUGGESTION_MESSAGE)));
        topicSuggestion.setId(cursor.getLong(cursor.getColumnIndex(SuggestionTable.Columns.ID)));
        return topicSuggestion;
    }

    public void getTopicSuggestionsAsync(final OnGetTopicsCallback callback) {
        AsyncTask<Void, Void, List<TopicSuggestion>> asyncTask = new AsyncTask<Void, Void, List<TopicSuggestion>>() {
            @Override
            protected List<TopicSuggestion> doInBackground(Void... params) {
                return getTopicSuggestionsBySelection(null);
            }

            @Override
            protected void onPostExecute(List<TopicSuggestion> topicSuggestions) {
                super.onPostExecute(topicSuggestions);
                callback.onGetTopics(topicSuggestions);
            }
        };

        asyncTask.execute();
    }

    public List<TopicSuggestion> getTopicSuggestionsForConference(long conferenceId) {
        String selection = SuggestionTable.Columns.CONFERENCE_ID + " = " + conferenceId;
        return getTopicSuggestionsBySelection(selection);
    }

    public interface OnGetTopicsCallback {
        void onGetTopics(List<TopicSuggestion> topicSuggestions);
    }

}
