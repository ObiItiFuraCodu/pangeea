package com.example.pangeea.other;

import java.util.List;

public class Test_Question {
    String prompt;
    String A;
    String B;
    String C;
    List<String> valid;

    public Test_Question(String prompt,String a,String b,String c) {
        this.prompt = prompt;
        A = a;
        B = b;
        C = c;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }
}
