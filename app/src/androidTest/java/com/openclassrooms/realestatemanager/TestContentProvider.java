package com.openclassrooms.realestatemanager;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.provider.DatabaseContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestContentProvider {

    //this is a content provider to access room by another application
    private ContentResolver myContentResolver;

    @Before
    public void setup(){
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
        myContentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    @Test
    public void getRealEstate(){
        final Cursor cursor = myContentResolver.query(ContentUris.withAppendedId(DatabaseContentProvider.Companion.getMyUriItem(),1),  null,null,null,null);
        assertThat(cursor,notNullValue());
        assertThat(cursor.getCount(), is(6));
        cursor.close();


    }
}