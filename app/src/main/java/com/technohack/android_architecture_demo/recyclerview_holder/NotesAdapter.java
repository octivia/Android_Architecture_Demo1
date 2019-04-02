package com.technohack.android_architecture_demo.recyclerview_holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technohack.android_architecture_demo.R;
import com.technohack.android_architecture_demo.room_database.NotesEntity;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends ListAdapter<NotesEntity,NotesAdapter.MyViewHolder> {

    private OnItemClickListener listener;
     //After using the ListAdapter we don't need to use the List of notes because ListAdapter cares of it already
    //private List<NotesEntity> notes=new ArrayList<>();
    private Context context;

    //this method will take care of default recyclerView animation
    public NotesAdapter() {
        super(diffCallBack);
    }

    //for that we need to apply some logic so that ListAdapter will what to do when we apply some logic inside the method
    private static final DiffUtil.ItemCallback<NotesEntity> diffCallBack= new DiffUtil.ItemCallback<NotesEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull NotesEntity oldNotes, @NonNull NotesEntity newNotes) {
            return oldNotes.getId()==newNotes.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NotesEntity oldNotes, @NonNull NotesEntity newNotes) {

            // return oldNotes.getId()==newNotes.getId();
            /*
             We don't use this logic because in this logic after deleting or updating the any notes all the notes are
             flashed that will show that all the notes items are get updated but we don't this type of flashing in our
             so we will apply another logic to implement some cool animation
             */
            return oldNotes.getTitle().equals(newNotes.getTitle()) &&
                    oldNotes.getDescription().equals(newNotes.getDescription()) &&
                    oldNotes.getPriority()==newNotes.getPriority();

        }
    };

    /*
    public NotesAdapter(List<NotesEntity> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }
    */

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_notes,viewGroup,false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        myViewHolder.mTitle.setText(getItem(position).getTitle());
        myViewHolder.mDescription.setText(getItem(position).getDescription());
        myViewHolder.mPriority.setText(getItem(position).getPriority());

    }

    //We don't need anymore this method because ListAdapter will care take of it
    /*
    @Override
    public int getItemCount() {
        return notes.size();
    }
    */


    //To save the notes
    /*
    public void setNotes(List<NotesEntity> notes){
         this.notes=notes;
         notifyDataSetChanged();
    }
    */
    //To get the position of the notes so that we can delete the particular notes by swiping left or right
    public NotesEntity getNoteAt(int position) {
        return getItem(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mDescription;
        TextView mPriority;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle=itemView.findViewById(R.id.text_view_title);
            mDescription=itemView.findViewById(R.id.text_view_description);
            mPriority=itemView.findViewById(R.id.text_view_priority);

        }
    }
    //These steps will use to update the data of the notes

    //To make the item of the recyclerView Clickable
    public interface OnItemClickListener {
        void onItemClick(NotesEntity note);
    }

    //Making a method to access the onclick method by implementing the OnItemClickListener in this method
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
