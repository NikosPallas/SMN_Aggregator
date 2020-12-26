package gr.uom.newsmn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FbAdapter extends RecyclerView.Adapter<FbAdapter.MyViewHolder> {

    private ArrayList<FbPost> fbpostlist;

    public FbAdapter(ArrayList<FbPost> fbpostlist) {
        this.fbpostlist = fbpostlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textView.setText(fbpostlist.get(position).getPost());

    }

    @Override
    public int getItemCount() {
        return fbpostlist.size();
    }

    public void clear(){
        fbpostlist.clear();
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.fbImage);
            textView = itemView.findViewById(R.id.postTxt);

        }
    }



}
