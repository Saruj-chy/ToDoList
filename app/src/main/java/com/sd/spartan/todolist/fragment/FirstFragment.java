package com.sd.spartan.todolist.fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sd.spartan.todolist.db.DatabaseHelperTest;
import com.sd.spartan.todolist.adapter.SectionAdapter;
import com.sd.spartan.todolist.Interface.OnDeleteInterface;
import com.sd.spartan.todolist.model.SectionModel;
import com.sd.spartan.todolist.R;

import java.util.ArrayList;

public class FirstFragment extends Fragment implements OnDeleteInterface {
    private ConstraintLayout mSectionCL;
    private TextView mSectionTV;
    private RecyclerView mSectionRV;
    private DatabaseHelperTest mDatabaseHelperTest;

    private ArrayList<SectionModel> mSectionList;
    private SectionAdapter mSectionAdapter;
    private CountDownTimer mCountDownTimer;
    private final long mRemainingRefreshTime = 2000 ;

    private MaterialAlertDialogBuilder mMaterialAlertDialogBuilder;
    private AlertDialog mAlertDialog;
    private TextView mNameItemTV, mYesBtnTV, mNoBtnTV;




    public FirstFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        Initialize(view) ;

        mSectionList = new ArrayList<>();
        LoadAdapter() ;

        GetNotificationList();



        return  view ;
    }

    private void LoadAdapter() {
        mSectionAdapter = new SectionAdapter(getContext(), mSectionList, this);
        LinearLayoutManager mNotifyLLM = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mSectionRV.setLayoutManager(mNotifyLLM);
        mSectionRV.setAdapter(mSectionAdapter);
        mSectionAdapter.notifyDataSetChanged();
    }

    private void SetAutoRefresh(){
        if(mRemainingRefreshTime <=0){
            if(mCountDownTimer != null){
                mCountDownTimer.cancel();
            }
            return;
        }
        if(mCountDownTimer == null){
            mCountDownTimer = new CountDownTimer(mRemainingRefreshTime, 2500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mSectionCL.setVisibility(View.GONE);
                }

                @Override
                public void onFinish() {
                    mSectionCL.setVisibility(View.VISIBLE);
                    CancelAutoRefresh() ;
                }
            };

            mCountDownTimer.start() ;
        }
    }
    private void CancelAutoRefresh(){
        if(mCountDownTimer != null){
            mCountDownTimer.cancel();
            mCountDownTimer =null;
        }
    }

    private void GetNotificationList() {
        mSectionList.clear();
        Cursor y = mDatabaseHelperTest.checkTable(1) ;
        if (y.moveToFirst()) {
            while (true) {
                mSectionList.add(new SectionModel(
                        y.getString(0),
                        y.getString(1),
                        y.getString(2),
                        y.getString(3)
                ));
                if (y.isLast())
                    break;
                y.moveToNext();
            }
        }
        mSectionAdapter.notifyDataSetChanged();
        if(mSectionList.size()<=0){
            mSectionRV.setVisibility(View.GONE);
            mSectionTV.setVisibility(View.VISIBLE);
        }else{
            mSectionRV.setVisibility(View.VISIBLE);
            mSectionTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDeleteClick(String id, String secId) {
        CreatePopupDialog(id, secId );
    }
    private void CreatePopupDialog(String id, String secId) {
        View view = getLayoutInflater().inflate(R.layout.popup_delete,  null);
        mNameItemTV = view.findViewById(R.id.text_del_name) ;
        mYesBtnTV = view.findViewById(R.id.text_yes) ;
        mNoBtnTV = view.findViewById(R.id.text_no) ;

//        mNameItemTV.setText(String.format("%s?", name));

        mYesBtnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean l  = mDatabaseHelperTest.DeleteToDoTbl(id,secId) ;
                if(l){
                    Toast.makeText(getContext(), "delete successful", Toast.LENGTH_SHORT).show();
//                    ToastNotify.ShowToast(NotifyActivity.this, AppConstants.TOAST_ONE_ITEM_DEL);

                    GetNotificationList();
                    mSectionAdapter.notifyDataSetChanged();
                }
                mAlertDialog.cancel();
            }
        });

        mNoBtnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.cancel();
            }
        });


        mMaterialAlertDialogBuilder.setView(view);
        mAlertDialog = mMaterialAlertDialogBuilder.create();
        mAlertDialog.show();
        mAlertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }


    private void Initialize(View view) {
        mSectionRV = view.findViewById(R.id.recycler_todo) ;
        mSectionTV = view.findViewById(R.id.text_error_msg) ;
        mSectionCL = view.findViewById(R.id.constraint_notify) ;

        mDatabaseHelperTest = new DatabaseHelperTest(getContext()) ;
        mMaterialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialog_rounded) ;
    }



}