package alexwolf.cs.dartmouth.edu.myruns;

/**
 * Created by alexwolf on 2/14/16. **/
 class WekaClassifier {

 public static double classify(Object[] i)
 throws Exception {

 double p = Double.NaN;
 p = WekaClassifier.N1c7039220(i);
 return p;
 }
 static double N1c7039220(Object []i) {
 double p = Double.NaN;
 if (i[0] == null) {
 p = 1;
 } else if (((Double) i[0]).doubleValue() <= 721.244188) {
 p = WekaClassifier.Nbb6f5c41(i);
 } else if (((Double) i[0]).doubleValue() > 721.244188) {
 p = WekaClassifier.N779dfd5a21(i);
 }
 return p;
 }

 static double Nbb6f5c41(Object []i) {
 double p = Double.NaN;
 if (i[0] == null) {
 p = 1;
 } else if (((Double) i[0]).doubleValue() <= 144.095778) {
 p = WekaClassifier.N18c11cde2(i);
 } else if (((Double) i[0]).doubleValue() > 144.095778) {
 p = WekaClassifier.N34b07c5916(i);
 }
 return p;
 }
 static double N18c11cde2(Object []i) {
 double p = Double.NaN;
 if (i[1] == null) {
 p = 1;
 } else if (((Double) i[1]).doubleValue() <= 0.419028) {
 p = 1;
 } else if (((Double) i[1]).doubleValue() > 0.419028) {
 p = WekaClassifier.N2e4e3f843(i);
 }
 return p;
 }
 static double N2e4e3f843(Object []i) {
 double p = Double.NaN;
 if (i[0] == null) {
 p = 1;
 } else if (((Double) i[0]).doubleValue() <= 40.656385) {
 p = WekaClassifier.N132c0adc4(i);
 } else if (((Double) i[0]).doubleValue() > 40.656385) {
 p = WekaClassifier.N44132b1d8(i);
 }
 return p;
 }
 static double N132c0adc4(Object []i) {
 double p = Double.NaN;
 if (i[6] == null) {
 p = 1;
 } else if (((Double) i[6]).doubleValue() <= 2.669357) {
 p = WekaClassifier.N307cfb595(i);
 } else if (((Double) i[6]).doubleValue() > 2.669357) {
 p = 0;
 }
 return p;
 }
 static double N307cfb595(Object []i) {
 double p = Double.NaN;
 if (i[0] == null) {
 p = 1;
 } else if (((Double) i[0]).doubleValue() <= 5.413829) {
 p = WekaClassifier.N5d59a8ed6(i);
 } else if (((Double) i[0]).doubleValue() > 5.413829) {
 p = WekaClassifier.N29618f7c7(i);
 }
 return p;
 }
 static double N5d59a8ed6(Object []i) {
 double p = Double.NaN;
 if (i[11] == null) {
 p = 1;
 } else if (((Double) i[11]).doubleValue() <= 0.37654) {
 p = 1;
 } else if (((Double) i[11]).doubleValue() > 0.37654) {
 p = 0;
 }
 return p;
 }
 static double N29618f7c7(Object []i) {
 double p = Double.NaN;
 if (i[20] == null) {
 p = 0;
 } else if (((Double) i[20]).doubleValue() <= 0.070946) {
 p = 0;
 } else if (((Double) i[20]).doubleValue() > 0.070946) {
 p = 1;
 }
 return p;
 }
 static double N44132b1d8(Object []i) {
 double p = Double.NaN;
 if (i[10] == null) {
 p = 1;
 } else if (((Double) i[10]).doubleValue() <= 1.319884) {
 p = WekaClassifier.N46739fd59(i);
 } else if (((Double) i[10]).doubleValue() > 1.319884) {
 p = WekaClassifier.N7a62173f11(i);
 }
 return p;
 }
 static double N46739fd59(Object []i) {
 double p = Double.NaN;
 if (i[32] == null) {
 p = 1;
 } else if (((Double) i[32]).doubleValue() <= 0.887539) {
 p = 1;
 } else if (((Double) i[32]).doubleValue() > 0.887539) {
 p = WekaClassifier.N51027d1510(i);
 }
 return p;
 }
 static double N51027d1510(Object []i) {
 double p = Double.NaN;
 if (i[16] == null) {
 p = 2;
 } else if (((Double) i[16]).doubleValue() <= 0.855567) {
 p = 2;
 } else if (((Double) i[16]).doubleValue() > 0.855567) {
 p = 1;
 }
 return p;
 }
 static double N7a62173f11(Object []i) {
 double p = Double.NaN;
 if (i[10] == null) {
 p = 1;
 } else if (((Double) i[10]).doubleValue() <= 6.472264) {
 p = WekaClassifier.N3de467de12(i);
 } else if (((Double) i[10]).doubleValue() > 6.472264) {
 p = WekaClassifier.N1adb513515(i);
 }
 return p;
 }
 static double N3de467de12(Object []i) {
 double p = Double.NaN;
 if (i[7] == null) {
 p = 1;
 } else if (((Double) i[7]).doubleValue() <= 10.514994) {
 p = WekaClassifier.N5e5f3f9013(i);
 } else if (((Double) i[7]).doubleValue() > 10.514994) {
 p = 0;
 }
 return p;
 }
 static double N5e5f3f9013(Object []i) {
 double p = Double.NaN;
 if (i[64] == null) {
 p = 0;
 } else if (((Double) i[64]).doubleValue() <= 2.162849) {
 p = WekaClassifier.N3f46375714(i);
 } else if (((Double) i[64]).doubleValue() > 2.162849) {
 p = 1;
 }
 return p;
 }
 static double N3f46375714(Object []i) {
 double p = Double.NaN;
 if (i[1] == null) {
 p = 0;
 } else if (((Double) i[1]).doubleValue() <= 10.147944) {
 p = 0;
 } else if (((Double) i[1]).doubleValue() > 10.147944) {
 p = 1;
 }
 return p;
 }
 static double N1adb513515(Object []i) {
 double p = Double.NaN;
 if (i[3] == null) {
 p = 0;
 } else if (((Double) i[3]).doubleValue() <= 23.886136) {
 p = 0;
 } else if (((Double) i[3]).doubleValue() > 23.886136) {
 p = 1;
 }
 return p;
 }
 static double N34b07c5916(Object []i) {
 double p = Double.NaN;
 if (i[0] == null) {
 p = 1;
 } else if (((Double) i[0]).doubleValue() <= 611.551156) {
 p = 1;
 } else if (((Double) i[0]).doubleValue() > 611.551156) {
 p = WekaClassifier.N2eb3d21517(i);
 }
 return p;
 }
 static double N2eb3d21517(Object []i) {
 double p = Double.NaN;
 if (i[3] == null) {
 p = 2;
 } else if (((Double) i[3]).doubleValue() <= 46.608713) {
 p = 2;
 } else if (((Double) i[3]).doubleValue() > 46.608713) {
 p = WekaClassifier.N1fa578a718(i);
 }
 return p;
 }
 static double N1fa578a718(Object []i) {
 double p = Double.NaN;
 if (i[19] == null) {
 p = 1;
 } else if (((Double) i[19]).doubleValue() <= 7.284925) {
 p = 1;
 } else if (((Double) i[19]).doubleValue() > 7.284925) {
 p = WekaClassifier.N28942c2c19(i);
 }
 return p;
 }
 static double N28942c2c19(Object []i) {
 double p = Double.NaN;
 if (i[10] == null) {
 p = 2;
 } else if (((Double) i[10]).doubleValue() <= 8.831553) {
 p = 2;
 } else if (((Double) i[10]).doubleValue() > 8.831553) {
 p = WekaClassifier.N17e0118420(i);
 }
 return p;
 }
 static double N17e0118420(Object []i) {
 double p = Double.NaN;
 if (i[8] == null) {
 p = 1;
 } else if (((Double) i[8]).doubleValue() <= 54.888508) {
 p = 1;
 } else if (((Double) i[8]).doubleValue() > 54.888508) {
 p = 2;
 }
 return p;
 }
 static double N779dfd5a21(Object []i) {
 double p = Double.NaN;
 if (i[4] == null) {
 p = 2;
 } else if (((Double) i[4]).doubleValue() <= 120.016046) {
 p = 2;
 } else if (((Double) i[4]).doubleValue() > 120.016046) {
 p = WekaClassifier.N25645d1022(i);
 }
 return p;
 }
 static double N25645d1022(Object []i) {
 double p = Double.NaN;
 if (i[0] == null) {
 p = 1;
 } else if (((Double) i[0]).doubleValue() <= 871.481104) {
 p = WekaClassifier.N6019d89d23(i);
 } else if (((Double) i[0]).doubleValue() > 871.481104) {
 p = WekaClassifier.N544a624426(i);
 }
 return p;
 }
 static double N6019d89d23(Object []i) {
 double p = Double.NaN;
 if (i[2] == null) {
 p = 2;
 } else if (((Double) i[2]).doubleValue() <= 124.297657) {
 p = 2;
 } else if (((Double) i[2]).doubleValue() > 124.297657) {
 p = WekaClassifier.N40dc5cb524(i);
 }
 return p;
 }
 static double N40dc5cb524(Object []i) {
 double p = Double.NaN;
 if (i[25] == null) {
 p = 1;
 } else if (((Double) i[25]).doubleValue() <= 8.280538) {
 p = 1;
 } else if (((Double) i[25]).doubleValue() > 8.280538) {
 p = WekaClassifier.N434a905725(i);
 }
 return p;
 }
 static double N434a905725(Object []i) {
 double p = Double.NaN;
 if (i[21] == null) {
 p = 2;
 } else if (((Double) i[21]).doubleValue() <= 7.040939) {
 p = 2;
 } else if (((Double) i[21]).doubleValue() > 7.040939) {
 p = 1;
 }
 return p;
 }
 static double N544a624426(Object []i) {
 double p = Double.NaN;
 if (i[20] == null) {
 p = 2;
 } else if (((Double) i[20]).doubleValue() <= 4.997153) {
 p = WekaClassifier.N5f79899727(i);
 } else if (((Double) i[20]).doubleValue() > 4.997153) {
 p = 2;
 }
 return p;
 }
 static double N5f79899727(Object []i) {
 double p = Double.NaN;
 if (i[14] == null) {
 p = 1;
 } else if (((Double) i[14]).doubleValue() <= 14.291931) {
 p = 1;
 } else if (((Double) i[14]).doubleValue() > 14.291931) {
 p = 2;
 }
 return p;
 }
 }
