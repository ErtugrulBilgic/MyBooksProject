package com.examples.mybooksproject.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.examples.mybooksproject.classes.Book;
import com.examples.mybooksproject.adepter.BookAdepter;
import com.examples.mybooksproject.classes.Bridge;
import com.examples.mybooksproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView list;
    BookAdepter adapter;
    AlertDialog.Builder alert;
    Dialog dialog;
    Button cancel;
    Button addBtn;
    EditText bookName;
    EditText authorName;
    EditText description;
    String sBookName;
    String sAuthorName;
    String sDescription;
    ImageView closeCross;
    ImageView addPic;
    private int imgDeniedCode =0;
    private int imgGrantedCode =1;
    Bitmap selectedBitMap;
    Bitmap edittedPic;
    public static Bridge bookDetail;





    @Override
    public void onBackPressed() {
        alertDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);

        adapter = new BookAdepter(Book.getData(this),this);
        list.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        list.setLayoutManager(manager);
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new BookAdepter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                bookDetail = new Bridge(book.getName(),book.getAuthorName(),book.getDescription(),book.getPic(),book.getId());
                Intent intent = new Intent(MainActivity.this, BookActivity.class);
                startActivity(intent);
                finish();




            }
        });


        fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();

            }
        });
    }
    public void alertDialog() {
        alert = new AlertDialog.Builder(MainActivity.this);

        alert.setTitle("Exit");
        alert.setMessage("Are you sure you want to exit");
        alert.setCancelable(false);
        alert.setIcon(R.drawable.ic_action_exit);
        alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);

            }
        });
        alert.show();

    }

    public void customDialog() {
        showAddDialog();
    }

    private void showAddDialog() {
        dialog = new Dialog(MainActivity.this);
        final boolean[] isSelectedPic = {false};
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(R.layout.add_book_dialog);

        cancel = dialog.findViewById(R.id.cancel);
        addBtn = dialog.findViewById(R.id.add);
        bookName =  dialog.findViewById(R.id.newNameLine);
        authorName = dialog.findViewById(R.id.newAuthorLine);
        description = dialog.findViewById(R.id.newDescriptionLine);
        closeCross = dialog.findViewById(R.id.closeCross);
        addPic = dialog.findViewById(R.id.addPic);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sBookName = bookName.getText().toString();
                sAuthorName = authorName.getText().toString();
                sDescription = description.getText().toString();
                if (TextUtils.isEmpty(sBookName)){
                    Toast.makeText(MainActivity.this, "Book Name can not be empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(sAuthorName)){
                    Toast.makeText(MainActivity.this, "Author Name can not be empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(sDescription)){
                    Toast.makeText(MainActivity.this, "Description can not be empty", Toast.LENGTH_SHORT).show();
                }
                else if(!isSelectedPic[0]){
                    Toast.makeText(MainActivity.this, "Image can not be empty", Toast.LENGTH_SHORT).show();
                }

                else {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    edittedPic = resizeBitmap(selectedBitMap);
                    edittedPic.compress(Bitmap.CompressFormat.PNG,75,outputStream);
                    byte[] gonnaSave = outputStream.toByteArray();


                    try {
                        SQLiteDatabase database = openOrCreateDatabase("Books",MODE_PRIVATE,null);
                         database.execSQL("CREATE TABLE IF NOT EXISTS books(id INTEGER PRIMARY KEY,bookName VARCHAR, authorName VARCHAR,description VARCHAR, bookPic BLOB)");
                         String sqlQuery= "INSERT INTO books(bookName,authorName,description,bookPic) VALUES(?,?,?,?) ";
                        SQLiteStatement sqLiteStatement = database.compileStatement(sqlQuery);
                        sqLiteStatement.bindString(1,sBookName);
                        sqLiteStatement.bindString(2,sAuthorName);
                        sqLiteStatement.bindString(3,sDescription);
                        sqLiteStatement.bindBlob(4,gonnaSave);
                        sqLiteStatement.execute();
                        Toast.makeText(MainActivity.this, "Book saved sucsessfully", Toast.LENGTH_SHORT).show();

                        adapter = new BookAdepter(Book.getData(getApplicationContext()),getApplicationContext());
                        list.setHasFixedSize(true);
                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                        list.setLayoutManager(manager);
                        list.setAdapter(adapter);

                        dialog.dismiss();


                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Server connection error", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });
        closeCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }

        });

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, imgDeniedCode);
                }
                else {
                    isSelectedPic[0] = true;
                    Intent pickPic =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPic,imgGrantedCode);
                }

            }
        });
        dialog.getWindow().setAttributes(params);
        dialog.show();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==imgDeniedCode){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent pickPic =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPic,imgGrantedCode);

            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == imgGrantedCode){
            if (resultCode== RESULT_OK&& data != null){
                Uri picUri = data.getData();

                try {
                    if(Build.VERSION.SDK_INT >= 28){
                        ImageDecoder.Source picSource = ImageDecoder.createSource(this.getContentResolver(),picUri);
                        selectedBitMap = ImageDecoder.decodeBitmap(picSource);
                        addPic.setImageBitmap(selectedBitMap);
                    }
                    else{
                        selectedBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),picUri);
                        addPic.setImageBitmap(selectedBitMap);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private Bitmap resizeBitmap(Bitmap pic){
        return Bitmap.createScaledBitmap(pic,55,62,true);

    }


}