package com.example.hi.checkout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by SRIDHARAN JOTHIRAMAN on 8/12/2017.
 */

public class StudentChoice extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_choice);
    }

    public void StudentDetails(View v)
    {
        Intent myIntent = new Intent(StudentChoice.this,StudentDetail.class);
        Bundle bundle = getIntent().getExtras();
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }
    public void Announce(View v)
    {
        Intent myIntent = new Intent(StudentChoice.this,StudentAnnounce.class);
        Bundle bundle = getIntent().getExtras();
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }
    public void TimeTable(View v)
    {
        Intent myintent=new Intent(this,TimeTable.class);
        startActivity(myintent);
    }
}
