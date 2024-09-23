
	package com.mycompany.myapp;
	import android.Manifest;
	import android.content.pm.PackageManager;
	import android.os.Bundle;
	import android.util.Log;

import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrength;
import android.telephony.TelephonyManager;

	//import androidx.appcompat.app.AppCompatActivity;
	//import androidx.core.app.ActivityCompat;
	//import androidx.core.content.ContextCompat;

	import java.util.ArrayList;
	import java.util.List;
	import java.util.Random;
	
	import android.app.*;
	import android.os.*;
import android.widget.TextView;

public class MainActivity extends Activity {
		private static final int PERMISSIONS_REQUEST_CODE = 123;
		private static final int NUM_CALCULATIONS = 100;

		private List<Double> results;
	private TextView average_x_sigma_textview;


		@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
			// Check and request location permission if not granted
		//	if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

           //     != PackageManager.PERMISSION_GRANTED) {
			//	ActivityCompat.requestPermissions(this,
		//										  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
		//										  PERMISSIONS_REQUEST_CODE);
		//	} else {
		//		startCalculations();
		//	}
		}

		private void startCalculations() {
			results = new ArrayList<>();

			for (int i = 0; i < NUM_CALCULATIONS; i++) {
				double xT = getRandomPosition();
				double yT = getRandomPosition();
				double xI = getRandomPosition();
				double yI = getRandomPosition();

				double d = Math.sqrt(Math.pow(xT - xI, 2) + Math.pow(yT - yI, 2));

				double beta = getBetaFromBTS();
				double p0 = getP0FromBTS();
				double pr = p0 - 10 * beta * Math.log10(d);

				double xSigma = pr - p0 + 10 * beta * Math.log10(d);

				results.add(xSigma);
			}

			double averageXSigma = calculateAverage(results);
			Log.d("MainActivity", "Average Xσ: " + averageXSigma);
			TextView average_x_sigma_textview=findViewById(R.id.average_x_sigma_textview);
			average_x_sigma_textview.setText("fit Xσ: " + averageXSigma);
		}

		private double getRandomPosition() {
			// Generate a random position between 0 and 100
			Random random = new Random();
			return random.nextDouble() * 100;
		}

		

	private double readBtsInfo1() {

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		List cellInfoList = telephonyManager.getAllCellInfo();
		double rsrp=0;// تعریف مقدار پیش‌فرض برای rsrp


		if (cellInfoList != null) {
			for (CellInfo cellInfo : cellInfoList) {
				if (cellInfo instanceof CellInfoLte) {
					CellSignalStrength signalStrength = ((CellInfoLte) cellInfo).getCellSignalStrength();
					rsrp= signalStrength.getLevel(); // بروزرسانی مقدار p0
				}
			}
		}

		return rsrp;
	}
	private double readBtsInfo2() {

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		List cellInfoList = telephonyManager.getAllCellInfo();
		double p0 = 0; // تعریف مقدار پیش‌فرض برای p0


		if (cellInfoList != null) {
			for (CellInfo cellInfo : cellInfoList) {
				if (cellInfo instanceof CellInfoLte) {
					CellSignalStrength signalStrength = ((CellInfoLte) cellInfo).getCellSignalStrength();
					p0= signalStrength.getDbm(); // بروزرسانی مقدار p1
				}
			}
		}

		return p0;
	}
		

	
	private double getBetaFromBTS() {
// TODO: Implement method to retrieve β value from BTS
		double rsrpp= readBtsInfo1();
		return rsrpp;



	}

	private double getP0FromBTS() {
// TODO: Implement method to retrieve P0 value from BTS
	double	pp=readBtsInfo2();
		return pp;
	}

	
		private double calculateAverage(List<Double> values) {
			double sum = 0.0;
			for (Double value : values) {
				sum += value;
			}
			return sum / values.size();
		}

	
		@Override
		public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
			if (requestCode == PERMISSIONS_REQUEST_CODE) {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					startCalculations();
				} else {
					Log.e("MainActivity", "Location permission denied");
				}
			}
		}
	}
	
