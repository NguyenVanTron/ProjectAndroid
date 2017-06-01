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

import com.firebase.woflfish.goodbox.CharityOrganizationRecyclerAdapter.RecyclerViewHolder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 1/14/2017.
 */

public class CharityOrganizationRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerViewHolder> {
    private List<CharityOrganization> mapCharityOrgs;
    private Context parent;
    public CharityOrganizationRecyclerAdapter(Context par){
        this.mapCharityOrgs = new ArrayList<CharityOrganization>();
        this.parent = par;
    }
    public CharityOrganizationRecyclerAdapter(List<CharityOrganization> data) {
        this.mapCharityOrgs = new ArrayList<CharityOrganization>();
        this.mapCharityOrgs = data;
    }

    public void setAdapter(CharityOrganization data){
        this.mapCharityOrgs.add(data);
    }
    public CharityOrganizationRecyclerAdapter() {
        this.mapCharityOrgs = new ArrayList<CharityOrganization>();
    }

    public void updateList(List<CharityOrganization> data){
        this.mapCharityOrgs = data;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                 int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.charity_organization_list_item, viewGroup, false);
        return new RecyclerViewHolder(itemView);
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
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        String fullname = this.mapCharityOrgs.get(position).getFullName();
        String mPhotoUrl = this.mapCharityOrgs.get(position).getUrlPhoto();
        viewHolder.tvName.setText(fullname);
        new DownloadImageTask(viewHolder.imageViewPhoto)
                .execute(mPhotoUrl);
    }

    @Override
    public int getItemCount() {
        return this.mapCharityOrgs.size();
    }

    public void addItem(int position, CharityOrganization user) {
        this.mapCharityOrgs.add(user);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        this.mapCharityOrgs.remove(position);
        notifyItemRemoved(position);
    }
    public int GetPoss(int pos){
        return pos;
    }
    /**
     * ViewHolder for item view of list
     * */

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public TextView tvName;
        public ImageView imageViewPhoto;
        public CardView cardView;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.txt_charity_organization_item);
            imageViewPhoto = (ImageView) itemView.findViewById(R.id.image_charity_organization_item);
            cardView = (CardView)itemView.findViewById(R.id.card_view_item);
            // set listener for button delete
            cardView.setOnClickListener(this);
        }

        // xem chi tiết tổ chức từ thiện
        @Override
        public void onClick(View v) {
            Log.e("Click card","Thanh công");
            final Context context = parent;
            int pos = getAdapterPosition();
            SharedPreferences sharedPreferences= parent.getSharedPreferences("gameSetting", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("position", pos);
            editor.putBoolean("checkusser",true);
            editor.apply();

            FragmentManager fm = ((Activity) context).getFragmentManager();
            Log.e("Packedname",parent.getClass().getName() );
            /*if(parent.getClass().getName().equals("com.firebase.woflfish.goodbox.HomeCharityOrganizationActivity")) {
                fm.beginTransaction()
                    .replace(R.id.content_frame_home_charity,new DetailCharityActivity())
                    .commit();
            }
            else{*/
                fm.beginTransaction()
                        .replace(R.id.content_frame_home_donor,new DetailCharityActivity())
                        .commit();

           // }
        }
    }
}
