package com.comm.map.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;
import android.view.View;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CompassManager {
	/**
	 * 指南针管理器，主要用于控制指南针以及地图转向
	 */
	private static boolean mCompassIsOn = false;
	private final Context mContext;

	private SensorManager mMapCompassManager;
	private static final boolean Lock = true;
	private SensorEventListener mapCompassListener;
	private final List<View> compassViewList = new ArrayList<>(); //用来存放小指南针对象
	private static CompassManager instance_;
	private final List<OnMapSensorListener> mapCompassListeners = new Vector<>();
	private OnMapSensorListener headerCompassListeners;
	private int curOrientation;
	private boolean isUpsideDown;

	/**
	 * 单例模式构造Manager
	 *
	 * @return
	 */
	public static CompassManager getInstance(Context context){
		if(instance_ == null){
			instance_ = new CompassManager(context.getApplicationContext());
		}
		return instance_;
	}

	private CompassManager(Context context) {
		mContext = context;
	}

	/**
	 * 设置指南针是否打开
	 * @param compassIsOn
	 */
	private static void setCompassState(boolean compassIsOn) {
		mCompassIsOn = compassIsOn;
	}

	/**
	 * 设置指南针监听
	 * @param sensorListener
	 */
	public void setCompassListener(OnMapSensorListener sensorListener) {
		if (!mapCompassListeners.contains(sensorListener)) {
			mapCompassListeners.add(sensorListener);
		}
	}

	public void removeCompassListener(OnMapSensorListener sensorListener){
		mapCompassListeners.remove(sensorListener);
	}

	/**
	 * 打开指南针
	 */
	public void startCompass() {
		if (mapCompassListener == null) {
			mapCompassListener = new MySensorListener();
		}
		mMapCompassManager = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor1 = mMapCompassManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor sensor2 = mMapCompassManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mMapCompassManager.registerListener(mapCompassListener,sensor1,SensorManager.SENSOR_DELAY_GAME);
		mMapCompassManager.registerListener(mapCompassListener,sensor2,SensorManager.SENSOR_DELAY_GAME);
		setCompassState(true);
	}


	private float[] accValue = new float[3];
	private float[] magValue = new float[3];
	private final float[] values1 = new  float[3];
	private final float[] rotate = new float[9];

	/**
	 * 关闭指南针
	 */
	public void stopCompass() {
		if (mMapCompassManager != null) {
			mMapCompassManager.unregisterListener(mapCompassListener);
			mMapCompassManager = null;
		}
		if (compassViewList != null) {
			for (int i = 0; i < compassViewList.size(); i++) {
				View v = compassViewList.get(i);
				if (v != null) {
					v.setRotation(0);
				}
			}
		}
		setCompassState(false);
	}

	public void setCurOrientation(int curOrientation ){
		this.curOrientation = curOrientation;
	}

	public int getCurOrientation(){
		return this.curOrientation;
	}

	private final AngleLowpassFilter filter = new AngleLowpassFilter();
	private final float mDegree = 1000;
	private final class MySensorListener implements SensorEventListener{

		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				accValue = event.values;
				isUpsideDown = accValue[2] < 0;
			}
			if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
				magValue = event.values;
			}
			SensorManager.getRotationMatrix(rotate,null,accValue,magValue);
			SensorManager.getOrientation(rotate,values1);

			filter.add(values1[0]);
			float degree = (float)Math.toDegrees(filter.average()) + 180 - 90;// 存放了方向值
			if (mCompassIsOn) {
				for (int i = 0; i < mapCompassListeners.size(); i++){
					if(isUpsideDown){
						if(curOrientation == Surface.ROTATION_90){
							mapCompassListeners.get(i).onSensorChange(degree + 180);
						}else{
							mapCompassListeners.get(i).onSensorChange(degree);
						}
					}else{
						if(curOrientation == Surface.ROTATION_270){
							mapCompassListeners.get(i).onSensorChange(degree + 180);
						}else{
							mapCompassListeners.get(i).onSensorChange(degree);
						}
					}


				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	}

	/**

	 /**
	 * 地图监听接口
	 */
	public interface OnMapSensorListener {
		void onSensorChange(float degree);
	}

}
