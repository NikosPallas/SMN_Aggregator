package gr.uom.newsmn;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.MyViewHolder>{


    private ArrayList<String> InstaUrls;

    public InstaAdapter(ArrayList<String> InstaUrls){
        this.InstaUrls = InstaUrls;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(InstaUrls.get(position)).into(holder.InstaImage);

    }

    @Override
    public int getItemCount() {
        return InstaUrls.size();
    }

    public void clear(){
        InstaUrls.clear();
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView InstaImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            InstaImage = itemView.findViewById(R.id.instaImage);
        }

    }
}
