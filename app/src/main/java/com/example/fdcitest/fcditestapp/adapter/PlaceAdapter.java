package com.example.fdcitest.fcditestapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fdcitest.R;
import com.example.fdcitest.fcditestapp.model.Place;
import com.example.fdcitest.fcditestapp.utility.Debugger;

import java.util.ArrayList;


public class PlaceAdapter  extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{

    private ArrayList<Place> mData;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener itemClickListener;

    public PlaceAdapter(Context context, ArrayList<Place> data)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.listrow_place, parent, false);

        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        try
        {
            final Place place = mData.get(position);

            holder.tvName.setText(place.getName());

        }catch (Exception err)
        {
            Debugger.logD("onBindViewHolder "+err.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<Place> getData()
    {
        return mData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView tvName;
        CardView cvPlace;

        ViewHolder(View itemView)
        {
            super(itemView);
            tvName  = itemView.findViewById(R.id.tvName);
            cvPlace = itemView.findViewById(R.id.cvPlace);

            cvPlace.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if (itemClickListener != null)
            {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public Place getItem(int id)
    {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }
}
