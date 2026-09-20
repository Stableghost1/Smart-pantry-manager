package com.example.smartpantrymanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private final ArrayList<PantryItem> pantryItems;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    public PantryAdapter(ArrayList<PantryItem> pantryItems,
                         OnItemClickListener listener) {

        this.pantryItems = pantryItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position) {

        PantryItem item = pantryItems.get(position);

        holder.itemName.setText(item.getName());

        holder.itemDetails.setText(
                "Quantity: " + item.getQuantity() +
                        " " + item.getUnit() +
                        "\nCategory: " + item.getCategory() +
                        "\nExpiry: " + item.getExpiryDate()
        );

        holder.editButton.setTextColor(Color.WHITE);
        holder.deleteButton.setTextColor(Color.WHITE);

        holder.editButton.setOnClickListener(v ->
                listener.onEditClick(item)
        );

        holder.deleteButton.setOnClickListener(v ->
                listener.onDeleteClick(item)
        );
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public static class PantryViewHolder
            extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView itemDetails;
        Button editButton;
        Button deleteButton;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(
                    R.id.textItemName
            );

            itemDetails = itemView.findViewById(
                    R.id.textItemDetails
            );

            editButton = itemView.findViewById(
                    R.id.buttonEdit
            );

            deleteButton = itemView.findViewById(
                    R.id.buttonDelete
            );
        }
    }
}