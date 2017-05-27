package com.mt.androidtest.myselfview;

import com.mt.androidtest.R;


/**
 * PlanetsInfo：九大行星详细信息
 * @author mengtao1
 *
 */
public class PlanetsInfo {
	public static String[] PlanetNames={
		"Mercury",
		"Venus",
		"Earth",
		"Mars",
		"Jupiter",
		"Saturn",
		"Uranus",
		"Neptune",
		"Pluto"
	};
	
	public static String[] PlanetNamesCH={
		"水星",
		"金星",
		"地球",
		"火星",
		"木星",
		"土星",
		"天王星",
		"海王星",
		"冥王星"
	};
	
	public static float[] PlanetRadius = {//行星半径(模拟值，不准确)
			3.0f,
			5.0f,
			5.0f,
			4.0f,
			7.2f,
			6.0f,
			4.5f,
			4.0f,
			3.0f
	};
	
	public static float[] planetRadiusAccu = {//行星半径(准确值，单位：公里)
			2439.7f,
			6051.8f	,
			6378.14f,
			3397f,
			71492f,
			60268f,
			25559f,
			24764f,
			1151f
	};
	
	public static float[] RevolutionDays={//行星公转日期(单位：天)
			87.97f,
			224.7f,
			365.24f,
			686.93f,
			(float) (11.8565*365.24),
			(float) (29.448*365.24),
			(float) (84.02*365.24),
			(float) (164.79*365.24),
			(float) (247.92*365.24),
	};
	
	public static boolean[] RevolutionCounterClockWise={//行星公转方向，都是逆时针
		true,
		true,
		true,
		true,
		true,
		true,
		true,
		true,
		true		
	};
	
	public static int[] PlanetsColor = {
		   R.color.red,
		   R.color.yellow,
		   R.color.magenta,
		   R.color.green,
		   R.color.royalblue,
		   R.color.crimson,
		   R.color.darkviolet,
		   R.color.lawngreen,
		   R.color.tomato
	};
	
	
}
