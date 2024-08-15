package com.wu1015.sbfilemanager.filemanager.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wu1015.sbfilemanager.MainActivity;
import com.wu1015.sbfilemanager.R;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<MyFile> myFileList;
    class ViewHolder{
        ImageView iv;
        TextView tv_name;
        TextView tv_large;
        TextView tv_date;
    }
    public void setContext(Context context){
        this.context=context;
    }
    public List<MyFile> getMyFileList(){
        return myFileList;
    }
    public void setMyFileList(List<MyFile> myFileList){
        this.myFileList=myFileList;
    }
    @Override
    public int getCount() {
//        return 0;
        return myFileList.size();
    }

    @Override
    public Object getItem(int i) {
//        return null;
        return myFileList.get(i);
    }

    @Override
    public long getItemId(int i) {
//        return 0;
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        return null;
        ViewHolder viewHolder;
        LayoutInflater mInflater=LayoutInflater.from(context);
        if(view == null){
            view = mInflater.inflate(R.layout.item,null);
            viewHolder = new ViewHolder();
            viewHolder.iv = view.findViewById(R.id.imageView);
            viewHolder.tv_name = view.findViewById(R.id.tv_name);
            viewHolder.tv_large = view.findViewById(R.id.tv_large);
            viewHolder.tv_date = view.findViewById(R.id.tv_date);
            // 将 viewHolder 绑定到 convertView 中实现复用
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.tv_name.setText(myFileList.get(i).getName());
        long large=myFileList.get(i).getLarge();
        int c = 0;
        String largeStr="";
        while(large > (1 << 10)){
            large=large/(1 << 10);
            c++;
        }
        switch (c){
            case 1:
                largeStr= large +" KB";
                break;
            case 2:
                largeStr= large +" MB";
                break;
            case 3:
                largeStr= large +" GB";
                break;
            default:
                largeStr= large +" B";
        }
        viewHolder.tv_large.setText(largeStr);
        viewHolder.tv_date.setText(myFileList.get(i).getDate());
//        .....各种设置
        return view;
    }
}
