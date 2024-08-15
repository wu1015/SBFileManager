package com.wu1015.sbfilemanager.filemanager.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GetDirList {
    public static List<MyFile> getDirList(String path_str) throws IOException {
        List<MyFile> dirList=new ArrayList<>();
        List<MyFile> diList=new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path_str));
            for(Path path : stream){
//                隐藏隐藏文件（夹）
                if(path.getFileName().toString().startsWith(".")){
                    continue;
                }
                MyFile myFile=new MyFile();
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                myFile.setName(path.getFileName().toString());
                myFile.setPath(path.toString());
                myFile.setLarge(Files.size(path));
                myFile.setDate(String.valueOf(attr.lastModifiedTime()));
                myFile.setFolder(attr.isDirectory());
                if(attr.isDirectory()){
                    dirList.add(myFile);
                }else {
                    diList.add(myFile);
                }
            }
        }
//        对列表进行排序 按名称A-Z 文件夹优先
        dirList.sort(Comparator.comparing(MyFile::getName));
        diList.sort(Comparator.comparing(MyFile::getName));
        dirList.addAll(diList);
        return dirList;
    }
}
