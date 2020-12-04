package com.example.firebase;

public class BMIModel {
        static String height;
        static String weight;

    public BMIModel(String height, String weight) {
        this.height = height;
        this.weight = weight;
    }

    public static float calculateBMI(){
            String heightStr = height;
            String weightStr = weight;

            float bmi = 0;

            if(heightStr!=null && !"".equals(heightStr) && weightStr!=null &&!"".equals(weightStr)){
                float heightvalue=Float.parseFloat(heightStr)/100;
                float weightValue=Float.parseFloat(weightStr);

                bmi =weightValue/(heightvalue * heightvalue);
            }
            return bmi;
        }

        public static String displayBMI(float bmi){
            String bmiLabel="";

            if (Float.compare(bmi, 15f) <= 0) {
                bmiLabel = "Very severely underweight";
            } else if (Float.compare(bmi, 15f) > 0  &&  Float.compare(bmi, 16f) <= 0) {
                bmiLabel = "very underweight";
            } else if (Float.compare(bmi, 16f) > 0  &&  Float.compare(bmi, 18.5f) <= 0) {
                bmiLabel = "Underweight";
            } else if (Float.compare(bmi, 18.5f) > 0  &&  Float.compare(bmi, 25f) <= 0) {
                bmiLabel = "Normal";
            } else if (Float.compare(bmi, 25f) > 0  &&  Float.compare(bmi, 30f) <= 0) {
                bmiLabel = "Overweight";
            } else if (Float.compare(bmi, 30f) > 0  &&  Float.compare(bmi, 35f) <= 0) {
                bmiLabel = "Very Overweight";
            } else if (Float.compare(bmi, 35f) > 0  &&  Float.compare(bmi, 40f) <= 0) {
                bmiLabel = "Obese";
            } else {
                bmiLabel = "Fat fuck";
            }

            bmiLabel = bmi + "\n\n" + bmiLabel;
            //answer.setText(bmiLabel);
            return bmiLabel;
        }
}
