package com.tao.answersys;

import android.graphics.Bitmap;

import com.tao.answersys.net.DownloadUtil;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        System.out.print("jello");
    }

    @Test
    public void testImageDownload() {
        DownloadUtil downloadUtil = DownloadUtil.getInstance();
        try {
            Bitmap bitmap = downloadUtil.downloadImage("http://d.hiphotos.baidu.com/zhidao/pic/item/ac345982b2b7d0a277d6c541caef76094a369acb.jpg");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}