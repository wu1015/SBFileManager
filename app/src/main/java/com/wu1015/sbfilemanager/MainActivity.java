package com.wu1015.sbfilemanager;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wu1015.sbfilemanager.filemanager.utils.GetDirList;
import com.wu1015.sbfilemanager.filemanager.utils.MyAdapter;
import com.wu1015.sbfilemanager.filemanager.utils.MyFile;
import com.wu1015.sbfilemanager.filemanager.utils.MyFileVisitor;
import com.wu1015.sbfilemanager.R;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private MyAdapter myAdapter;
    private String basePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        toolbar = findViewById(R.id.toolbar);

        // 获取屏幕高度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        // 获取状态栏高度
        int statusBarHeight = getStatusBarHeight();


        // 使用 ViewTreeObserver 来获取 Toolbar 的高度
        toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 移除监听器以避免重复调用
                toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // 获取 Toolbar 的高度
                int toolbarHeight = toolbar.getHeight();

                // 计算 ListView 应该占用的高度
                int listViewHeight = screenHeight - toolbarHeight - statusBarHeight - 5;

                // 设置 ListView 的高度
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = listViewHeight;
                listView.setLayoutParams(params);
            }
        });

//        MyFileVisitor myFileVisitor = new MyFileVisitor();

//        String basePath = "/sdcard/Download/";
        basePath = "/sdcard/";
//        String basePath="/storage/10EE-310C/Download/";
//        直接编译安装是不会有授权的。
//        Stream<Path> paths = null;
//        DirectoryStream<Path> stream = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            try {
//                Files.walkFileTree(Paths.get(basePath), myFileVisitor);
//                paths = Files.walk(Paths.get(basePath), 1);
//                stream = Files.newDirectoryStream(Paths.get(basePath));
//                System.out.println(myFileVisitor.getStringList());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        System.out.println(myFileVisitor.getStringList());
//        Log.d("test", "onCreate: " + myFileVisitor.getStringList());
//        Log.d("test", "onCreate: " + myFileVisitor.getDirStringList());
//        paths.forEach(System.out::println);
//        Log.d("test", "onCreate: " + paths);
//        stream.forEach(System.out::println);


        try {
            List<MyFile> ll = GetDirList.getDirList(basePath);
            System.out.println(ll.get(1).getName());
            System.out.println(ll.get(1).getDate());
            System.out.println(ll.get(1).getLarge());

            myAdapter = new MyAdapter();
            myAdapter.setContext(this);
            myAdapter.setMyFileList(ll);
            listView.setAdapter(myAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        if (myAdapter.getMyFileList().get(i).isFolder()) {
                            basePath = myAdapter.getMyFileList().get(i).getPath();
                            List<MyFile> ll = GetDirList.getDirList(basePath);
                            myAdapter.setMyFileList(ll);
                            myAdapter.notifyDataSetChanged();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Toast.makeText(MainActivity.this, "long button pressed again to back", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    AlertDialog alert = builder
                            .setTitle("系统提示：")
                            .setMessage("是否进行删除")
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean flag= false;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        try {
                                            flag = Files.deleteIfExists(Paths.get(myAdapter.getMyFileList().get(i).getPath()));
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    if (flag){
                                        Log.d("TAG", "onClick:update ");
                                        Toast.makeText(MainActivity.this, "删除成功" , Toast.LENGTH_SHORT).show();
                                    }else {
                                        Log.d("TAG", "onClick:noUpdate ");
                                        Toast.makeText(MainActivity.this, "删除失败" , Toast.LENGTH_SHORT).show();
                                    }
                                    try {
                                        List<MyFile> ll = GetDirList.getDirList(basePath);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    myAdapter.notifyDataSetChanged();
//                                        Toast.makeText(MainActivity.this, "你点击了取消按钮~", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                        Toast.makeText(MainActivity.this, "你点击了中立按钮~", Toast.LENGTH_SHORT).show();
                                }
                            }).create();
                    alert.show();
                    return true;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 获取 OnBackPressedDispatcher
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

        // 添加自定义的返回行为
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 自定义的返回事件处理逻辑
                try {
                    String[] parts = basePath.split("/");

                    if (parts.length > 2) {
                        basePath = "";
                        // 重新拼接路径
                        int i=0;
                        for (String s : parts) {
                            i++;
                            if (i!=parts.length) {
                                basePath += s + "/";
                            }
                        }
                        Log.d("test", "handleOnBackPressed: " + basePath);
                        List<MyFile> ll = GetDirList.getDirList(basePath);
                        myAdapter.setMyFileList(ll);
                        myAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Back button pressed again to back", Toast.LENGTH_SHORT).show();
                        setEnabled(false); // 暂时禁用当前回调，避免循环调用
                        getOnBackPressedDispatcher().onBackPressed();
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // 获取状态栏高度
    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        @SuppressLint({"InternalInsetResource", "DiscouragedApi"}) int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    // 获取导航栏高度
    private int getNavigationBarHeight() {
        int navigationBarHeight = 0;
        Resources resources = getResources();
        @SuppressLint({"InternalInsetResource", "DiscouragedApi"}) int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        return navigationBarHeight;
    }
}