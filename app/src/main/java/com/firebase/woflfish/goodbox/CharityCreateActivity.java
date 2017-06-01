package com.firebase.woflfish.goodbox;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by USER on 1/7/2017.
 */

public class CharityCreateActivity extends Fragment {
    View myView;

    private ListView lvAdress;
    private ArrayList<String> arrList=null;
    private ArrayAdapter<String> adapterlv=null;

    //
    private ImageView imagev1,imagev2,imagev3;
    private Uri imageCaptureUri;
    private List<Uri> listUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    FirebaseStorage storage;
    StorageReference storageRef;
    private Uri imageChose;
    //Input data
    private Map<String,String> urlImages;
    private Map<String,String> categories;
    private Map<String,String> adress;
    private Map<String,String> idDonations;
    //display data
    private EditText edName;
    private EditText edStartDate;
    private EditText edFinishDate;
    private EditText edDescription;
    private CheckBox ckCategory;
    private CheckBox ckOffLocation;
    private CheckBox ckFromHome;
    private EditText txtAdress;
    private Button btnAddAdress;
    private Button btnChoseCategory;
    private Button btnCreateActivity;
    //User
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //Database
    private DatabaseReference mDatabase;
    public boolean ischeckImage1  = false ,isCheckImage2 = false , isCheckImage3 = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.charity_create_activity, container, false);
        listUri = new ArrayList<Uri>();
        btnAddAdress = (Button)myView.findViewById(R.id.btn_chose);
        txtAdress = (EditText) myView.findViewById(R.id.edit_adress);
        lvAdress=(ListView) myView.findViewById(R.id.lv_adress);
        imagev1 = (ImageView)myView.findViewById(R.id.imagev1);
        imagev2 = (ImageView)myView.findViewById(R.id.imagev2);
        imagev3 = (ImageView)myView.findViewById(R.id.imagev3);
        edName = (EditText) myView.findViewById(R.id.edit_name);
        edStartDate= (EditText) myView.findViewById(R.id.edit_startdate);
        edFinishDate= (EditText) myView.findViewById(R.id.edit_finishdate);
        edDescription = (EditText) myView.findViewById(R.id.edit_description);
        ckOffLocation =(CheckBox)myView.findViewById(R.id.expandablecheckbox1) ;
        ckFromHome=(CheckBox)myView.findViewById(R.id.ck_fromhome);
        btnChoseCategory = (Button)myView.findViewById(R.id.btn_chose_category);
        btnCreateActivity = (Button)myView.findViewById(R.id.btn_create_activity);
        ckCategory = (CheckBox)myView.findViewById(R.id.expandablecheckbox1) ;
        //
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://goodboxproject.appspot.com");
        //
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //Chọn đồ
        categories = new HashMap<String,String>();
        urlImages = new HashMap<String,String>();
        adress = new HashMap<String, String>();
        btnCreateActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateActivity();
            }
        });
        final String[] itemcategory =
                new String[]{"Phương tiện di chuyển",
                "Quần áo, giày dép, trang sức,...",
                "Đồ chơi, dụng cụ thể thao",
                "Sách, vở, băng đĩa, dụng cụ học tập",
                 "Đồ dùng trong nhà",
                        "Thiết bị điện tử",
                        "Trang thiết bị nội thất",
        "Khác"};
        boolean[] ischeck = new boolean[8];
        AlertDialog.Builder buildercate = new AlertDialog.Builder(myView.getContext());
        buildercate.setMultiChoiceItems(itemcategory, ischeck, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b) {
                    switch (i) {
                        case 0: {
                            categories.put("Category0" , itemcategory[0]);break;
                        }
                        case 1: {
                            categories.put("Category1" , itemcategory[1]);break;
                        }
                        case 2: {
                            categories.put("Category2" , itemcategory[2]);break;
                        }
                        case 3: {
                            categories.put("Category3" , itemcategory[3]);break;
                        }
                        case 4: {
                            categories.put("Category4" , itemcategory[4]);break;
                        }
                        case 5: {
                            categories.put("Category5", itemcategory[5]);break;
                        }
                        case 6: {
                            categories.put("Category6" , itemcategory[6]);break;
                        }
                        case 7: {
                            categories.put("Category7" , itemcategory[7]);break;
                        }
                    }
                }
                else
                {
                    switch (i) {
                        case 0: {
                            categories.remove("Category0");break;
                        }
                        case 1: {
                            categories.remove("Category1");break;
                        }
                        case 2: {
                            categories.remove("Category2");break;
                        }
                        case 3: {
                            categories.remove("Category3");break;
                        }
                        case 4: {
                            categories.remove("Category4");break;
                        }
                        case 5: {
                            categories.remove("Category5");break;
                        }
                        case 6: {
                            categories.remove("Category6");break;
                        }
                        case 7: {
                            categories.remove("Category7");break;
                        }
                    }
                }
            }
        });
        buildercate.setTitle("Chọn loại đồ bạn muốn !");
        buildercate.setPositiveButton("Xong", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        buildercate.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                categories.clear();
                dialogInterface.cancel();
            }
        });
        final AlertDialog dialogcate = buildercate.create();
        btnChoseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogcate.show();
            }
        });
        //Chọn ảnh
        final String[] items = new String[]{"Chụp hình","Chọn từ bộ sưu tập"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(myView.getContext(),android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i ==0){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(),"tmp_avatar"+String.valueOf(System.currentTimeMillis())+".jpg");
                    imageCaptureUri = Uri.fromFile(file);
                    try{
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageCaptureUri);
                        intent.putExtra("return data",true);
                        startActivityForResult(intent,PICK_FROM_CAMERA);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                    dialogInterface.cancel();
                }
                else{
                    Log.e("Vao dc else","Debug");
                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //intent.setType("image/*");
                    //intent.setAction(Intent.ACTION_GET_CONTENT);Intent.createChooser(intent,"Complete action using")
                    startActivityForResult(intent,PICK_FROM_FILE);
                }
            }
        });
        builder.setTitle("Thêm ảnh bạn muốn !");
        final AlertDialog dialog = builder.create();

        imagev1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ischeckImage1 =true;
                isCheckImage3 = false;
                isCheckImage2 = false;
                dialog.show();
            }
        });
        imagev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCheckImage2 =true;
                ischeckImage1 =false;
                isCheckImage3 = false;
                dialog.show();

            }
        });
        imagev3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCheckImage3 =true;
                ischeckImage1 =false;
                isCheckImage2 = false;
                dialog.show();

            }
        });
        //1. Tạo ArrayList object
        arrList=new ArrayList<String>();
        //2. Gán Data Source (ArrayList object) vào ArrayAdapter
        adapterlv=new ArrayAdapter<String>
                (this.getActivity(),
                        android.R.layout.simple_list_item_1,
                        arrList);
        //3. gán Adapter vào ListView
        lvAdress.setAdapter(adapterlv);
        btnAddAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrList.add(txtAdress.getText()+"");
                adapterlv.notifyDataSetChanged();
            }
        });
        return myView;
    }
    //Tạo hoạt động
    private boolean CreateActivity(){
        if(edName.getText().toString().equals("")){
            Toast.makeText(this.getActivity(),"Bạn chưa nhập tên",Toast.LENGTH_LONG).show();
            return false;
        }
        if(edDescription.getText().toString().equals("")){
            Toast.makeText(this.getActivity(),"Bạn chưa nhập mô tả",Toast.LENGTH_LONG).show();
            return false;
        }
        if(edFinishDate.getText().toString().equals("")){
            Toast.makeText(this.getActivity(),"Bạn chưa nhập ngày kết thúc",Toast.LENGTH_LONG).show();
            return false;
        }
        if(edStartDate.getText().toString().equals("")){
            Toast.makeText(this.getActivity(),"Bạn chưa nhập ngày bắt đầu",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!ckCategory.isChecked()){
            if(listUri.size() == 0){
                Toast.makeText(this.getActivity(),"Bạn chưa phải chọn ít nhất một hình ảnh",Toast.LENGTH_LONG).show();
                return false;
            }
            Activities newActi = new Activities();
            newActi.setName(edName.getText().toString());
            Date today = new Date();
            CharSequence sDate  = DateFormat.format("MMMM d, yyyy ", today.getTime());
            newActi.setCreateDate(sDate.toString());
            newActi.setFinishDate(edFinishDate.getText().toString());
            newActi.setDescription(edDescription.getText().toString());
            newActi.setStartDate(edStartDate.getText().toString());
            int size = listUri.size();
            for(int i = 0;i<size;i++){
                String index = "Url" + String.valueOf(i);
                UpLoadImage(listUri.get(i));
                String value = "" + imageChose;
                urlImages.put(index,value);
            }
            newActi.setUrlImages(urlImages);
            String log = urlImages.get("Url0");
            //Log.e("Xem url: ",log);
            String idchar = mUser.getUid();
            newActi.setIdCharity(idchar);

            mDatabase.child("CharityActivities").child("Activities").push().setValue(newActi);
            String idActivity = mDatabase.child("CharityActivities").child("Activities").push().getKey();
            mDatabase.child("Users").child("CharityOrganizations").child(idchar).child("activity").child(idActivity).setValue(true);
            Log.e("Xem key",idActivity);
            return true;
        }
        else{
            DonationActivities newActi = new DonationActivities();
            newActi.setName(edName.getText().toString());
            Date today = new Date();
            CharSequence sDate  = DateFormat.format("MMMM d, yyyy ", today.getTime());
            newActi.setCreateDate(sDate.toString());
            newActi.setFinishDate(edFinishDate.getText().toString());
            newActi.setDescription(edDescription.getText().toString());
            newActi.setStartDate(edStartDate.getText().toString());
            int size = listUri.size();
            for(int i = 0;i<size;i++){
                String index = "Url" + String.valueOf(i);
                UpLoadImage(listUri.get(i));
                if(imageChose==null){
                    Toast.makeText(myView.getContext(),"Lỗi load hình",Toast.LENGTH_LONG).show();
                    return false;
                }
                String value = "" + imageChose;
                //urlImages.put(index,value);
                newActi.setUrlImage(index,value);
            }

            //newActi.setUrlImages(urlImages);
            String log = urlImages.get("Url0");
            //Log.e("Xem url: ",log);
            String idchar = mUser.getUid();
            newActi.setIdCharity(idchar);
            newActi.setCategories(categories);
            newActi.setOffLocation(ckOffLocation.isChecked());
            newActi.setFromHome(ckFromHome.isChecked());
            if(ckOffLocation.isChecked()){
                for(int i = 0;i<arrList.size();i++){
                    String index = "Adress"  +String.valueOf(i);
                    newActi.setOnlyAdress(index,arrList.get(i));
                }
            }

            mDatabase.child("CharityActivities").child("DonationActivities").push().setValue(newActi);
            String idActivity = mDatabase.child("CharityActivities").child("Activities").push().getKey();
            mDatabase.child("Users").child("CharityOrganizations").child(idchar).child("activity").child(idActivity).setValue(true);
            Log.e("Xem key",idActivity);
            return true;
        }
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
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    private void LoadImages(){
        //List<Uri> images = new ArrayList<Uri>();
        StorageReference forestRef = storageRef.child("images");
        String url1 = forestRef.getDownloadUrl().toString();
        Log.e("Debug",url1);
        new DownloadImageTask(imagev1)
                .execute("https://firebasestorage.googleapis.com/v0/b/goodboxproject.appspot.com/o/images%2F14101?alt=media&token=0603b5ac-53db-4cba-8ad7-2a2d6d01758c");
        List task = forestRef.getActiveDownloadTasks();
        int i=0,n = task.size();
        while(n> 0){
            String url = task.get(i).toString();
            i++;
            n--;
            new DownloadImageTask(imagev1)
                    .execute("http://2.bp.blogspot.com/-2f3RJi8bT2E/TmFBsSWBibI/AAAAAAAACOc/WWyyA8FNPi4/s1600/onClick.png");
        }
    }

    private void UpLoadImage(Uri file){
        Log.e("Debug","Vào được UploadImage");
        final String[] uri = new String[1];
        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Log.e("Debug","Thành công");
                    imageChose =  taskSnapshot.getDownloadUrl();

            }
        });
        /*uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e("Debug","Lỗi");

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Log.e("Debug","Thành công");
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                while(taskSnapshot.getDownloadUrl()==null);
                imageChose =  taskSnapshot.getDownloadUrl();
                //Toast.makeText(myView.getContext(),"Thành công",Toast.LENGTH_LONG).show();
                Log.e("Xem url: ",imageChose + "");
                //urlImages.put(index,downloadUrl +"");
            }
        });*/
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Vao dc result","Debug");
        if(resultCode != RESULT_OK){
            return;
        }
        Bitmap bitmap = null;
        String path = "";
        if(requestCode == PICK_FROM_FILE){
            imageCaptureUri = data.getData();
            path = getRealPathFromUri(imageCaptureUri);
            if(path==null){
                path = imageCaptureUri.getPath();
            }
            if(path!=null){
                //Uri file = Uri.fromFile(new File(path));

                Log.e("Path khac null","Debug");
                bitmap = BitmapFactory.decodeFile(path);
            }
        }
        else{
            //Uri file = Uri.fromFile(new File(path));

            path = imageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }
        if(ischeckImage1) {
            listUri.add(imageCaptureUri);
            Log.e("Hinh anh",imageCaptureUri.toString());
            imagev1.setImageBitmap(bitmap);
        }
        else if(isCheckImage2) {
            listUri.add(imageCaptureUri);
            imagev2.setImageBitmap(bitmap);
        }
        else if(isCheckImage3) {
            listUri.add(imageCaptureUri);
            imagev3.setImageBitmap(bitmap);
        }
    }

    private String getRealPathFromUri(Uri contentUri) {
        String [] proj = {MediaStore.Images.Media.DATA};
        Cursor cusor = this.getActivity().managedQuery(contentUri,proj,null,null,null);
        if(cusor == null) return null;
        int column_index = cusor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cusor.moveToFirst();
        Log.e("Cusor khac null","Debug");
        return cusor.getString(column_index);
    }
}