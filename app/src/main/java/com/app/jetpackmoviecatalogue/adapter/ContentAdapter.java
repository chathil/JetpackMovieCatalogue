package com.app.jetpackmoviecatalogue.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jetpackmoviecatalogue.R;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.databinding.ItemContentBinding;
import com.app.jetpackmoviecatalogue.databinding.ItemContentHeaderBinding;
import com.app.jetpackmoviecatalogue.utils.GlideApp;
import com.app.jetpackmoviecatalogue.utils.MyUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import static com.app.jetpackmoviecatalogue.utils.MyUtils.drawableToBitmap;

public class ContentAdapter extends PagedListAdapter<Content, RecyclerView.ViewHolder> {

    private static final int HEADER = 0;
    private static final int ITEM = 1;
    private final String TAG = ContentAdapter.class.getSimpleName();
    private List<Content> contentList = new ArrayList<>();
    private String sectionLabel;
    private boolean showHeader;
    private OnItemClickCallback onItemClickCallback;

    public void setData(List<Content> contents, boolean showHeader) {
        contentList.clear();
        contentList.addAll(contents);
        this.showHeader = showHeader;
        notifyDataSetChanged();
    }

    private static DiffUtil.ItemCallback<Content> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Content>() {
                @Override
                public boolean areItemsTheSame(@NonNull Content oldItem, @NonNull Content newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull Content oldItem, @NonNull Content newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public ContentAdapter(ContentAdapter.OnItemClickCallback onItemClickCallback, String sectionLabel) {
        super(DIFF_CALLBACK);
        this.sectionLabel = sectionLabel;
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            ItemContentHeaderBinding headerBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_content_header,
                    parent,
                    false);
            return new HeaderViewHolder(headerBinding);
        }
        ItemContentBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_content,
                parent,
                false);
        return new MovieAdapterViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if ((holder.getItemViewType() == ITEM) && (contentList != null && position < contentList.size() + 1)) {
            final Content content = contentList.get(position - 1);

            String imagePath = "https://image.tmdb.org/t/p/w185";
            GlideApp.with(holder.itemView.getContext())
                    .load(imagePath + content.getMvPoster()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    changeBgColor(resource, holder);
                    return false;
                }
            })
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_movie_black_24dp).error(R.drawable.ic_broken_image_black_24dp))
                    .into(((MovieAdapterViewHolder) holder).itemBinding.mvImg);

            ((MovieAdapterViewHolder) holder).itemBinding.mvTitle.setText(content.getTitle());
            ((MovieAdapterViewHolder) holder).itemBinding.getRoot().setOnClickListener(v -> onItemClickCallback.onItemClicked(position - 1, contentList));
            ((MovieAdapterViewHolder) holder).itemBinding.mvDate.setText(MyUtils.getYear(content.getDate()));
            ((MovieAdapterViewHolder) holder).itemBinding.mvIsReleased.setVisibility(MyUtils.isReleased(content.getDate()) ? View.VISIBLE : View.GONE);
            ((MovieAdapterViewHolder) holder).itemBinding.mvRatingChart.setProgress(content.getUserScore());
            ((MovieAdapterViewHolder) holder).itemBinding.mvRatingPercentage.setText(MyUtils.isReleased(content.getDate()) ? holder.itemView.getResources().getString(R.string.coming_soon) : Html.fromHtml(content.getUserScore() + "<sup><small>%</small></sup>"));
            ((MovieAdapterViewHolder) holder).itemBinding.btnLike.setImageDrawable(holder.itemView.getResources().getDrawable(content.isLiked() ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp));
            ((MovieAdapterViewHolder) holder).itemBinding.btnLike.setOnClickListener(view -> onItemClickCallback.onLikeClicked(content));
        }
        if (holder.getItemViewType() == HEADER && showHeader) {
            ((HeaderViewHolder) holder).headerBinding.sectionLabel.setText(sectionLabel);
        }
    }

    private void changeBgColor(Drawable resource, RecyclerView.ViewHolder holder) {
        Palette p = Palette.from(drawableToBitmap(resource)).generate();
        Palette.Swatch bg = p.getLightVibrantSwatch();
        if (bg != null) {
            ((MovieAdapterViewHolder) holder).itemBinding.cardBg.setCardBackgroundColor(bg.getRgb());
            ((MovieAdapterViewHolder) holder).itemBinding.mvRatingChart.setProgressBarColor(bg.getTitleTextColor());
            ((MovieAdapterViewHolder) holder).itemBinding.mvRatingChart.setBackgroundProgressBarColor(bg.getBodyTextColor());
        } else {
            ((MovieAdapterViewHolder) holder).itemBinding.cardBg.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.colorAccent));
            ((MovieAdapterViewHolder) holder).itemBinding.mvRatingChart.setProgressBarColor(holder.itemView.getResources().getColor(R.color.colorPrimaryDark));
            ((MovieAdapterViewHolder) holder).itemBinding.mvRatingChart.setBackgroundProgressBarColor(holder.itemView.getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public int getItemCount() {
        return contentList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER : ITEM;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
        private ItemContentBinding itemBinding;

        MovieAdapterViewHolder(@NonNull ItemContentBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private ItemContentHeaderBinding headerBinding;

        HeaderViewHolder(@NonNull ItemContentHeaderBinding binding) {
            super(binding.getRoot());
            headerBinding = binding;
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(int index, List<Content> contents);
        void onLikeClicked(Content content);
    }


}
