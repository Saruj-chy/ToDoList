package com.sd.spartan.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.sd.spartan.todolist.Interface.AlertInterface;
import com.sd.spartan.todolist.adapter.PagerAdapter;
import com.sd.spartan.todolist.db.DatabaseHelperTest;
import com.sd.spartan.todolist.fragment.FirstFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private FloatingActionButton floatingActionButton ;
    private PagerAdapter pagerAdapter ;

    private int numOfTabs = 3 ;
    private MaterialAlertDialogBuilder materialAlertDialogBuilder ;
    private AlertDialog dialog ;
    private TextView textView ;
    private Calendar calendar;
    private int year, month, day, secID ;
    private String title ="", dateSelect ="" ;
    private DatabaseHelperTest mDatabaseHelperTest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Initialize() ;

//        mTabLayout.addTab(mTabLayout.newTab().setText("First"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("Second"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("Third"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        pagerAdapter = new PagerAdapter(this, numOfTabs);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setScrollX(mTabLayout.getWidth());
        new TabLayoutMediator(mTabLayout, mViewPager,
                (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Section A " );
                    break;
                case 1:
                    tab.setText("Section B " );
                    break;
                case 2:
                    tab.setText("Section C " );
                    break;
                default:
                    break;
            }}).attach() ;

//        mViewPager.addItemDecoration(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
//        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                mViewPager.setCurrentItem(tab.getPosition());
//            }
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        loadProducts() ;



    }

    private void Initialize() {
        mTabLayout = findViewById(R.id.tab_layout) ;
        mViewPager = findViewById(R.id.view_pager) ;
        floatingActionButton = findViewById(R.id.floating_add) ;

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(HomeActivity.this, R.style.MaterialAlertDialog_rounded) ;
        mDatabaseHelperTest = new DatabaseHelperTest(this) ;

    }



    public void OnPopupClick(View view) {
        View mView = getLayoutInflater().inflate(R.layout.popup_add,  null);
        TextInputEditText titleTET = mView.findViewById(R.id.edit_title_layout) ;
        Spinner spinner = mView.findViewById(R.id.spinner_section) ;
        textView = mView.findViewById(R.id.text_select_date) ;
        Button button = mView.findViewById(R.id.btn_combine) ;
        ProgressBar progressBar = mView.findViewById(R.id.progress) ;


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                secID = i+1 ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickDateTime() ;
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleTET.getText().toString().trim() ;
                if(title.equalsIgnoreCase("")){
                    Toast.makeText(HomeActivity.this, "Please enter title", Toast.LENGTH_SHORT).show();
                }else if(dateSelect.equalsIgnoreCase("")){
                    Toast.makeText(HomeActivity.this, "Please select the date", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    boolean insert = mDatabaseHelperTest.InsertToDoTbl(title, dateSelect, secID ) ;
                    if(insert){
                        mViewPager.setAdapter(pagerAdapter);
                        Log.e("toast", "Insert Successfully") ;
                        Toast.makeText(HomeActivity.this, "Insert Successfully", Toast.LENGTH_SHORT).show();
                        AllClear();
                        dialog.dismiss();


                    }else{
                        Log.e("toast", "Insert failed") ;
                        button.setVisibility(View.VISIBLE);
                        Toast.makeText(HomeActivity.this, "Insert failed", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });


        materialAlertDialogBuilder.setView(mView);
        dialog = materialAlertDialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void AllClear() {
        dateSelect = "";
        title = "" ;
    }

    private void OnClickDateTime() {
        DatePickerDialog datepick = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar selectCalendars = Calendar.getInstance();
                selectCalendars.set(year, month, dayOfMonth);
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateFormat = simpleDateFormat.format(selectCalendars.getTime());
                Log.e("date_format", "  dateString:  "  + dateFormat ) ;
                textView.setText(dateFormat);

                dateSelect = dateFormat ;


            }
        } ,year,month,day);
//        datepick.setTitle("select date");
        datepick.show();
    }

    private void CreateAlertDialog() {
        View view = getLayoutInflater().inflate(R.layout.popup_alert,  null);
        TextView alertTitle = view.findViewById(R.id.text_alert_title) ;
        TextView alertMsg = view.findViewById(R.id.text_alert_msg) ;

        materialAlertDialogBuilder.setView(view);
        dialog = materialAlertDialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void loadProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://snakehumanconflict.org/venom_resource_center/alert.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("serverResponse", "response: "+response );
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getString("error").equalsIgnoreCase("false")){
                                CreateAlertDialog() ;
                            }
                          } catch (JSONException e) {


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e("serverResponse", "error: "+error.toString() );
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }




}