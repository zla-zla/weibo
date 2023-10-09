package com.example.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weibo.Api.Api;
import com.example.weibo.Api.ApiConfig;
import com.example.weibo.Api.TtitCallback;
import com.example.weibo.R;
import com.example.weibo.activity.PersonInfoActivity;
import com.example.weibo.activity.PostDetailActivity;
import com.example.weibo.entity.Post;
import com.example.weibo.utils.AppConfig;
import com.example.weibo.view.CircleTransform;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Post> datas;
    private boolean isSelf;

    public void setDatas(List<Post> datas) {
        this.datas = datas;
    }

    public PostAdapter(Context context, List<Post> data, boolean isSelf){
        this.context = context;
        this.datas = data;
        this.isSelf = isSelf;
        //初始化NineGridView的图片加载器
        NineGridView.setImageLoader(new PicassoImageLoader());
    }

    /** Picasso 加载 */
    private class PicassoImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Picasso.with(context).load(url).into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        Post post = datas.get(position);
        vh.tvName.setText(post.getName());
        vh.tvtime.setText(post.getTime());
        vh.tvtext.setText(post.getText());
        vh.tvComment.setText(String.valueOf(post.getCommentCount()));
        vh.tvCollect.setText(String.valueOf(post.getCollectCount()));
        vh.tvLike.setText(String.valueOf(post.getLikeCount()));
        //加载头像
        Picasso.with(context)
                .load(ApiConfig.PROFILE+post.getProfileURL())
                .transform(new CircleTransform())
                .into(vh.profile);
        //加载九宫格图
        if(!post.getPics().equals("")){
            String[] urls = post.getPics().split(";");
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for(int i=0;i<urls.length;i++){
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(ApiConfig.POST_IMG + urls[i]);
                info.setBigImageUrl(ApiConfig.POST_IMG + urls[i]);
                imageInfo.add(info);
                System.out.println("绑定图片:"+ApiConfig.POST_IMG + urls[i]);
            }
            vh.nineGrid.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
        }
        //点击帖子进入帖子详情页
        vh.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("post", post);
                context.startActivity(intent);
            }
        });
        vh.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(context, PersonInfoActivity.class);
                intent.putExtra("uid", post.getUid());
                context.startActivity(intent);
            }
        });
        if(isSelf){
            vh.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer id = post.getId();
                    FormBody formBody = new FormBody.Builder()
                            .add("pid", String.valueOf(id))
                            .build();
                    Api.config(ApiConfig.POST_Delete, formBody).postRequest(context,new TtitCallback() {
                        @Override
                        public void onSuccess(final String res) {   //不理解，这个res=响应体？
                            Looper.prepare();
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        @Override
                        public void onFailure(Exception e) {
                        }
                    });
                }
            });
        }
        else{
            vh.delete.setVisibility (View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout item;
        private TextView tvName;
        private TextView tvtime;
        private TextView tvtext;
        private TextView tvComment;
        private TextView tvCollect;
        private TextView delete;
        private TextView tvLike;
        private ImageView profile;
        private NineGridView nineGrid;

        public ViewHolder(@NonNull View view) {
            super(view);
            item = view.findViewById(R.id.item);
            tvName = view.findViewById(R.id.name);
            tvtime = view.findViewById(R.id.time);
            tvtext = view.findViewById(R.id.text);
            tvComment = view.findViewById(R.id.comment);
            tvCollect = view.findViewById(R.id.collect);
            tvLike = view.findViewById(R.id.like);
            delete = view.findViewById(R.id.delete_item);
            profile = view.findViewById(R.id.profile);
            nineGrid = view.findViewById(R.id.nineGrid);
        }
    }


}
