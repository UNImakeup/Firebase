package com.example.firebase;

public class BMIModel {
        static String height;
        static String weight;

    public BMIModel(String height, String weight) {  https://l.facebook.com/l.php?u=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DWbYQN5rRqUQ%26fbclid%3DIwAR1BCNjz3LWLMBkitBKcN9UFIvw4xWl4zu-P0i2B0xd1Zn04l94B7f9MCbk&h=AT3GA-WNNaiUNkLffnj6-P_9acfuALfWUxLsLWzi_l5zwuAa_zUAXNCe-UwqKXM9fub5fNu8a5gHNim9qJgkyjkC0FNSMjLqyFxQ1BsUlnKFfM8VprI0p7zlNtBi_2DzqHz-W14VF6nk1tTOXJxdyw
    //Vi lavede det om til en klasse, ifht youtube linket
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
            return bmiLabel;
        }
}
