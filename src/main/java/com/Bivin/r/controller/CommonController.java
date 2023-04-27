package com.Bivin.r.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 *      文件上传和下载
 */
@RestController // @Controller和@ResponseBody注解 （表现层bean注解和响应前端数据注解）
@RequestMapping(value = "/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;    // 把yml属性配置中的路径读取到basePath属性当中

    /**
     *  文件上传
     *
     *
     * @param file  // 注意：这个参数名一定要保证和前端的那个name的值一致，要不然进不到该上传方法当中来。
     * @return
     */

    @PostMapping(value = "/upload")
    public R upload(MultipartFile file){

        /**
         *  文件名优化
         */

        // 1、先获取前端上传的照片的原始名
        String filename = file.getOriginalFilename();
        //System.out.println(filename);    // a.jpg

        // 2、截取原始照片名的后缀名 .jpg
        String s = filename.substring(filename.lastIndexOf("."));// 从最后一个.开始往后截取。【包括.】
        // System.out.println(s);  // .jpg

        // 3、通过UUID随机生成文件名 （其实就是随机生成的一串字母，我们把生成的字母再加上上面截取的后缀名，那么不就是一个新的原始照片的名字了嘛~）
        String s1 = UUID.randomUUID().toString();//  随机生成文件名
        // System.out.println(s1); // 如随机生成的文件名： 3ee3b9bc-70d8-4b87-b91b-5832714fae82

        String fileName = s1+s; // 为第三步通过UUID随机生成的文件名拼接加上截取到的原始照片名的后缀名
        // System.out.println(fileName);   // 3ee3b9bc-70d8-4b87-b91b-5832714fae82.jpg


        /**
         *  再次优化： 判断照片上传的路径是否存在，不存在的话先创建好再上传照片
         */

        // 创建一个目录对象
        File file1 = new File(basePath);    // basePath是上面配置了@Value注解的属性，里面封装的是yml属性配置中设置的图片上传路径
        // 判断当前目录是否存在 （也就是说判断yml属性设置的图片上传路径D:\iiiiiiis\是否存在）
        if (! file1.exists()){
            // 如果路径目录不存在，那么就先把路径目录创建出来
            file1.mkdirs();
        }

        // 如果指定的上传路径不存在的话，通过上面的mkdirs方法把上传的路径创建好了，然后再执行下面的上传照片到该指定的路径下就能成功了。

        try {

            // 指定照片上传路径
            file.transferTo(new File(basePath+fileName));   // 直接路径+生成的文件名即可。

        } catch (IOException e) {
            e.printStackTrace();
        }


        return new R(fileName); // 把UUID随机生成的照片名称加上后缀名后响应给前端
        // （因为这个照片已经上传到我们D盘中了，把这个照片新名称响应给前端的目的就是以后前端好拿着这个照片的名称
        //   来我们D盘中下载这张照片展示在页面上）
    }


    /**
     **  文件下载
     *
     *      前端向我们后台发送的请求路径： /common/download?name=${response.data}
     *
     *      回忆基础知识：
     *          FileInputStream ： 输入流  万能的，任何类型的文件都可以采用这种流来读（图片、视频、文件等）
     *          read方法： 读硬盘数据的方法，该方法当指针到数据的末尾在硬盘中读取不到数据的时候，为-1
     *
     */
    @GetMapping("/download")
    public R download(String name, HttpServletResponse response) throws IOException {   // 参数name用来接收前端发送的name请求资源（该name就是对应的照片的名）


        FileInputStream fis =null;
        ServletOutputStream outputStream =null;


        try {

            /**
             * // 1、输入流，通过输入流读取文件内容 （输入流：就是从硬盘【相当于电脑的D盘】中把数据读入到内存【idea上】中）
             */

            fis = new FileInputStream(basePath+name);
            // 假设我们就读yml属性配置的D:\iiiiiiis\路径下的name照片名的资源照片（D:\iiiiiiis\下的资源就相当于在硬盘中，然后读入到我们idea上）

            /**
             *  2、初始化一个byte数组 （如果忘记怎么用的话，看IO流改进循环升级版笔记）
             *      目的：就是把1中读取出来的硬盘上的数据，循环读入到数组当中。
             *
             */
            byte[] bytes = new byte[1024];
            int readCount =0;
            while ((readCount =fis.read(bytes))!=-1){    // read(bytes)方法是用来往bytes数组中一直读输入流读取出来的硬件上的数据的，全部读完的时候为-1

                // 能到这里说明read为-1了，也就意味着硬盘上的数据全部已经读取到了bytes数组当中了，此时的数组当中已经有了硬盘上的数据了。

                /**
                 * // 那么数组中有了硬盘上的数据之后，我们就创建输出流，
                 * // 把数组中读取到的硬盘中的数据（也就是图片资源）通过输出流响应给前端浏览器，然后前端拿到该照片后，在页面上展示该图片
                 *
                 *  实现思路： 通过Response向前端页面响应字节数据 。这也是我们方法中有response参数的原因（忘记的话看Response笔记）
                 *
                 */

                // 1、通过Response对象获取字符输出流
                outputStream = response.getOutputStream();
                // 2、写数据,把上面通过输入流读入到bytes数组中的硬盘中的图片资源写给前端页面
                outputStream.write(bytes);  // 把上面通过输入流读入到bytes数组中的硬盘中的图片资源写给前端页面

                /**
                 *  注意输出流写完之后别忘记刷新一下管道
                 */
                outputStream.flush();

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {  // 在finally语句块当中确保流一定关闭（因为如果前面的代码出现异常了，但是finally块还是能执行了，所以在finally块中关闭流确保了流的关闭）
            // 关闭流的前提是：流不是空,流是null的时候没必要关闭

            /**
             *  把输入流和输出流别忘记关闭
             *
             *      注意： 这里因为是在finally分块当中关闭输入流和输出流的，因此这也是为什么上面的输入流和输出流要写成如下这种形式：
             *                      FileInputStream fis =null;
             *         ServletOutputStream outputStream =null;
             *         如果不这样把输入流和输出流定义成全局变量的话，那么这里的finally分块中就无法让输入流和输出流关闭流。
             *
             */
            fis.close();
            outputStream.close();


        }

        return null;
    }


}
