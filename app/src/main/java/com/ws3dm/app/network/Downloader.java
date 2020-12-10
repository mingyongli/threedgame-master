package com.ws3dm.app.network;//package com.dk.network;
//
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//
//import com.dk.config.Config;
//import com.dk.util.EventBus;
//import com.hwangjr.rxbus.annotation.Subscribe;
//import com.hwangjr.rxbus.annotation.Tag;
//import com.hwangjr.rxbus.thread.EventThread;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import okhttp3.ResponseBody;
//
///**
// * Created by xia on 5/25/17.
// */
//
//public class Downloader {
//
//    private static final String TAG         = "Downloader";
//    private static       Downloader mDownloader = null;
//
//    public static final int TYPE_BOOK    = 1;
//    public static final int TYPE_CHAPTER = 2;
//
//    private final int mChapterPrefix = 10000000;
//
//    private Config mConfig;
//    private Handler mHandler;
//    private DownloadService        mDownloadService;
//    private List<Work> mPending;
//    private HashMap<Integer, Work> mWorking;
//
//    public Downloader() {
//        mConfig = Config.getInstance();
//        mPending = new ArrayList<>();
//        mWorking = new HashMap<>();
//        mDownloadService = new DownloadService();
//        EventBus.getDefault().register(this);
//    }
//
//    public static Downloader getInstance() {
//        if (mDownloader == null) {
//            mDownloader = new Downloader();
//        }
//        return mDownloader;
//    }
//
//    public void finish() {
//        EventBus.getDefault().unregister(this);
//    }
//
//    public void setHandler(Handler handler) {
//        mHandler = handler;
//    }
//
//    public void notify(int what, Object obj) {
//        if (mHandler != null) {
//            Message message = new Message();
//            message.what = what;
//            message.obj = obj;
//            mHandler.sendMessage(message);
//        }
//    }
//    public void download(int bookId, int chapterId) {
//        startWork(new Work(TYPE_CHAPTER, bookId, chapterId));
//    }
//
//    public void push(int bookId) {
//        mPending.add(new Work(TYPE_BOOK, bookId, 0));
//        run();
//    }
//
//    public void push(int bookId, int chapterId) {
//        mPending.add(new Work(TYPE_CHAPTER, bookId, chapterId));
//        run();
//    }
//
//    private void next() {
//        run();
//    }
//
//
//    private void startWork(Work work) {
//        switch (work.type) {
//            case TYPE_BOOK:
//                mWorking.put(work.bookId, work);
//                mDownloadService.downloadBook(work.bookId);
//                break;
//            case TYPE_CHAPTER:
//                int key = mChapterPrefix + work.bookId + work.chapterId;
//                mWorking.put(key, work);
//                mDownloadService.downloadChapter(work.bookId, work.chapterId);
//                break;
//            default:
//                break;
//        }
//    }
//
//    public void run() {
//        if (mPending.isEmpty() || !mWorking.isEmpty()) {
//            return;
//        }
//
//        Work work = mPending.get(0);
//        mPending.remove(0);
//
//        startWork(work);
//    }
//
//    private boolean writeResponseBodyToDisk(String filepath, ResponseBody body) {
//        try {
//            File writeFile = new File(filepath);
//
//            InputStream inputStream  = null;
//            OutputStream outputStream = null;
//
//            try {
//                byte[] fileReader = new byte[4096];
//
//                long fileSize           = body.contentLength();
//                long fileSizeDownloaded = 0;
//
//                inputStream = body.byteStream();
//                outputStream = new FileOutputStream(writeFile);
//
//                while (true) {
//                    int read = inputStream.read(fileReader);
//                    if (read == -1) {
//                        break;
//                    }
//
//                    outputStream.write(fileReader, 0, read);
//                    fileSizeDownloaded += read;
//                }
//                outputStream.flush();
//                return true;
//
//            } catch (IOException e) {
//                Log.e(TAG, e.toString());
//                return false;
//            } finally {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            }
//        } catch (IOException e) {
//            Log.e(TAG, e.toString());
//            return false;
//        }
//    }
//
//    @Subscribe(thread = EventThread.IO, tags = {@Tag(Constant.Event.BOOK_DOWNLOAD_SUCCESS)})
//    public void BOOK_DOWNLOAD_SUCCESS(BookDownloadBean bean) {
//
//        if (!mWorking.containsKey(bean.bookId)) {
//            return;
//        }
//        mWorking.remove(bean.bookId);
//
//        String strBookId = String.valueOf(bean.bookId);
//        String tempFile  = mConfig.getStoragePath() + strBookId + ".tmp";
//
//        if (writeResponseBodyToDisk(tempFile, bean.getBody())) {
//            String bookDir = mConfig.getBookStoragePath() + strBookId;
//            try {
//                FileUtil.createDirIfNotExist(bookDir);
//                FileUtil.decompress(tempFile, bookDir);
//
//                notify(Constant.Notify.DOWNLOAD_BOOK_SUCCESS, strBookId);
//            } catch (Exception e) {
//                notify(Constant.Notify.DOWNLOAD_BOOK_FAILURE, e.toString());
//            }
//            FileUtil.deleteFile(tempFile);
//        }
//
//        next();
//    }
//
//    @Subscribe(thread = EventThread.IO, tags = {@Tag(Constant.Event.BOOK_CHAPTER_DOWNLOAD_SUCCESS)})
//    public void BOOK_CHAPTER_DOWNLOAD_SUCCESS(BookDownloadBean bean) {
//
//        int key = mChapterPrefix + bean.bookId + bean.chapterId;
//        if (!mWorking.containsKey(key)) {
//            return;
//        }
//        mWorking.remove(key);
//
//        String strBookId    = String.valueOf(bean.bookId);
//        String strChapterId = String.valueOf(bean.chapterId);
//        BookReadRespBean resp         = (BookReadRespBean) bean.response.body();
//
//        if (!resp.hasData()) {
//            notify(Constant.Notify.BOOK_CHAPTER_DOWNLOAD_FAILURE, strChapterId);
//            next();
//            return;
//        }
//
//        BookReadModel chapter = resp.getData();
//
//        String bookDir  = mConfig.getBookStoragePath() + strBookId;
//        String filename = strChapterId + ".txt";
//        try {
//            FileUtil.createDirIfNotExist(bookDir);
//            FileUtil.writeFile(chapter.getContent(), bookDir + File.separator + filename, false);
//            notify(Constant.Notify.BOOK_CHAPTER_DOWNLOAD_SUCCESS, chapter.getChapter_id());
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }
//
//        next();
//    }
//
//    @Subscribe(thread = EventThread.IO, tags = {@Tag(Constant.Event.BOOK_DOWNLOAD_FAILURE)})
//    public void BOOK_DOWNLOAD_FAILURE(ErrorEvent event) {
//        notify(Constant.Notify.DOWNLOAD_BOOK_FAILURE, event.toString());
//        next();
//    }
//
//    @Subscribe(thread = EventThread.IO, tags = {@Tag(Constant.Event.BOOK_CHAPTER_DOWNLOAD_FAILURE)})
//    public void BOOK_CHAPTER_DOWNLOAD_FAILURE(ErrorEvent event) {
//        notify(Constant.Notify.BOOK_CHAPTER_DOWNLOAD_FAILURE, event.toString());
//        next();
//    }
//
//    public static class Work {
//
//        public int type;
//        public int bookId;
//        public int chapterId;
//
//        public Work(int type, int bookId, int chapterId) {
//            this.type = type;
//            this.bookId = bookId;
//            this.chapterId = chapterId;
//        }
//    }
//}
