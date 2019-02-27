

import meggy.Meggy;

class ArrayExp {
    public static void main(String[] a){
        Meggy.setPixel((byte)(new MyClass().testing()), (byte)4, Meggy.Color.GREEN);
    }
}


class MyClass {
    public int testing() {
        int [] x;
        x = new int [7];
        x[0] = 1;
        x[1] = 6;
        x[6] = 3;
        return x[0] + x[x[1]];     
    }
}
