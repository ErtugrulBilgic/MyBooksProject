package com.examples.mybooksproject.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.examples.mybooksproject.R;

import java.util.Arrays;

public class BookActivity extends AppCompatActivity {
    AlertDialog.Builder alert;

    private ImageView picHolder;
    private TextView authorNameHolder;
    private TextView descriptionHolder;

    private String name;
    private String authorName;
    private String description;
    private Bitmap pic;


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            alertDialog();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        this.getMenuInflater().inflate(R.menu.menu,menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);


        picHolder = findViewById(R.id.detailedPic);

        authorNameHolder = findViewById(R.id.detailedAuthorName);
        descriptionHolder = findViewById(R.id.detailedDescription);

        name = MainActivity.bookDetail.getName();
        authorName = MainActivity.bookDetail.getAuthorName();
        description = MainActivity.bookDetail.getDescription();
        pic = MainActivity.bookDetail.getPic();



        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(authorName) && !TextUtils.isEmpty(description)){
            picHolder.setBackgroundColor(Color.TRANSPARENT);
            setTitle(name);
            authorNameHolder.setText(authorName);
            descriptionHolder.setText(description);
            picHolder.setImageBitmap(pic);


        }



    }

    public void alertDialog() {
        alert = new AlertDialog.Builder(BookActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete");
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
                try {
                    SQLiteDatabase database = openOrCreateDatabase("Books",MODE_PRIVATE,null);
                    database.execSQL("DELETE FROM books WHERE "+MainActivity.bookDetail.getId()+" LIKE id;");

                    onBackPressed();


                } catch (SQLException e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));

                }
            }
        });
        alert.show();

    }

}