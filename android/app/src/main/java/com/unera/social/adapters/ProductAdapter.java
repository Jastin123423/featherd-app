package com.unera.social.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unera.social.R;
import com.unera.social.models.Product;
import com.unera.social.utils.ImageLoader;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvTitle.setText(product.title);
        holder.tvPrice.setText(product.price);
        holder.tvLocation.setText(product.location);

        if (product.imageUrl != null && !product.imageUrl.isEmpty()) {
            ImageLoader.getInstance().load(product.imageUrl, holder.ivImage);
        }

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(v.getContext(), product.title + " • " + product.price, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvPrice;
        TextView tvTitle;
        TextView tvLocation;

        ProductViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_product_image);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            tvTitle = itemView.findViewById(R.id.tv_product_title);
            tvLocation = itemView.findViewById(R.id.tv_product_location);
        }
    }
}
