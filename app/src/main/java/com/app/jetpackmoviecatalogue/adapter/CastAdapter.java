package com.app.jetpackmoviecatalogue.adapter;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jetpackmoviecatalogue.R;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Crew;
import com.app.jetpackmoviecatalogue.databinding.ItemCastBinding;
import com.app.jetpackmoviecatalogue.databinding.ItemMvPosterBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class CastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = CastAdapter.class.getSimpleName();
    private SparseArray<Crew> crewSparseArray = new SparseArray<>();
    private String  content;
    private static final int POSTER = 0;
    private static final int CREW = 1;

    public void setData(SparseArray<Crew> crewList, String mvPoster) {
        this.content = mvPoster;
        crewSparseArray.clear();
        for (int i = 0; i < crewList.size(); i++) {
            crewSparseArray.put(i, crewList.valueAt(i));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == POSTER) {
            ItemMvPosterBinding posterBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_mv_poster,
                    parent,
                    false);
            return new PosterViewHolder(posterBinding);
        }
        ItemCastBinding castBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_cast,
                parent,
                false);
        return new CrewViewHolder(castBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        String imagePath = "https://image.tmdb.org/t/p/w185";
        if ((holder.getItemViewType() == CREW) && (crewSparseArray != null && position < crewSparseArray.size() + 1)) {

            Glide.with(holder.itemView.getContext())
                    .load(imagePath + crewSparseArray.get(position).getPhoto())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            ((CrewViewHolder) holder).mCrewBinding.imgNotFound.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            ((CrewViewHolder) holder).mCrewBinding.imgNotFound.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(((CrewViewHolder) holder).mCrewBinding.castPhoto);
            assert crewSparseArray != null;
            ((CrewViewHolder) holder).mCrewBinding.castName.setText(crewSparseArray.get(position).getName());
            ((CrewViewHolder) holder).mCrewBinding.castCharacter.setText(crewSparseArray.get(position).getCharacter());
        }
        if (holder.getItemViewType() == POSTER) {
            Glide.with(holder.itemView.getContext())
                    .load(imagePath + content)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            ((PosterViewHolder) holder).mPosterBinding.imgNotFound.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            ((PosterViewHolder) holder).mPosterBinding.imgNotFound.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(((PosterViewHolder) holder).mPosterBinding.mvImg);
        }
    }

    @Override
    public int getItemCount() {
        return crewSparseArray.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? POSTER : CREW;
    }

    class CrewViewHolder extends RecyclerView.ViewHolder {
        private ItemCastBinding mCrewBinding;

        CrewViewHolder(@NonNull ItemCastBinding crewBinding) {
            super(crewBinding.getRoot());
            mCrewBinding = crewBinding;
        }
    }

    class PosterViewHolder extends RecyclerView.ViewHolder {
        ItemMvPosterBinding mPosterBinding;

        public PosterViewHolder(@NonNull ItemMvPosterBinding posterBinding) {
            super(posterBinding.getRoot());
            mPosterBinding = posterBinding;
        }
    }
}
