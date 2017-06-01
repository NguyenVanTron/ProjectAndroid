package com.firebase.woflfish.goodbox;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.woflfish.goodbox.ActivityListRecyclerAdapter.RecyclerViewHolderActivity;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 1/14/2017.
 */

public class ActivityListRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerViewHolderActivity> {
    private List<DonationActivities> mapActivities;
    private Context parent;
    public ActivityListRecyclerAdapter(Context par){
        this.mapActivities = new ArrayList<DonationActivities>();
        this.parent = par;
    }
    public ActivityListRecyclerAdapter(List<DonationActivities> data) {
        this.mapActivities = new ArrayList<DonationActivities>();
        this.mapActivities = data;
    }

    public void setAdapter(DonationActivities data){
        this.mapActivities.add(data);
    }
    public ActivityListRecyclerAdapter() {
        this.mapActivities = new ArrayList<DonationActivities>();
    }

    public void updateList(List<DonationActivities> data){
        this.mapActivities = data;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerViewHolderActivity onCreateViewHolder(ViewGroup viewGroup,
                                                 int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.charity_activity_list_item, viewGroup, false);
        return new RecyclerViewHolderActivity(itemView);
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                //e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerViewHolderActivity viewHolder, int position) {
        String fullname = this.mapActivities.get(position).getName();
        if(this.mapActivities.get(position).getUrlImages().size()>0){
            String mPhotoUrl = this.mapActivities.get(position).getUrlImages().get("Url0");
            new DownloadImageTask(viewHolder.imageViewPhoto)
                    .execute(mPhotoUrl);
        }
        viewHolder.tvName.setText(fullname);
        SharedPreferences sharedPreferences = parent.getSharedPreferences("IdCharity", Context.MODE_PRIVATE);
        String Name;
        if (sharedPreferences != null) {
            Name = sharedPreferences.getString("name", "IdName");
        } else {
            Name = "";
        }
        viewHolder.tvNameChar.setText(Name);

    }

    @Override
    public int getItemCount() {
        return this.mapActivities.size();
    }

    public void addItem(int position, DonationActivities user) {
        this.mapActivities.add(user);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        this.mapActivities.remove(position);
        notifyItemRemoved(position);
    }
    public int GetPoss(int pos){
        return pos;
    }
    /**
     * ViewHolder for item view of list
     * */

    public class RecyclerViewHolderActivity extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public TextView tvName;
        public TextView tvNameChar;
        public ImageView imageViewPhoto;
        public CardView cardView;
        public RecyclerViewHolderActivity(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.txt_name_charity_activity_item);
            imageViewPhoto = (ImageView) itemView.findViewById(R.id.image_charity_activity_item);
            cardView = (CardView)itemView.findViewById(R.id.cv_activity_item);
            tvNameChar =(TextView)itemView.findViewById(R.id.txt_organization_charity_activity_item) ;
            // set listener for button delete
            cardView.setOnClickListener(this);
        }

        // xem chi tiết tổ chức từ thiện
        @Override
        public void onClick(View v) {
            Log.e("Click card","Thanh công");
            final Context context = parent;
            int pos = getAdapterPosition();
            SharedPreferences sharedPreferences= parent.getSharedPreferences("PosActivity", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("positionA", pos);
            editor.apply();

            FragmentManager fm = ((Activity) context).getFragmentManager();
            Log.e("Packedname",parent.getClass().getName() );
            if(parent.getClass().getName().equals("com.firebase.woflfish.goodbox.HomeCharityOrganizationActivity")) {
                fm.beginTransaction()
                    .replace(R.id.content_frame_home_charity,new DetailActivityCharityActivity())
                    .commit();
            }
            else{
                fm.beginTransaction()
                        .replace(R.id.content_frame_home_donor,new DetailActivityDonorActivity())
                        .commit();
            }
        }
    }
}
