# Gaussian Noise Calculation Project

This project calculates the Gaussian noise between two random positions using various parameters such as distance (`d`), `beta`, and `p0`. The noise is computed based on signal strength retrieved from the BTS (Base Transceiver Station) and the distance between two points.

## Table of Contents🧩
- [Installation](#installation)
- [Usage](#usage)
- [Main Functions](#main-functions)
- [Results](#results)
- [Contributing](#contributing)
- [License](#license)

## Installation ⚙
To use this project, clone the repository and ensure you have the required libraries for Android development:

You'll also need the following Android libraries:
```
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrength;
import android.telephony.TelephonyManager;
import java.util.Random;
```
## Usage
1.Requesting Permissions: 
The app checks for location permissions and requests them if not already granted. The startCalculations() function is called once permission is obtained.
```
if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
} else {
    startCalculations();
}
```
2.Main Calculations: 
The project generates random positions for two points, calculates the distance between them, retrieves the beta and p0 values from the BTS, and computes Gaussian noise.
```
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
}

```
3.Display the Results: 
The average of the Gaussian noise calculations is displayed on the screen.
```
double averageXSigma = calculateAverage(results);
TextView average_x_sigma_textview = findViewById(R.id.average_x_sigma_textview);
average_x_sigma_textview.setText("fit X: " + averageXSigma);
```

## Main Functions
**getRandomPosition()**
Generates a random position between 0 and 100.
```
private double getRandomPosition() {
    Random random = new Random();
    return random.nextDouble() * 100;
}

```
**getBetaFromBTS()**
Retrieves the beta value by accessing the phone's SIM card information using the TelephonyManager.
```
private double getBetaFromBTS() {
    double rsrpp = readBtsInfo1();
    return rsrpp;
}

```
**getP0FromBTS()**
Retrieves the p0 value from the BTS.

```
private double getP0FromBTS() {
    double pp = readBtsInfo2();
    return pp;
}

```
**calculateAverage(List<Double> values)**
Calculates the average of the Gaussian noise values.
```
private double calculateAverage(List<Double> values) {
    double sum = 0.0;
    for (Double value : values) {
        sum += value;
    }
    return sum / values.size();
}
```
## Results
The average Gaussian noise value is calculated and logged. The result is displayed in the TextView on the Android device's screen:

```
Log.d("MainActivity", "Average X: " + averageXSigma);
```
