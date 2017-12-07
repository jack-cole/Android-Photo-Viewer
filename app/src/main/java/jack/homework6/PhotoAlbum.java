/*
 * Filename: PhotoAlbum.java
 * Author: Jack Cole
 * Class: CS211D T 7PM - 10PM
 * Due: 2017-05-23
 * Assignment: Homework 6
 * Description: This program loads up the pictures located in
 * the resource folder using reflection, then displays one
 * picture at a time. The user can select Next or Previous, or
 * turn on auto cycle and have the pictures automatically
 * change with user variable speeds.
 */
package jack.homework6;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PhotoAlbum extends Activity
{

  private ArrayList<Integer> photos;
  private int currentPhoto;
  private boolean auto_enabled = false;
  private int autoTime = 2000;
  private final static String LOG_ID = "hw6";


  //************************onCreate()***************************\
  @Override
  protected void onCreate(Bundle b)
  {
    super.onCreate(b);
    remove_activity_title(this);

    setContentView(R.layout.activity_photo_album);
    get_photos();
    currentPhoto = 0;
    set_photo_to_current();
  }

  //****************set_photo_to_current()***********************\
  public void set_photo_to_current()
  {
    Log.i(LOG_ID, "Setting photo to " + currentPhoto);
    ImageView iv = (ImageView) findViewById(R.id.current_image);
    iv.setImageResource(photos.get(currentPhoto));
  }

  //***************remove_activity_title()***********************\
  public void remove_activity_title(Activity activity)
  {
    activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
  }

  //*************************get_photos()************************\
  private void get_photos()
  {
    photos = new ArrayList<>();
    Field fld[] = jack.homework6.R.drawable.class.getFields();
    for (Field f : fld)
    {
      try
      {
        Integer imgid = f.getInt(null);
        this.photos.add(imgid);
      } catch (IllegalAccessException e)
      {
        e.printStackTrace();
      }
    }
  }

  //************************cycle_photo()************************\
  public void cycle_photo(View v)
  {

    switch (v.getId())
    {
      case R.id.next_button:
        if (currentPhoto < photos.size() - 1)
          currentPhoto += 1;
        break;
      case R.id.prev_button:
        if (currentPhoto > 0)
          currentPhoto -= 1;
        break;
    }

    set_photo_to_current();
  }

  //*****************toggle_auto_cycle()*************************\
  public void toggle_auto_cycle(View v)
  {

    Handler h = new Handler();

    auto_enabled = !auto_enabled;

    if (auto_enabled)
    {
      Log.i(LOG_ID, "Auto enabled");
      h.postDelayed(new Runnable()
      {
        public void run()
        {
          if (auto_enabled)
          {
            currentPhoto = (currentPhoto + 1) % photos.size();
            set_photo_to_current();
            h.postDelayed(this, autoTime);
            Log.i(LOG_ID, "Delaying  " + autoTime / 1000.0 + " seconds " +
                "for next photo.");
          }
        }
      }, autoTime);
    } else
      Log.i(LOG_ID, "Auto disabled");


  }

  //*****************toggle_auto_cycle()*************************\
  public void change_speed(View v)
  {
    Log.i(LOG_ID, "Changing speed from " + autoTime/1000.0 + " " +
        "seconds...");
    switch (v.getId())
    {
      case R.id.slower_button:
        if (autoTime < 8000)
          autoTime *= 2;
        break;
      case R.id.faster_button:
        if (autoTime > 500)
          autoTime /= 2;
        break;
    }
    Log.i(LOG_ID, "to " + autoTime/1000.0 + " " +
        "seconds.");

  }
}
