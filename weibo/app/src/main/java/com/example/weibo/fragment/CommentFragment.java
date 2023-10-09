package com.example.weibo.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weibo.R;
import com.google.gson.Gson;
import com.jidcoo.android.widget.commentview.CommentView;
import com.jidcoo.android.widget.commentview.callback.OnItemClickCallback;
import com.jidcoo.android.widget.commentview.defaults.DefaultCommentModel;
import com.jidcoo.android.widget.commentview.defaults.DefaultViewStyleConfigurator;


public class CommentFragment extends BaseFragment {

    private CommentView commentView;
    private Gson gson;
    private EditText user, editor;
    private Button button;
    private boolean isReply = false;
    private boolean isChildReply = false;
    private int cp, rp;
    private long fid, pid;

    public CommentFragment() {
    }

    public static CommentFragment newInstance() {
        CommentFragment fragment = new CommentFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_comment;
    }

    @Override
    protected void initView() {
        gson = new Gson();
        user = mRootView.findViewById(R.id.user);
        editor = mRootView.findViewById(R.id.editor);
        button = mRootView.findViewById(R.id.button);
        commentView = mRootView.findViewById(R.id.commentView);//初始化控件
        commentView.setViewStyleConfigurator(new DefaultViewStyleConfigurator(getActivity()));


    }

    @Override
    protected void initData() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userStr = user.getText().toString();
                String data = editor.getText().toString();
                if (!userStr.isEmpty() && !data.isEmpty()) {
                    if (isReply && isChildReply) {
                        //现在需要构建一个回复数据实体类
                        DefaultCommentModel.Comment.Reply reply = new DefaultCommentModel.Comment.Reply();
                        reply.setKid(fid);
                        reply.setReplierName(userStr);
                        reply.setReply(data);
                        reply.setDate(System.currentTimeMillis());
                        reply.setPid(pid);
                        commentView.addReply(reply, cp);
                    } else if (isReply && !isChildReply) {
                        //现在需要构建一个回复数据实体类
                        DefaultCommentModel.Comment.Reply reply = new DefaultCommentModel.Comment.Reply();
                        reply.setKid(fid);
                        reply.setReplierName(userStr);
                        reply.setReply(data);
                        reply.setDate(System.currentTimeMillis());
                        reply.setPid(0);
                        commentView.addReply(reply, cp);
                    } else {
                        DefaultCommentModel.Comment comment = new DefaultCommentModel.Comment();
                        comment.setFid(System.currentTimeMillis());
                        comment.setId(comment.getFid() + 1);
                        comment.setDate(comment.getFid());
                        comment.setPid(0);
                        comment.setPosterName(userStr);
                        comment.setComment(data);
                        commentView.addComment(comment);
                    }
                } else {
                    Toast.makeText(getActivity(), "用户名和内容都不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 点击事件回调
     */
    class MyOnItemClickCallback implements OnItemClickCallback<DefaultCommentModel.Comment, DefaultCommentModel.Comment.Reply> {

        @Override
        public void commentItemOnClick(int position, DefaultCommentModel.Comment comment, View view) {
            isReply = true;
            cp = position;
            isChildReply = false;
            fid = comment.getFid();
            editor.setHint("回复@" + comment.getPosterName() + ":");
        }

        @Override
        public void replyItemOnClick(int c_position, int r_position, DefaultCommentModel.Comment.Reply reply, View view) {
            isReply = true;
            cp = c_position;
            rp = r_position;
            isChildReply = true;
            fid = reply.getKid();
            pid = reply.getId();
            editor.setHint("回复@" + reply.getReplierName() + ":");
        }
    }
}