package com.xinmiao.aavideo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.meicam.sdk.NvsStreamingContext;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NvsStreamingContext.CaptureDeviceCallback {
//    private static final String TAG = "12345";
//
//    private NvsStreamingContext m_streamingContext;
//    private NvsLiveWindow mNvsLiveWindow;
//    private boolean m_permissionGranted = true;

    private Button mButton;
    private ListView mListView;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        m_streamingContext = NvsStreamingContext.init(this, null);//初始化Streaming Context
        setContentView(R.layout.activity_main);
//        mNvsLiveWindow = (NvsLiveWindow) findViewById(R.id.liveWindow);
//        initUI();
        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, MainActivity2.class));
                final Glide glide = Glide.get(MainActivity.this);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        glide.clearDiskCache();
                    }
                }).start();
                glide.clearMemory();
            }
        });
        mListView = (ListView) findViewById(R.id.listview);

        String imageurl = "http://image.tianjimedia.com/uploadImages/2015/131/10/13NC7PL97GES.jpg";
        list = new ArrayList<String>();

        list.add("http://img01.taopic.com/170823/240508-1FR30Q63374.jpg");
        list.add("http://image.tianjimedia.com/uploadImages/2015/145/07/7X04183HF89I.jpg");
        list.add("http://pic1.win4000.com/wallpaper/1/55daab78c98c6.jpg");
        list.add("http://www.114nba.com/uploadfile/2013/0107/20130107044212901.jpg");
        list.add("http://image.l99.com/ad8/1437453022715_5swgd5.jpg");
        list.add("http://www.114nba.com/uploadfile/2013/0107/20130107044214569.jpg");


        list.add("http://p1.so.qhimgs1.com/t0164aabb64a8236121.jpg");
        list.add("http://p0.so.qhmsg.com/t01ad3641a09574b2f5.jpg");
        list.add("http://p1.so.qhmsg.com/t01702d70f973322346.jpg");
        list.add("http://p0.so.qhimgs1.com/t0121c248a899ad2530.jpg");


        list.add("http://image.tianjimedia.com/uploadImages/2014/218/06/088J8P3P5MW7.jpg");
        list.add("http://pic.jj20.com/up/allimg/1011/091GG30518/1F91G30518-2.jpg");
        list.add("http://n1.itc.cn/img8/wb/recom/2016/07/18/146882671668637372.JPEG");
        list.add("http://pic1.win4000.com/wallpaper/c/55da8eab9aa87.jpg");

        list.add("http://img.mp.itc.cn/upload/20170111/621e8cee69804a2b8260941bc1f3f2ff_th.jpg");
        list.add("http://image.tianjimedia.com/uploadImages/2015/159/42/3JFPCDDY03A1.jpg");
        list.add("http://file.mumayi.com/forum/201408/21/140833d3223g83a9n2ln10.jpg");
        list.add("http://www.windows7en.com/uploads/140829/2009101310225041.jpg");


        list.add("http://p5.so.qhimgs1.com/t01e93105df8d93c2eb.jpg");
        list.add("http://image.tianjimedia.com/uploadImages/2015/150/26/0LDD0O6RT87L.jpg");
        list.add("http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1309/01/c1/25137985_1378001186056.jpg");
        list.add("http://pic1.win4000.com/wallpaper/3/533296a65a5c7.jpg");

        list.add(imageurl);
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.itme,
                            parent,false);
                    holder.mImageView = (ImageView) convertView.findViewById(R.id.itmeimage);
                    convertView.setTag(holder);
                }
                holder = (ViewHolder) convertView.getTag();
                RequestOptions option = new RequestOptions()
//                        .transforms(new RoundedCorners(20))
                        .transforms(new CenterCrop(),new BlurTransformation(MainActivity.this,10))
                        .placeholder(R.mipmap.ic_launcher_round);


                Glide.with(MainActivity.this)
                        .load(list.get(position))
                        .apply(option)
                        .into(holder.mImageView);


                return convertView;
            }
        });
    }

    private class ViewHolder {
        ImageView mImageView;
    }

    //初始化
    private void initUI() {
        // 给streaming context设置回调接口
//        m_streamingContext.setCaptureDeviceCallback(this);
//        if (m_streamingContext.getCaptureDeviceCount() == 0) {
//            return;
//        }
//        // 将采集预览输出连接到NvsLiveWindow控件
//        if (!m_streamingContext.connectCapturePreviewWithLiveWindow(mNvsLiveWindow)) {
//            Log.d(TAG, "连接预览窗口失败");
//            return;
//        }
//        m_permissionGranted = true;
//        if (!startCapturePreview()) {
//            return;
//        }

    }

//    // 获取当前引擎状态
//    private int getCurrentEngineState() {
//        return m_streamingContext.getStreamingEngineState();
//    }

//    private boolean startCapturePreview() {
//        // 判断当前引擎状态是否为采集预览状态
//        if (m_permissionGranted && (getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTUREPREVIEW)) {
//            //启动采集预览
//            if (!m_streamingContext.startCapturePreview(1, NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH, 0, null)) {
//                Log.e(TAG, "未能开始捕捉预览");
////                m_seekBarStrength.setEnabled(false);
////                m_seekBarWhitening.setEnabled(false);
//                return false;
//            } else {
////                m_seekBarStrength.setEnabled(true);
////                m_seekBarWhitening.setEnabled(true);
//            }
//        }
//        return true;
//    }

    @Override
    protected void onDestroy() {
//        //streamingContext销毁
//        m_streamingContext = null;
//        NvsStreamingContext.close();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
//        m_streamingContext.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
//        m_streamingContext.startCapturePreview(1, NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH, 0, null);
        super.onResume();
    }


    @Override
    public void onCaptureDeviceCapsReady(int i) {

    }

    @Override
    public void onCaptureDevicePreviewResolutionReady(int i) {

    }

    @Override
    public void onCaptureDevicePreviewStarted(int i) {

    }

    @Override
    public void onCaptureDeviceError(int i, int i1) {

    }

    @Override
    public void onCaptureDeviceStopped(int i) {

    }

    @Override
    public void onCaptureDeviceAutoFocusComplete(int i, boolean b) {

    }

    @Override
    public void onCaptureRecordingFinished(int i) {

    }

    @Override
    public void onCaptureRecordingError(int i) {

    }
}
