package com.examples.mybooksproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class Book {
    private String name;
    private String authorName;
    private String description;
    private Bitmap pic;

    Book(){}

    public Book(String name, String authorName, String description, Bitmap pic) {
        this.name = name;
        this.authorName = authorName;
        this.description = description;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }
    public static ArrayList<Book> getData(Context context){
        ArrayList<Book> books = new ArrayList<Book>();
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> authorNames = new ArrayList<String>();
        ArrayList<String> descriptions = new ArrayList<String>();
        ArrayList<Bitmap> pics = new ArrayList<Bitmap>();

        try {
            SQLiteDatabase database = context.openOrCreateDatabase("Books", Context.MODE_PRIVATE,null);
            Cursor cursor = database.rawQuery("SELECT * FROM books",null);

            int nameIndex = cursor.getColumnIndex("bookName");
            int authorNameIndex = cursor.getColumnIndex("authorName");
            int descriptionIndex = cursor.getColumnIndex("description");
            int picIndex = cursor.getColumnIndex("bookPic");

            while(cursor.moveToNext()){
                names.add(cursor.getString(nameIndex));
                authorNames.add(cursor.getString(authorNameIndex));
                descriptions.add(cursor.getString(descriptionIndex));
                byte[] picFromServer = cursor.getBlob(picIndex);
                Bitmap bitmap = BitmapFactory.decodeByteArray(picFromServer,0, picFromServer.length);
                pics.add(bitmap);

            }
            cursor.close();

            for (int i = 0 ;i < names.size(); i++){
                Book book = new Book(names.get(i), authorNames.get(i), descriptions.get(i), pics.get(i) );
                books.add(book);
            }



        }catch (Exception e){
            e.printStackTrace();
        }


        return books;
    }
}
