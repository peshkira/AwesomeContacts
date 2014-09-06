package com.creativepragmatics.awesomecontacts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ContactsActivity extends Activity {

  private static final String[] CONTACTS = {"Petar Petrov", "Manuel Maly", "Peter Riegler", "Klemens Zleptnig", "Alex Leutgöb", "Bernd Bergler", "Peter Zainzinger", "Gerald Novak", "Annie", "Todo Lazov", "Luka Mirosevic", "Christian Patterer", "Ben Schröder", "Martin Keiblinger", "Eduardo Sabbatella", "Robert Hesele", "Daniel Maria Niedermühlbichler"};

  private View mContactForm;

  private ImageButton mFAB;

  private EditText mContactTxt;

  private Button mSave;

  private ListView mList;

  private ArrayAdapter<String> mListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // you have to define this here or within styles.xml
    getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

    setContentView(R.layout.activity_contacts);

    // set them to overlap in order to make them seem smoother
    getWindow().setAllowExitTransitionOverlap(true);

    // set an exit transition
    // you can use new Slide() and new Fade()
    getWindow().setExitTransition(new Explode());

    mContactForm =  findViewById(R.id.contact_form);
    mFAB = (ImageButton) findViewById(R.id.fab);
    mContactTxt = (EditText) findViewById(R.id.contact_txt);
    mSave = (Button) findViewById(R.id.btn_save);
    mList = (ListView) findViewById(R.id.contacts_list);

    initList();

    // sets an outline to the custom shaped FAB (floating action button)
    int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
    Outline outline = new Outline();
    outline.setOval(0, 0, size, size);
    mFAB.setOutline(outline);

    // sets the on click listener which reveals the add form
    mFAB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        hide(mFAB, new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mFAB.setVisibility(View.INVISIBLE);
            reveal(mContactForm, null);
          }
        });
      }
    });

    // sets the on click listener which saves and hides
    // the add form
    mSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mListAdapter.add(mContactTxt.getText().toString());
        mListAdapter.sort(new Comparator<String>() {
          @Override
          public int compare(String s, String s2) {
            return s.compareToIgnoreCase(s2);
          }
        });
        hideContactForm();
      }
    });

  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.contacts, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    if (mContactForm.getVisibility() == View.VISIBLE) {
      hideContactForm();
    } else {
      super.onBackPressed();
    }
  }

  private void initList() {
    ArrayList<String> items = new ArrayList<String>();
    for (String c : CONTACTS) {
      items.add(c);
    }

    Collections.sort(items);
    mListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
    mList.setAdapter(mListAdapter);

    mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(ContactsActivity.this, ContactActivity.class);
        intent.putExtra("CONTACT", ((TextView) view).getText().toString());
        startActivity(intent);
      }
    });

  }

  private void hideContactForm() {
    hide(mContactForm, new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        mContactForm.setVisibility(View.INVISIBLE);
        reveal(mFAB, null);
      }
    });
  }

  // uses the createCircularReveal API to show the given view.
  private void reveal(View view, AnimatorListenerAdapter listener) {
    view.setVisibility(View.VISIBLE);
    // get the center for the clipping circle
    int cx = (view.getLeft() + view.getRight()) / 2;
    int cy = (view.getTop() + view.getBottom()) / 2;

    // get the final radius for the clipping circle
    int finalRadius = view.getWidth();

  // (the start radius is zero)
    ValueAnimator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
    if (listener != null) {
      anim.addListener(listener);
    }
    anim.start();
  }

  // uses the createCircularReveal API to hide the given view
  private void hide(final View view, AnimatorListenerAdapter listener) {
  // get the center for the clipping circle
    int cx = (view.getLeft() + view.getRight()) / 2;
    int cy = (view.getTop() + view.getBottom()) / 2;

  // get the initial radius for the clipping circle
    int initialRadius = view.getWidth();

    ValueAnimator anim =
        ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

    if (listener != null) {
      anim.addListener(listener);
    }

// start the animation
    anim.start();
  }
}
