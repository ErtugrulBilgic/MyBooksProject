package com.examples.mybooksproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdepter extends RecyclerView.Adapter<BookAdepter.BookHolder> {

    private ArrayList<Book> bookList;
    private Context context;
    private OnItemClickListener clickListener;

    public BookAdepter(ArrayList<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_item,parent,false);

        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Book book = bookList.get(position);
        holder.setData(book);



    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    class BookHolder extends RecyclerView.ViewHolder {
        TextView txtBookName;
        TextView txtBookAuthor;
        TextView txtBookDescription;
        ImageView imgBookPic;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            txtBookName = itemView.findViewById(R.id.bookName);
            txtBookAuthor = itemView.findViewById(R.id.authorName);
            txtBookDescription = itemView.findViewById(R.id.description);
            imgBookPic = itemView.findViewById(R.id.pic_place_holder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(clickListener!=null && position != RecyclerView.NO_POSITION){
                        clickListener.onItemClick(bookList.get(position));
                    }
                }
            });

        }
        public void setData(Book book){
            this.txtBookName.setText(book.getName());
            this.txtBookAuthor.setText(book.getAuthorName());
            this.txtBookDescription.setText(book.getDescription());
            this.imgBookPic.setImageBitmap(book.getPic());




        }


    }
    public interface OnItemClickListener{
        void onItemClick(Book book);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.clickListener = listener;

    }

}
