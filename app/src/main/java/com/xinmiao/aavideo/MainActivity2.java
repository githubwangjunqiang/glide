package com.xinmiao.aavideo;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meicam.sdk.NvsCaptureVideoFx;
import com.meicam.sdk.NvsFxDescription;
import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;

import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity implements NvsStreamingContext.CaptureDeviceCallback {

    private static final String TAG = "beauty";
    private static final int REQUEST_CAMERA_PERMISSION_CODE = 0;

    private NvsLiveWindow m_liveWindow;
    private Button m_openButton;
    private TextView m_textStrength;
    private SeekBar m_seekBarStrength;
    private TextView m_textStrengthValue;
    private TextView m_textWhitening;
    private SeekBar m_seekBarWhitening;
    private TextView m_textWhiteningValue;
    private TextView m_textReddening;
    private SeekBar m_seekBarReddening;
    private TextView m_textReddeningValue;

    private NvsStreamingContext m_streamingContext;

    private double m_strengthValue;
    private double m_whiteningValue;
    private double m_reddeningValue;
    
    private boolean m_permissionGranted;
    private boolean m_useBeautyFx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_streamingContext = NvsStreamingContext.init(this, null);
        setContentView(R.layout.activity_main2);

        initUI();
        setData();
        setControlListener();
    }

    private void initUI() {
        m_liveWindow = (NvsLiveWindow) findViewById(R.id.liveWindow);
        m_openButton = (Button) findViewById(R.id.buttonOpen);
        m_textStrength = (TextView) findViewById(R.id.textStrength);
        m_seekBarStrength = (SeekBar) findViewById(R.id.seekBarStrength);
        m_textStrengthValue = (TextView) findViewById(R.id.textStrengthValue);
        m_textWhitening = (TextView) findViewById(R.id.textWhitening);
        m_seekBarWhitening = (SeekBar) findViewById(R.id.seekBarWhitening);
        m_textWhiteningValue = (TextView) findViewById(R.id.textWhiteningValue);
        m_textReddening = (TextView) findViewById(R.id.textReddening);
        m_seekBarReddening = (SeekBar) findViewById(R.id.seekBarReddening);
        m_textReddeningValue = (TextView) findViewById(R.id.textReddeningValue);

        m_permissionGranted = false;

        // 给streaming context设置回调接口
        m_streamingContext.setCaptureDeviceCallback(this);
        if (m_streamingContext.getCaptureDeviceCount() == 0)
            return;



        // 将采集预览输出连接到NvsLiveWindow控件
        if (!m_streamingContext.connectCapturePreviewWithLiveWindow(m_liveWindow)) {
            Log.d(TAG, "连接预览窗口失败");
            return;
        }

        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)) {
                m_permissionGranted = true;
                if (!startCapturePreview())
                    return;
            }
            else {
                setCaptureEnabled(false);
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION_CODE);
            }
        } else {
            m_permissionGranted = true;
            if (!startCapturePreview())
                return;
        }
    }

    private void setData() {
        m_strengthValue = 0;
        m_whiteningValue = 0;
        m_reddeningValue = 0;
        m_useBeautyFx = true;

        NvsFxDescription fxDescription = m_streamingContext.getVideoFxDescription("Beauty");
        List<NvsFxDescription.ParamInfoObject> paramInfo =  fxDescription.getAllParamsInfo();
        for (NvsFxDescription.ParamInfoObject param:paramInfo) {
            String paramName = param.getString("paramName");
            if (paramName.equals("Strength")) {
                double maxValue = param.getFloat("floatMaxVal");
                m_strengthValue = param.getFloat("floatDefVal");
                m_seekBarStrength.setMax((int)(maxValue * 100));
                m_seekBarStrength.setProgress((int)(m_strengthValue * 100));
                m_textStrengthValue.setText(String.format(Locale.getDefault(), "%.2f", m_strengthValue));
            } else if (paramName.equals("Whitening")) {
                double maxValue = param.getFloat("floatMaxVal");
                m_whiteningValue = param.getFloat("floatDefVal");
                m_seekBarWhitening.setMax((int)(maxValue * 100));
                m_seekBarWhitening.setProgress((int)(m_whiteningValue * 100));
                m_textWhiteningValue.setText(String.format(Locale.getDefault(), "%.2f", m_whiteningValue));
            } else if (paramName.equals("Reddening")) {
                double maxValue = param.getFloat("floatMaxVal");
                m_reddeningValue = param.getFloat("floatDefVal");
                m_seekBarReddening.setMax((int)(maxValue * 100));
                m_seekBarReddening.setProgress((int)(m_reddeningValue * 100));
                m_textReddeningValue.setText(String.format(Locale.getDefault(), "%.2f", m_reddeningValue));
            }
        }

        NvsCaptureVideoFx fx = m_streamingContext.appendBeautyCaptureVideoFx();     //添加美颜采集特效
        fx.setFloatVal("Strength", m_strengthValue);
        fx.setFloatVal("Whitening", m_whiteningValue);
    }

    private void setControlListener() {

        m_openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_useBeautyFx) {
                    m_streamingContext.removeAllCaptureVideoFx(); //移除所有采集视频特效
                    m_useBeautyFx = false;
                    m_openButton.setText(R.string.open);
                } else {
                    NvsCaptureVideoFx fx = m_streamingContext.appendBeautyCaptureVideoFx();     //添加美颜采集特效
                    fx.setFloatVal("Strength", m_strengthValue);
                    fx.setFloatVal("Whitening", m_whiteningValue);
                    m_useBeautyFx = true;
                    m_openButton.setText(R.string.close);
                }
            }
        });

        m_seekBarStrength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_strengthValue = progress * 0.01;
                m_textStrengthValue.setText(String.format(Locale.getDefault(), "%.2f", m_strengthValue));
                if(m_useBeautyFx) {
                    NvsCaptureVideoFx fx = m_streamingContext.getCaptureVideoFxByIndex(0);
                    fx.setFloatVal("Strength", m_strengthValue);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        m_seekBarWhitening.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_whiteningValue = progress * 0.01;
                m_textWhiteningValue.setText(String.format(Locale.getDefault(), "%.2f", m_whiteningValue));
                if(m_useBeautyFx) {
                    NvsCaptureVideoFx fx = m_streamingContext.getCaptureVideoFxByIndex(0);
                    fx.setFloatVal("Whitening", m_whiteningValue);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        m_seekBarReddening.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_reddeningValue = progress * 0.01;
                m_textReddeningValue.setText(String.format(Locale.getDefault(), "%.2f", m_reddeningValue));
                if(m_useBeautyFx) {
                    NvsCaptureVideoFx fx = m_streamingContext.getCaptureVideoFxByIndex(0);
                    fx.setFloatVal("Reddening", m_reddeningValue);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    // 获取当前引擎状态
    private int getCurrentEngineState() {
        return m_streamingContext.getStreamingEngineState();
    }

    private boolean startCapturePreview() {
        // 判断当前引擎状态是否为采集预览状态
        if (m_permissionGranted && (getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTUREPREVIEW)) {
            //启动采集预览
            if (!m_streamingContext.startCapturePreview(1, NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH, 0, null)) {
                Log.e(TAG, "Failed to start capture preview!");
                m_seekBarStrength.setEnabled(false);
                m_seekBarWhitening.setEnabled(false);
                return false;
            } else {
                m_seekBarStrength.setEnabled(true);
                m_seekBarWhitening.setEnabled(true);
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        m_streamingContext = null;
        NvsStreamingContext.close();

        super.onDestroy();
    }

    private void setCaptureEnabled(boolean enabled) {
        m_openButton.setEnabled(enabled);
        m_seekBarStrength.setEnabled(enabled);
        m_seekBarWhitening.setEnabled(enabled);
        m_seekBarReddening.setEnabled(enabled);
    }

    @Override
    protected void onPause() {
        m_streamingContext.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        m_streamingContext.startCapturePreview(1, NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH, 0, null);
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
            return;

        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION_CODE:
                m_permissionGranted = true;
                m_streamingContext.startCapturePreview(1, NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH, 0, null);
                break;
        }
    }

    public void onCaptureDeviceCapsReady(int captureDeviceIndex)
    {
        if (m_permissionGranted) {
            setCaptureEnabled(true);
            m_permissionGranted = false;
        }
    }

    @Override
    public void onCaptureDevicePreviewResolutionReady(int captureDeviceIndex)
    {

    }

    @Override
    public void onCaptureDevicePreviewStarted(int captureDeviceIndex)
    {

    }

    @Override
    public void onCaptureDeviceError(int captureDeviceIndex, int errorCode)
    {

    }

    @Override
    public void onCaptureDeviceStopped(int captureDeviceIndex)
    {

    }

    @Override
    public void onCaptureDeviceAutoFocusComplete(int captureDeviceIndex, boolean succeeded)
    {

    }

    @Override
    public void onCaptureRecordingFinished(int captureDeviceIndex)
    {

    }

    @Override
    public void onCaptureRecordingError(int captureDeviceIndex)
    {

    }

}
