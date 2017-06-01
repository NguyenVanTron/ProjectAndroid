package com.firebase.woflfish.goodbox;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by USER on 1/11/2017.
 */

public class DonorCreateDonationActivity extends Fragment{
    View myView;
    private DatabaseReference mDatabase;
    private List<String> strkeydact;
    private List<DonationActivities> donationactivities;
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
    private Button btnChoseCategory;
    private Button btnChoseAdress;
    private EditText edAdress;
    private  EditText edDescription;
    private EditText edName;
    private EditText edPhone;
    private Button btnDone;
    private CheckBox cbFromHome,cbDropAtLocation;
    private TextView tv_address;
    private TextView tv_category;
    public boolean ischeckImage1  = false ,isCheckImage2 = false , isCheckImage3 = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.donor_create_donation, container, false);
        cbFromHome = (CheckBox)myView.findViewById(R.id.expfromhome) ;
        cbDropAtLocation = (CheckBox)myView.findViewById(R.id.expdropoff);
        listUri = new ArrayList<Uri>();
        urlImages = new HashMap<String,String>();
        categories = new HashMap<String,String>();
        edAdress = (EditText) myView.findViewById(R.id.edit_adress_fromhome);
        imagev1 = (ImageView)myView.findViewById(R.id.imageview1);
        imagev2 = (ImageView)myView.findViewById(R.id.imageview2);
        imagev3 = (ImageView)myView.findViewById(R.id.imageview3);
        edDescription = (EditText) myView.findViewById(R.id.edit_description_donation);
        btnChoseCategory = (Button)myView.findViewById(R.id.btn_chose_category_donation);
        tv_address = (TextView)myView.findViewById(R.id.tv_adress_donation);
        tv_category = (TextView)myView.findViewById(R.id.tv_category_donor);
        btnChoseAdress = (Button) myView.findViewById(R.id.btn_chose_adress_donation);
        btnDone = (Button)myView.findViewById(R.id.btndone);
        edName = (EditText)myView.findViewById(R.id.editNameDonor);
        edPhone = (EditText)myView.findViewById(R.id.edityourphone);
        //
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://goodboxproject.appspot.com");
        //
        mDatabase = FirebaseDatabase.getInstance().getReference();
        strkeydact = new ArrayList<String>();
        donationactivities = new ArrayList<DonationActivities>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatDontion();
            }
        });

        btnChoseAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = adress.size();
                final String[] itemcategory =
                        new String[size];
                for(int i = 0,j = 0;i<6;i++){
                    if(adress.containsKey("Adress" + String.valueOf(i))) {
                        itemcategory[j] = adress.get("Adress" + String.valueOf(j));
                        j++;
                    }
                }

                boolean[] ischeck = new boolean[size];

                adress.clear();
                AlertDialog.Builder buildercate = new AlertDialog.Builder(myView.getContext());
                buildercate.setMultiChoiceItems(itemcategory, ischeck, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            switch (i) {
                                case 0: {
                                    adress.put("Adress0" , itemcategory[0]);break;
                                }
                                case 1: {
                                    adress.put("Adress1" , itemcategory[1]);break;
                                }
                                case 2: {
                                    adress.put("Adress2" , itemcategory[2]);break;
                                }
                                case 3: {
                                    adress.put("Adress3" , itemcategory[3]);break;
                                }
                                case 4: {
                                    adress.put("Adress4" , itemcategory[4]);break;
                                }
                                case 5: {
                                    adress.put("Adress5", itemcategory[5]);break;
                                }
                            }
                        }
                        else
                        {
                            switch (i) {
                                case 0: {
                                    adress.remove("Adress0");break;
                                }
                                case 1: {
                                    adress.remove("Adress1");break;
                                }
                                case 2: {
                                    adress.remove("Adress2");break;
                                }
                                case 3: {
                                    adress.remove("Adress3");break;
                                }
                                case 4: {
                                    adress.remove("Adress4");break;
                                }
                                case 5: {
                                    adress.remove("Adres5");break;
                                }
                            }
                        }
                    }
                });
                buildercate.setTitle("Chọn dia chi bạn muốn den quyen gop!");
                buildercate.setPositiveButton("Xong", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                buildercate.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adress.clear();
                        dialogInterface.cancel();
                    }
                });
                final AlertDialog dialogcate = buildercate.create();
                dialogcate.show();
                String adr = "Cac loai do da chon: ";
                for(int i =0 ;i< adress.size();i++){
                    adr = adress.get("Adress" + String.valueOf(i));
                }
                tv_address.setText(adr);
            }
        });
        btnChoseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = category.size();
                final String[] itemcategory =
                        new String[size];
                for(int i = 0,j = 0;i<8;i++){
                    if(category.containsKey("Category" + String.valueOf(i))) {
                        itemcategory[j] = category.get("Category" + String.valueOf(j));
                        j++;
                    }
                }

                boolean[] ischeck = new boolean[size];

                categories.clear();
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
                dialogcate.show();
                String cate = "Cac loai do da chon: ";
                for(int i =0 ;i< categories.size();i++){
                    cate = categories.get("Category" + String.valueOf(i));
                }
                tv_category.setText(cate);
            }
        });
        //
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
        ReadDonationActivities();
        return myView;
    }
    private String idActi;
    private Map<String,String> category;
    private Map<String,String> adress;
    private boolean CheckIDAcTi(String idActi){
        for(int i = 0 ;i<strkeydact.size();i++){
            if(idActi.equals(strkeydact.get(i))){

                category = new HashMap<String,String>();
                adress = new HashMap<String,String>();
                if(donationactivities.get(i).isOffLocation()) {
                    category.putAll(donationactivities.get(i).getCategories());
                    adress.putAll(donationactivities.get(i).getAdress());
                    return true;
                }
            }
        }
        return false;
    }
    private void CreatDontion(){
        if(edDescription.getText().toString().equals("")){
            Toast.makeText(myView.getContext(),"Bạn chưa nhập mô tả",Toast.LENGTH_LONG).show();
            return;
        }
        if(cbDropAtLocation.isChecked() == false && cbFromHome.isChecked() == false){
            Toast.makeText(myView.getContext(),"Bạn phải chọn cách quyên góp trực tiếp hoặc tại nhà",Toast.LENGTH_LONG).show();
            return;
        }
        if(cbFromHome.isChecked()){
            cbDropAtLocation.setChecked(false);
        }
        if(cbDropAtLocation.isChecked()){
            cbFromHome.setChecked(false);
        }
        String iddonor;
        SharedPreferences sharedPreferences6= myView.getContext().getSharedPreferences("IdDonor", Context.MODE_PRIVATE);
        if(sharedPreferences6 !=null){
            iddonor = sharedPreferences6.getString("idDon","IdDon");
        }
        else{
            iddonor = "";}
        if(cbDropAtLocation.isChecked()){//nếu quyên góp trực tiếp
            AtDropOffLocation atdr = new AtDropOffLocation();
            atdr.setStatus(0);
            atdr.setCategories(categories);
            atdr.setAdress(adress);
            atdr.setIdActivity(idActi);
            atdr.setIdDonor(iddonor);
            Date today = new Date();
            CharSequence sDate  = DateFormat.format("MMMM d, yyyy ", today.getTime());
            atdr.setDonationDate(sDate.toString());
            atdr.setDescription(edDescription.getText().toString());
            int size = listUri.size();
            for(int i = 0;i<size;i++){
                String index = "Url" + String.valueOf(i);
                UpLoadImage(listUri.get(i));
                if(imageChose==null){
                    Toast.makeText(myView.getContext(),"Lỗi load hình",Toast.LENGTH_LONG).show();
                    return;
                }
                String value = "" + imageChose;
                //urlImages.put(index,value);
                atdr.setUrlImage(index,value);
            }

            //
            mDatabase.child("Donations").child("AtDropOffLocation").push().setValue(atdr);
            String iddonation = mDatabase.child("Donations").child("AtDropOffLocation").push().getKey();
            //
            mDatabase.child("Users").child("Donors").child(iddonor).child("donation").child(iddonation).setValue(true);
            mDatabase.child("CharityActivies").child("DonationActivities").child(idActi).child("idDonations").child(iddonation).setValue(true);



        }
        else{
            PickedUpFromHome pufh = new PickedUpFromHome();
            pufh.setStatus(0);
            pufh.setCategories(categories);
            pufh.setIdActivity(idActi);
            pufh.setIdDonor(iddonor);
            Date today = new Date();
            CharSequence sDate  = DateFormat.format("MMMM d, yyyy ", today.getTime());
            pufh.setDonationDate(sDate.toString());
            pufh.setDescription(edDescription.getText().toString());
            pufh.setAdress(edAdress.getText().toString());
            pufh.setName(edName.getText().toString());
            pufh.setPhoneNumber(edPhone.getText().toString());
            int size = listUri.size();
            for(int i = 0;i<size;i++){
                String index = "Url" + String.valueOf(i);
                UpLoadImage(listUri.get(i));
                if(imageChose==null){
                    Toast.makeText(myView.getContext(),"Lỗi load hình",Toast.LENGTH_LONG).show();
                    return;
                }
                String value = "" + imageChose;
                //urlImages.put(index,value);
                pufh.setUrlImage(index,value);
            }
            //
            mDatabase.child("Donations").child("PickedUpFromHome").push().setValue(pufh);
            String iddonation = mDatabase.child("Donations").child("AtDropOffLocation").push().getKey();
            //
            mDatabase.child("Users").child("Donors").child(iddonor).child("donation").child(iddonation).setValue(true);
            mDatabase.child("CharityActivies").child("DonationActivities").child(idActi).child("idDonations").child(iddonation).setValue(true);
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
    private void ReadDonationActivities(){
        mDatabase.child("CharityActivities").child("DonationActivities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postDatanapshot : dataSnapshot.getChildren()) {
                    String uid = postDatanapshot.getKey();
                    strkeydact.add(uid);
                    DonationActivities dact = postDatanapshot.getValue(DonationActivities.class);
                    donationactivities.add(dact);
                }
                if(strkeydact.size()>0){
                    SharedPreferences sharedPreferences1= myView.getContext().getSharedPreferences("IdActivity", Context.MODE_PRIVATE);
                    if(sharedPreferences1!=null){
                        idActi = sharedPreferences1.getString("IdActi","IdActi");
                    }
                    else{
                        idActi = "IdActi";
                    }
                    CheckIDAcTi(idActi);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
