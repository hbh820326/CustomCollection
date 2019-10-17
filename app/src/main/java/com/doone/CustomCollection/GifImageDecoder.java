package com.doone.CustomCollection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义 解码GIF类
 */
public class GifImageDecoder {
    // Trailer
    private static final byte TRR_CODE = (byte) 0x3B;//文件终结符
    // Image Block
    private static final byte IMG_CODE = (byte) 0x2C;//下一帧开始符
    // Extension
    private static final byte EXT_CODE = (byte) 0x21;//图形控制扩展
    // Graphic Control Extension
    private static final byte GC_EXT = (byte) 0xF9;
    // Application Extension
    private static final byte APP_EXT = (byte) 0xFF;
    // Comment Extension
    private static final byte CMT_EXT = (byte) 0xFE;
    // Plain Text Extension
    private static final byte TXT_EXT = (byte) 0x01;

    private int mWidth; // full mCurrentImage mWidth
    private int mHeight; // full mCurrentImage mHeight
    private ArrayList<Map> mGifFrames; // gif图片集合
    private int mOffset = 0;
    private GifHeader mGifHeader;
    private GraphicControlExtension mGcExt;
    private ImageBlock mImageBlock;
    Callback callback;

    public interface Callback {
        void current(Bitmap bitmap);
    }

    boolean isloop;

    public void start(Callback callback) {
        this.callback = callback;
        isloop = true;
        int i = 0;
        do{
            try {
                Map map = mGifFrames.get(i);
                callback.current((Bitmap) map.get("bitmap"));
                Thread.sleep((int) map.get("time"));
                if (i < mGifFrames.size() - 1) {
                    i++;
                } else {
                    i = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (isloop&&mGifFrames.size()>1);
    }

    public void stop() {
        isloop = false;
        mGifFrames.clear();
        mGifFrames = null;
        this.callback = null;
    }

    public GifImageDecoder(Object object) {
        mGifFrames = new ArrayList<>();
        if (object instanceof InputStream) {
            read(streamToBytes((InputStream) object));
        } else {
            if (object instanceof byte[]) {
                read((byte[]) object);
            }
        }
    }


    /**
     * 传入GIF数据流
     *
     * @param buffer GIF数据
     * @return 非GIF 返回 字节数组
     */

    public void read(final byte[] buffer) {
        mGifHeader = new GifHeader(buffer);
        if ("GIF".equals(mGifHeader.getSignature())) {
            mOffset += mGifHeader.size;
            mWidth = mGifHeader.getWidth();
            mHeight = mGifHeader.getHeight();
            while (buffer[mOffset] != TRR_CODE) {
                if (buffer[mOffset] == IMG_CODE) {
                    mImageBlock = new ImageBlock(buffer, mOffset);
                    mOffset += mImageBlock.size;
                    mGifFrames.add(new HashMap() {{
                        put("bitmap", extractImage());
                        put("time", mGcExt.getDelayTime() * 10);
                    }}); // add image to frame
                } else if (buffer[mOffset] == EXT_CODE) {
                    if (buffer[mOffset + 1] == GC_EXT) {
                        mGcExt = new GraphicControlExtension(buffer, mOffset);
                        mOffset += mGcExt.size;
                    } else if (buffer[mOffset + 1] == APP_EXT) {
                        //ApplicationExtension
                        ApplicationExtension appExt = new ApplicationExtension(buffer, mOffset);
                        mOffset += appExt.size;
                    } else if (buffer[mOffset + 1] == CMT_EXT) {
                        //CommentExtension
                        CommentExtension cmtExt = new CommentExtension(buffer, mOffset);
                        mOffset += cmtExt.size;
                    } else if (buffer[mOffset + 1] == TXT_EXT) {
                        //PlainTextExtension
                        PlainTextExtension txtExt = new PlainTextExtension(buffer, mOffset);
                        mOffset += txtExt.size;
                    }
                }
            }
        }else{
            mGifFrames.add(new HashMap() {{
                put("bitmap", BitmapFactory.decodeByteArray(buffer, 0, buffer.length));
                put("time", 0);
            }});   ;
        }
    }


    /**
     * 输入流转数组
     *
     * @param stream
     * @return
     */
    public byte[] streamToBytes(InputStream stream) {
        try (ByteArrayOutputStream bao = new ByteArrayOutputStream();) {
            byte[] buff = new byte[2048];
            int read;
            while ((read = stream.read(buff)) != -1) {
                bao.write(buff, 0, read);
            }
            stream.close();
            return bao.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *解析每一桢图片
     * @return image
     */
    private Bitmap extractImage() {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if ((mWidth == mImageBlock.getImageWidth() || mHeight == mImageBlock.getImageHeight()) ) {
            if (mGifFrames.size() > 0)canvas.drawBitmap((Bitmap) mGifFrames.get(mGifFrames.size() - 1).get("bitmap"), 0, 0, null);
        }
        byte[] byt = new byte[mGifHeader.size + mGcExt.size + mImageBlock.size + 1];
        System.arraycopy(mGifHeader.bytes, 0, byt, 0, mGifHeader.size);
        System.arraycopy(mGcExt.bytes, 0, byt, mGifHeader.size, mGcExt.size);
        System.arraycopy(mImageBlock.bytes, 0, byt, mGcExt.size + mGifHeader.size, mImageBlock.size);
        byt[mGifHeader.size + mGcExt.size + mImageBlock.size] = TRR_CODE;
        canvas.drawBitmap(BitmapFactory.decodeByteArray(byt, 0, byt.length), 0, 0, null);
        return bitmap;
    }

    private class GifHeader {
        public byte[] bytes;
        public int size = 0x0D;//文件头长度

        public GifHeader(byte[] bytes) {
            if ((bytes[0x0A] & 0x80) > 0) {//是否具有全局调色板
                size += Math.pow(2, (bytes[0x0A] & 0x07) + 1) * 3;
            }
            this.bytes = new byte[size];
            System.arraycopy(bytes, 0, this.bytes, 0, size);
        }

        public String getSignature() {
            return new String(bytes, 0, 3);
        }

        public String getVersion() {
            return new String(bytes, 3, 3);
        }

        public int getWidth() {
            return (bytes[6] & 0xFF) + ((bytes[7] & 0xFF) << 8);
        }

        public int getHeight() {
            return (bytes[8] & 0xFF) + ((bytes[9] & 0xFF) << 8);
        }

        public int getGlobalColorTableFlag() {
            return (bytes[10] & 0x80) >> 7;
        }

        public int getColorResolution() {
            return (bytes[10] & 0x70) >> 4;
        }

        public int getSortFlag() {
            return (bytes[10] & 0x08) >> 3;
        }

        public int getSizeOfGlobalColorTable() {
            return (bytes[10] & 0x07);
        }

        public int getBackgroundColorIndex() {
            return bytes[11] & 0xFF;
        }

        public int getPixelAspectRatio() {
            return bytes[12];
        }

        public int[] getGlobalColorTable() {
            if (getGlobalColorTableFlag() == 0) {
                return new int[0];
            }
            int[] colors = new int[(int) Math.pow(2, getSizeOfGlobalColorTable() + 1)];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = ((bytes[13 + (i * 3)] & 0xFF) << 16) + ((bytes[13 + (i * 3) + 1] & 0xFF) << 8) + (bytes[13 + (i * 3) + 2] & 0xFF);
            }
            return colors;
        }
    }

    private class ImageBlock {
        public byte[] bytes;
        public int size;

        public ImageBlock(byte[] bytes, int offset) {
            int blockSize;
            boolean localColorTableFlag = (bytes[offset + 0x09] & 0x80) != 0x00;
            int localColorTableSize = (bytes[offset + 0x09] & 0x07);

            //get size
            size = 0x0A;
            if (localColorTableFlag) {
                size += Math.pow(2, (localColorTableSize + 1)) * 3;
            }
            size += 1; //LZW Minimum Code Size

            //ImageData
            blockSize = bytes[offset + size] & 0xFF;
            size += 1;
            while (blockSize != 0x00) {
                size += blockSize;
                blockSize = bytes[offset + size] & 0xFF;
                size += 1;
            }

            this.bytes = new byte[size];
            System.arraycopy(bytes, offset, this.bytes, 0, size);
        }

        public int getImageSeparator() {
            return bytes[0] & 0xFF;
        }

        public int ImageLeftPosition() {
            return (bytes[1] & 0xFF) + ((bytes[2] & 0xFF) << 8);
        }

        public int getImageTopPosition() {
            return (bytes[3] & 0xFF) + ((bytes[4] & 0xFF) << 8);
        }

        public int getImageWidth() {
            return (bytes[5] & 0xFF) + ((bytes[6] & 0xFF) << 8);
        }

        public int getImageHeight() {
            return (bytes[7] & 0xFF) + ((bytes[8] & 0xFF) << 8);
        }

        public int getLocalColorTableFlag() {
            return (bytes[9] & 0x80) >> 7;
        }

        public int getInterlaceFlag() {
            return (bytes[9] & 0x40) >> 6;
        }

        public int getSortFlag() {
            return (bytes[9] & 0x20) >> 5;
        }

        public int getReserved() {
            return (bytes[9] & 0x18) >> 2;
        }

        public int getSizeOfLocalColorTable() {
            return bytes[9] & 0x03;
        }

        public int[] getLocalColorTable() {
            if (getLocalColorTableFlag() == 0) {
                return new int[0];
            }
            int[] colors = new int[(int) Math.pow(2, getSizeOfLocalColorTable() + 1)];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = ((bytes[10 + (i * 3)] & 0xFF) << 16) + ((bytes[10 + (i * 3) + 1] & 0xFF) << 8) + (bytes[10 + (i * 3) + 2] & 0xFF);
            }
            return colors;
        }

        public int getLZWMinimumCodeSize() {
            if (getLocalColorTableFlag() == 0) {
                return bytes[10] & 0xFF;
            } else {
                return bytes[10 + (int) Math.pow(2, getSizeOfLocalColorTable() + 1) * 3] & 0xFF;
            }
        }
    }

    private class ApplicationExtension {
        public byte[] bytes;
        public int size;

        public ApplicationExtension(byte[] bytes, int offset) {
            int blockSize;
            // get size
            size = 0x0E;

            blockSize = bytes[offset + size] & 0xFF;
            size += 1;
            while (blockSize != 0x00) {
                size += blockSize;
                blockSize = bytes[offset + size] & 0xFF;
                size += 1;
            }

            this.bytes = new byte[size];
            System.arraycopy(bytes, offset, this.bytes, 0, size);
        }

        public int getExtensionIntroducer() {
            return bytes[0] & 0xFF;
        }

        public int getExtensionLabel() {
            return bytes[1] & 0xFF;
        }

        public int getBlockSize1() {
            return bytes[2] & 0xFF;
        }

        public String getApplicationIdentifier() {
            return new String(bytes, 3, 8);
        }

        public String getApplicationAuthenticationCode() {
            return new String(bytes, 11, 3);
        }
    }

    private class GraphicControlExtension {
        public byte[] bytes;
        public int size;

        public GraphicControlExtension(byte[] bytes, int offset) {
            size = 8;
            this.bytes = new byte[size];
            System.arraycopy(bytes, offset, this.bytes, 0, size);
        }

        public int getExtensionIntroducer() {
            return bytes[0] & 0xFF;
        }

        public int getGraphicControlLabel() {
            return bytes[1] & 0xFF;
        }

        public int getBlockSize() {
            return bytes[2] & 0xFF;
        }

        public int getReserved() {
            return (bytes[3] & 0xE0) >> 5;
        }

        public int getDisposalMothod() {
            return (bytes[3] & 0x1C) >> 2;
        }

        public int getUserInputFlag() {
            return (bytes[3] & 0x02) >> 1;
        }

        public int getTransparentColorFlag() {
            return (bytes[3] & 0x01);
        }

        public int getDelayTime() {
            return (bytes[4] & 0xFF) + ((bytes[5] & 0xFF) << 8);
        }

        public int getTransparentColorIndex() {
            return bytes[6];
        }

        public void setTransparentColorFlagTrue() {
            int value = getReserved() | getDisposalMothod() | getUserInputFlag() | 0x01;
            bytes[3] = (byte) (value & 0xff);
        }
    }

    private class CommentExtension {
        public byte[] bytes;
        public int size;

        public CommentExtension(byte[] bytes, int offset) {
            int blockSize;
            // get size
            size = 0x02;

            blockSize = bytes[offset + size] & 0xFF;
            size += 1;
            while (blockSize != 0x00) {
                size += blockSize;
                blockSize = bytes[offset + size] & 0xFF;
                size += 1;
            }

            this.bytes = new byte[size];
            System.arraycopy(bytes, offset, this.bytes, 0, size);
        }
    }

    private class PlainTextExtension {
        public byte[] bytes;
        public int size;

        public PlainTextExtension(byte[] bytes, int offset) {
            int blockSize;
            // get size
            size = 0x0F;

            blockSize = bytes[offset + size] & 0xFF;
            size += 1;
            while (blockSize != 0x00) {
                size += blockSize;
                blockSize = bytes[offset + size] & 0xFF;
                size += 1;
            }

            this.bytes = new byte[size];
            System.arraycopy(bytes, offset, this.bytes, 0, size);
        }
    }
}


//            if ("GIF".equals(new String(bytes, 0, 3))) {
//
//                Log.e("Z", new String(bytes, 0, 3));//GIF           这个是判断是否为GIF                   占（0，1，2）   3位
//                Log.e("Z", new String(bytes, 3, 3));//89a和87a      这是GIF版本 （89a版本才支持动画、注释扩展和文本扩展） 占（3，4，5） 3位
//                Log.e("Z", "宽：" + ((bytes[6] & 0xFF) + ((bytes[7] & 0xFF) << 8)));   // 占（6，7）   2位
//                Log.e("Z", "高：" + ((bytes[8] & 0xFF) + ((bytes[9] & 0xFF) << 8)));   // 占（8，9）   2位
//                Log.e("Z", "是否具有全局调色板：" + ((bytes[10] & 0b10000000)>0));
//                Log.e("Z", "色彩分辨率：" + (bytes[10] >> 4 & 0b111));
//                Log.e("Z", "RGB是否按使用率从高到低排序：" + ((bytes[10]  & 0b1000)>0));
//                Log.e("Z", "全局颜色列表大小：" + Math.pow(2, (bytes[10] & 0b111) + 1));   //
//                Log.e("Z", "背景色索引：" + (bytes[11] & 0xFF));   // 在全局颜色列表中的索引
//                Log.e("Z", "像素宽高比：" + (bytes[12] & 0xFF));   // 在全局颜色列表中的索引
//                Log.e("Z", "全局颜色列表共"+Math.pow(2, (bytes[10] & 0b111) + 1)+"色：");
//                int pixel = (int) Math.pow(2, (bytes[10] & 0b111) + 1)*3 + 13;
//                String samp = "0123456789ABCDEF";
//
//                for ( int i = 13, j=0; i < pixel; i += 3,j++) {
//                    int r = bytes[i] & 0xFF;
//                    int g = bytes[i + 1] & 0xFF;
//                    int b = bytes[i + 2] & 0xFF;
//                    Log.e("Z", "全局颜色列表"+j+"："+samp.charAt(r/16)+samp.charAt(r%16)+samp.charAt(g/16)+samp.charAt(g%16)+samp.charAt(b/16)+samp.charAt(b%16));
//                }
//                // int start = (buffer[pixel] & 0xFF);
//                Log.e("Z",""+(char)bytes[pixel]);
//            }