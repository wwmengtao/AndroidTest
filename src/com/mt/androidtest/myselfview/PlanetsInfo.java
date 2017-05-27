package com.mt.androidtest.myselfview;

import com.mt.androidtest.R;


/**
 * PlanetsInfo���Ŵ�������ϸ��Ϣ
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
		"ˮ��",
		"����",
		"����",
		"����",
		"ľ��",
		"����",
		"������",
		"������",
		"ڤ����"
	};
	
	public static float[] PlanetRadius = {//���ǰ뾶(ģ��ֵ����׼ȷ)
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
	
	public static float[] planetRadiusAccu = {//���ǰ뾶(׼ȷֵ����λ������)
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
	
	public static float[] RevolutionDays={//���ǹ�ת����(��λ����)
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
	
	public static boolean[] RevolutionCounterClockWise={//���ǹ�ת���򣬶�����ʱ��
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
