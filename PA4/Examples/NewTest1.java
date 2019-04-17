

import meggy.Meggy;

class NewTest1 
{ 
    public static void main(String[] whatever){
            Meggy.Color [] p;            
            byte row;           
            int i; 

            row = 5; 
            p = new Meggy.Color [8];         
            p[0] = Meggy.Color.RED;         
            p[1] = Meggy.Color.ORANGE;         
            p[2] = Meggy.Color.YELLOW;         
            p[3] = Meggy.Color.GREEN;         
            p[4] = Meggy.Color.BLUE;         
            p[5] = Meggy.Color.VIOLET;         
            p[6] = Meggy.Color.WHITE;         
            p[7] = Meggy.Color.DARK;         
            Meggy.setPixel((byte)2, (byte)3, p[0]);         
            Meggy.setPixel((byte)2, (byte)4, p[4]); 

            i=0;          
            while (i<8) {             
                Meggy.setPixel((byte)i, row, p[i]);        
                i = i+1;         
            } 

    }
} 

