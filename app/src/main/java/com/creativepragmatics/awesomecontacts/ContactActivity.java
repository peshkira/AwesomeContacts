package com.creativepragmatics.awesomecontacts;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by petar on 02/09/14.
 */
public class ContactActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
    getWindow().setAllowEnterTransitionOverlap(true);
    getWindow().setEnterTransition(new Explode());

    setContentView(R.layout.activity_contact);

    getActionBar().hide();

    String contact = getIntent().getStringExtra("CONTACT");
    TextView tv = (TextView) findViewById(R.id.contact_name);
    tv.setText(contact);
  }
}
