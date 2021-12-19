package com.sd.spartan.todolist.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.sd.spartan.todolist.db.DatabaseHelperTest;
import com.sd.spartan.todolist.Interface.OnDeleteInterface;
import com.sd.spartan.todolist.R;
import com.sd.spartan.todolist.model.SectionModel;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mCtx;
    private final List<SectionModel> mItemList;
    private SectionModel mNotifyModel;
    private DatabaseHelperTest mDatabaseHelperTest;
    private final OnDeleteInterface onDeleteInterface ;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    long elapsedYears, elapsedMonths, elapsedDays, elapsedHours ;
    int timeNumber;


    public SectionAdapter(Context mCtx, List<SectionModel> mItemList, OnDeleteInterface onDeleteInterface) {
        this.mCtx = mCtx;
        this.mItemList = mItemList;
        this.onDeleteInterface = onDeleteInterface ;
    }




    @Override
    public RecyclerView.@NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_task, null);
        mDatabaseHelperTest = new DatabaseHelperTest(mCtx) ;
        return new NotifyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.@NotNull ViewHolder holder, final int position) {
        mNotifyModel = mItemList.get(position);
        ((NotifyViewHolder) holder).bind(mNotifyModel);



    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    class NotifyViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitleTV, mDateTV;
        private ImageButton deleteImg ;
        public NotifyViewHolder(View itemView) {
            super(itemView);

            mTitleTV = itemView.findViewById(R.id.text_title_Layout);
            mDateTV = itemView.findViewById(R.id.text_date_layout);
            deleteImg = itemView.findViewById(R.id.img_btn_delete);

        }

        @SuppressLint("SetTextI18n")
        public void bind(SectionModel notifyModel) {

            mTitleTV.setText( notifyModel.getTitle() );
            mDateTV.setText( notifyModel.getDate() );

            //date
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            setUpDateTime(notifyModel.getDate(), currentDate, currentTime);

//            if(notifyModel.getNotify_read().equalsIgnoreCase("0")){
//                mDotNotifyImgView.setVisibility(View.VISIBLE);
//            }else{
//                mDotNotifyImgView.setVisibility(View.GONE);
//            }

//            relativeNotify.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mDatabaseHelperTest.NotificationUpdate(notifyModel.getNotify_id(), 1+"", notifyModel.getNotify_item_id()) ;
//                    mDotNotifyImgView.setVisibility(View.GONE);
//
//                }
//            });
            deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteInterface.onDeleteClick( notifyModel.getId(), notifyModel.getSec_id() );
                }
            });

        }

        private void setUpDateTime(String cartdatetime, String currentDate, String currentTime) {
            Log.e("toast", "cartdatetime "+cartdatetime+" currentDate:"+currentDate + " currentTime: "+currentTime  ) ;
            Date startDate = null;
            Date endDate = null;
            try {
                endDate = simpleDateFormat.parse(currentDate+" "+currentTime);
                startDate = simpleDateFormat.parse(cartdatetime+" "+ "00:00:00");

            } catch (ParseException e) {
//                e.printStackTrace();
            }

            Log.e("toast", "startDate "+startDate ) ;
            Log.e("toast", "endDate "+endDate ) ;


           StringBuilder proTitle = new StringBuilder();
           proTitle.append(CalculateTimer(startDate, endDate));

           if(elapsedYears !=0){
               proTitle.append(elapsedYears+" years ") ;
           }
           if(elapsedMonths != 0){
               proTitle.append(elapsedMonths+" months ") ;
            }
           if(elapsedDays != 0 && elapsedYears == 0){
               proTitle.append(elapsedDays+" days ") ;
            }
           if(elapsedHours != 0 &&(elapsedYears==0 ||elapsedMonths == 0)){
               proTitle.append(elapsedHours+" hours ") ;
           }

           mDateTV.setText(proTitle);

//            switch (timeNumber){
//                case 0:
//                    mDateTV.setText(proTitle +"few hours");
//                    break;
//                case 1:
//                    mDateTV.setText(proTitle +elapsedDays+" days");
//                    break;
//                case 2:
//                    mDateTV.setText(proTitle +elapsedMonths+" months "+elapsedDays+" days");
//                    break;
//                case 3:
//                    mDateTV.setText(proTitle + elapsedYears+" years "+ elapsedMonths+" months "+elapsedDays+" days" );
//                    break;
//                case 4:
//                    mDateTV.setText(proTitle +"few minutes");
//                    break;
//                default:
//                    break;
//            }

        }
        public String CalculateTimer ( Date startDate, Date endDate){

            String proTitle = "before " ;
//        AppController.getAppController().getInAppNotifier().log("start",  startDate.toString());
//        AppController.getAppController().getInAppNotifier().log("end", endDate.toString());

            long different = endDate.getTime() - startDate.getTime();
            long hoursInMilli = 1000 * 60 * 60;
            long daysInMilli = hoursInMilli * 24;
            long monthInMilli = daysInMilli * 30;
            long yearsInMilli = monthInMilli * 12;
            Log.e("toast", "different:"+different ) ;
            if(different<0){
                different = startDate.getTime() - endDate.getTime();
                proTitle = "after " ;
//                if(different>hoursInMilli){
//                    timeNumber = 5 ;
//                } else if(different>daysInMilli){
//                    timeNumber = 6;
//                } else if(different>monthInMilli){
//                    timeNumber = 7;
//                } else if(different>yearsInMilli){
//                    timeNumber = 8;
//                } else {
//                    timeNumber = 9 ;
//                }
            }
            if(different>yearsInMilli){
                timeNumber = 3;
            } else if(different>monthInMilli){
                timeNumber = 2;
            }  else if(different>daysInMilli){
                timeNumber = 1;
            }else if(different>hoursInMilli){
                timeNumber = 0 ;
            } else {
                timeNumber = 4 ;
            }




            Log.e("toast", "------yearsInMilli:"+yearsInMilli ) ;
            elapsedYears = different / yearsInMilli;
            different = different % yearsInMilli;
            Log.e("toast", "different:"+different ) ;
            Log.e("toast", "elapsedYears:"+elapsedYears ) ;


            elapsedMonths = different / monthInMilli;
            different = different % monthInMilli;
            Log.e("toast", "different:"+different ) ;
            Log.e("toast", "elapsedMonths:"+elapsedMonths ) ;


            elapsedDays = different / daysInMilli;
            different = different % daysInMilli;
            Log.e("toast", "different:"+different ) ;
            Log.e("toast", "elapsedDays:"+elapsedDays ) ;


            elapsedHours =(different / hoursInMilli);
            different = different % hoursInMilli;

            return proTitle ;

        }
    }


}