package com.example.YuCeClient.widget.photo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.YuCeClient.R;
import com.example.YuCeClient.background.PathManager;
import com.example.YuCeClient.ui.ActivityBase;
import com.example.YuCeClient.util.BitmapUtil;
import com.example.YuCeClient.util.Debug;
import com.example.YuCeClient.widget.FixHeightRadioImageView;

import java.io.File;
import java.util.List;

@SuppressLint("NewApi")
//默认的相机为横平，所以Activity设置为横屏，拍出的照片才正确
public class ActivityCapture extends ActivityBase implements
		View.OnClickListener, CaptureSensorsObserver.RefocuseListener {
	private ImageView bnToggleCamera;
	private ImageView bnCapture;
	private TextView save;
	private TextView notSave;
	private FixHeightRadioImageView showImage;
	private ImageView bnOpenLight;
	private boolean isOpenLight = false;

	private FrameLayout framelayoutPreview;
	private CameraPreview preview;
	private CameraCropBorderView cropBorderView;
	private Camera camera;
	private PictureCallback pictureCallBack;
	private Camera.AutoFocusCallback focusCallback;
	private CaptureSensorsObserver observer;
	private View focuseView;

	private int currentCameraId;
	private int frontCameraId;
	private boolean _isCapturing;

	CaptureOrientationEventListener _orientationEventListener;
	private int _rotation;

	public static final int kPhotoMaxSaveSideLen = 1600;
	public static final String kPhotoPath = "photo_path";
	private Bitmap finalBitmap;
	private File photoFile;

	private int cropWidth = 0;
	private int cropHeight = 0;
	public static final String kCropWidth = "crop_width";
	public static final String kCropHeight = "crop_height";

		private Camera.ShutterCallback _shutterCallback = new Camera.ShutterCallback() {
			@Override
			public void onShutter() {
			}
		};

	final static String TAG = "capture";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (getIntent() != null) {
			Intent intent = getIntent();
			cropWidth = intent.getIntExtra(kCropWidth, 750);
			cropHeight = intent.getIntExtra(kCropHeight, 750);
		}
		observer = new CaptureSensorsObserver(this);
		_orientationEventListener = new CaptureOrientationEventListener(this);
		setContentView(R.layout.activity_capture);
		super.onCreate(savedInstanceState);
		setupDevice();
	}

	@Override
	protected void onDestroy() {
		if (null != observer) {
			observer.setRefocuseListener(null);
			observer = null;
		}
		_orientationEventListener = null;
		if (finalBitmap != null && !finalBitmap.isRecycled()) {
			finalBitmap.recycle();
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera(); // release the camera immediately on pause event

		observer.stop();
		_orientationEventListener.disable();
	}

	@Override
	protected void onResume() {
		super.onResume();
		openCamera();
	}

	private void releaseCamera() {
		if (camera != null) {
			camera.release(); // release the camera for other applications
			camera = null;
		}

		if (null != preview) {
			framelayoutPreview.removeAllViews();
			preview = null;
		}
	}

	@Override
	protected void getViews() {
		bnOpenLight = (ImageView) findViewById(R.id.bnOpenLight);
		save = (TextView) findViewById(R.id.save);
		notSave = (TextView) findViewById(R.id.notSave);
		showImage = (FixHeightRadioImageView) findViewById(R.id.showImage);
		bnToggleCamera = (ImageView) findViewById(R.id.bnToggleCamera);
		bnCapture = (ImageView) findViewById(R.id.bnCapture);
		framelayoutPreview = (FrameLayout) findViewById(R.id.cameraPreview);
		focuseView = findViewById(R.id.viewFocuse);
	}

	@Override
	protected void initViews() {
		showImage.setRadio(cropWidth / (cropHeight * 1.0f));
		bnOpenLight.setRotation(-90);
		save.setRotation(-90);
		notSave.setRotation(-90);
		showImage.setRotation(-90);
		bnCapture.setRotation(-90);
		bnToggleCamera.setRotation(-90);
	}

	@Override
	protected void setListeners() {
		bnOpenLight.setOnClickListener(this);
		save.setOnClickListener(this);
		notSave.setOnClickListener(this);
		bnToggleCamera.setOnClickListener(this);
		bnCapture.setOnClickListener(this);
		observer.setRefocuseListener(this);
		pictureCallBack = new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				_isCapturing = false;
				Bitmap bitmap = null;
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeByteArray(data, 0, data.length, options);
					options.inJustDecodeBounds = false;
					options.inPreferredConfig = Bitmap.Config.ARGB_8888;
					//此处就把图片压缩了
					options.inSampleSize = Math.max(options.outWidth
							/ kPhotoMaxSaveSideLen, options.outHeight
							/ kPhotoMaxSaveSideLen);
					bitmap = BitmapUtil.decodeByteArrayUnthrow(data, options);

					if (null == bitmap) {
						options.inSampleSize = Math.max(2, options.inSampleSize * 2);
						bitmap = BitmapUtil.decodeByteArrayUnthrow(data, options);
					}
				} catch (Throwable e) {
				}
				if (null == bitmap) {
					Toast.makeText(ActivityCapture.this, "内存不足，保存照片失败！", Toast.LENGTH_SHORT).show();
					return;
				}
				//long start = System.currentTimeMillis();
				Bitmap addBitmap = BitmapUtil.rotateAndScale(bitmap, _rotation, kPhotoMaxSaveSideLen, true);
				finalBitmap = cropPhotoImage(addBitmap);
				photoFile = PathManager.getCropPhotoPath();
				boolean successful = BitmapUtil.saveBitmap2file(finalBitmap, photoFile, Bitmap.CompressFormat.JPEG, 100);
				while (!successful) {
					successful = BitmapUtil.saveBitmap2file(finalBitmap, photoFile, Bitmap.CompressFormat.JPEG, 100);
				}
				displayCropImage();
			}
		};
		focusCallback = new Camera.AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean successed, Camera camera) {
				focuseView.setVisibility(View.INVISIBLE);
			}
		};
	}

	private void openCameraLight() {
		//直接开启
		if (camera != null) {
			Camera.Parameters parameters = camera.getParameters();
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
			camera.setParameters(parameters);
		}
	}

	private void closeCameraLight() {
		//直接关闭
		if (camera != null) {
			Camera.Parameters parameters = camera.getParameters();
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//开启
			camera.setParameters(parameters);
		}
	}

	private void displayCropImage() {
		if (finalBitmap != null) {
			showImage.setImageBitmap(finalBitmap);
			showImage.setVisibility(View.VISIBLE);
			bnCapture.setVisibility(View.INVISIBLE);
			save.setVisibility(View.VISIBLE);
			notSave.setVisibility(View.VISIBLE);
			cropBorderView.setFullBlack();
		}
	}

	private void hideDisplayCropImage() {
		showImage.setVisibility(View.INVISIBLE);
		bnCapture.setVisibility(View.VISIBLE);
		save.setVisibility(View.INVISIBLE);
		notSave.setVisibility(View.INVISIBLE);
		cropBorderView.setNotFullBlack();
		if (finalBitmap != null && !finalBitmap.isRecycled()) {
			finalBitmap.recycle();
		}
		camera.startPreview();
	}
	//根据拍照的图片来剪裁

	private Bitmap cropPhotoImage(Bitmap bmp) {
		int dw = bmp.getWidth();
		int dh = bmp.getHeight();
		//		Debug.info("dw--:" + dw + " dh--:" + dh);
		int height;
		int width;
		if (dh > dw) {//图片竖直方向
			//切图片时按照竖屏来计算
			height = getWindowManager().getDefaultDisplay().getWidth();
			width = getWindowManager().getDefaultDisplay().getHeight();
		} else {//图片是水平方向
			//切图片时按照横屏来计算
			width = getWindowManager().getDefaultDisplay().getWidth();
			height = getWindowManager().getDefaultDisplay().getHeight();
		}
		//		Debug.info("height--:" + height + "  width--:" + width);
		Rect rect = new Rect();
		int left = (width - cropBorderView.getRect().width()) / 2;
		int top = (height - cropBorderView.getRect().height()) / 2;
		int right = left + cropBorderView.getRect().width();
		int bottom = top + cropBorderView.getRect().height();
		rect.set(left, top, right, bottom);
		//		Debug.info("left--:" + left + " top--:" + top + " right--:" + right + " bottom--:" + bottom);
		float scale = 1.0f;
		// 如果图片的宽或者高大于屏幕，则缩放至屏幕的宽或者高
		if (dw > width && dh <= height) {
			scale = width * 1.0f / dw;
		}
		if (dh > height && dw <= width) {
			scale = height * 1.0f / dh;
		}
		// 如果宽和高都大于屏幕，则让其按按比例适应屏幕大小
		if (dw > width && dh > height) {
			scale = Math.max(width * 1.0f / dw, height * 1.0f / dh);
		}
		//如果图片的宽度和高度都小于屏幕的宽度和高度，则放大至屏幕大小
		if (dw < width && dh < height) {
			scale = width * 1.0f / dw;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		try {
			Bitmap b2 = Bitmap.createBitmap(bmp, 0, 0, dw, dh, matrix, true);
			if (null != b2 && bmp != b2) {
				bmp.recycle();
				bmp = b2;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		//		Debug.info("b2 width--:" + bmp.getWidth() + " b2 height--:" + bmp.getHeight());
		try {
			Bitmap b3 = Bitmap.createBitmap(bmp, rect.left, rect.top, rect.width(), rect.height());
			if (null != b3 && bmp != b3) {
				bmp.recycle();
				bmp = b3;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		//将图片压缩至cropWidth*cropHeight
		try {
			Bitmap b4 = Bitmap.createScaledBitmap(bmp, cropWidth, cropHeight, false);
			if (null != b4 && bmp != b4) {
				bmp.recycle();
				bmp = b4;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bmp;
	}

	private void setupDevice() {
		if (android.os.Build.VERSION.SDK_INT > 8) {
			int cameraCount = Camera.getNumberOfCameras();

			if (cameraCount < 1) {
				Toast.makeText(this, "你的设备木有摄像头。。。", Toast.LENGTH_SHORT).show();
				finish();
				return;
			} else if (cameraCount == 1) {
				bnToggleCamera.setVisibility(View.INVISIBLE);
			} else {
				bnToggleCamera.setVisibility(View.VISIBLE);
			}

			currentCameraId = 0;
			frontCameraId = findFrontFacingCamera();
			if (-1 == frontCameraId) {
				bnToggleCamera.setVisibility(View.INVISIBLE);
			}
		}
	}

	private void openCamera() {
		if (android.os.Build.VERSION.SDK_INT > 8) {
			try {
				camera = Camera.open(currentCameraId);
			} catch (Exception e) {
				Toast.makeText(this, getString(R.string.cameraOpenField), Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			setCameraDisplayOrientation(this, 0, camera);
		} else {
			try {
				camera = Camera.open();
			} catch (Exception e) {
				Toast.makeText(this, getString(R.string.cameraOpenField), Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
		}

		Camera.Parameters camParmeters = camera.getParameters();
		List<Size> sizes = camParmeters.getSupportedPreviewSizes();
		for (Size size : sizes) {
			Log.v(TAG, "w:" + size.width + ",h:" + size.height);
		}
		preview = new CameraPreview(this, camera);
		cropBorderView = new CameraCropBorderView(this);
		cropBorderView.setWidthHeight(cropWidth, cropHeight);
		FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		framelayoutPreview.addView(preview, params1);
		framelayoutPreview.addView(cropBorderView, params2);
		observer.start();
		_orientationEventListener.enable();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bnOpenLight: {
			if (isOpenLight) {
				isOpenLight = false;
				bnOpenLight.setImageResource(R.drawable.icon_flash_off);
				closeCameraLight();
			} else {
				isOpenLight = true;
				bnOpenLight.setImageResource(R.drawable.icon_flash_on);
				openCameraLight();
			}
		}
		break;
		case R.id.bnToggleCamera:
			switchCamera();
			break;
		case R.id.bnCapture:
			bnCaptureClicked();
			break;
		case R.id.save: {
			Intent intent = new Intent();
			intent.putExtra(kPhotoPath, photoFile.getAbsolutePath());
			ActivityCapture.this.setResult(RESULT_OK, intent);
			ActivityCapture.this.finish();
		}
		break;
		case R.id.notSave: {
			hideDisplayCropImage();
		}
		break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	//横竖屏切换的时候
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void switchCamera() {
		if (currentCameraId == 0) {
			currentCameraId = frontCameraId;
		} else {
			currentCameraId = 0;
		}
		releaseCamera();
		openCamera();
	}

	private void bnCaptureClicked() {
		if (_isCapturing) {
			return;
		}
		_isCapturing = true;
		focuseView.setVisibility(View.INVISIBLE);

		try {
			camera.takePicture(null, null, pictureCallBack);
		} catch (RuntimeException e) {
			e.printStackTrace();
			_isCapturing = false;
		}
	}


	/**
	 * A basic Camera preview class
	 */
	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
		private SurfaceHolder mHolder;
		private Camera mCamera;

		@SuppressWarnings("deprecation")
		public CameraPreview(Context context, Camera camera) {
			super(context);
			mCamera = camera;

			// Install a SurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = getHolder();
			mHolder.addCallback(this);
			// deprecated setting, but required on Android versions prior to 3.0
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		public void surfaceCreated(SurfaceHolder holder) {
			// The Surface has been created, now tell the camera where to draw
			// the preview.
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (Exception e) {
				Log.d(TAG, "Error setting camera preview: " + e.getMessage());
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// empty. Take care of releasing the Camera preview in your
			// activity.
			if (camera != null) {
				camera.setPreviewCallback(null);
				camera.stopPreview();// 停止预览
				camera.release(); // 释放摄像头资源
				camera = null;
			}
		}

		private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
			final double ASPECT_TOLERANCE = 0.05;
			double targetRatio = (double) w / h;
			if (sizes == null)
				return null;

			Size optimalSize = null;
			double minDiff = Double.MAX_VALUE;

			int targetHeight = h;

			// Try to find an size match aspect ratio and size
			for (Size size : sizes) {
				double ratio = (double) size.width / size.height;
				if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
					continue;
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}

			// Cannot find the one match the aspect ratio, ignore the
			// requirement
			if (optimalSize == null) {
				minDiff = Double.MAX_VALUE;
				for (Size size : sizes) {
					if (Math.abs(size.height - targetHeight) < minDiff) {
						optimalSize = size;
						minDiff = Math.abs(size.height - targetHeight);
					}
				}
			}

			return optimalSize;
		}

		private Size getOptimalPictureSize(List<Size> sizes, double targetRatio) {
			final double ASPECT_TOLERANCE = 0.05;

			if (sizes == null)
				return null;

			Size optimalSize = null;
			int optimalSideLen = 0;
			double optimalDiffRatio = Double.MAX_VALUE;

			for (Size size : sizes) {

				int sideLen = Math.max(size.width, size.height);
				//LogEx.i("size.width: " + size.width + ", size.height: " + size.height);
				boolean select = false;
				if (sideLen < kPhotoMaxSaveSideLen) {
					if (0 == optimalSideLen || sideLen > optimalSideLen) {
						select = true;
					}
				} else {
					if (kPhotoMaxSaveSideLen > optimalSideLen) {
						select = true;
					} else {
						double diffRatio = Math.abs((double) size.width / size.height - targetRatio);
						if (diffRatio + ASPECT_TOLERANCE < optimalDiffRatio) {
							select = true;
						} else if (diffRatio < optimalDiffRatio + ASPECT_TOLERANCE && sideLen < optimalSideLen) {
							select = true;
						}
					}
				}

				if (select) {
					optimalSize = size;
					optimalSideLen = sideLen;
					optimalDiffRatio = Math.abs((double) size.width / size.height - targetRatio);
				}
			}

			return optimalSize;
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			// If your preview can change or rotate, take care of those events
			// here.
			// Make sure to stop the preview before resizing or reformatting it.

			Debug.debug("surfaceChanged format:" + format + ", w:" + w + ", h:" + h);
			if (mHolder.getSurface() == null) {
				// preview surface does not exist
				return;
			}

			// stop preview before making changes
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				// ignore: tried to stop a non-existent preview
			}

			try {
				Camera.Parameters parameters = mCamera.getParameters();

				List<Size> sizes = parameters.getSupportedPreviewSizes();
				Size optimalSize = getOptimalPreviewSize(sizes, w, h);
				parameters.setPreviewSize(optimalSize.width, optimalSize.height);
				double targetRatio = (double) w / h;
				sizes = parameters.getSupportedPictureSizes();
				optimalSize = getOptimalPictureSize(sizes, targetRatio);
				parameters.setPictureSize(optimalSize.width, optimalSize.height);
				parameters.setRotation(0);
				mCamera.setParameters(parameters);
			} catch (Exception e) {
				Debug.debug(e.toString());
			}

			// set preview size and make any resize, rotate or
			// reformatting changes here

			// start preview with new settings
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();
			} catch (Exception e) {
				Debug.debug("Error starting camera preview: " + e.getMessage());
			}
		}
	}

	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				Log.d(TAG, "Camera found");
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}

	private static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}

		//LogEx.i("result: " + result);
		camera.setDisplayOrientation(result);
	}

	@Override
	public void needFocuse() {

		//LogEx.i("_isCapturing: " + _isCapturing);
		if (null == camera || _isCapturing) {
			return;
		}

		//LogEx.i("autoFocus");
		camera.cancelAutoFocus();
		try {
			camera.autoFocus(focusCallback);
		} catch (Exception e) {
			Debug.debug(e.toString());
			return;
		}

		if (View.INVISIBLE == focuseView.getVisibility()) {
			focuseView.setVisibility(View.VISIBLE);
			focuseView.getParent().requestTransparentRegion(preview);
		}
	}

	//相机旋转监听的类，最后保存图片时用到
	private class CaptureOrientationEventListener extends OrientationEventListener {
		public CaptureOrientationEventListener(Context context) {
			super(context);
		}

		@Override
		public void onOrientationChanged(int orientation) {
			if (null == camera)
				return;
			if (orientation == ORIENTATION_UNKNOWN)
				return;

			orientation = (orientation + 45) / 90 * 90;
			if (android.os.Build.VERSION.SDK_INT <= 8) {
				_rotation = (90 + orientation) % 360;
				return;
			}

			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(currentCameraId, info);

			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				_rotation = (info.orientation - orientation + 360) % 360;
			} else { // back-facing camera
				_rotation = (info.orientation + orientation) % 360;
			}
		}
	}
}
